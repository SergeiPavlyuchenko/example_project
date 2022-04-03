package example.code.some_project.presentation.ui.fragment.profile

import androidx.fragment.app.Fragment
import example.code.some_project.domain.interactor.LogoutInteractor
import example.code.some_project.domain.models.SubscriptionInfo
import example.code.some_project.domain.models.SubscriptionList
import example.code.some_project.domain.repo.NetworkManager
import example.code.some_project.presentation.mvp.base.BaseFragmentPresenterBase
import example.code.some_project.presentation.mvp.base.BaseFragmentView
import moxy.viewstate.strategy.alias.AddToEndSingle
import moxy.viewstate.strategy.alias.OneExecution

interface ProfileView: BaseFragmentView {

    @AddToEndSingle
    fun setSubscriptionState(subscription: SubscriptionInfo)

    @AddToEndSingle
    fun setSyncContactsItemVisibility(readContactsGranted: Boolean)

    @AddToEndSingle
    fun setRateAppItemVisibility(visible: Boolean)

    @OneExecution
    fun navigateToManageSubscription(subscriptions: SubscriptionList?)

    @OneExecution
    fun showLanguageAppDialog(currentLanguage: String)

    @OneExecution
    fun reCreateActivity()

    @OneExecution
    fun checkPermission(permission: String)

    @OneExecution
    fun navigateToAuth()

    @OneExecution
    fun navigateTo(fragment: Fragment)
}

interface ProfilePresenter {

    fun initialize(readContactsGranted: Boolean)

    fun navigateToManageSubscription()

    fun navigateTo(fragment: Fragment)

    fun showLanguageAppDialog()

    fun setBackEndLanguage(language: String)

    fun onSynchronizationAllowed()

    fun logout()

}

abstract class AbstractProfilePresenter(
    logoutInteractor: LogoutInteractor,
    networkManager: NetworkManager
): BaseFragmentPresenterBase<ProfileView>(logoutInteractor, networkManager), ProfilePresenter