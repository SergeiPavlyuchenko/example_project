package example.code.some_project.presentation.ui.activity.onboarding

import android.content.Intent
import android.view.View
import androidx.viewpager2.widget.ViewPager2
import kotlinx.android.synthetic.main.activity_onboarding.*
import example.code.some_project.R
import example.code.some_project.databinding.ActivityOnboardingBinding
import example.code.some_project.presentation.ui.activity.main.MainActivity
import example.code.some_project.presentation.ui.activity.viewData.OnboardingScreenData
import example.code.some_project.presentation.ui.adapter.OnboardingAdapter
import example.code.some_project.presentation.ui.base.BaseMvpActivity
import example.code.some_project.presentation.ui.base.binding.viewBinding
import example.code.some_project.presentation.ui.extensions.lastItemPosition
import example.code.some_project.presentation.ui.listener.ActivityInterfaceListener
import moxy.ktx.moxyPresenter
import org.koin.android.ext.android.get

class OnboardingActivity : BaseMvpActivity(), ActivityInterfaceListener, OnboardingActivityView {

    private val binding by viewBinding(ActivityOnboardingBinding::inflate)
    override val presenter: AbstractOnboardingActivityPresenter by moxyPresenter { get() }
    override val contentViewId: Int = 0
    private lateinit var onboardingAdapter: OnboardingAdapter
    private lateinit var dotViews: List<View>

    private val screens = listOf(
        OnboardingScreenData(
            image = R.drawable.img_onboarding1,
            title = R.string.onboarding1_title,
            description = R.string.onboarding1_description
        ),
        OnboardingScreenData(
            image = R.drawable.img_onboarding2,
            title = R.string.onboarding2_title,
            description = R.string.onboarding2_description
        )
    )

    override fun initUi() {
        setContentView(binding.root)
        onboardingAdapter = OnboardingAdapter(screens, this)
        dotViews = listOf(
            findViewById(R.id.dot1),
            findViewById(R.id.dot2)
        )

        with(vpOnboardingPager) {
            adapter = onboardingAdapter
            registerOnPageChangeCallback(object : ViewPager2.OnPageChangeCallback() {
                override fun onPageSelected(position: Int) = setupDots(position)
            })
        }

    }

    override fun onActionClicked() {
        with(binding) {
            if (vpOnboardingPager.currentItem >= onboardingAdapter.lastItemPosition) {
                presenter.onLastClicked()
            } else {
                vpOnboardingPager.setCurrentItem(vpOnboardingPager.currentItem + 1, true)
            }
        }
    }

    override fun openMainPage() {
        Intent(this, MainActivity::class.java)
            .addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
            .let(::startActivity)
        finish()
    }

    private fun setupDots(newPosition: Int) {
        dotViews.forEach { view -> view.isSelected = false }
        dotViews[newPosition].isSelected = true
    }
}