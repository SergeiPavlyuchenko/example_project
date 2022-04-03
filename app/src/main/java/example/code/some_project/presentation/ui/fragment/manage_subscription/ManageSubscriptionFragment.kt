package example.code.some_project.presentation.ui.fragment.manage_subscription

import android.text.SpannableStringBuilder
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.core.view.isVisible
import kotlinx.android.synthetic.main.fragment_manage_subscription.view.*
import kotlinx.android.synthetic.main.item_subscription_management.view.*
import example.code.some_project.R
import example.code.some_project.databinding.FragmentManageSubscriptionBinding
import example.code.some_project.domain.extensions.getCurrentSubscription
import example.code.some_project.domain.models.SubscriptionInfo
import example.code.some_project.domain.models.SubscriptionList
import example.code.some_project.domain.models.hasActiveMonthSubscription
import example.code.some_project.domain.models.hasActiveWeekSubscription
import example.code.some_project.domain.models.hasDisabledSubscription
import example.code.some_project.domain.models.priceOfMonth
import example.code.some_project.domain.models.priceOfWeek
import example.code.some_project.presentation.ui.base.BaseMvpFragment
import example.code.some_project.presentation.ui.extensions.setOnClick
import example.code.some_project.presentation.ui.extensions.withArguments
import example.code.some_project.presentation.ui.fragment.viewData.SubscriptionPriceAndDescription
import example.code.some_project.presentation.ui.provider.DialogProvider
import example.code.some_project.presentation.ui.util.Constansts.KEY_MANAGE_SUBSCRIPTION_SUBSCRIPTION_LIST
import moxy.ktx.moxyPresenter
import org.koin.android.ext.android.get

class ManageSubscriptionFragment : BaseMvpFragment<FragmentManageSubscriptionBinding>(), ManageSubscriptionView {
    override val presenter: AbstractManageSubscriptionPresenter by moxyPresenter { get() }
    override val contentViewId: Int = R.id.llManageSubscriptionContentView

    override fun createViewBinding() = FragmentManageSubscriptionBinding.inflate(layoutInflater)


    override fun initData() = binding.run {
        val currentSubscriptionList =
            arguments?.getParcelableArrayList<SubscriptionInfo>(
                KEY_MANAGE_SUBSCRIPTION_SUBSCRIPTION_LIST
            )

        presenter.updateData(currentSubscriptionList)
    }

    override fun initUx() {
        binding.run {
            tbManageSubscription.setNavigationOnClickListener { activity?.onBackPressed() }
        }
    }

    override fun setSubscriptionState(
        subscriptions: List<SubscriptionInfo>,
        data: SubscriptionPriceAndDescription
    ) {
        val currentSubscription = subscriptions.getCurrentSubscription()
        when {
            currentSubscription.hasActiveWeekSubscription -> setActiveWeekSubscriptionUi(
                subscriptions
            )
            currentSubscription.hasActiveMonthSubscription -> setActiveMonthSubscriptionUi()
            currentSubscription.hasDisabledSubscription -> setDisabledSubscriptionUi()
            else -> setDisabledSubscriptionUi()
        }
        setSubscriptionPriceAndDescription(data, subscriptions)
    }

    private fun setActiveWeekSubscriptionUi(subscriptions: List<SubscriptionInfo>) = binding.run {
        val currentSubscription = subscriptions.getCurrentSubscription()
        with(llManageSubscriptionContentView.itemManageSubscriptionWeek) {
            llSubscriptionManagementInfo.isVisible = true
            tvSubscriptionManagementInfoDescription.text =
                getString(
                    R.string.manage_subscription_week_until_date_text,
                    subscriptions.getCurrentSubscription().endTimeString
                )
            tvSubscriptionManagementTitle.text =
                getString(R.string.profile_product_is_active_week_description)
            with(btnSubscriptionManagement) {
                isVisible = true
                setOnClick { showDeleteSubscriptionDialog(isWeekSubscription = true) }
                changeButtonState(this, true)
            }
        }

        with(llManageSubscriptionContentView.itemManageSubscriptionMonth) {
            llSubscriptionManagementInfo.isVisible = false
            tvSubscriptionManagementTitle.text =
                getString(R.string.profile_product_is_active_month_description)
            with(btnSubscriptionManagement) {
                isVisible = true
                setOnClick(::showAlreadyConnectedWeekSubscriptionDialog)
                changeButtonState(this)
            }
        }
    }

    private fun setActiveMonthSubscriptionUi() = binding.run {
        with(llManageSubscriptionContentView.itemManageSubscriptionWeek) {
            llSubscriptionManagementInfo.isVisible = false
            tvSubscriptionManagementTitle.text =
                getString(R.string.profile_product_is_active_week_description)
            with(btnSubscriptionManagement) {
                isVisible = true
                setOnClick(::showAlreadyConnectedMonthSubscriptionDialog)
                changeButtonState(this)
            }
        }

        with(llManageSubscriptionContentView.itemManageSubscriptionMonth) {
            llSubscriptionManagementInfo.isVisible = true
            tvSubscriptionManagementInfoDescription.text =
                getString(R.string.manage_subscription_month_connected)
            tvSubscriptionManagementTitle.text =
                getString(R.string.profile_product_is_active_month_description)
            with(btnSubscriptionManagement) {
                isVisible = true
                setOnClick { showDeleteSubscriptionDialog(isWeekSubscription = false) }
                changeButtonState(this, true)
            }
        }
    }

    private fun setDisabledSubscriptionUi() = binding.run {
        with(llManageSubscriptionContentView.itemManageSubscriptionWeek) {
            llSubscriptionManagementInfo.isVisible = false
            tvSubscriptionManagementTitle.text =
                getString(R.string.profile_product_is_active_week_description)
            with(btnSubscriptionManagement) {
                isVisible = true
                setOnClick(presenter::connectWeekSubscription)
                changeButtonState(this)
            }
        }

        with(llManageSubscriptionContentView.itemManageSubscriptionMonth) {
            llSubscriptionManagementInfo.isVisible = false
            tvSubscriptionManagementTitle.text =
                getString(R.string.profile_product_is_active_month_description)
            with(btnSubscriptionManagement) {
                isVisible = true
                setOnClick(presenter::connectMonthSubscription)
                changeButtonState(this)
            }
        }
    }

    private fun setSubscriptionPriceAndDescription(
        data: SubscriptionPriceAndDescription,
        subscriptions: List<SubscriptionInfo>
    ) = with(binding.llManageSubscriptionContentView) {
        itemManageSubscriptionWeek.tvSubscriptionManagementPrice.text =
            data.weekPrice
        itemManageSubscriptionWeek.tvSubscriptionManagementDescription.text = data.weekDescription

        itemManageSubscriptionMonth.tvSubscriptionManagementPrice.text =
            data.monthPrice
        itemManageSubscriptionMonth.tvSubscriptionManagementDescription.text = data.monthDescription
    }

    private fun TextView.changeButtonState(
        button: TextView,
        isDeleteSubscription: Boolean = false
    ) {
        context?.let {
            with(button) {
                if (isDeleteSubscription) {
                    background =
                        ContextCompat.getDrawable(it, R.drawable.bg_bordered_rounded_button)
                    setTextColor(ContextCompat.getColor(it, R.color.black_to_rename))
                    text = getString(R.string.manage_subscription_button_disconnect_text)
                } else {
                    background = ContextCompat.getDrawable(it, R.drawable.bg_black_rounded_button)
                    setTextColor(ContextCompat.getColor(it, R.color.white))
                    text = getString(R.string.manage_subscription_button_connect_text)
                }
            }
        }
    }

    private fun showAlreadyConnectedWeekSubscriptionDialog() = context?.let {
        DialogProvider.createDialog(
            context = it,
            title = getString(R.string.common_dialog_success_activate_subscription_title),
            spannedDescription = getSpannedWeekAlreadyConnectedDescription(),
            positiveButtonText = getString(R.string.common_dialog_understand_button_text),
        ).show()
    }

    private fun showAlreadyConnectedMonthSubscriptionDialog() = context?.let {
        DialogProvider.createDialog(
            context = it,
            title = getString(R.string.common_dialog_success_activate_subscription_title),
            spannedDescription = getSpannedMonthAlreadyConnectedDescription(),
            positiveButtonText = getString(R.string.common_dialog_understand_button_text),
        ).show()
    }

    private fun getSpannedWeekAlreadyConnectedDescription(): CharSequence {
        return getText(R.string.common_dialog_already_active_week_subscription_description)
    }

    private fun showDeleteSubscriptionDialog(isWeekSubscription: Boolean) = context?.let {
        DialogProvider.createDialog(
            context = it,
            title = getString(R.string.common_dialog_delete_subscription_title),
            spannedDescription = getString(R.string.common_dialog_delete_subscription_description),
            positiveButtonText = getString(R.string.common_dialog_yes_deactivate_button_text),
            negativeButtonText = getString(R.string.common_dialog_cancel_button_text),
            positiveButtonListener = if (isWeekSubscription) {
                { presenter.deleteWeekSubscription() }
            } else {
                { presenter.deleteMonthSubscription() }
            }
        ).show()
    }

    private fun getSpannedMonthAlreadyConnectedDescription(): SpannableStringBuilder =
        SpannableStringBuilder(getText(R.string.common_dialog_already_active_month_subscription_description))

    override fun showNotEnoughMoneyDialog() {
        context?.let {
            DialogProvider.createDialog(
                context = it,
                title = getString(R.string.common_dialog_error_activate_subscription_title),
                description = getString(R.string.common_dialog_error_activate_subscription_description),
                positiveButtonText = getString(R.string.common_dialog_retry_button_text),
                negativeButtonText = getString(R.string.common_dialog_cancel_button_text),
                positiveButtonListener = presenter::retryConnectSubscription
            ).show()
        }
    }

    companion object {
        const val TAG = "ManageSubscriptionFragment"

        fun newInstance(subscriptionList: SubscriptionList?): ManageSubscriptionFragment =
            ManageSubscriptionFragment().withArguments {
                putParcelableArrayList(
                    KEY_MANAGE_SUBSCRIPTION_SUBSCRIPTION_LIST,
                    subscriptionList
                )
            }
    }
}