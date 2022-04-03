package example.code.some_project.presentation.mvp.base

import example.code.some_project.domain.interactor.LogoutInteractor
import example.code.some_project.domain.repo.NetworkManager


abstract class BaseFragmentPresenterBase<V : BaseFragmentView>(
    logoutInteractor: LogoutInteractor,
    networkManager: NetworkManager
): BaseMvpPresenter<V>(logoutInteractor, networkManager), BaseFragmentPresenter {

    override var processCommand: (() -> Unit)? = null

    fun onManageSubscriptionResult(resultCode: String?) {
        when (resultCode) {
        }
    }

    override fun onViewCreated() {
        // does nothing by default
    }
}