package example.code.some_project.presentation.mvp.base

import moxy.MvpView
import moxy.viewstate.strategy.alias.AddToEndSingle
import moxy.viewstate.strategy.alias.OneExecution

interface BaseView : MvpView {

    @AddToEndSingle
    fun showLoading()

    @AddToEndSingle
    fun hideLoading()

    @AddToEndSingle
    fun showNoInternetScreen()

    @AddToEndSingle
    fun showCommonErrorScreen()

    @AddToEndSingle
    fun hideErrorScreen()

    @OneExecution
    fun showSnackBar(text: CharSequence)

    @OneExecution
    fun showErrorSnackBar(text: CharSequence)

    @OneExecution
    fun showNoInternetSnackBar()

    @OneExecution
    fun showCommonErrorSnackBar()

    @OneExecution
    fun openAuth()

    @OneExecution
    fun showWeekSubscriptionSuccessConnectedSnackBar()

    @OneExecution
    fun showMonthSubscriptionSuccessConnectedSnackBar()

    @OneExecution
    fun showWeekSubscriptionSuccessDeletedSnackBar()

    @OneExecution
    fun showMonthSubscriptionSuccessDeletedSnackBar()

}

interface BasePresenter {

    fun onResume()

    fun onPause()

    fun onDestroy()

    fun retry()
}

interface BaseFragmentView : BaseView

interface BaseFragmentPresenter : BasePresenter {

    fun onViewCreated()
}

interface BaseActivityView : BaseView

interface BaseActivityPresenter : BasePresenter