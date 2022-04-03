package example.code.some_project.domain.interactor

import io.reactivex.Completable
import io.reactivex.Single
import example.code.some_project.domain.models.SubscriptionInfo

interface SubscriptionInteractor {

    fun getSubscriptions(): Single<List<SubscriptionInfo>>

    fun connectWeekSubscription(): Completable

    fun connectMonthSubscription(): Completable

    fun deleteWeekSubscription(): Completable

    fun deleteMonthSubscription(): Completable

    fun getWeekSubscriptionPrice(): Single<String>

    fun getWeekSubscriptionDescription(): Single<String>

    fun getMonthSubscriptionPrice(): Single<String>

    fun getMonthSubscriptionDescription(): Single<String>

}