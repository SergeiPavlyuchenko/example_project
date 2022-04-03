package example.code.some_project.data.repo

import android.content.SharedPreferences
import io.reactivex.Completable
import io.reactivex.Single
import io.reactivex.schedulers.Schedulers
import example.code.some_project.data.util.string
import example.code.some_project.domain.repo.SettingsRepo
import example.code.some_project.domain.datasource.local.storage.LocalStorage
import example.code.some_project.presentation.ui.util.Constansts.KEY_RU_LANGUAGE

class SettingsRepoImpl(
    private val storage: LocalStorage,
    private val userPrefs: SharedPreferences
) : SettingsRepo {

    override var currentLanguage: String by userPrefs.string(key = { LANGUAGE_KEY }, defaultValue = KEY_RU_LANGUAGE)

    override fun isOnboardingShown(): Single<Boolean> = storage
        .read<Boolean>(SETTINGS_BOOK, ONBOARDING_SHOWN_KEY)
        .onErrorReturn { false }
        .subscribeOn(Schedulers.io())

    override fun setOnboardingShown(): Completable = storage
        .write(SETTINGS_BOOK, ONBOARDING_SHOWN_KEY, true)
        .observeOn(Schedulers.io())

    override fun isContactsSynchronized(): Single<Boolean> = storage
        .read<Boolean>(SETTINGS_BOOK, CONTACTS_SYNCHRONIZED_KEY)
        .onErrorReturn { false }
        .subscribeOn(Schedulers.io())

    override fun setContactsSynchronized(): Completable = storage
        .write(SETTINGS_BOOK, CONTACTS_SYNCHRONIZED_KEY, true)
        .observeOn(Schedulers.io())

    override fun isWeekHidden(): Single<Boolean> = storage
        .read<Boolean>(SETTINGS_BOOK, WEEK_HIDDEN_KEY)
        .onErrorReturn { false }

    override fun setWeekHidden(value: Boolean): Completable = storage
        .write(SETTINGS_BOOK, WEEK_HIDDEN_KEY, value)

    override fun isRateAppHidden(): Single<Boolean> = storage
        .read<Boolean>(SETTINGS_BOOK, RATE_APP_HIDDEN_KEY)
        .onErrorReturn { false }

    override fun setRateAppHidden(): Completable = storage
        .write(SETTINGS_BOOK, RATE_APP_HIDDEN_KEY, true)

    override fun updateStartCounter(): Completable = getStartCount()
        .flatMapCompletable {
            val newCount = if (it == 4) 0 else it + 1
            storage.write(SETTINGS_BOOK, START_COUNT_KEY, newCount)
        }

    override fun getStartCount(): Single<Int> = storage
        .read<Int>(SETTINGS_BOOK, START_COUNT_KEY)
        .onErrorReturn { -1 }

    companion object {
        private const val SETTINGS_BOOK = "SETTINGS_BOOK"

        private const val ONBOARDING_SHOWN_KEY = "ONBOARDING_SHOWN_KEY"
        private const val CONTACTS_SYNCHRONIZED_KEY = "CONTACTS_SYNCHRONIZED_KEY"
        private const val LANGUAGE_KEY = "LANGUAGE_KEY"
        private const val WEEK_HIDDEN_KEY = "WEEK_HIDDEN_KEY"
        private const val RATE_APP_HIDDEN_KEY = "RATE_APP_HIDDEN_KEY"
        private const val START_COUNT_KEY = "START_COUNT_KEY"
    }
}