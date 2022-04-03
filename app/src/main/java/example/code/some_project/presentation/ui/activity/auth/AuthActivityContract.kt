package example.code.some_project.presentation.ui.activity.auth

import example.code.some_project.domain.repo.NetworkManager
import example.code.some_project.presentation.mvp.base.BaseActivityPresenter
import example.code.some_project.presentation.mvp.base.BaseActivityView
import example.code.some_project.presentation.ui.base.BaseActivityPresenterBase
import moxy.viewstate.strategy.alias.AddToEndSingle

interface AuthActivityView: BaseActivityView {

    @AddToEndSingle
    fun changeUiToPasswordState()

    @AddToEndSingle
    fun setNumberFieldAsNotOperatorError()

    @AddToEndSingle
    fun setNumberFieldAsEmptyError()

    @AddToEndSingle
    fun setNumberFieldAsIncorrectError()

    @AddToEndSingle
    fun setPasswordFieldAsError()

    @AddToEndSingle
    fun setTimerCount(count: String)

    @AddToEndSingle
    fun clearTimer()

    @AddToEndSingle
    fun showProgressBar()

    @AddToEndSingle
    fun hideProgressBar()

    @AddToEndSingle
    fun navigateToMainPage()

    @AddToEndSingle
    fun showFiveMinutesBlockDialog()

    @AddToEndSingle
    fun showTwentyFourHoursBlockDialog()

    @AddToEndSingle
    fun setAuthOfferDescription(text: String)

}

interface AuthActivityPresenter: BaseActivityPresenter {

    var isTimerLaunched: Boolean

    fun validateSmsCode(code: String)

    fun validateNumberAndSendSmsCode(msisdn: String)
}

abstract class AbstractAuthActivityPresenter(
    networkManager: NetworkManager
): BaseActivityPresenterBase<AuthActivityView>(
    networkManager
), AuthActivityPresenter