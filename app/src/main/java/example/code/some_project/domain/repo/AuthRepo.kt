package example.code.some_project.domain.repo

import io.reactivex.Completable
import io.reactivex.Single
import example.code.some_project.data.remote.models.AuthResponse
import example.code.some_project.data.remote.models.UpdateTokensResponse
import example.code.some_project.domain.models.AuthData
import example.code.some_project.domain.models.RegistrationData
import retrofit2.Response

interface AuthRepo {

    fun authorize(authData: AuthData): Single<AuthResponse>

    fun updateTokens(): Single<UpdateTokensResponse>

    fun sendSmsCode(registrationData: RegistrationData): Completable

    fun saveTokensAfterAuth(data: AuthResponse)

    fun getAuthorized(): Single<Boolean>

    fun getUserMsisdn(): Single<String>

    fun clearAuthorization(): Completable

    fun <T : Response<K>, K> makeAuthorizedRequest(baseRequest: () -> Single<T>): Single<T>

}