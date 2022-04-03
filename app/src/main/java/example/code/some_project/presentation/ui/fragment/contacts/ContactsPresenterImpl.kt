package example.code.some_project.presentation.ui.fragment.contacts

import android.Manifest
import io.reactivex.Completable
import io.reactivex.Single
import example.code.some_project.R
import example.code.some_project.domain.interactor.ContactsInteractor
import example.code.some_project.domain.interactor.ContactsWithBlacklistInteractor
import example.code.some_project.domain.interactor.LogoutInteractor
import example.code.some_project.domain.models.Contact
import example.code.some_project.domain.repo.NetworkManager
import example.code.some_project.domain.repo.SettingsRepo
import example.code.some_project.domain.repo.device.ResourcesManager
import example.code.some_project.util.isAllowedNumber
import example.code.some_project.util.toBlacklistNumber
import moxy.InjectViewState

@InjectViewState
class ContactsPresenterImpl(
    logoutInteractor: LogoutInteractor,
    private val contactsWithBlacklistInteractor: ContactsWithBlacklistInteractor,
    private val contactsInteractor: ContactsInteractor,
    private val settingsRepo: SettingsRepo,
    private val resourceManager: ResourcesManager,
    networkManager: NetworkManager
) : AbstractContactsPresenter(logoutInteractor, networkManager) {

    private var model: ContactsScreenModel? = null

    override fun onResume() {
        super.onResume()
        initialize()
    }

    private fun initialize() {
        if (!hasConnection) {
            viewState.showNoInternetScreen()
            return
        }

        viewState.hideErrorScreen()

        viewState.showLoading()
        executeSingle(
            Single.zip(
                contactsWithBlacklistInteractor.prepareData(),
                settingsRepo.isContactsSynchronized(),
                resourceManager.isPermissionGranted(Manifest.permission.READ_CONTACTS),
                contactsInteractor.getUserMsisdn(),
                { data, synchronized, granted, selfMsisdn ->
                    ContactsScreenModel(
                        granted,
                        synchronized,
                        data.contacts,
                        selfMsisdn
                    )
                }
            ),
            {
                model = it
                viewState.hideLoading()
                viewState.setupUi(it)
            },
            {
                viewState.hideLoading()
                viewState.showCommonErrorScreen()
            }
        )
    }

    override fun onSynchronizationAllowed() =
        viewState.requestPermission(Manifest.permission.READ_CONTACTS)

    override fun onSynchronizationDisallowed() {
        model?.let {
            it.synchronizationAllowed = false
            it.synchronized = true
            viewState.updateSyncronization(it)

            executeCompletable(settingsRepo.setContactsSynchronized())
        }
    }

    override fun onRequestPermissionResult(granted: Boolean) {
        startWhenResumed { startSynchronization() }
    }

    private fun startSynchronization() {
        val currentModel = model ?: return

        viewState.showLoading()

        executeSingle(
            contactsInteractor.syncContacts()
                .flatMap { settingsRepo.setContactsSynchronized().andThen(Single.just(it)) },
            {
                currentModel.synchronizationAllowed = true
                currentModel.synchronized = true
                currentModel.contacts = it.toMutableList()
                model = currentModel

                viewState.hideLoading()
                if (!hasConnection) {
                    viewState.showNoInternetScreen()
                } else {
                    viewState.setupUi(currentModel)
                }
                viewState.showSnackBar(resourceManager.getString(R.string.snackbar_text_contacts_synchronized_is_success))
            },
            { viewState.showCommonErrorSnackBar() }
        )
    }

    override fun removeContact(position: Int) {
        if (!hasConnection) {
            viewState.showNoInternetSnackBar()
            return
        }

        val contacts = model?.contacts ?: return
        val contact = contacts[position]

        executeCompletable(
            contactsInteractor.removeContact(contact),
            {
                contacts.removeAt(position)
                viewState.removeContact(position, contact, contacts)
            },
            { viewState.showCommonErrorSnackBar() }
        )
    }

    override fun retry() {
        initialize()
    }
}