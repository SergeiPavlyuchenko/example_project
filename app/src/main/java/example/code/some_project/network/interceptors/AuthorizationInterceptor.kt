package example.code.some_project.network.interceptors

import okhttp3.Interceptor
import okhttp3.Response

class AuthorizationInterceptor(private val accessToken: String) : Interceptor {

    override fun intercept(chain: Interceptor.Chain): Response =
        chain.proceed(
            chain.request()
                .newBuilder()
                .header(HEADER_AUTHORIZATION, accessToken)
                .build()
        )

    companion object {

        const val HEADER_AUTHORIZATION = "Authorization"
    }
}