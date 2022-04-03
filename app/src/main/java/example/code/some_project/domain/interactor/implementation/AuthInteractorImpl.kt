package example.code.some_project.domain.interactor.implementation

import io.reactivex.Completable
import io.reactivex.Single
import io.reactivex.schedulers.Schedulers
import example.code.some_project.data.remote.models.AuthResponse
import example.code.some_project.domain.interactor.AuthInteractor
import example.code.some_project.domain.models.AuthData
import example.code.some_project.domain.models.RegistrationData
import example.code.some_project.domain.repo.AuthRepo
import example.code.some_project.domain.repo.FirebaseRepo

class AuthInteractorImpl(
    private val firebaseRepo: FirebaseRepo,
    private val authRepo: AuthRepo
): AuthInteractor {

    override fun getAuthOfferDescription(): Single<String> = firebaseRepo
        .getAuthOfferDescription()
        .subscribeOn(Schedulers.io())

    override fun authorize(authData: AuthData): Single<AuthResponse> = authRepo
        .authorize(authData)
        .subscribeOn(Schedulers.io())

    override fun saveTokens(response: AuthResponse): Unit = authRepo
        .saveTokensAfterAuth(response)

    override fun sendSmsCode(registrationData: RegistrationData): Completable = authRepo
        .sendSmsCode(registrationData)
        .subscribeOn(Schedulers.io())

}