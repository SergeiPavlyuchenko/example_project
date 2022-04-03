package example.code.some_project.domain.interactor.implementation

import io.reactivex.Completable
import io.reactivex.Single
import example.code.some_project.domain.interactor.RateAppInteractor
import example.code.some_project.domain.repo.SettingsRepo

class RateAppInteractorImpl(private val settingsRepo: SettingsRepo): RateAppInteractor {

    override fun showRateAppOnStart(): Single<Boolean> =
        Single.zip(
            settingsRepo.isRateAppHidden(),
            settingsRepo.getStartCount(),
            { hidden, count -> !hidden && count == 4 }
        )

    override fun rateAppHiddenAlways(): Single<Boolean> = settingsRepo.isRateAppHidden()

    override fun hideRateAppAlways(): Completable = settingsRepo.setRateAppHidden()
}