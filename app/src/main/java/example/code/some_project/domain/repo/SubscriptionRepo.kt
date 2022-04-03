package example.code.some_project.domain.repo

import io.reactivex.Completable
import io.reactivex.Single
import example.code.some_project.domain.models.SubscriptionInfo

interface SubscriptionRepo {

    fun getSubscriptions(): Single<List<SubscriptionInfo>>

    fun connectWeekSubscription(): Completable

    fun connectMonthSubscription(): Completable

    fun deleteSubscription(id: String): Completable

}