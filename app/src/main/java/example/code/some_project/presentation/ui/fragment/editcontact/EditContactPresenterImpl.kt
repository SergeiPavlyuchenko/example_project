package example.code.some_project.presentation.ui.fragment.editcontact

import example.code.some_project.domain.interactor.ContactsInteractor
import example.code.some_project.domain.interactor.EditContactInteractor
import example.code.some_project.domain.interactor.LogoutInteractor
import example.code.some_project.domain.models.Contact
import example.code.some_project.domain.repo.NetworkManager
import example.code.some_project.domain.repo.SettingsRepo
import example.code.some_project.presentation.utils.CharInputFilter
import example.code.some_project.util.maskFiltered
import moxy.InjectViewState

@InjectViewState
class EditContactPresenterImpl(
    logoutInteractor: LogoutInteractor,
    private val editContactInteractor: EditContactInteractor,
    private val contactsInteractor: ContactsInteractor,
    private val settingsRepo: SettingsRepo,
    networkManager: NetworkManager
) : AbstractEditContactPresenter(logoutInteractor, networkManager) {

    override var msisdn: String? = null

    private var initialized: Boolean = false
    private var originalContact: Contact? = null

    override fun onResume() {
        super.onResume()
        if (!initialized) {
            initialize()
        }
    }

    private fun initialize() {
        val localMsisdn = msisdn
        if (localMsisdn == null) {
            initialized = true
            viewState.hideLoading()
            viewState.setupContact(null)
            return
        }

        viewState.showLoading()

        executeSingle(
            editContactInteractor.loadContact(localMsisdn),
            {
                initialized = true
                viewState.hideLoading()
                if (it.present) {
                    originalContact = it.get()
                    viewState.setupContact(it.get())
                } else {
                    viewState.setupMsisdn(localMsisdn)
                }
            },
            {
                viewState.hideLoading()
                viewState.showCommonErrorScreen()
            }
        )
    }

    override fun saveContact(name: String?, phone: String?) {
        var hasError = false

        if (name.isNullOrBlank()) {
            viewState.showEmptyName()
            hasError = true
        }

        if (phone.isNullOrBlank()) {
            viewState.showEmptyPhone()
            hasError = true
        } else if (!validatePhone(phone)) {
            viewState.showInvalidPhone()
            hasError = true
        }

        if (hasError) {
            return
        }

        val contact = Contact(phone!!, name!!)

        executeSingle(
            settingsRepo.isContactsSynchronized(),
            onSuccessAction = { isSynchronized ->
                if (isSynchronized) {
                    processingSaveContact(contact)
                } else {
                    saveContact(contact)
                }
            },
            onErrorAction = { viewState.showCommonErrorSnackBar() }
        )
    }

    private fun processingSaveContact(contact: Contact) {
        executeSingle(
            contactsInteractor.syncContacts(),
            onSuccessAction = { contactList ->
                if (
                    contactList.map { it.number.maskFiltered() }
                        .contains(contact.number.maskFiltered())
                    && originalContact == null
                ) {
                    viewState.showAlreadyHasContact()
                } else {
                    saveContact(contact)
                }
            },
            onErrorAction = { viewState.showCommonErrorSnackBar() }
        )
    }

    private fun saveContact(contact: Contact) {
        executeCompletable(
            editContactInteractor.saveContact(originalContact, contact),
            {
                originalContact = null
                viewState.onSuccessfullySaved(contact)
            },
            { viewState.showCommonErrorSnackBar() }
        )
    }

    private fun validatePhone(phone: String?): Boolean {
        if (phone.isNullOrBlank()) {
            return false
        }

        if (phone.length < 2) {
            return false
        }

        val first = phone[0]
        val others = phone.substring(1)
        return (first.isDigit() || first == '+')
                && others.all { it in CharInputFilter.editContactNumberCharacterSet }
    }

}