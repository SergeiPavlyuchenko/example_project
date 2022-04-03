package example.code.some_project.presentation.ui.fragment.manage_subscription

import example.code.some_project.domain.interactor.LogoutInteractor
import example.code.some_project.domain.models.SubscriptionInfo
import example.code.some_project.domain.repo.NetworkManager
import example.code.some_project.presentation.mvp.base.BaseFragmentPresenterBase
import example.code.some_project.presentation.mvp.base.BaseFragmentView
import example.code.some_project.presentation.ui.fragment.viewData.SubscriptionPriceAndDescription
import moxy.viewstate.strategy.alias.AddToEndSingle
import moxy.viewstate.strategy.alias.OneExecution

interface ManageSubscriptionView: BaseFragmentView {

    @AddToEndSingle
    fun setSubscriptionState(subscriptions: List<SubscriptionInfo>, data: SubscriptionPriceAndDescription)

    @OneExecution
    fun showNotEnoughMoneyDialog()
}

interface ManageSubscriptionPresenter {

    fun getSubscriptionPriceAndDescription()

    fun initialize(data: SubscriptionPriceAndDescription)

    fun updateData(subscriptionList: ArrayList<SubscriptionInfo>?)

    fun connectWeekSubscription()

    fun connectMonthSubscription()

    fun deleteWeekSubscription()

    fun deleteMonthSubscription()

    fun retryConnectSubscription()

}

abstract class AbstractManageSubscriptionPresenter(
    logoutInteractor: LogoutInteractor,
    networkManager: NetworkManager
): BaseFragmentPresenterBase<ManageSubscriptionView>(
    logoutInteractor, networkManager
), ManageSubscriptionPresenter