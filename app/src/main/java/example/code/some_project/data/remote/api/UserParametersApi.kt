package example.code.some_project.data.remote.api

import io.reactivex.Single
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.http.Header
import retrofit2.http.PUT

interface UserParametersApi {

    @PUT("v1/subscriber/language")
    fun setNewLanguage(
        @Header(HEADER_NAME_ACCEPT_LANGUAGE) language: String,
    ): Single<Response<Unit>>

    companion object {
        fun create(retrofit: Retrofit): UserParametersApi {
            return retrofit.create(UserParametersApi::class.java)
        }

    }
}