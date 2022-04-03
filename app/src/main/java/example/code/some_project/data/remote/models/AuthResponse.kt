package example.code.some_project.data.remote.models

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class AuthResponse(
    @Json(name = "result")
    val authResult: AuthResult,
    @Json(name = "accessToken")
    val newAccessToken: String,
    @Json(name = "refreshToken")
    val newRefreshToken: String
)

@JsonClass(generateAdapter = true)
data class AuthResult(
    @Json(name = "id")
    val id: Int,
    @Json(name = "msisdn")
    val msisdn: String
)
