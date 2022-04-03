package example.code.some_project.data.repo

import com.google.firebase.remoteconfig.FirebaseRemoteConfig
import com.google.firebase.remoteconfig.FirebaseRemoteConfigSettings
import io.reactivex.Single
import example.code.some_project.BuildConfig
import example.code.some_project.R
import example.code.some_project.domain.repo.FirebaseRepo
import example.code.some_project.domain.repo.device.ResourcesManager
import example.code.some_project.presentation.SomeApplication
import example.code.some_project.presentation.ui.util.Constansts.KEY_KZ_LANGUAGE
import example.code.some_project.presentation.ui.util.Constansts.KEY_RU_LANGUAGE
import timber.log.Timber

class FirebaseRepoImpl(resourcesManager: ResourcesManager) : FirebaseRepo {

    private val defaultValues: Map<String, String> = with(resourcesManager) {
        mapOf(
            AUTH_OFFER_DESCRIPTION_RU to getString(R.string.auth_offer_description),
            AUTH_OFFER_DESCRIPTION_KK to getString(R.string.auth_offer_description),
            MONTH_TARIFF_DESCRIPTION_RU to getString(R.string.manage_subscription_month_description),
            MONTH_TARIFF_DESCRIPTION_KK to getString(R.string.manage_subscription_month_description),
            MONTH_TARIFF_PRICE_RU to getString(R.string.manage_subscription_month_price),
            MONTH_TARIFF_PRICE_KK to getString(R.string.manage_subscription_month_price),
            WEEK_TARIFF_DESCRIPTION_RU to getString(R.string.manage_subscription_week_description),
            WEEK_TARIFF_DESCRIPTION_KK to getString(R.string.manage_subscription_week_description),
            WEEK_TARIFF_PRICE_RU to getString(R.string.manage_subscription_week_price),
            WEEK_TARIFF_PRICE_KK to getString(R.string.manage_subscription_week_price),
        )
    }


    private var remoteConfig: FirebaseRemoteConfig

    init {
        remoteConfig = getFirebaseRemoteConfig()
    }

    private fun getFirebaseRemoteConfig(): FirebaseRemoteConfig {
        val remoteConfig = FirebaseRemoteConfig.getInstance()
        val configSettings = FirebaseRemoteConfigSettings.Builder().apply {
            minimumFetchIntervalInSeconds = if (BuildConfig.DEBUG) 0 else 60
        }.build()

        return remoteConfig.apply {
            setConfigSettingsAsync(configSettings)
            setDefaultsAsync(defaultValues)
            fetchAndActivate().addOnCompleteListener { task ->
                Timber.i("$TAG. Remote Config Fetch Complete, task: $task")
            }
        }
    }

    override fun getAuthOfferDescription(): Single<String> =
        Single.fromCallable { SomeApplication.currentLanguage }
            .map {
                when (it) {
                    KEY_RU_LANGUAGE -> AUTH_OFFER_DESCRIPTION_RU
                    KEY_KZ_LANGUAGE -> AUTH_OFFER_DESCRIPTION_KK
                    else -> throw IllegalArgumentException("unknown language")
                }
            }
            .map { remoteConfig.getString(it) }

    override fun getWeekSubscriptionPrice(): Single<String> =
        Single.fromCallable { SomeApplication.currentLanguage }
            .map {
                when (it) {
                    KEY_RU_LANGUAGE -> WEEK_TARIFF_PRICE_RU
                    KEY_KZ_LANGUAGE -> WEEK_TARIFF_PRICE_KK
                    else -> throw IllegalArgumentException("unknown language")
                }
            }
            .map { remoteConfig.getString(it) }

    override fun getWeekSubscriptionDescription(): Single<String> =
        Single.fromCallable { SomeApplication.currentLanguage }
            .map {
                when (it) {
                    KEY_RU_LANGUAGE -> WEEK_TARIFF_DESCRIPTION_RU
                    KEY_KZ_LANGUAGE -> WEEK_TARIFF_DESCRIPTION_KK
                    else -> throw IllegalArgumentException("unknown language")
                }
            }
            .map { remoteConfig.getString(it) }

    override fun getMonthSubscriptionPrice(): Single<String> =
        Single.fromCallable { SomeApplication.currentLanguage }
            .map {
                when (it) {
                    KEY_RU_LANGUAGE -> MONTH_TARIFF_PRICE_RU
                    KEY_KZ_LANGUAGE -> MONTH_TARIFF_PRICE_KK
                    else -> throw IllegalArgumentException("unknown language")
                }
            }
            .map { remoteConfig.getString(it) }

    override fun getMonthSubscriptionDescription(): Single<String> =
        Single.fromCallable { SomeApplication.currentLanguage }
            .map {
                when (it) {
                    KEY_RU_LANGUAGE -> MONTH_TARIFF_DESCRIPTION_RU
                    KEY_KZ_LANGUAGE -> MONTH_TARIFF_DESCRIPTION_KK
                    else -> throw IllegalArgumentException("unknown language")
                }
            }
            .map { remoteConfig.getString(it) }

    companion object {
        private const val TAG = "RemoteConfigRepo"

        private const val AUTH_OFFER_DESCRIPTION_RU = "auth_offer_description_ru"
        private const val AUTH_OFFER_DESCRIPTION_KK = "auth_offer_description_kk"
        private const val MONTH_TARIFF_DESCRIPTION_RU = "month_tariff_description_ru"
        private const val MONTH_TARIFF_DESCRIPTION_KK = "month_tariff_description_kk"
        private const val MONTH_TARIFF_PRICE_RU = "month_tariff_price_ru"
        private const val MONTH_TARIFF_PRICE_KK = "month_tariff_price_kk"
        private const val WEEK_TARIFF_DESCRIPTION_RU = "week_tariff_description_ru"
        private const val WEEK_TARIFF_DESCRIPTION_KK = "week_tariff_description_kk"
        private const val WEEK_TARIFF_PRICE_RU = "week_tariff_price_ru"
        private const val WEEK_TARIFF_PRICE_KK = "week_tariff_price_kk"
    }
}