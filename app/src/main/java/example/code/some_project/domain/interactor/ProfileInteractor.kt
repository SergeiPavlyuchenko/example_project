package example.code.some_project.domain.interactor

import io.reactivex.Completable

interface ProfileInteractor {

    fun getCurrentLanguage(): String

    fun setBackEndLanguage(language: String): Completable

    fun setNewAppLanguage(language: String): Completable

}