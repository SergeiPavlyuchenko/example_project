package example.code.some_project.network

import com.squareup.moshi.Moshi
import example.code.some_project.data.remote.api.AuthApi
import example.code.some_project.data.remote.api.SubscriptionApi
import example.code.some_project.data.remote.api.UserParametersApi
import example.code.some_project.domain.datasource.local.AuthDataSourceLocal
import example.code.some_project.network.interceptors.NetworkInterceptor
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.moshi.MoshiConverterFactory
import java.util.concurrent.TimeUnit

class ServiceFactory(private val authDataSourceLocal: AuthDataSourceLocal) {

    private val okHttpClient = createOkHttpClient(authDataSourceLocal)
    private val moshi = createMoshi()
    private val retrofit = createRetrofit(okHttpClient, moshi)

    private val authApiService = AuthApi.create(retrofit)
    private val subscriptionApi = SubscriptionApi.create(retrofit)
    private val userParametersApi = UserParametersApi.create(retrofit)

    fun getAuthApiService() = authApiService

    fun getSubscriptionApiService() = subscriptionApi

    fun getUserParametersService() = userParametersApi

    private fun createOkHttpClient(authDataSourceLocal: AuthDataSourceLocal): OkHttpClient {
        val logger = HttpLoggingInterceptor()
        val timeout: Long = 30
        logger.level = HttpLoggingInterceptor.Level.BODY

        val okHttpBuilder = OkHttpClient.Builder()
            .connectTimeout(timeout, TimeUnit.SECONDS)
            .writeTimeout(timeout, TimeUnit.SECONDS)
            .readTimeout(timeout, TimeUnit.SECONDS)
            .addInterceptor(NetworkInterceptor(authDataSourceLocal))
            .addInterceptor(logger)

        return okHttpBuilder.build()
    }

    private fun createRetrofit(okHttpClient: OkHttpClient, moshi: Moshi): Retrofit =
        Retrofit.Builder()
            .client(okHttpClient)
            .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
            .addConverterFactory(MoshiConverterFactory.create(moshi))
            .baseUrl(NetworkConstants.BASE_URL)
            .build()

    private fun createMoshi(): Moshi = Moshi.Builder()
//        .add() // Add custom adapter if you need
        .build()
}