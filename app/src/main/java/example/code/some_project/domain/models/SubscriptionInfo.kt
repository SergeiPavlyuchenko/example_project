package example.code.some_project.domain.models

import android.annotation.SuppressLint
import android.os.Parcelable
import com.google.firebase.crashlytics.FirebaseCrashlytics
import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass
import kotlinx.android.parcel.Parcelize
import example.code.some_project.util.beforeDot
import timber.log.Timber
import java.text.SimpleDateFormat
import java.util.*
import java.util.concurrent.TimeUnit

@Parcelize
@JsonClass(generateAdapter = true)
class SubscriptionInfo(
    @Json(name = "id")
    val id: String,
    @Json(name = "name")
    val name: String,
    @Json(name = "price")
    val price: String,
    @Json(name = "status")
    val status: String,
    @Json(name = "expirationTime")
    val expirationTime: String? = null
) : Parcelable {

    val endTime: Date?
        get() =
            if (expirationTime == null) {
                null
            } else {
                try {
                    PARSE_DATE_FORMAT.parse(expirationTime)
                } catch (e: java.lang.Exception) {
                    FirebaseCrashlytics.getInstance().recordException(e)
                    Timber.e(e)
                    null
                }
            }

    val endTimeString: String?
        get() {
            return try {
                expirationTime?.let { time ->
                    PARSE_DATE_FORMAT.parse(time)?.time?.apply { this * 1000 }
                        ?.let { OUTPUT_DATE_FORMAT.format(it) }
                }
            } catch (e: Exception) {
                null
            }
        }

    val daysBeforeExpiration: Int
        get() {
            val expTime = endTime?.time

            return if (expTime == null) {
                0
            } else {
                val diff = expTime - System.currentTimeMillis()
                if (diff < 0) {
                    -1
                } else {
                    diff / TimeUnit.DAYS.toMillis(1) + 1
                }
            }.toInt()
        }

    companion object {
        const val SUBSCRIPTION_TYPE_WEEK = "411441"
        const val SUBSCRIPTION_TYPE_MONTH = "411440"

        const val SUBSCRIPTION_STATUS_ACTIVE = "ACTIVE"
        const val SUBSCRIPTION_STATUS_DISABLED = "DISABLED"
        const val SUBSCRIPTION_STATUS_BLOCKED = "BLOCKED"

        @SuppressLint("SimpleDateFormat")
        private val PARSE_DATE_FORMAT = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSSSS'Z'").apply {
            timeZone = TimeZone.getTimeZone("UTC")
        }

        @SuppressLint("SimpleDateFormat")
        private val OUTPUT_DATE_FORMAT = SimpleDateFormat("dd.MM.yyyy")
    }
}

@Parcelize
class SubscriptionList : ArrayList<SubscriptionInfo>(), Parcelable

val SubscriptionInfo.hasSubscription: Boolean
    get() = status != SubscriptionInfo.SUBSCRIPTION_STATUS_DISABLED &&
            (id == SubscriptionInfo.SUBSCRIPTION_TYPE_MONTH || id == SubscriptionInfo.SUBSCRIPTION_TYPE_WEEK)

val SubscriptionInfo.hasWeekSubscription: Boolean
    get() = status != SubscriptionInfo.SUBSCRIPTION_STATUS_DISABLED && id == SubscriptionInfo.SUBSCRIPTION_TYPE_WEEK

val SubscriptionInfo.hasMonthSubscription: Boolean
    get() = status != SubscriptionInfo.SUBSCRIPTION_STATUS_DISABLED && id == SubscriptionInfo.SUBSCRIPTION_TYPE_MONTH

val SubscriptionInfo.hasActiveSubscription: Boolean
    get() = status == SubscriptionInfo.SUBSCRIPTION_STATUS_ACTIVE
            && (id == SubscriptionInfo.SUBSCRIPTION_TYPE_WEEK || id == SubscriptionInfo.SUBSCRIPTION_TYPE_MONTH)

val SubscriptionInfo.hasActiveWeekSubscription: Boolean
    get() = status == SubscriptionInfo.SUBSCRIPTION_STATUS_ACTIVE && id == SubscriptionInfo.SUBSCRIPTION_TYPE_WEEK

val SubscriptionInfo.hasActiveMonthSubscription: Boolean
    get() = status == SubscriptionInfo.SUBSCRIPTION_STATUS_ACTIVE && id == SubscriptionInfo.SUBSCRIPTION_TYPE_MONTH

val SubscriptionInfo.hasBlockedSubscription: Boolean
    get() = hasSubscription && status == SubscriptionInfo.SUBSCRIPTION_STATUS_BLOCKED

val SubscriptionInfo.hasDisabledSubscription: Boolean
    get() = status == SubscriptionInfo.SUBSCRIPTION_STATUS_DISABLED

val SubscriptionInfo.isStatusWeek: Boolean get() = id == SubscriptionInfo.SUBSCRIPTION_TYPE_WEEK

val SubscriptionInfo.isStatusMonth: Boolean get() = id == SubscriptionInfo.SUBSCRIPTION_TYPE_MONTH

val List<SubscriptionInfo>.priceOfWeek: String get() = if (this.first().id == SubscriptionInfo.SUBSCRIPTION_TYPE_WEEK) this.first().price.beforeDot() else this.last().price.beforeDot()

val List<SubscriptionInfo>.priceOfMonth: String get() = if ((this.first().id == SubscriptionInfo.SUBSCRIPTION_TYPE_MONTH)) this.first().price.beforeDot() else this.last().price.beforeDot()