package example.code.some_project.presentation.ui.fragment.contacts

import example.code.some_project.domain.interactor.LogoutInteractor
import example.code.some_project.domain.models.Contact
import example.code.some_project.domain.repo.NetworkManager
import example.code.some_project.presentation.mvp.base.BaseFragmentPresenter
import example.code.some_project.presentation.mvp.base.BaseFragmentPresenterBase
import example.code.some_project.presentation.mvp.base.BaseFragmentView
import moxy.viewstate.strategy.alias.AddToEndSingle
import moxy.viewstate.strategy.alias.OneExecution

interface ContactsView : BaseFragmentView {

    @AddToEndSingle
    fun setupUi(model: ContactsScreenModel)

    @AddToEndSingle
    fun updateSyncronization(model: ContactsScreenModel)

    @AddToEndSingle
    fun updateContacts(model: ContactsScreenModel)

    @OneExecution
    fun requestPermission(permission: String)

    @OneExecution
    fun removeContact(
        position: Int,
        contact: Contact,
        contacts: MutableList<Contact>,
    )

}

interface ContactsPresenter : BaseFragmentPresenter {

    fun onSynchronizationAllowed()

    fun onSynchronizationDisallowed()

    fun onRequestPermissionResult(granted: Boolean)

    fun removeContact(position: Int)

}

abstract class AbstractContactsPresenter(
    logoutInteractor: LogoutInteractor,
    networkManager: NetworkManager
) : BaseFragmentPresenterBase<ContactsView>(logoutInteractor, networkManager), ContactsPresenter

data class ContactsScreenModel(
    var synchronizationAllowed: Boolean,
    var synchronized: Boolean,
    var contacts: MutableList<Contact>,
    val selfMsisdn: String
)