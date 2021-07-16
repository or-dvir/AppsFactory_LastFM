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
        enum class Size
        {
            //todo only keep 2 sizes, and create a mechanism that
            // if one is not available, try the other one.
            // remove the rest (unused)
            @Json(name = "small")
            SMALL(),

            @Json(name = "medium")
            MEDIUM(),

            @Json(name = "large")
            LARGE(),

            @Json(name = "extralarge")
            EXTRA_LARGE,

            @Json(name = "mega")
            MEGA(),

            //its possible that there are other "edge cases",
            //however i have not encountered any.
            @Json(name = "")
            UNKNOWN()
        }
    }
}