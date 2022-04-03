package example.code.some_project.presentation.ui.fragment.profile

import android.Manifest
import androidx.fragment.app.Fragment
import io.reactivex.Single
import example.code.some_project.domain.extensions.getCurrentSubscription
import example.code.some_project.domain.interactor.LogoutInteractor
import example.code.some_project.domain.interactor.ProfileInteractor
import example.code.some_project.domain.interactor.RateAppInteractor
import example.code.some_project.domain.interactor.SubscriptionInteractor
import example.code.some_project.domain.models.SubscriptionInfo
import example.code.some_project.domain.models.SubscriptionList
import example.code.some_project.domain.repo.NetworkManager

class ProfilePresenterImpl(
    private val logoutInteractor: LogoutInteractor,
    private val subscriptionInteractor: SubscriptionInteractor,
    private val profileInteractor: ProfileInteractor,
    private val rateAppInteractor: RateAppInteractor,
    networkManager: NetworkManager
) : AbstractProfilePresenter(logoutInteractor, networkManager) {

    private val subInfoStub = SubscriptionInfo(SubscriptionInfo.SUBSCRIPTION_TYPE_WEEK,"Услуга","100", SubscriptionInfo.SUBSCRIPTION_STATUS_ACTIVE, "2022-04-07T13:50:38.541038Z")
    private var subscriptionInfoList: List<SubscriptionInfo>? = listOf(subInfoStub, subInfoStub)

    override fun onResume() {
        super.onResume()
        onSynchronizationAllowed()
    }

    override fun initialize(readContactsGranted: Boolean) {
        executeSingle(
                rateAppInteractor.rateAppHiddenAlways(),
            onSuccessAction = {
                viewState.hideLoading()
                viewState.setSubscriptionState(subInfoStub)
                viewState.setSyncContactsItemVisibility(readContactsGranted)
                viewState.setRateAppItemVisibility(true)
            },
            onErrorAction = {
                viewState.hideLoading()
                viewState.showCommonErrorScreen()
            }
        )
//        executeSingle(
//            Single.zip(
//                subscriptionInteractor.getSubscriptions(),
//                rateAppInteractor.rateAppHiddenAlways(),
//                { subscriptions, hidden -> Pair(subscriptions, hidden) }
//            ),
//            onSuccessAction = { (subscriptions, hidden) ->
//                viewState.hideLoading()
//                subscriptionInfoList = subscriptions
//                viewState.setSubscriptionState(subscriptions.getCurrentSubscription())
//                viewState.setSyncContactsItemVisibility(readContactsGranted)
//                viewState.setRateAppItemVisibility(!hidden)
//            },
//            onErrorAction = {
//                viewState.hideLoading()
//                viewState.showCommonErrorScreen()
//            }
//        )
    }

    override fun onSynchronizationAllowed() {
        if (!hasConnection) {
            viewState.showNoInternetScreen()
            return
        }

        viewState.hideErrorScreen()

        viewState.showLoading()
        viewState.checkPermission(Manifest.permission.READ_CONTACTS)
    }

    override fun navigateTo(fragment: Fragment) {
        if (!hasConnection) {
            viewState.showNoInternetSnackBar()
            return
        }

        viewState.navigateTo(fragment)
    }

    override fun navigateToManageSubscription() {
        subscriptionInfoList?.let {
            viewState.navigateToManageSubscription(it as? SubscriptionList)
        }
    }

    override fun showLanguageAppDialog() {
        if (!hasConnection) {
            viewState.showNoInternetSnackBar()
            return
        }

        viewState.showLanguageAppDialog(profileInteractor.getCurrentLanguage())
    }

    override fun setBackEndLanguage(language: String) {
        if (!hasConnection) {
            viewState.showNoInternetSnackBar()
            return
        }

//        executeCompletable(
//            profileInteractor.setBackEndLanguage(language),
//            onCompleteAction = { setNewAppLanguage(language) },
//            onErrorAction = { viewState.showCommonErrorSnackBar() }
//        )
        setNewAppLanguage(language)
    }

    private fun setNewAppLanguage(language: String) {
        if (!hasConnection) {
            viewState.showNoInternetSnackBar()
            return
        }

        executeCompletable(
            profileInteractor.setNewAppLanguage(language),
            onCompleteAction = { viewState.reCreateActivity() }
        )
    }

    override fun logout() {
        executeCompletable(
            logoutInteractor.logout(),
            { viewState.navigateToAuth() },
            { viewState.showCommonErrorSnackBar() }
        )
    }

    override fun retry() = onSynchronizationAllowed()
}