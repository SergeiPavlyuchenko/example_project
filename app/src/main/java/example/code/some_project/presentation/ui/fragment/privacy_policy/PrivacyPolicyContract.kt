package example.code.some_project.presentation.ui.fragment.privacy_policy

import example.code.some_project.domain.interactor.LogoutInteractor
import example.code.some_project.domain.repo.NetworkManager
import example.code.some_project.presentation.mvp.base.BaseFragmentPresenterBase
import example.code.some_project.presentation.mvp.base.BaseFragmentView
import moxy.viewstate.strategy.alias.AddToEndSingle

interface PrivacyPolicyView: BaseFragmentView {

    @AddToEndSingle
    fun startWebView(url: String)

}

interface PrivacyPolicyPresenter

abstract class AbstractPrivacyPolicyPresenter(
    logoutInteractor: LogoutInteractor,
    networkManager: NetworkManager
): BaseFragmentPresenterBase<PrivacyPolicyView>(
    logoutInteractor, networkManager
), PrivacyPolicyPresenter