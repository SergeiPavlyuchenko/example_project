package example.code.some_project.presentation.ui.activity.auth

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.text.InputFilter
import android.widget.ScrollView
import androidx.constraintlayout.widget.ConstraintSet
import androidx.core.content.ContextCompat
import androidx.core.view.isInvisible
import androidx.core.view.isVisible
import androidx.core.widget.doOnTextChanged
import kotlinx.android.synthetic.main.activity_auth.*
import example.code.some_project.R
import example.code.some_project.databinding.ActivityAuthBinding
import example.code.some_project.presentation.ui.activity.main.MainActivity
import example.code.some_project.presentation.ui.base.BaseMvpActivity
import example.code.some_project.presentation.ui.base.binding.viewBinding
import example.code.some_project.presentation.ui.extensions.hideKeyboard
import example.code.some_project.presentation.ui.extensions.setOnClick
import example.code.some_project.presentation.ui.fragment.about_product.AboutProductFragment
import example.code.some_project.presentation.ui.listener.ActivityInterfaceListener
import example.code.some_project.presentation.ui.provider.DialogProvider
import example.code.some_project.presentation.ui.util.Constansts.EXTRA_IS_FROM_ONBOARDING
import example.code.some_project.presentation.ui.util.Constansts.REQUIRED_DIGITS_OF_CODE
import example.code.some_project.presentation.utils.CharInputFilter
import example.code.some_project.presentation.utils.JavaTextWatcher
import moxy.ktx.moxyPresenter
import org.koin.android.ext.android.get

class AuthActivity : BaseMvpActivity(), AuthActivityView, ActivityInterfaceListener {

    private val binding by viewBinding(ActivityAuthBinding::inflate)
    override val presenter: AbstractAuthActivityPresenter by moxyPresenter { get() }
    override val contentViewId: Int = R.id.llAuthContentView
    private var isFromOnboarding: Boolean = false
    private var isNumberScreen: Boolean = true

    override fun initData() {
        intent?.getBooleanExtra(EXTRA_IS_FROM_ONBOARDING, false)?.let {
            isFromOnboarding = it
        }
    }

    override fun initUi() {
        setContentView(binding.root)
        vGradientView.bringToFront()
    }

    override fun initUx() {
        with(binding) {
            root.post(::disableScrollOfView)
            btnAuthMoveToOffer.setOnClick(::showOffer)
            btnAuthMoveToAuth.setOnClick(::hideOffer)
            btnAuthValidateNumber.setOnClick(::validateNumber)
            btnAuthSendSmsCode.setOnClick(::validateNumber)
            authToolbar.setNavigationOnClickListener { changeUiToNumberState() }
            tilAuthEnterNumber.editText?.apply {
                setOnFocusChangeListener { _, hasFocus -> changeHintState(hasFocus) }
                filters = arrayOf(
                    CharInputFilter(CharInputFilter.numberCharacterSet),
//                    ToDo изменить когда прикрутишь кастомный
                    InputFilter.LengthFilter(12)
                )
                doOnTextChanged { _, _, _, _ -> clearErrorState() }
//                ToDo доработать и запилить кастомный
//                addTextChangedListener(CustomTextWatcher(this))
                addTextChangedListener(JavaTextWatcher(this))
            }
            tilAuthEnterPassword.editText?.apply {
                setOnFocusChangeListener { _, hasFocus -> changeHintState(hasFocus) }
                filters = arrayOf(
                    CharInputFilter(CharInputFilter.codeCharacterSet),
                    InputFilter.LengthFilter(REQUIRED_DIGITS_OF_CODE)
                )
                doOnTextChanged { text, _, _, _ ->
                    clearErrorState()
                    if (text?.length == REQUIRED_DIGITS_OF_CODE) {
                        presenter.validateSmsCode(text.toString())
                    }
                }
            }
            tvMoveToFullOffer.setOnClick(::navigateToAboutApp)
        }
    }

    private fun changeUiToNumberState() {
        isNumberScreen = true
        binding.run {
            authToolbar.isVisible = false
            tvAuthTitle.isVisible = true
            tvAuthTitle.text = getString(R.string.auth_number_title)
            tvAuthDescription.isVisible = true
            tvAuthDescription.text = ""
            tvAuthNumberPasswordHint.isVisible = true
            tvAuthNumberPasswordHint.text = getString(R.string.auth_number_hint)
            tilAuthEnterNumber.isVisible = true
            tilAuthEnterPassword.isVisible = false
            tilAuthEnterNumber.hint = null
            btnAuthSendSmsCode.isVisible = false
            btnAuthValidateNumber.isVisible = true
            llRetryAfterTimer.isVisible = false
            btnAuthValidateNumber.setOnClick(::validateNumber)
            tvOfferTitle.isVisible = false
            vGradientView.isVisible = true
            btnAuthMoveToAuth.isVisible = false
            btnAuthMoveToOffer.isVisible = true
            changeHintState(false)
        }
    }

    override fun changeUiToPasswordState() {
        isNumberScreen = false
        binding.run {
            authToolbar.isVisible = true
            tvAuthTitle.isVisible = true
            tvAuthTitle.text = getString(R.string.auth_password_title)
            tvAuthDescription.isVisible = false
            tvAuthNumberPasswordHint.isVisible = true
            tvAuthNumberPasswordHint.text = getString(R.string.auth_password_hint)
            tilAuthEnterNumber.isVisible = false
            tilAuthEnterPassword.isVisible = true
            btnAuthValidateNumber.isVisible = false
            tvAuthDescription.isVisible = true
            vGradientView.isVisible = true
            tvOfferTitle.isVisible = false
            btnAuthMoveToAuth.isVisible = false
            btnAuthMoveToOffer.isVisible = true
            tvAuthDescription.text =
                getString(R.string.auth_password_description, tilAuthEnterNumber.editText?.text)

            if (presenter.isTimerLaunched) {
                llRetryAfterTimer.isVisible = true
                btnAuthSendSmsCode.isVisible = false
            } else {
                llRetryAfterTimer.isVisible = false
                btnAuthSendSmsCode.isVisible = true
            }
        }

    }

    private fun validateNumber() = binding.run {
        tilAuthEnterNumber.editText?.text.toString().let(presenter::validateNumberAndSendSmsCode)
    }

    private fun changeHintState(hasFocus: Boolean) {
        changeNumberHintState(hasFocus)
        changePasswordHintState(hasFocus)
    }

    private fun changeNumberHintState(hasFocus: Boolean) = binding.run {
        val isNumberEmpty = tilAuthEnterNumber.editText?.text?.isEmpty() ?: true
        if (hasFocus || !isNumberEmpty) {
            tilAuthEnterNumber.hint = null
        } else {
            tilAuthEnterNumber.hint = getString(R.string.auth_enter_number_hint)
        }
    }

    private fun changePasswordHintState(hasFocus: Boolean) = binding.run {
        val isPasswordEmpty = tilAuthEnterPassword.editText?.text?.isEmpty() ?: true
        if (hasFocus || !isPasswordEmpty) {
            tilAuthEnterPassword.hint = null
        } else {
            tilAuthEnterPassword.hint = getString(R.string.auth_enter_password_hint)
        }
    }

    @SuppressLint("ClickableViewAccessibility")
    private fun disableScrollOfView(isDisable: Boolean = true) {
        with(binding) {
            svOffer.setOnTouchListener { _, _ -> isDisable }
        }
    }

    private fun showOffer() {
        val set = ConstraintSet()
        disableScrollOfView(false)
        hideKeyboard()

        with(binding) {
            authToolbar.isVisible = false
            tvOfferTitle.isVisible = true
            vGradientView.isVisible = false

            btnAuthMoveToAuth.isVisible = true
            btnAuthMoveToOffer.isVisible = false

            tvAuthTitle.isVisible = false
            tvAuthDescription.isVisible = false
            tvAuthNumberPasswordHint.isVisible = false
            tilAuthEnterPassword.isVisible = false
            btnAuthValidateNumber.isVisible = false
            btnAuthSendSmsCode.isVisible = false
            llRetryAfterTimer.isVisible = false
            tilAuthEnterNumber.isVisible = false

            svOffer.post {
                svOffer.smoothScrollTo(0, tvOfferTitle.y.toInt())
            }
        }

        with(set) {
            clone(binding.clAuthActivityContainer)
            connect(
                R.id.svOffer,
                ConstraintSet.TOP,
                R.id.btnAuthMoveToAuth,
                ConstraintSet.BOTTOM
            )
            applyTo(binding.clAuthActivityContainer)
        }
    }

    private fun hideOffer() {
        val set = ConstraintSet()
        disableScrollOfView()

        with(binding) {
            if (isNumberScreen) {
                changeUiToNumberState()
            } else {
                changeUiToPasswordState()
            }

            svOffer.post {
                svOffer.fullScroll(ScrollView.FOCUS_UP)
            }

        }

        with(set) {
            clone(binding.clAuthActivityContainer)
            clear(R.id.svOffer, ConstraintSet.TOP)
            connect(
                R.id.svOffer,
                ConstraintSet.TOP,
                R.id.vGradientView,
                ConstraintSet.TOP,
            )
            applyTo(binding.clAuthActivityContainer)
        }
    }

    private fun clearErrorState() {
        binding.run {
            tilAuthEnterNumber.error = null
            tilAuthEnterPassword.error = null
            tilAuthEnterNumber.editText?.setTextColor(
                ContextCompat.getColor(this@AuthActivity, R.color.black_to_rename)
            )
            tilAuthEnterPassword.editText?.setTextColor(
                ContextCompat.getColor(this@AuthActivity, R.color.black_to_rename)
            )
        }

    }

    override fun setNumberFieldAsNotOperatorError() = binding.run {
        tilAuthEnterNumber.error = getString(R.string.auth_error_number)
        tilAuthEnterNumber.errorIconDrawable = null
    }

    override fun setNumberFieldAsEmptyError() = binding.run {
        tilAuthEnterNumber.error = getString(R.string.auth_error_empty_number)
        tilAuthEnterNumber.errorIconDrawable = null
    }

    override fun setNumberFieldAsIncorrectError() {
        tilAuthEnterNumber.error = getString(R.string.auth_error_not_full_number)
        tilAuthEnterNumber.errorIconDrawable = null
    }

    override fun setPasswordFieldAsError() = binding.run {
        tilAuthEnterPassword.error = getString(R.string.auth_error_code)
        tilAuthEnterPassword.errorIconDrawable = null
    }

    override fun setTimerCount(count: String) {
        binding.tvTimer.text = count
    }

    override fun clearTimer() {
        binding.tvTimer.text = getString(R.string.auth_timer_default_text)
    }

    override fun showProgressBar() {
        binding.pbAuthProgress.root.isVisible = true
    }

    override fun hideProgressBar() {
        binding.pbAuthProgress.root.isVisible = false
    }

    override fun onBackPressed() {
        if (isFromOnboarding) {
            DialogProvider.createDialog(
                context = this,
                title = getString(R.string.common_dialog_exit_title),
                description = getString(R.string.common_dialog_exit_description),
                positiveButtonText = getString(R.string.common_dialog_exit_yes_button_text),
                negativeButtonText = getString(R.string.common_dialog_cancel_button_text),
                positiveButtonListener = this::finishAffinity
            ).show()
        } else {
            super.onBackPressed()
        }
    }

    override fun navigateToMainPage() {
        Intent(this, MainActivity::class.java).let(::startActivity)
        finish()
    }

    override fun showFiveMinutesBlockDialog() = DialogProvider
        .createDialog(
            context = this,
            title = getString(R.string.auth_spam_block_title),
            description = getString(R.string.auth_5_minutes_block_description),
            positiveButtonText = getString(R.string.common_dialog_ok_button_text)
        )
        .show()

    override fun showTwentyFourHoursBlockDialog() = DialogProvider
        .createDialog(
            context = this,
            title = getString(R.string.auth_spam_block_title),
            description = getString(R.string.auth_24_hours_block_description),
            positiveButtonText = getString(R.string.common_dialog_ok_button_text)
        )
        .show()

    override fun setAuthOfferDescription(text: String) {
        binding.tvOfferDescription.text = text
    }

    override fun openAuth() {
        // Do nothing
    }

    private fun navigateToAboutApp() {
        supportFragmentManager.beginTransaction()
            .replace(R.id.clAuthActivityContainer, AboutProductFragment())
            .addToBackStack(null)
            .commit()

        binding.btnAuthMoveToAuth.isInvisible = true
    }

    override fun onActionClicked() {
        binding.btnAuthMoveToAuth.isInvisible = false
    }

    companion object {
        private const val MAX_LENGTH_FOR_CURRENT_MASK = 12

        fun createIntent(
            context: Context,
            isFromOnboarding: Boolean = false
        ) = Intent(context, AuthActivity::class.java)
            .putExtra(EXTRA_IS_FROM_ONBOARDING, isFromOnboarding)
    }
}


/*

// ToDo изначально делал контентную вьюху через scrollView, но потом упёрся в то что сам большой
    текст оферты тоже нужно скроллить на мелких экранах, nestedScrollView не выручила, т.к
    скролится и основная scrollView.
    Пока оставить так, если примут - этот код удалить. Если нет, то использовать.
private fun initContentPlacement() {
        with(binding) {
            tvAuthDescription.isVisible = false
            val displayMetrics = resources.displayMetrics
            val middleOfScreenSize = displayMetrics.heightPixels / 2
            val middleContentSize = (
                    (tilEnterNumberPassword.height / 2) + tvAuthNumberPasswordHint.height +
                            resources.getDimensionPixelSize(R.dimen.margin_big) +
                            (if (tvAuthDescription.isVisible) tvAuthDescription.height else 0) +
                            resources.getDimensionPixelSize(R.dimen.margin_common) +
                            tvAuthTitle.height +
                            deviceManager?.getComplexBarsHeight().orZero()
                    )
            val marginTop = middleOfScreenSize - middleContentSize
            (llAuthContentView.layoutParams as ViewGroup.MarginLayoutParams).apply {
                setMargins(leftMargin, marginTop, rightMargin, bottomMargin)
            }.let(llAuthContentView::setLayoutParams)

            initOfferPlacement()

            // block from showOffer
//            (tvOfferDescription.layoutParams as ViewGroup.MarginLayoutParams).apply {
//                setMargins(leftMargin, resources.getDimensionPixelSize(R.dimen.margin_small), rightMargin, bottomMargin)
//            }.let(tvOfferDescription::setLayoutParams)
///////////////
//
//            val displayMetrics = resources.displayMetrics
//            val middleOfScreenSize = displayMetrics.heightPixels / 2
//            val middleContentSize = (
//                    (tilEnterNumberPassword.height / 2) + btnAuth.height +
//                            resources.getDimensionPixelSize(R.dimen.auth_main_button_margin) +
//                            deviceManager?.getComplexBarsHeight().orZero()
//                    )
//            val marginTop = middleOfScreenSize - middleContentSize
//            (tvOfferTitle.layoutParams as ViewGroup.MarginLayoutParams).apply {
//                setMargins(leftMargin, marginTop, rightMargin, bottomMargin)
//            }.let(tvOfferTitle::setLayoutParams)
//
//
//            val screenSize = displayMetrics.heightPixels
//            (tvOfferDescription.layoutParams as ViewGroup.LayoutParams).apply {
//                width = ViewGroup.LayoutParams.MATCH_PARENT
//                height = screenSize
//            }.let(tvOfferDescription::setLayoutParams)
        }
    }

    private fun initOfferPlacement() {
        val displayMetrics = resources.displayMetrics
        val middleOfScreenSize = displayMetrics.heightPixels / 2
        with(binding) {
            val marginTop = middleOfScreenSize - (tilEnterNumberPassword.height / 2 +
                    resources.getDimensionPixelSize(R.dimen.auth_main_button_margin) +
                    btnAuth.height +
                    resources.getDimensionPixelSize(R.dimen.auth_offer_top_margin))

            (tvOfferDescription.layoutParams as ViewGroup.MarginLayoutParams).apply {
                setMargins(leftMargin, marginTop, rightMargin, bottomMargin)
            }.let(tvOfferDescription::setLayoutParams)
        }
    }*/