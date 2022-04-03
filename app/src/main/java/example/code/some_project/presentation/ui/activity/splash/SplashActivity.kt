package example.code.some_project.presentation.ui.activity.splash

import android.content.Intent
import android.os.Handler
import android.os.Looper
import example.code.some_project.R
import example.code.some_project.databinding.ActivitySplashBinding
import example.code.some_project.presentation.ui.activity.auth.AuthActivity
import example.code.some_project.presentation.ui.activity.main.MainActivity
import example.code.some_project.presentation.ui.activity.onboarding.OnboardingActivity
import example.code.some_project.presentation.ui.base.BaseMvpActivity
import example.code.some_project.presentation.ui.base.binding.viewBinding
import moxy.ktx.moxyPresenter
import org.koin.android.ext.android.get

class SplashActivity : BaseMvpActivity(), SplashView {

    private val binding by viewBinding(ActivitySplashBinding::inflate)
    override val presenter: AbstractSplashPresenter by moxyPresenter { get() }
    override val contentViewId: Int = R.id.root

    override fun initUi() {
        setContentView(binding.root)
    }

    override fun openOnboarding() {
        startActivity(Intent(this, OnboardingActivity::class.java))
        finish()
    }

    override fun openAuthorization() {
        startActivity(AuthActivity.createIntent(this, true))
        finish()
    }

    override fun openMain() {
        startActivity(MainActivity.createIntent(this))
        finish()
    }

    override fun initializationFailed() {
        showCommonErrorSnackBar()
        Handler(Looper.getMainLooper()).postDelayed({ finish() }, 500)
    }
}