package example.code.some_project.presentation.ui.base

import android.content.res.Configuration
import android.os.Bundle
import android.view.Gravity
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import android.widget.ImageView
import android.widget.TextView
import androidx.activity.result.ActivityResultLauncher
import androidx.annotation.CallSuper
import androidx.core.content.ContextCompat
import androidx.core.view.WindowInsetsControllerCompat
import androidx.core.view.isVisible
import com.google.firebase.analytics.FirebaseAnalytics
import example.code.some_project.R
import example.code.some_project.data.device.DeviceManagerImpl
import example.code.some_project.domain.repo.device.DeviceManager
import example.code.some_project.presentation.SomeApplication
import example.code.some_project.presentation.mvp.base.BaseActivityPresenter
import example.code.some_project.presentation.mvp.base.BaseView
import example.code.some_project.presentation.ui.activity.auth.AuthActivity
import example.code.some_project.presentation.ui.provider.SnackBarProvider
import example.code.some_project.presentation.ui.util.Constansts.KEY_KZ_LANGUAGE
import example.code.some_project.presentation.ui.util.Constansts.KEY_RU_LANGUAGE
import moxy.MvpAppCompatActivity
import java.util.*


abstract class BaseMvpActivity : MvpAppCompatActivity(), BaseView {

    abstract val presenter: BaseActivityPresenter?
    abstract val contentViewId: Int

    protected var deviceManager: DeviceManager? = DeviceManagerImpl(SomeApplication.context)

    private var contentView: View? = null
    private var errorView: View? = null
    private var errorImageView: ImageView? = null
    private var errorTitleView: TextView? = null
    private var errorDescriptionView: TextView? = null
    private var progressRootView: View? = null
    private var authLauncher: ActivityResultLauncher<Unit>? = null
    open var isDarkTheme = false


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        initAnalytics()
        applyOtherTheme()
        initLanguage()
        baseInitUi()
        initData()
        initUi()
        initUx()
    }

    open fun initData() {}

    open fun initUi() {}

    open fun initUx() {}

    open fun applyOtherTheme() {
        window?.apply {
            if (isDarkTheme) {
                statusBarColor = ContextCompat.getColor(context, R.color.black_to_rename)
                WindowInsetsControllerCompat(this, this.decorView).isAppearanceLightStatusBars =
                    false
            } else {
                statusBarColor = ContextCompat.getColor(context, R.color.white)
                WindowInsetsControllerCompat(this, this.decorView).isAppearanceLightStatusBars =
                    true
            }
        }
    }

    override fun setContentView(view: View?) {
        super.setContentView(view)
        baseInitUi()
    }

    override fun setContentView(layoutResID: Int) {
        super.setContentView(layoutResID)
        baseInitUi()
    }

    override fun setContentView(view: View?, params: ViewGroup.LayoutParams?) {
        super.setContentView(view, params)
        baseInitUi()
    }

    private fun baseInitUi() {
        contentView = findViewById(contentViewId)
        errorView = findViewById(R.id.vError)
        errorImageView = findViewById(R.id.ivNotificationScreenImage)
        errorTitleView = findViewById(R.id.tvNotificationScreenTitle)
        errorDescriptionView = findViewById(R.id.tvNotificationScreenDescription)
        progressRootView = findViewById(R.id.vProgress)
        findViewById<View>(R.id.btnNotificationScreenAction)?.setOnClickListener { presenter?.retry() }
    }

    @CallSuper
    override fun onResume() {
        super.onResume()
        presenter?.onResume()
    }

    @CallSuper
    override fun onPause() {
        super.onPause()
        presenter?.onPause()
    }

    override fun onDestroy() {
        super.onDestroy()
        presenter?.onDestroy()
        deviceManager = null
        authLauncher = null
    }

    override fun showLoading() {
        (progressRootView?.layoutParams as? FrameLayout.LayoutParams).apply {
            this?.gravity = Gravity.CENTER
        }.let { progressRootView?.layoutParams = it }

        contentView?.isVisible = false
        errorView?.isVisible = false
        progressRootView?.isVisible = true
    }

    override fun hideLoading() {
        contentView?.isVisible = true
        errorView?.isVisible = false
        progressRootView?.isVisible = false
    }

    override fun showNoInternetScreen() {
        isDarkTheme = true
        applyOtherTheme()
        errorView?.let {
            showErrorScreen()
            errorImageView?.setImageResource(R.drawable.img_no_internet)
            errorTitleView?.setText(R.string.error_screen_no_internet_title)
            errorDescriptionView?.setText(R.string.error_screen_no_internet_description)
        }
    }

    override fun showCommonErrorScreen() {
        isDarkTheme = true
        applyOtherTheme()
        errorView?.let {
            showErrorScreen()
            errorImageView?.setImageResource(R.drawable.img_common_error)
            errorTitleView?.setText(R.string.error_screen_common_error_title)
            errorDescriptionView?.setText(R.string.error_screen_common_error_description)
        }
    }

    private fun showErrorScreen() {
        contentView?.isVisible = false
        errorView?.isVisible = true
        progressRootView?.isVisible = false
    }

    override fun hideErrorScreen() {
        contentView?.isVisible = true
        errorView?.isVisible = false
        progressRootView?.isVisible = false
    }

    override fun showSnackBar(text: CharSequence) {
        contentView?.let {
            SnackBarProvider.showSnackBar(it, text)
        }
    }

    override fun showErrorSnackBar(text: CharSequence) {
        contentView?.let {
            SnackBarProvider.showSnackBar(it, text, true)
        }
    }

    override fun showNoInternetSnackBar() =
        showErrorSnackBar(getString(R.string.error_no_internet_snackbar))

    override fun showCommonErrorSnackBar() =
        showErrorSnackBar(getString(R.string.error_common_snackbar))

    override fun openAuth() {
        finish()
        startActivity(AuthActivity.createIntent(this))
    }

    override fun showWeekSubscriptionSuccessConnectedSnackBar() {
        contentView?.let {
            SnackBarProvider.showSnackBar(
                it,
                getString(R.string.snackbar_text_week_subscription_success_connected)
            )
        }
    }

    override fun showMonthSubscriptionSuccessConnectedSnackBar() {
        contentView?.let {
            SnackBarProvider.showSnackBar(
                it,
                getString(R.string.snackbar_text_month_subscription_success_connected)
            )
        }
    }

    override fun showWeekSubscriptionSuccessDeletedSnackBar() {
        contentView?.let {
            SnackBarProvider.showSnackBar(
                it,
                getString(R.string.snackbar_text_week_subscription_success_deleted)
            )
        }
    }

    override fun showMonthSubscriptionSuccessDeletedSnackBar() {
        contentView?.let {
            SnackBarProvider.showSnackBar(
                it,
                getString(R.string.snackbar_text_month_subscription_success_deleted)
            )
        }
    }

    fun getLanguage(): String {
        return if (Locale.getDefault().displayLanguage == resources.getString(R.string.kz_language)) KEY_KZ_LANGUAGE else KEY_RU_LANGUAGE
    }

    private fun initLanguage() {
        val currentAppLanguage = SomeApplication.currentLanguage
        if (currentAppLanguage.isNotEmpty()) {
            val locale = Locale(currentAppLanguage)
            Locale.setDefault(locale)
            val config: Configuration = resources.configuration
            config.setLocale(locale)
            resources.updateConfiguration(config, resources.displayMetrics)

//            ToDo разобраться, как уйти от Deprecated метода, способом ниже не получилось. С целью не тратить много времени на ресерч пока оставляю так.
//            this.createConfigurationContext(config)
        }
    }

    private fun initAnalytics() {
        val firebaseAnalytics = FirebaseAnalytics.getInstance(this)
    }
}
