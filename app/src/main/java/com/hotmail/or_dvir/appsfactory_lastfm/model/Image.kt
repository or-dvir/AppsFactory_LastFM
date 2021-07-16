package com.hotmail.or_dvir.appsfactory_lastfm.model

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class Image(
    @Json(name = "#text")
    val url: String?,
    @Json(name = "size")
    val size: Size?
)
{
    companion object
    {
        //even though not all of these values are used, they are required for moshi
        @Suppress("unused")
        enum class Size
        {
            @Json(name = "small")
            SMALL,

            @Json(name = "medium")
            MEDIUM,

            @Json(name = "large")
            LARGE,

            @Json(name = "extralarge")
            EXTRA_LARGE,

            @Json(name = "mega")
            MEGA,

            //its possible that there are other "edge cases",
            //however i have not encountered any.
            @Json(name = "")
            UNKNOWN
        }
    }
}