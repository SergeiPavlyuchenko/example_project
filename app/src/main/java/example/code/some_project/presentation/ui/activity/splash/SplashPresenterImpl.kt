package example.code.some_project.presentation.ui.activity.splash

import example.code.some_project.domain.interactor.SplashInteractor
import example.code.some_project.domain.repo.NetworkManager
import moxy.InjectViewState

@InjectViewState
class SplashPresenterImpl(
    private val splashInteractor: SplashInteractor,
    networkManager: NetworkManager
) : AbstractSplashPresenter(networkManager) {

    override fun onResume() {
        super.onResume()

        executeSingle(
            splashInteractor.initialize(),
            { (authorized, onboardingShown) ->
                if (!onboardingShown) {
                    viewState.openOnboarding()
                } else if (!authorized) {
                    viewState.openAuthorization()
                } else {
                    viewState.openMain()
                }
            },
            { viewState.initializationFailed() }
        )
    }
}