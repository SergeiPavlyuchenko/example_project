package example.code.some_project.presentation.ui.fragment.editcontact

import example.code.some_project.domain.interactor.LogoutInteractor
import example.code.some_project.domain.models.Contact
import example.code.some_project.domain.repo.NetworkManager
import example.code.some_project.presentation.mvp.base.BaseFragmentPresenter
import example.code.some_project.presentation.mvp.base.BaseFragmentPresenterBase
import example.code.some_project.presentation.mvp.base.BaseFragmentView
import moxy.viewstate.strategy.alias.AddToEndSingle
import moxy.viewstate.strategy.alias.OneExecution

interface EditContactView : BaseFragmentView {

    @AddToEndSingle
    fun setupContact(contact: Contact?)

    @AddToEndSingle
    fun setupMsisdn(msisdn: String)

    @OneExecution
    fun onSuccessfullySaved(contact: Contact)

    @OneExecution
    fun showEmptyName()

    @OneExecution
    fun showEmptyPhone()

    @OneExecution
    fun showInvalidPhone()

    @OneExecution
    fun showAlreadyHasContact()
}

interface EditContactPresenter : BaseFragmentPresenter {

    var msisdn: String?

    fun saveContact(name: String?, phone: String?)
}

abstract class AbstractEditContactPresenter(
    logoutInteractor: LogoutInteractor,
    networkManager: NetworkManager
) : BaseFragmentPresenterBase<EditContactView>(
    logoutInteractor, networkManager
), EditContactPresenter