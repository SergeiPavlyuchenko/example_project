package example.code.some_project.presentation.ui.activity.auth

import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import example.code.some_project.R
import example.code.some_project.domain.FiveMinutesBlockedException
import example.code.some_project.domain.TwentyFourHoursBlockedException
import example.code.some_project.domain.interactor.AuthInteractor
import example.code.some_project.domain.models.AuthData
import example.code.some_project.domain.models.RegistrationData
import example.code.some_project.domain.repo.NetworkManager
import example.code.some_project.domain.repo.device.ResourcesManager
import example.code.some_project.presentation.ui.util.Constansts.REQUIRED_DIGITS_OF_PHONE
import example.code.some_project.presentation.ui.util.Constansts.TOTAL_TIMER
import moxy.InjectViewState
import timber.log.Timber
import java.text.SimpleDateFormat
import java.util.*
import java.util.concurrent.TimeUnit

@InjectViewState
class AuthActivityPresenterImpl(
    private val authInteractor: AuthInteractor,
    private val resourcesManager: ResourcesManager,
    networkManager: NetworkManager
) : AbstractAuthActivityPresenter(networkManager) {

    override var isTimerLaunched = false
    private var currentMsisdn = ""
    private var currentSmsCode = ""

    override fun onResume() {
        super.onResume()
        getAuthOfferDescription()
    }

    override fun validateNumberAndSendSmsCode(msisdn: String) {
        if (!hasConnection) {
            viewState.showNoInternetSnackBar()
            return
        }

        currentMsisdn = resourcesManager.getString(R.string.auth_true_number, msisdn)
            .replace("-", "")
            .replace(" ", "")
        when {
            currentMsisdn == EMPTY_MSISDN -> viewState.setNumberFieldAsEmptyError()
            currentMsisdn.length < REQUIRED_DIGITS_OF_PHONE -> viewState.setNumberFieldAsIncorrectError()
            currentMsisdn.length == REQUIRED_DIGITS_OF_PHONE -> {
                if (!isTimerLaunched) {
                    launchRetryAgainTimer()
                }
                viewState.changeUiToPasswordState()
//                executeCompletable(
//                    authInteractor.sendSmsCode(
//                        RegistrationData(
//                            msisdn = currentMsisdn
//                        )
//                    ),
//                    onCompleteAction = {
//                        if (!isTimerLaunched) {
//                            launchRetryAgainTimer()
//                        }
//                        viewState.changeUiToPasswordState()
//                    },
//                    onErrorAction = {
//                        Timber.e(it)
//                        parseError(it)
//                        viewState.setNumberFieldAsNotOperatorError()
//                        isTimerLaunched = false
//                    }
//                )
            }
            else -> viewState.setNumberFieldAsIncorrectError()
        }
    }

    override fun validateSmsCode(code: String) {
        if (!hasConnection) {
            viewState.showNoInternetSnackBar()
            return
        }

//        currentSmsCode = code
//        viewState.showProgressBar()
//        executeSingle(
//            authInteractor.authorize(AuthData(msisdn = currentMsisdn, smsCode = code)),
//            onSuccessAction = {
//                with(viewState) {
//                    hideProgressBar()
//                    navigateToMainPage()
//                }
//                authInteractor.saveTokens(it)
//            },
//            onErrorAction = {
//                Timber.e(it)
//                viewState.hideProgressBar()
//                viewState.setPasswordFieldAsError()
//            }
//        )
        viewState.navigateToMainPage()
    }

    private fun launchRetryAgainTimer() {
        isTimerLaunched = true
        val formatter = SimpleDateFormat("m:ss", Locale.ROOT)
        Observable.interval(1, TimeUnit.SECONDS)
            .take(TOTAL_TIMER + 2)
            .map { formatter.format(Date((TOTAL_TIMER - it) * 1000L)) }
            .observeOn(AndroidSchedulers.mainThread())
            .doFinally {
                isTimerLaunched = false
                viewState.changeUiToPasswordState()
            }
            .subscribe(viewState::setTimerCount, Timber::e)
            .bind()
    }

    private fun getAuthOfferDescription() {
        if (!hasConnection) {
            viewState.showNoInternetScreen()
            return
        }

        executeSingle(
            authInteractor.getAuthOfferDescription(),
            onSuccessAction = viewState::setAuthOfferDescription
        )
    }

    private fun parseError(throwable: Throwable) {
        when (throwable) {
            is TwentyFourHoursBlockedException -> {
                viewState.showTwentyFourHoursBlockDialog()
            }
            is FiveMinutesBlockedException -> {
                viewState.showFiveMinutesBlockDialog()
            }
            else -> {
                viewState.showCommonErrorSnackBar()
            }
        }
    }

    companion object {
        private const val EMPTY_MSISDN = "7"
    }
}