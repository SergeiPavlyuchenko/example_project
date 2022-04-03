package example.code.some_project.data.remote.api

import io.reactivex.Single
import example.code.some_project.data.remote.models.AuthResponse
import example.code.some_project.data.remote.models.UpdateTokensResponse
import example.code.some_project.domain.models.AuthData
import example.code.some_project.domain.models.RegistrationData
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.http.Body
import retrofit2.http.Header
import retrofit2.http.Headers
import retrofit2.http.PATCH
import retrofit2.http.POST
import retrofit2.http.Query

interface AuthApi {

    @Headers(HEADER_OPERATOR, HEADER_SOURCE_ANDROID)
    @POST("v1/authorize")
    fun authorize(
        @Body authData: AuthData
    ): Single<Response<AuthResponse>>

    @PATCH("v1/authorize/token")
    fun updateTokens(
        @Query("refreshToken") refreshToken: String
    ): Single<Response<UpdateTokensResponse>>

    @Headers(HEADER_OPERATOR, HEADER_SOURCE_ANDROID)
    @POST("v1/register")
    fun sendSmsCode(
        @Header(HEADER_NAME_ACCEPT_LANGUAGE) language: String,
        @Body registrationData: RegistrationData
    ): Single<Response<Unit>>

    companion object {
        fun create(retrofit: Retrofit): AuthApi {
            return retrofit.create(AuthApi::class.java)
        }
    }
}