package example.code.some_project.domain.models

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class AuthData(
    @Json(name = "msisdn")
    val msisdn: String,
    @Json(name = "code")
    val smsCode: String
)
