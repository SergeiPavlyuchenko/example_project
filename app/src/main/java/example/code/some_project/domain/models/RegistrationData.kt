package example.code.some_project.domain.models

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class RegistrationData(
    @Json(name = "msisdn")
    val msisdn: String
)
