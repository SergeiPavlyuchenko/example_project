package example.code.some_project.data.local.di

import android.app.Application
import android.content.Context
import android.content.SharedPreferences
import example.code.some_project.data.local.AuthDataSourceLocalImpl
import example.code.some_project.data.local.ContactsProviderImpl
import example.code.some_project.data.local.PreferencesDataSourceLocalImpl
import example.code.some_project.data.local.storage.PaperLocalStorageImpl
import example.code.some_project.domain.datasource.local.AuthDataSourceLocal
import example.code.some_project.domain.datasource.local.PreferencesDataSourceLocal
import example.code.some_project.domain.datasource.local.storage.LocalStorage
import example.code.some_project.domain.repo.device.ContactsProvider
import org.koin.android.ext.koin.androidApplication
import org.koin.dsl.module

val dataSourceLocalModule = module {
    single<LocalStorage> { PaperLocalStorageImpl() }
    single<PreferencesDataSourceLocal> { PreferencesDataSourceLocalImpl(get()) }
    single<AuthDataSourceLocal> { AuthDataSourceLocalImpl(userPrefs = get()) }
    single { provideUserPreferences(androidApplication()) }
    single<ContactsProvider> { ContactsProviderImpl(get()) }
}

private const val COMMON_PREFS_KEY = "COMMON_PREFS_KEY"

private fun provideUserPreferences(app: Application): SharedPreferences =
    app.getSharedPreferences(COMMON_PREFS_KEY, Context.MODE_PRIVATE)