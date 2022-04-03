package example.code.some_project.domain.interactor.implementation

import io.reactivex.Completable
import io.reactivex.Single
import example.code.some_project.domain.interactor.OnboardingInteractor
import example.code.some_project.domain.repo.AuthRepo
import example.code.some_project.domain.repo.SettingsRepo

class OnboardingInteractorImpl(
    private val onboardingRepo: SettingsRepo,
    private val authRepo: AuthRepo
) : OnboardingInteractor {
    override fun setOnboardingShown(): Completable = onboardingRepo.setOnboardingShown()

    override fun isAuthorized(): Single<Boolean> = authRepo.getAuthorized()
}