package example.code.some_project.data.device

import android.content.Context
import android.util.TypedValue
import example.code.some_project.domain.extensions.orFalse
import example.code.some_project.domain.extensions.orZero
import example.code.some_project.domain.repo.device.DeviceManager
import javax.inject.Inject

@Deprecated ("view logic") // ToDo remove
class DeviceManagerImpl @Inject constructor(
    private val context: Context
) : DeviceManager {

    override fun getScreenWidth(): Int {
        return context.resources.displayMetrics.widthPixels
    }

    //  heightPixels - returns only available screen height for content.
    override fun getScreenHeight(): Int {
        return context.resources.displayMetrics.heightPixels
    }

    override fun getAppBarHeight(): Int {
        val tv = TypedValue()
        if (context.theme.resolveAttribute(android.R.attr.actionBarSize, tv, true)) {
            return TypedValue.complexToDimensionPixelSize(tv.data, context.resources.displayMetrics)
        }
        return 0
    }

    override fun getStatusBarHeight(): Int = getDimenPxByName(STATUS_BAR_HEIGHT)

    override fun getComplexBarsHeight(): Int = getAppBarHeight() + getStatusBarHeight()

    private fun getDimenPxByName(name: String): Int = context.resources
        .getIdentifier(name, DIMEN, ANDROID)
        .takeIf { it > 0 }
        ?.let(context.resources::getDimensionPixelSize)
        .orZero()

    private fun getBoolByName(name: String): Boolean = context.resources
        .getIdentifier(name, BOOL, ANDROID)
        .takeIf { it > 0 }
        ?.let(context.resources::getBoolean)
        .orFalse()

    companion object {
        private const val DIMEN = "dimen"
        private const val BOOL = "bool"
        private const val ANDROID = "android"
        private const val STATUS_BAR_HEIGHT = "status_bar_height"
    }
}
