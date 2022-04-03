package example.code.some_project.presentation.ui.fragment.profile

import android.content.pm.PackageManager
import android.view.View
import android.view.ViewGroup
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.core.view.children
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import com.bumptech.glide.Glide
import kotlinx.android.synthetic.main.fragment_profile.*
import example.code.some_project.R
import example.code.some_project.databinding.FragmentProfileBinding
import example.code.some_project.domain.models.SubscriptionInfo
import example.code.some_project.domain.models.SubscriptionList
import example.code.some_project.domain.models.hasActiveMonthSubscription
import example.code.some_project.domain.models.hasActiveSubscription
import example.code.some_project.domain.models.hasActiveWeekSubscription
import example.code.some_project.domain.models.hasBlockedSubscription
import example.code.some_project.domain.models.hasDisabledSubscription
import example.code.some_project.presentation.ui.activity.auth.AuthActivity
import example.code.some_project.presentation.ui.base.BaseMvpActivity
import example.code.some_project.presentation.ui.base.BaseMvpFragment
import example.code.some_project.presentation.ui.custom_views.ProfileItemView
import example.code.some_project.presentation.ui.dialog.LanguageAppBottomSheetDialogFragment
import example.code.some_project.presentation.ui.dialog.RateAppBottomSheetDialogFragment
import example.code.some_project.presentation.ui.dialog.RateAppBottomSheetDialogListener
import example.code.some_project.presentation.ui.extensions.openRateApp
import example.code.some_project.presentation.ui.extensions.setOnClick
import example.code.some_project.presentation.ui.fragment.AboutAppFragment
import example.code.some_project.presentation.ui.fragment.about_product.AboutProductFragment
import example.code.some_project.presentation.ui.fragment.manage_subscription.ManageSubscriptionFragment
import example.code.some_project.presentation.ui.fragment.privacy_policy.PrivacyPolicyFragment
import example.code.some_project.presentation.ui.fragment.sync_contacts.SyncContactsFragment
import example.code.some_project.presentation.ui.listener.ProfileFragmentListener
import example.code.some_project.presentation.ui.provider.DialogProvider
import example.code.some_project.presentation.ui.util.Constansts.KEY_RU_LANGUAGE
import moxy.ktx.moxyPresenter
import org.koin.android.ext.android.get


class ProfileFragment: BaseMvpFragment<FragmentProfileBinding> (), ProfileView, ProfileFragmentListener,
    RateAppBottomSheetDialogListener {

    override val presenter: AbstractProfilePresenter by moxyPresenter { get() }
    override val contentViewId: Int = R.id.clProfileContentView
    override var isDarkTheme: Boolean = false

    override fun createViewBinding(): FragmentProfileBinding = FragmentProfileBinding.inflate(layoutInflater)

    override fun initUi() {
        binding.run {
            itemProfileAboutProduct.apply {
                setIcon(R.drawable.ic_about_product)
                setText(R.string.profile_about_product)
            }
            itemProfileRateApp.apply {
                setIcon(R.drawable.ic_rate_app)
                setText(R.string.profile_rate_app)
            }
            itemProfileLanguageApp.apply {
                setIcon(R.drawable.ic_language_app)
                setText(R.string.profile_language_app)
            }
            itemProfilePrivacyPolicy.apply {
                setIcon(R.drawable.ic_privacy_policy)
                setText(R.string.profile_privacy_policy)
            }
            itemProfileAboutApp.apply {
                setIcon(R.drawable.ic_about_app)
                setText(R.string.profile_about_app)
            }
            itemProfileSyncContacts.apply {
                setIcon(R.drawable.ic_sync_contacts)
                setText(R.string.profile_sync_contacts)
            }
            setItemsBackgroundState()
        }
    }

    override fun initUx() {
        binding.run {
            itemProfileAboutProduct.setOnClick(::navigateToAboutProduct)
            itemProfileRateApp.setOnClick(::showRateAppDialog)
            itemProfileLanguageApp.setOnClick(presenter::showLanguageAppDialog)
            itemProfilePrivacyPolicy.setOnClick(::navigateToPrivacyPolicy)
            itemProfileAboutApp.setOnClick(::navigateToAboutApp)
            itemProfileSyncContacts.setOnClick(::navigateToSyncContacts)
            profileLogoutButton.setOnClick(::logout)
            llProfileProductManagement.syncButton.setOnClick(presenter::navigateToManageSubscription)
        }
    }

    private fun setItemsBackgroundState() {
        val children = binding.llProfileItemList.children
        children.forEachIndexed { index, view ->
            when (index) {
                0 -> {
                    (view as? ProfileItemView)?.setCustomBackground(isTopItem = true)
                    (view.layoutParams as ViewGroup.MarginLayoutParams).apply {
                        topMargin = ITEMS_DIVIDER_HEIGHT
                    }.let(view::setLayoutParams)
                }
                children.count() - 1 -> processLastItemsStates(view, children)
                else -> {
                    (view as? ProfileItemView)?.setCustomBackground()
                    (view.layoutParams as ViewGroup.MarginLayoutParams).apply {
                        topMargin = ITEMS_DIVIDER_HEIGHT
                    }.let(view::setLayoutParams)
                }
            }
        }
    }

    private fun processLastItemsStates(
        view: View,
        children: Sequence<View>
    ) {
        if (view.isVisible) {
            view as? ProfileItemView
        } else {
            children.elementAt(children.count() - 2) as? ProfileItemView
        }?.let {
            it.setCustomBackground(isBottomItem = true)
            (it.layoutParams as ViewGroup.MarginLayoutParams).apply {
                topMargin = ITEMS_DIVIDER_HEIGHT
            }.let(it::setLayoutParams)
        }
    }

    override fun checkPermission(permission: String) {
        context?.let {
            (ActivityCompat.checkSelfPermission(it, permission) == PackageManager.PERMISSION_GRANTED)
                .let(presenter::initialize)
        }
    }

    override fun setSyncContactsItemVisibility(readContactsGranted: Boolean) = binding.run {
        itemProfileSyncContacts.isVisible = !readContactsGranted
        setItemsBackgroundState()
    }

    override fun setRateAppItemVisibility(visible: Boolean) {
        itemProfileRateApp.isVisible = visible
        setItemsBackgroundState()
    }

    override fun setSubscriptionState(subscription: SubscriptionInfo) {
        setSubscriptionImage(subscription)
        binding.run {
            when {
                subscription.hasDisabledSubscription -> setDisabledSubscriptionUi()
                subscription.hasActiveWeekSubscription -> setActiveWeekSubscriptionUi(subscription)
                subscription.hasActiveMonthSubscription -> setActiveMonthSubscriptionUi()
                subscription.hasBlockedSubscription -> setBlockedSubscriptionUi()
                else -> error("Unknown subscription status")
            }
        }
    }

    private fun setDisabledSubscriptionUi() = binding.run {
        llProfileProductManagement.title.text =
            getString(R.string.profile_product_is_disabled_title)
        llProfileProductManagement.text.text =
            getString(R.string.profile_product_is_disabled_description)
        llProfileProductManagement.syncButton.apply {
            text = getString(R.string.profile_product_connect_button_text)
            context?.let {
                background = ContextCompat.getDrawable(it, R.drawable.bg_black_rounded_button)
                setTextColor(ContextCompat.getColor(it, R.color.white))
            }
        }
    }

    private fun setActiveWeekSubscriptionUi(subscription: SubscriptionInfo) = binding.run {
        llProfileProductManagement.title.text = getString(R.string.profile_product_is_active_title)
        llProfileProductManagement.text.text =
            getString(
                R.string.profile_product_is_active_week_description,
                subscription.endTimeString
            )
        llProfileProductManagement.syncButton.apply {
            text = getString(R.string.profile_product_management_button_text)
            context?.let {
                background = ContextCompat.getDrawable(it, R.drawable.bg_bordered_rounded_button)
                setTextColor(ContextCompat.getColor(it, R.color.black_to_rename))
            }
        }
    }

    private fun setActiveMonthSubscriptionUi() = binding.run {
        llProfileProductManagement.title.text = getString(R.string.profile_product_is_active_title)
        llProfileProductManagement.text.text =
            getString(R.string.profile_product_is_active_month_description)
        llProfileProductManagement.syncButton.apply {
            text = getString(R.string.profile_product_management_button_text)
            context?.let {
                background = ContextCompat.getDrawable(it, R.drawable.bg_bordered_rounded_button)
                setTextColor(ContextCompat.getColor(it, R.color.black_to_rename))
            }
        }
    }

    private fun setBlockedSubscriptionUi() = binding.run {
        llProfileProductManagement.title.text =
            getString(R.string.profile_product_is_blocked_title)
        llProfileProductManagement.text.text =
            getString(R.string.profile_product_is_blocked_description)
        llProfileProductManagement.syncButton.apply {
            text = getString(R.string.profile_product_management_button_text)
            context?.let {
                background = ContextCompat.getDrawable(it, R.drawable.bg_bordered_rounded_button)
                setTextColor(ContextCompat.getColor(it, R.color.black_to_rename))
            }
        }
    }

    private fun setSubscriptionImage(subscription: SubscriptionInfo) = binding.run {
        val image = when {
            subscription.hasDisabledSubscription -> R.drawable.ic_subscription_alert
            subscription.hasActiveSubscription -> R.drawable.ic_subscription_active
            subscription.hasBlockedSubscription -> R.drawable.ic_subscription_closed
            else -> error("Unknown subscription status")
        }

        view?.let {
            Glide.with(it)
                .load(image)
                .into(llProfileProductManagement.image)
        }


    }

    private fun navigateToAboutProduct() = presenter.navigateTo(AboutProductFragment())

    private fun showRateAppDialog() = RateAppBottomSheetDialogFragment.createInstance()
        .show(childFragmentManager, RateAppBottomSheetDialogFragment.TAG)

    override fun showLanguageAppDialog(currentLanguage: String) =
        LanguageAppBottomSheetDialogFragment.newInstance(
            if (currentLanguage.isEmpty()) {
                (activity as? BaseMvpActivity)?.getLanguage() ?: KEY_RU_LANGUAGE
            } else {
                currentLanguage
            }
        )
            .show(childFragmentManager, "LanguageAppBottomSheetDialogFragment")

    private fun navigateToPrivacyPolicy() = presenter.navigateTo(PrivacyPolicyFragment())

    private fun navigateToAboutApp() = presenter.navigateTo(AboutAppFragment())

    private fun navigateToSyncContacts() = navigateTo(SyncContactsFragment())

    override fun navigateToManageSubscription(subscriptions: SubscriptionList?) =
        navigateTo(ManageSubscriptionFragment.newInstance(subscriptions))

    private fun logout() {
        activity?.let {
            DialogProvider.createDialog(
                it,
                title = getString(R.string.profile_logout_dialog_title),
                description = getString(R.string.profile_logout_dialog_text),
                positiveButtonText = getString(R.string.profile_logout_accept_button),
                positiveButtonListener = { presenter.logout() },
                negativeButtonText = getString(R.string.cancel)
            ).show()
        }
    }

    override fun setNewAppLanguage(language: String) =
        presenter.setBackEndLanguage(language)

    override fun reCreateActivity() {
        activity?.recreate()
    }

    override fun navigateTo(fragment: Fragment) {
        activity?.supportFragmentManager?.beginTransaction()
            ?.replace(R.id.flMainContentView, fragment)
            ?.addToBackStack(null)
            ?.commit()
    }

    override fun navigateToAuth() {
        activity?.let {
            it.finish()
            it.startActivity(AuthActivity.createIntent(it))
        }
    }

    override fun hideAlways() {
        // does nothing here
    }

    override fun ratingSelected(rating: Int) {
        activity?.openRateApp()
    }

    companion object {
        private const val ITEMS_DIVIDER_HEIGHT = 4
    }
}