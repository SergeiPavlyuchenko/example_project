package example.code.some_project.domain.interactor.implementation

import io.reactivex.Completable
import example.code.some_project.domain.interactor.ProfileInteractor
import example.code.some_project.domain.repo.AuthRepo
import example.code.some_project.domain.repo.SettingsRepo
import example.code.some_project.domain.repo.UserParametersRepo
import example.code.some_project.presentation.SomeApplication

class ProfileInteractorImpl(
    private val settingsRepo: SettingsRepo,
    private val userParametersRepo: UserParametersRepo,
    private val authRepo: AuthRepo
) : ProfileInteractor {

    override fun getCurrentLanguage(): String = settingsRepo.currentLanguage

    override fun setBackEndLanguage(language: String): Completable =
        userParametersRepo.setNewLanguage(language)

    override fun setNewAppLanguage(language: String): Completable = Completable.fromAction {
        settingsRepo.currentLanguage = language
        SomeApplication.currentLanguage = language
    }

}