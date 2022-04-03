package example.code.some_project.data.remote.models

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class UpdateTokensResponse(
    @Json(name = "AccessToken")
    val accessToken: String,
    @Json(name = "RefreshToken")
    val refreshToken: String
)