package example.code.some_project.presentation.ui.activity.splash

import example.code.some_project.domain.repo.NetworkManager
import example.code.some_project.presentation.mvp.base.BaseActivityPresenter
import example.code.some_project.presentation.mvp.base.BaseActivityView
import example.code.some_project.presentation.ui.base.BaseActivityPresenterBase
import moxy.viewstate.strategy.alias.OneExecution

interface SplashView: BaseActivityView {

    @OneExecution
    fun openOnboarding()

    @OneExecution
    fun openAuthorization()

    @OneExecution
    fun openMain()

    @OneExecution
    fun initializationFailed()

}

interface SplashPresenter: BaseActivityPresenter

abstract class AbstractSplashPresenter(
    networkManager: NetworkManager
): BaseActivityPresenterBase<SplashView>(networkManager), SplashPresenter