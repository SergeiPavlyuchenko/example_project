package example.code.some_project.presentation.ui.extensions

import android.content.Context
import android.content.res.Resources
import android.util.TypedValue
import android.view.View
import example.code.some_project.domain.extensions.orZero
import kotlin.math.ceil
import kotlin.math.roundToInt

fun View.setOnClick(onClick: () -> Unit) = setOnClickListener { onClick() }

fun Int.toDp(): Int = ceil(this / Resources.getSystem().displayMetrics.density).roundToInt()

fun Context.getScreenHeight(): Int = this.resources.displayMetrics.heightPixels

fun Context.getAppBarHeight(): Int {
    val tv = TypedValue()
    if (this.theme.resolveAttribute(android.R.attr.actionBarSize, tv, true)) {
        return TypedValue.complexToDimensionPixelSize(tv.data, this.resources.displayMetrics)
    }
    return 0
}

fun Context.getStatusBarHeight(): Int = this.resources
    .getIdentifier("status_bar_height","dimen", "android")
    .takeIf { it > 0 }
    ?.let(this.resources::getDimensionPixelSize)
    .orZero()

fun Context.getComplexBarsHeight(): Int = getAppBarHeight() + getStatusBarHeight()

