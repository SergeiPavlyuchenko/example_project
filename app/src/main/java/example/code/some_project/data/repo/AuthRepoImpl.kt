package example.code.some_project.data.repo

import io.reactivex.Completable
import io.reactivex.Single
import example.code.some_project.data.remote.api.AuthApi
import example.code.some_project.data.remote.models.AuthResponse
import example.code.some_project.data.remote.models.UpdateTokensResponse
import example.code.some_project.domain.NeedUpdateTokensException
import example.code.some_project.domain.PatchTokensException
import example.code.some_project.domain.datasource.local.AuthDataSourceLocal
import example.code.some_project.domain.models.AuthData
import example.code.some_project.domain.models.RegistrationData
import example.code.some_project.domain.repo.AuthRepo
import example.code.some_project.domain.repo.SettingsRepo
import retrofit2.Response

class AuthRepoImpl(
    private val authApi: AuthApi,
    private val dataSourceLocal: AuthDataSourceLocal,
    private val settingsRepo: SettingsRepo
) : AuthRepo {

    override fun authorize(authData: AuthData): Single<AuthResponse> =
        authApi.authorize(authData)
            .map { it.body()!! }
            .doOnSuccess { dataSourceLocal.msisdn = authData.msisdn }

    override fun updateTokens(): Single<UpdateTokensResponse> {
        val currentRefreshToken = dataSourceLocal.refreshToken
        return authApi.updateTokens(currentRefreshToken)
            .map { it.body()!! }
            .onErrorResumeNext {
                Single.error(PatchTokensException())
            }
    }

    override fun sendSmsCode(registrationData: RegistrationData): Completable =
        authApi.sendSmsCode(settingsRepo.currentLanguage, registrationData)
            .ignoreElement()

    override fun saveTokensAfterAuth(data: AuthResponse) {
        dataSourceLocal.accessToken = data.newAccessToken
        dataSourceLocal.refreshToken = data.newRefreshToken
    }

    override fun getAuthorized(): Single<Boolean> =
        Single.just(dataSourceLocal)
            .map { it.accessToken.isNotBlank() }

    private fun saveNewTokens(updateTokensResponse: UpdateTokensResponse): Completable =
        Completable.fromAction {
            dataSourceLocal.apply {
                accessToken = updateTokensResponse.accessToken
                refreshToken = updateTokensResponse.refreshToken
            }
        }

    override fun getUserMsisdn(): Single<String> = Single.fromCallable {
        dataSourceLocal.msisdn
    }

    override fun clearAuthorization(): Completable = Completable.fromAction {
        with(dataSourceLocal) {
            accessToken = ""
            refreshToken = ""
            msisdn = ""
        }
    }

    override fun <T : Response<K>, K> makeAuthorizedRequest(
        baseRequest: () -> Single<T>
    ): Single<T> =
        baseRequest()
            .onErrorResumeNext { throwable ->
                if (throwable is NeedUpdateTokensException) {
                    updateTokens()
                        .flatMapCompletable { saveNewTokens(it) }
                        .andThen(baseRequest())
                } else {
                    Single.error<T>(throwable)
                }
            }
}