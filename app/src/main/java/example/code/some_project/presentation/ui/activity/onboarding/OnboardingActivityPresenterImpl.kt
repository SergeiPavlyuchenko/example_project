package example.code.some_project.presentation.ui.activity.onboarding

import example.code.some_project.domain.interactor.OnboardingInteractor
import example.code.some_project.domain.repo.NetworkManager
import moxy.InjectViewState

@InjectViewState
class OnboardingActivityPresenterImpl(
    private val onboardingInteractor: OnboardingInteractor,
    networkManager: NetworkManager
) : AbstractOnboardingActivityPresenter(networkManager) {

    override fun onResume() {
        super.onResume()
        setOnboardingShown()
    }

    private fun setOnboardingShown() {
        executeCompletable(onboardingInteractor.setOnboardingShown())
    }

    override fun onLastClicked() {
        executeSingle(
            onboardingInteractor.isAuthorized(),
            { authorized ->
                if (authorized) {
                    viewState.openMainPage()
                } else {
                    viewState.openAuth()
                }
            },
            { viewState.showCommonErrorSnackBar() }
        )
    }

}