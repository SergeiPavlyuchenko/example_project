package example.code.some_project.domain.interactor

import io.reactivex.Completable
import io.reactivex.Single
import example.code.some_project.data.remote.models.AuthResponse
import example.code.some_project.domain.models.AuthData
import example.code.some_project.domain.models.RegistrationData

interface AuthInteractor {

    fun getAuthOfferDescription(): Single<String>

    fun authorize(authData: AuthData): Single<AuthResponse>

    fun saveTokens(response: AuthResponse)

    fun sendSmsCode(registrationData: RegistrationData): Completable

}