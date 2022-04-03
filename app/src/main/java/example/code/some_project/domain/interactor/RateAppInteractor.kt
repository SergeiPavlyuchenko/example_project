package example.code.some_project.domain.interactor

import io.reactivex.Completable
import io.reactivex.Single

interface RateAppInteractor {

    fun showRateAppOnStart(): Single<Boolean>

    fun rateAppHiddenAlways(): Single<Boolean>

    fun hideRateAppAlways(): Completable
}