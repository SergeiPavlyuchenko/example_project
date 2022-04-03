package example.code.some_project.presentation.ui.activity.viewData

import androidx.annotation.DrawableRes
import androidx.annotation.StringRes

data class OnboardingScreenData(
    @DrawableRes val image: Int,
    @StringRes val title: Int,
    @StringRes val description: Int
)
