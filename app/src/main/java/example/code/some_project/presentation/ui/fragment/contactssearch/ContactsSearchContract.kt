package example.code.some_project.presentation.ui.fragment.contactssearch

import example.code.some_project.domain.interactor.LogoutInteractor
import example.code.some_project.domain.models.Contact
import example.code.some_project.domain.repo.NetworkManager
import example.code.some_project.presentation.mvp.base.BaseFragmentPresenter
import example.code.some_project.presentation.mvp.base.BaseFragmentPresenterBase
import example.code.some_project.presentation.mvp.base.BaseFragmentView
import moxy.viewstate.strategy.alias.AddToEndSingle
import moxy.viewstate.strategy.alias.OneExecution

interface ContactsSearchView : BaseFragmentView {

    @AddToEndSingle
    fun updateContacts(contacts: MutableList<Contact>, isBlankSearch: Boolean)

    @OneExecution
    fun removeContact(contact: Contact)

}

interface ContactsSearchPresenter : BaseFragmentPresenter {

    fun removeContact(position: Int)

    fun searchTextChanged(searchString: String)
}

abstract class AbstractContactsSearchPresenter(
    logoutInteractor: LogoutInteractor,
    networkManager: NetworkManager
) : BaseFragmentPresenterBase<ContactsSearchView>(
    logoutInteractor, networkManager
), ContactsSearchPresenter

data class ContactsScreenModel(
    var contacts: MutableList<Contact>,
    var showContacts: MutableList<Contact>,
    val selfMsisdn: String
)