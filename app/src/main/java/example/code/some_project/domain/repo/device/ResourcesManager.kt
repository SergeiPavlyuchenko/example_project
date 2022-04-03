package example.code.some_project.domain.repo.device

import android.graphics.drawable.Drawable
import androidx.annotation.StringRes
import io.reactivex.Single
import example.code.some_project.data.util.Language

interface ResourcesManager {

    fun getString(@StringRes resourceId: Int): String

    fun getString(@StringRes resourceId: Int, vararg args: Any): String

    fun getPluralString(@StringRes resourceId: Int, count: Int): String

    fun getPluralString(@StringRes resourceId: Int, count: Int, vararg args: Any): String

    fun getLanguage(): Language

    fun getColor(id: Int): Int

    fun getDrawable(resId: Int): Drawable

    fun isPermissionGranted(permission: String): Single<Boolean>

}