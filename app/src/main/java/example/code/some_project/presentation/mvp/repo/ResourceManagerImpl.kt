package example.code.some_project.presentation.mvp.repo

import android.content.Context
import android.content.pm.PackageManager
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.graphics.drawable.Drawable
import androidx.annotation.PluralsRes
import androidx.annotation.StringRes
import androidx.core.content.ContextCompat
import io.reactivex.Single
import example.code.some_project.R
import example.code.some_project.domain.repo.device.ResourcesManager
import example.code.some_project.data.util.Language
import java.util.*

class ResourceManagerImpl(private val context: Context) : ResourcesManager {

    override fun getString(resourceId: Int): String {
        return context.getString(resourceId)
    }

    override fun getString(@StringRes resourceId: Int, vararg args: Any): String =
        context.getString(resourceId, *args)


    override fun getPluralString(@PluralsRes resourceId: Int, count: Int): String =
        context.resources.getQuantityString(resourceId, count)

    override fun getPluralString(
        @PluralsRes resourceId: Int,
        count: Int,
        vararg args: Any
    ): String =
        context.resources.getQuantityString(resourceId, count, args)


    override fun getLanguage(): Language =
        if (Locale.getDefault().displayLanguage != context.getString(R.string.kz_language)) {
            Language.RU
        } else {
            Language.KZ
        }


    override fun getColor(id: Int): Int = ContextCompat.getColor(context, id)

    override fun getDrawable(resId: Int): Drawable {
        return ContextCompat.getDrawable(context, resId)
            ?: ColorDrawable(Color.TRANSPARENT)
    }

    override fun isPermissionGranted(permission: String): Single<Boolean> =
        Single.just(permission)
            .map {
                ContextCompat.checkSelfPermission(context, it) == PackageManager.PERMISSION_GRANTED
            }

}