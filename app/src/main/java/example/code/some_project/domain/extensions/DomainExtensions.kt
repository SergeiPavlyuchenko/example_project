package example.code.some_project.domain.extensions

import io.reactivex.Observable
import io.reactivex.Single
import example.code.some_project.domain.models.SubscriptionInfo
import example.code.some_project.domain.models.hasSubscription
import java.util.*

fun Long?.orZero() = this ?: 0L

fun Long?.orValue(value: Long) = this ?: value

fun Int?.orZero() = this ?: 0

fun Int?.orValue(value: Int) = this ?: value

fun Double?.orZero() = this ?: 0.0

fun Float?.orZero() = this ?: 0f

fun Float?.orValue(value: Float) = this ?: value

fun Date?.orEmpty() = this ?: Date(0)

fun Boolean?.orFalse() = this ?: false

fun Boolean?.orTrue() = this ?: true

fun String?.orValue(value: String) = this ?: value

fun <T : Any> T.justSingle() = Single.just(this)

fun Any.justObservable() = Observable.just(this)

fun List<SubscriptionInfo>.getCurrentSubscription() = if (this[0].hasSubscription) this[0] else this[1]

