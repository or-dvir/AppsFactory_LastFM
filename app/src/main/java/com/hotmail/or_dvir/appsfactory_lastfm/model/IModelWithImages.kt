package com.hotmail.or_dvir.appsfactory_lastfm.model

import com.hotmail.or_dvir.appsfactory_lastfm.model.Image.Companion.Size

interface IModelWithImages
{
    fun getImageList(): List<Image>

    fun getImageUrl(size: Size) =
        getImageList().find {
            it.size == size && !it.url.isNullOrBlank()
        }?.url
}