package example.code.some_project.domain.interactor

import io.reactivex.Completable

interface LogoutInteractor {

    fun logout(): Completable
}