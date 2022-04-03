package example.code.some_project.presentation

import android.app.Application
import android.content.Context
import android.os.Bundle
import com.google.firebase.analytics.FirebaseAnalytics
import example.code.some_project.data.db.dbModule
import example.code.some_project.data.device.di.deviceModule
import example.code.some_project.data.local.di.dataSourceLocalModule
import example.code.some_project.data.remote.di.dataSourceRemoteModule
import example.code.some_project.data.repo.di.repoModule
import example.code.some_project.domain.interactor.di.interactorModule
import example.code.some_project.network.di.networkModule
import example.code.some_project.presentation.di.screensModule
import example.code.some_project.presentation.ui.util.Constansts.KEY_RU_LANGUAGE
import io.paperdb.Paper
import io.reactivex.plugins.RxJavaPlugins
import org.koin.android.ext.koin.androidContext
import example.code.some_project.BuildConfig
import org.koin.core.context.startKoin
import timber.log.Timber


class SomeApplication : Application() {

    init {
        instance = this
    }
    private lateinit var analytics: FirebaseAnalytics


    companion object {
        var instance: SomeApplication? = null

        val context: Context
            get() = instance!!.applicationContext

        var currentLanguage: String = KEY_RU_LANGUAGE
    }

    override fun onCreate() {
        super.onCreate()
        initLogger()
        initPaper()
        initDI()
        RxJavaPlugins.setErrorHandler { t -> Timber.tag("SomeApp ErrorHandler").w(t) }
//        ServiceFactory.disableSsl()
    }

    private fun initPaper() {
        Paper.init(this)
    }

    private fun initDI() {
        val allModules = listOf(
            dbModule,
            networkModule,
            deviceModule,
            repoModule,
            interactorModule,
            dataSourceLocalModule,
            dataSourceRemoteModule,
            screensModule,
        )
        startKoin {
            androidContext(this@SomeApplication)
            modules(allModules)
        }
    }

    private fun initLogger() {
        if (BuildConfig.DEBUG) {
            Timber.plant(Timber.DebugTree())
        }
    }

    fun logOpenScreen(name: String) {
        val bundle = Bundle()
        bundle.putString(FirebaseAnalytics.Param.ITEM_NAME, name)
        bundle.putString(FirebaseAnalytics.Param.CONTENT_TYPE, "screen")
        analytics.logEvent(FirebaseAnalytics.Event.SELECT_CONTENT, bundle)

    }
}