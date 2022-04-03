package example.code.some_project.data.local

import android.content.SharedPreferences
import example.code.some_project.data.util.string
import example.code.some_project.domain.datasource.local.AuthDataSourceLocal

class AuthDataSourceLocalImpl(
    private val userPrefs: SharedPreferences
): AuthDataSourceLocal {

    override var accessToken: String by userPrefs.string(key = { ACCESS_TOKEN_KEY })

    override var refreshToken: String by userPrefs.string(key = { REFRESH_TOKEN_KEY })

    override var msisdn: String by userPrefs.string(key = { MSISDN_KEY })

    companion object {
        private const val ACCESS_TOKEN_KEY = "ACCESS_TOKEN_KEY"
        private const val REFRESH_TOKEN_KEY = "REFRESH_TOKEN_KEY"
        private const val LANGUAGE_KEY = "LANGUAGE_KEY"
        private const val MSISDN_KEY = "MSISDN_KEY"
    }

}