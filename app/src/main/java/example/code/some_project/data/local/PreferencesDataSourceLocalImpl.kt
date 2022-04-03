package example.code.some_project.data.local

import android.content.Context
import android.content.SharedPreferences
import example.code.some_project.domain.datasource.local.PreferencesDataSourceLocal

class PreferencesDataSourceLocalImpl(context: Context) : PreferencesDataSourceLocal {

    private val preferences: SharedPreferences =
        context.getSharedPreferences(SOME_PREFS, Context.MODE_PRIVATE)

    companion object {
        private const val SOME_PREFS = "SOME_PREFS"
    }
}
