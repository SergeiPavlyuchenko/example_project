package example.code.some_project.domain.repo

import io.reactivex.Completable

interface UserParametersRepo {

    fun setNewLanguage(language: String): Completable

}