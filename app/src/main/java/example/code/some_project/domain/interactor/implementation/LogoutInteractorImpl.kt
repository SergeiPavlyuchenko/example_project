package example.code.some_project.domain.interactor.implementation

import io.reactivex.Completable
import example.code.some_project.domain.interactor.LogoutInteractor
import example.code.some_project.domain.repo.AuthRepo

class LogoutInteractorImpl(private val authRepo: AuthRepo) : LogoutInteractor {

    override fun logout(): Completable = authRepo.clearAuthorization()
}