package example.code.some_project.domain.repo

import io.reactivex.Single

interface FirebaseRepo {

    fun getAuthOfferDescription(): Single<String>

    fun getWeekSubscriptionPrice(): Single<String>

    fun getWeekSubscriptionDescription(): Single<String>

    fun getMonthSubscriptionPrice(): Single<String>

    fun getMonthSubscriptionDescription(): Single<String>

}