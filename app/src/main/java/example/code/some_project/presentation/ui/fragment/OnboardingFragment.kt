package example.code.some_project.presentation.ui.fragment

import android.content.Intent
import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import androidx.core.view.isVisible
import example.code.some_project.R
import example.code.some_project.databinding.ViewNotificationBinding
import example.code.some_project.presentation.mvp.base.BaseFragmentPresenter
import example.code.some_project.presentation.ui.activity.auth.AuthActivity
import example.code.some_project.presentation.ui.base.BaseMvpFragment
import example.code.some_project.presentation.ui.extensions.withArguments
import example.code.some_project.presentation.ui.listener.ActivityInterfaceListener
import example.code.some_project.presentation.ui.util.Constansts.KEY_ONBOARDING_DESCRIPTION
import example.code.some_project.presentation.ui.util.Constansts.KEY_ONBOARDING_IMAGE
import example.code.some_project.presentation.ui.util.Constansts.KEY_ONBOARDING_TITLE

class OnboardingFragment : BaseMvpFragment<ViewNotificationBinding>() {

    private val activityListener get() = activity as? ActivityInterfaceListener

    override val presenter: BaseFragmentPresenter? = null
    override val contentViewId: Int = R.id.vNotificationScreen
    override var isDarkTheme: Boolean = true
    override var contentThemeDark: Boolean = true

    override fun createViewBinding() = ViewNotificationBinding.inflate(layoutInflater)

    override fun initUi() {
        with(binding) {
            ivNotificationScreenClose.isVisible = true
            arguments?.apply {
                ivNotificationScreenImage.setImageResource(getInt(KEY_ONBOARDING_IMAGE))
                tvNotificationScreenTitle.setText(getInt(KEY_ONBOARDING_TITLE))
                tvNotificationScreenDescription.setText(getInt(KEY_ONBOARDING_DESCRIPTION))
            }
            btnNotificationScreenAction.text = getString(R.string.onboarding_button_text)
        }
    }

    override fun initUx() {
        with(binding) {
            ivNotificationScreenClose.setOnClickListener {
                context?.let {
                    Intent(AuthActivity.createIntent(context = it, isFromOnboarding = true))
               }
                    .let(::startActivity)
            }
            btnNotificationScreenAction.setOnClickListener { activityListener?.onActionClicked() }
        }
    }

    companion object {
        fun newInstance(
            @DrawableRes image: Int,
            @StringRes title: Int,
            @StringRes description: Int
        ): OnboardingFragment = OnboardingFragment().withArguments {
            putInt(KEY_ONBOARDING_IMAGE, image)
            putInt(KEY_ONBOARDING_TITLE, title)
            putInt(KEY_ONBOARDING_DESCRIPTION, description)
        }
    }
}