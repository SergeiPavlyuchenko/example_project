package example.code.some_project.presentation.ui.fragment.manage_subscription

import io.reactivex.Single
import example.code.some_project.domain.NotEnoughMoneyException
import example.code.some_project.domain.interactor.LogoutInteractor
import example.code.some_project.domain.interactor.SubscriptionInteractor
import example.code.some_project.domain.models.SubscriptionInfo
import example.code.some_project.domain.repo.NetworkManager
import example.code.some_project.presentation.ui.fragment.viewData.SubscriptionPriceAndDescription

class ManageSubscriptionPresenterImpl(
    logoutInteractor: LogoutInteractor,
    private val subscriptionInteractor: SubscriptionInteractor,
    networkManager: NetworkManager
) : AbstractManageSubscriptionPresenter(logoutInteractor, networkManager) {

    private val subInfoStub = SubscriptionInfo(
        SubscriptionInfo.SUBSCRIPTION_TYPE_WEEK,
        "Услуга",
        "100",
        SubscriptionInfo.SUBSCRIPTION_STATUS_ACTIVE,
        "2022-04-07T13:50:38.541038Z"
    )
    private var subscriptionList: List<SubscriptionInfo>? = null
    private var subscriptionPriceAndDescription: SubscriptionPriceAndDescription? = null
    private var retrySubscription: Int = 0

    override fun onResume() {
        super.onResume()
        getSubscriptionPriceAndDescription()
    }

    override fun getSubscriptionPriceAndDescription() {
        if (!hasConnection) {
            viewState.showNoInternetSnackBar()
            return
        }

//        viewState.showLoading()
//        executeSingle(
//            Single.zip(
//                subscriptionInteractor.getWeekSubscriptionPrice(),
//                subscriptionInteractor.getWeekSubscriptionDescription(),
//                subscriptionInteractor.getMonthSubscriptionPrice(),
//                subscriptionInteractor.getMonthSubscriptionDescription()
//            ) { weekPrice, weekDescription, monthPrice, monthDescription ->
//                SubscriptionPriceAndDescription(
//                    weekPrice,
//                    weekDescription,
//                    monthPrice,
//                    monthDescription
//                )
//            },
//            onSuccessAction = {
//                subscriptionPriceAndDescription = it
//                initialize(it)
//            },
//            onErrorAction = { viewState.showCommonErrorSnackBar() }
//        )
        subscriptionList = listOf(subInfoStub, subInfoStub)
        initialize(
            SubscriptionPriceAndDescription(
                "100 ₽",
                "Описание услуги №1",
                "200 ₽",
                "Описание услуги №2"
            )
        )
    }

    override fun initialize(data: SubscriptionPriceAndDescription) {
        if (subscriptionList != null) {
            viewState.setSubscriptionState(subscriptionList!!, data)
        } else {
            if (!hasConnection) {
                viewState.showNoInternetScreen()
                return
            }

            viewState.hideErrorScreen()

//            executeSingle(
//                subscriptionInteractor.getSubscriptions(),
//                onSuccessAction = {
//                    viewState.setSubscriptionState(it, data)
//                    viewState.hideLoading()
//                },
//                onErrorAction = {
//                    viewState.hideLoading()
//                    viewState.showCommonErrorScreen()
//                }
//            )
            subscriptionList?.let {
                viewState.setSubscriptionState(it, data)
            }
        }
    }

    override fun updateData(subscriptionList: ArrayList<SubscriptionInfo>?) {
        this.subscriptionList = subscriptionList
    }

    override fun connectWeekSubscription() {
        if (!hasConnection) {
            viewState.showNoInternetSnackBar()
            return
        }

        viewState.showLoading()

        executeCompletable(
            subscriptionInteractor.connectWeekSubscription(),
            onCompleteAction = {
                subscriptionList = null
                subscriptionPriceAndDescription?.let(::initialize)
                viewState.showWeekSubscriptionSuccessConnectedSnackBar()
                viewState.hideLoading()
            },
            onErrorAction = {
                viewState.hideLoading()
                if (it is NotEnoughMoneyException) {
                    retrySubscription = RETRY_WEEK_SUBSCRIPTION
                    viewState.showNotEnoughMoneyDialog()
                } else {
                    viewState.showCommonErrorSnackBar()
                }
            }
        )
    }

    override fun connectMonthSubscription() {
        if (!hasConnection) {
            viewState.showNoInternetSnackBar()
            return
        }

        viewState.showLoading()

        executeCompletable(
            subscriptionInteractor.connectMonthSubscription(),
            onCompleteAction = {
                subscriptionList = null
                subscriptionPriceAndDescription?.let(::initialize)
                viewState.showMonthSubscriptionSuccessConnectedSnackBar()
                viewState.hideLoading()
            },
            onErrorAction = {
                viewState.hideLoading()
                if (it is NotEnoughMoneyException) {
                    retrySubscription = RETRY_MONTH_SUBSCRIPTION
                    viewState.showNotEnoughMoneyDialog()
                } else {
                    viewState.showCommonErrorSnackBar()
                }
            }
        )
    }

    override fun deleteWeekSubscription() {
        if (!hasConnection) {
            viewState.showNoInternetSnackBar()
            return
        }

        viewState.showLoading()

        executeCompletable(
            subscriptionInteractor.deleteWeekSubscription(),
            onCompleteAction = {
                subscriptionList = null
                subscriptionPriceAndDescription?.let(::initialize)
                viewState.showMonthSubscriptionSuccessDeletedSnackBar()
                viewState.hideLoading()
            },
            onErrorAction = {
                viewState.hideLoading()
                viewState.showCommonErrorSnackBar()
            }
        )
    }

    override fun deleteMonthSubscription() {
        if (!hasConnection) {
            viewState.showNoInternetSnackBar()
            return
        }

        viewState.showLoading()

        executeCompletable(
            subscriptionInteractor.deleteMonthSubscription(),
            onCompleteAction = {
                subscriptionList = null
                subscriptionPriceAndDescription?.let(::initialize)
                viewState.showMonthSubscriptionSuccessDeletedSnackBar()
                viewState.hideLoading()
            },
            onErrorAction = {
                viewState.hideLoading()
                viewState.showCommonErrorSnackBar()
            }
        )
    }

    override fun retryConnectSubscription() =
        when (retrySubscription) {
            RETRY_WEEK_SUBSCRIPTION -> connectWeekSubscription()
            RETRY_MONTH_SUBSCRIPTION -> connectMonthSubscription()
            else -> viewState.showCommonErrorSnackBar()
        }

    override fun retry() {
        subscriptionPriceAndDescription?.let(::initialize)
    }

    companion object {
        private const val RETRY_WEEK_SUBSCRIPTION = 1
        private const val RETRY_MONTH_SUBSCRIPTION = 2
    }
}