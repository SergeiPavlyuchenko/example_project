package example.code.some_project.presentation.ui.base

import android.os.Bundle
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import android.widget.ImageView
import android.widget.TextView
import androidx.annotation.CallSuper
import androidx.core.view.isVisible
import androidx.viewbinding.ViewBinding
import example.code.some_project.R
import example.code.some_project.presentation.mvp.base.BaseFragmentPresenter
import example.code.some_project.presentation.mvp.base.BaseFragmentView
import example.code.some_project.presentation.ui.activity.main.MainActivity
import example.code.some_project.presentation.ui.provider.SnackBarProvider
import moxy.MvpAppCompatFragment


abstract class BaseMvpFragment<B: ViewBinding> : MvpAppCompatFragment(), BaseFragmentView {

    private var _binding: B? = null
    val binding get() = _binding!!

    val baseActivity: BaseMvpActivity? get() = activity as? BaseMvpActivity
    val mainActivity: MainActivity? get() = activity as? MainActivity

    abstract val presenter: BaseFragmentPresenter?
    abstract val contentViewId: Int
    abstract fun createViewBinding(): B

    private var contentView: View? = null
    private var errorView: View? = null
    private var errorImageView: ImageView? = null
    private var errorTitleView: TextView? = null
    private var errorDescriptionView: TextView? = null
    private var progressRootView: View? = null
    protected open var isDarkTheme = false
    protected open var contentThemeDark = false

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        applyOtherTheme()
        _binding = createViewBinding()
        return binding.root
    }

    override fun onDestroyView() {
        _binding = null
        super.onDestroyView()
    }

    @CallSuper
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        baseInitUi(view)
        initData()
        initUi()
        initUx()
        presenter?.onViewCreated()
    }

    @CallSuper
    override fun onPause() {
        super.onPause()
        presenter?.onPause()
    }

    @CallSuper
    override fun onResume() {
        super.onResume()
        presenter?.onResume()
    }

    override fun onDetach() {
        super.onDetach()
//        manageSubscriptionLauncher = null
//        authLauncher = null
    }

    @CallSuper
    override fun onDestroy() {
        super.onDestroy()
        presenter?.onDestroy()
    }

    open fun initData() {}

    open fun initUi() {}

    open fun initUx() {}

    @CallSuper
    protected open fun baseInitUi(view: View) {
        with(view) {
            contentView = findViewById(contentViewId)
            errorView = findViewById(R.id.vError)
            errorImageView = findViewById(R.id.ivNotificationScreenImage)
            errorTitleView = findViewById(R.id.tvNotificationScreenTitle)
            errorDescriptionView = findViewById(R.id.tvNotificationScreenDescription)
            progressRootView = findViewById(R.id.vProgress)
            findViewById<View>(R.id.btnNotificationScreenAction)?.setOnClickListener { presenter?.retry() }
        }
    }

    private fun applyOtherTheme() {
        baseActivity?.apply {
            isDarkTheme = this@BaseMvpFragment.isDarkTheme
            applyOtherTheme()
        }
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

        isDarkTheme = contentThemeDark
        applyOtherTheme()
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
        baseActivity?.openAuth()
    }

    override fun showWeekSubscriptionSuccessConnectedSnackBar() {
        contentView?.let {
            SnackBarProvider.showSnackBar(it, getString(R.string.snackbar_text_week_subscription_success_connected))
        }
    }

    override fun showMonthSubscriptionSuccessConnectedSnackBar() {
        contentView?.let {
            SnackBarProvider.showSnackBar(it, getString(R.string.snackbar_text_month_subscription_success_connected))
        }
    }

    override fun showWeekSubscriptionSuccessDeletedSnackBar() {
        contentView?.let {
            SnackBarProvider.showSnackBar(it, getString(R.string.snackbar_text_week_subscription_success_deleted))
        }
    }

    override fun showMonthSubscriptionSuccessDeletedSnackBar() {
        contentView?.let {
            SnackBarProvider.showSnackBar(it, getString(R.string.snackbar_text_month_subscription_success_deleted))
        }
    }

    protected fun goBack() {
        activity?.onBackPressed()
    }

}
