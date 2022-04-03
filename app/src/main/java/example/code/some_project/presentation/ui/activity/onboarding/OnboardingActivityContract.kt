package example.code.some_project.presentation.ui.activity.onboarding

import example.code.some_project.domain.repo.NetworkManager
import example.code.some_project.presentation.mvp.base.BaseActivityPresenter
import example.code.some_project.presentation.mvp.base.BaseActivityView
import example.code.some_project.presentation.ui.base.BaseActivityPresenterBase
import moxy.viewstate.strategy.alias.OneExecution

interface OnboardingActivityView: BaseActivityView {

    @OneExecution
    fun openMainPage()
}

interface OnboardingActivityPresenter : BaseActivityPresenter {

    fun onLastClicked()
}

abstract class AbstractOnboardingActivityPresenter(
    networkManager: NetworkManager
): BaseActivityPresenterBase<OnboardingActivityView>(
    networkManager
), OnboardingActivityPresenter