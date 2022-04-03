package example.code.some_project.presentation.ui.fragment.about_product

import example.code.some_project.domain.interactor.LogoutInteractor
import example.code.some_project.domain.repo.NetworkManager
import example.code.some_project.presentation.mvp.base.BaseFragmentPresenterBase
import example.code.some_project.presentation.mvp.base.BaseFragmentView
import moxy.viewstate.strategy.alias.AddToEndSingle

interface AboutProductView: BaseFragmentView {

    @AddToEndSingle
    fun startWebView(url: String)

}

interface AboutProductPresenter

abstract class AbstractAboutProductPresenter(
    logoutInteractor: LogoutInteractor,
    networkManager: NetworkManager
) : BaseFragmentPresenterBase<AboutProductView>(
    logoutInteractor, networkManager
), AboutProductPresenter