package example.code.some_project.domain.repo

import io.reactivex.Completable
import io.reactivex.Single

interface SettingsRepo {

    var currentLanguage: String

    fun isOnboardingShown(): Single<Boolean>

    fun setOnboardingShown(): Completable

    fun isContactsSynchronized(): Single<Boolean>

    fun setContactsSynchronized(): Completable

    fun isWeekHidden(): Single<Boolean>

    fun setWeekHidden(value: Boolean): Completable

    fun isRateAppHidden(): Single<Boolean>

    fun setRateAppHidden(): Completable

    fun updateStartCounter(): Completable

    fun getStartCount(): Single<Int>

}