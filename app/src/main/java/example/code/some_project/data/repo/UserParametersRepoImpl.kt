package example.code.some_project.data.repo

import io.reactivex.Completable
import example.code.some_project.data.remote.api.UserParametersApi
import example.code.some_project.domain.repo.AuthRepo
import example.code.some_project.domain.repo.UserParametersRepo

class UserParametersRepoImpl(
    private val authRepo: AuthRepo,
    private val userParametersApi: UserParametersApi
): UserParametersRepo {

    override fun setNewLanguage(language: String): Completable = authRepo.makeAuthorizedRequest {
        userParametersApi.setNewLanguage(language)
    }
        .ignoreElement()
}