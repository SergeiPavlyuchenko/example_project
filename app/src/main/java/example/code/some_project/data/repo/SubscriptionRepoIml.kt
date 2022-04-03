package example.code.some_project.data.repo

import io.reactivex.Completable
import io.reactivex.Single
import example.code.some_project.data.remote.api.SubscriptionApi
import example.code.some_project.domain.models.SubscriptionInfo
import example.code.some_project.domain.repo.AuthRepo
import example.code.some_project.domain.repo.SubscriptionRepo

class SubscriptionRepoIml(
    private val authRepo: AuthRepo,
    private val subscriptionApi: SubscriptionApi
) : SubscriptionRepo {

    override fun getSubscriptions(): Single<List<SubscriptionInfo>> = authRepo
        .makeAuthorizedRequest { subscriptionApi.getSubscriptions() }
        .map { it.body() }

    override fun connectWeekSubscription(): Completable = authRepo
        .makeAuthorizedRequest { subscriptionApi.connectSubscription(WEEK_SUBSCRIPTION_ID) }
        .ignoreElement()

    override fun connectMonthSubscription(): Completable = authRepo
        .makeAuthorizedRequest { subscriptionApi.connectSubscription(MONTH_SUBSCRIPTION_ID) }
        .ignoreElement()

    override fun deleteSubscription(id: String): Completable = authRepo
        .makeAuthorizedRequest { subscriptionApi.deleteSubscription(id) }
        .ignoreElement()

    companion object {
        private const val WEEK_SUBSCRIPTION_ID = "411441"
        private const val MONTH_SUBSCRIPTION_ID = "411440"
    }
}