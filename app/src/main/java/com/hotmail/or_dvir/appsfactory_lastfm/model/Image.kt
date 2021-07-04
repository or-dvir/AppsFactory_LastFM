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
        //todo make sure serialization of this works with retrofit/moshi!!!!
        enum class Size
        {
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

            //todo what other possible "edge case" values are there?
            // add a note in documentation...
            @Json(name = "")
            UNKNOWN()
        }
    }
}