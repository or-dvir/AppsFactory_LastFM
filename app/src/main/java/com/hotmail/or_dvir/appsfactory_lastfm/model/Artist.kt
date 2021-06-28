package com.hotmail.or_dvir.appsfactory_lastfm.model

import com.hotmail.or_dvir.appsfactory_lastfm.model.Image.Companion.Size
import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class Artist(
    @Json(name = "mbid")
    val id: String,
    @Json(name = "name")
    val name: String,
    @Json(name = "image")
    val images: List<Image>
)
{
    fun getImageUrl(size: Size) = images.find { it.size == size }?.url
}