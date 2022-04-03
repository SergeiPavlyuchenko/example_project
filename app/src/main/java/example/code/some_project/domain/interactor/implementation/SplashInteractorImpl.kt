package example.code.some_project.domain.interactor.implementation

import io.reactivex.Completable
import io.reactivex.Single
import example.code.some_project.domain.interactor.SplashInteractor
import example.code.some_project.domain.repo.AuthRepo
import example.code.some_project.domain.repo.SettingsRepo
import example.code.some_project.presentation.SomeApplication

class SplashInteractorImpl(
    private val authRepo: AuthRepo,
    private val settingsRepo: SettingsRepo,
) : SplashInteractor {

    override fun initialize(): Single<Pair<Boolean, Boolean>> =
        setCurrentLanguage()
            .andThen(settingsRepo.updateStartCounter())
            .andThen(getInitParamsSingle())

    private fun setCurrentLanguage(): Completable = Completable.fromAction {
        SomeApplication.currentLanguage = settingsRepo.currentLanguage
    }

    private fun getInitParamsSingle() =
        Single.zip(
            authRepo.getAuthorized(),
            settingsRepo.isOnboardingShown(),
            { auth, shown -> Pair(auth, shown) }
        )
}