package example.code.some_project.presentation.ui.util.glide

import android.content.Context
import com.bumptech.glide.Glide
import com.bumptech.glide.Registry
import com.bumptech.glide.annotation.GlideModule
import com.bumptech.glide.module.AppGlideModule

//ToDo use if will be troubles with Glide
@GlideModule
class UnsafeOkHttpGlideModule : AppGlideModule() {
    override fun registerComponents(context: Context, glide: Glide, registry: Registry) {
//        val client = ServiceFactory.createUnsafeOkhttp()
//        registry.replace(
//            GlideUrl::class.java, InputStream::class.java, OkHttpUrlLoader.Factory(client)
//        )
    }
}