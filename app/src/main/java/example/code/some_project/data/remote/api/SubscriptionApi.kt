package example.code.some_project.data.remote.api

import io.reactivex.Single
import example.code.some_project.domain.models.SubscriptionInfo
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.http.*

interface SubscriptionApi {

    @Headers(HEADER_OPERATOR, HEADER_SOURCE_ANDROID)
    @POST("v1/service/{id}")
    fun connectSubscription(@Path("id") id: String): Single<Response<Unit>>

    @Headers(HEADER_OPERATOR, HEADER_SOURCE_ANDROID)
    @HTTP(method = "DELETE", path = "v1/service/{id}", hasBody = false)
    fun deleteSubscription(@Path("id") id: String): Single<Response<Unit>>

    @GET("v1/services")
    fun getSubscriptions(): Single<Response<List<SubscriptionInfo>>>

    companion object {
        fun create(retrofit: Retrofit): SubscriptionApi {
            return retrofit.create(SubscriptionApi::class.java)
        }
    }
}