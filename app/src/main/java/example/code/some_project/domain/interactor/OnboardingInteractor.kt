package example.code.some_project.domain.interactor

import io.reactivex.Completable
import io.reactivex.Single

interface OnboardingInteractor {

    fun setOnboardingShown(): Completable

    fun isAuthorized(): Single<Boolean>
}