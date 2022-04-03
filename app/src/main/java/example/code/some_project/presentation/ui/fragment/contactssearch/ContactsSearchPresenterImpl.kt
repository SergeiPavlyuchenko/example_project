package example.code.some_project.presentation.ui.fragment.contactssearch

import androidx.compose.ui.text.intl.Locale
import androidx.compose.ui.text.toLowerCase
import io.reactivex.Completable
import io.reactivex.Single
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import example.code.some_project.domain.interactor.ContactsInteractor
import example.code.some_project.domain.interactor.ContactsWithBlacklistInteractor
import example.code.some_project.domain.interactor.LogoutInteractor
import example.code.some_project.domain.models.Contact
import example.code.some_project.domain.repo.NetworkManager
import example.code.some_project.util.isAllowedNumber
import example.code.some_project.util.toBlacklistNumber
import moxy.InjectViewState

@InjectViewState
class ContactsSearchPresenterImpl(
    logoutInteractor: LogoutInteractor,
    private val contactsWithBlacklistInteractor: ContactsWithBlacklistInteractor,
    private val contactsInteractor: ContactsInteractor,
    networkManager: NetworkManager
) : AbstractContactsSearchPresenter(logoutInteractor, networkManager) {

    private var model: ContactsScreenModel? = null

    private var currentSearch: String = ""

    override fun removeContact(position: Int) {
        if (!hasConnection) {
            viewState.showNoInternetSnackBar()
            return
        }

        val contacts = model?.showContacts ?: return
        val contact = contacts[position]

        executeCompletable(
            contactsInteractor.removeContact(contact),
            {
                contacts.removeAt(position)
                viewState.removeContact(contact)
            },
            {
                viewState.showCommonErrorSnackBar()
            }
        )
    }

    override fun searchTextChanged(searchString: String) {
        val processedSearchString = searchString.trim()
        if (processedSearchString == currentSearch) {
            return
        }

        currentSearch = processedSearchString
        search(processedSearchString)
    }

    private fun search(searchString: String) {
        if (!hasConnection) {
            viewState.showNoInternetScreen()
            return
        }

        viewState.hideErrorScreen()

        executeSingle(
            getLoadModelSingle()
                .map {
                    if (searchString.isBlank()) {
                        mutableListOf()
                    } else {
                        it.contacts.filter { contact ->
                            contact.name.toLowerCase(Locale.current)
                                .contains(searchString.toLowerCase(Locale.current))
                                    || contact.number.contains(searchString)
                        }.toMutableList()
                    }
                },
            { showContacts ->
                model?.let {
                    it.showContacts = showContacts
                    viewState.updateContacts(showContacts, currentSearch.isBlank())
                }
            },
            { viewState.showCommonErrorSnackBar() }
        )
    }

    private fun getLoadModelSingle() =
        if (model == null) {
            contactsWithBlacklistInteractor.prepareData()
                .map { data ->
                    ContactsScreenModel(
                        data.contacts,
                        mutableListOf(),
                        data.selfMsisdn
                    )
                }
                .observeOn(AndroidSchedulers.mainThread())
                .doOnSuccess {
                    model = it
                }
                .observeOn(Schedulers.computation())
        } else {
            Single.just(model)
        }

    override fun retry() {
        if (currentSearch.isNotBlank()) {
            search(currentSearch)
        }
    }
}