package example.code.some_project.domain.interactor.implementation

import io.reactivex.Completable
import io.reactivex.Single
import example.code.some_project.domain.interactor.SubscriptionInteractor
import example.code.some_project.domain.models.SubscriptionInfo
import example.code.some_project.domain.repo.FirebaseRepo
import example.code.some_project.domain.repo.SubscriptionRepo

class SubscriptionInteractorImpl(
    private val subscriptionRepo: SubscriptionRepo,
    private val firebaseRepo: FirebaseRepo
) : SubscriptionInteractor {

    override fun getSubscriptions(): Single<List<SubscriptionInfo>> = subscriptionRepo
        .getSubscriptions()

    override fun connectWeekSubscription(): Completable = subscriptionRepo
        .connectWeekSubscription()

    override fun connectMonthSubscription(): Completable = subscriptionRepo
        .connectMonthSubscription()

    override fun deleteWeekSubscription(): Completable = subscriptionRepo
        .deleteSubscription(SubscriptionInfo.SUBSCRIPTION_TYPE_WEEK)

    override fun deleteMonthSubscription(): Completable = subscriptionRepo
        .deleteSubscription(SubscriptionInfo.SUBSCRIPTION_TYPE_MONTH)

    override fun getWeekSubscriptionPrice(): Single<String> = firebaseRepo
        .getWeekSubscriptionPrice()

    override fun getWeekSubscriptionDescription(): Single<String> = firebaseRepo
        .getWeekSubscriptionDescription()

    override fun getMonthSubscriptionPrice(): Single<String> = firebaseRepo
        .getMonthSubscriptionPrice()

    override fun getMonthSubscriptionDescription(): Single<String> = firebaseRepo
        .getMonthSubscriptionDescription()
}