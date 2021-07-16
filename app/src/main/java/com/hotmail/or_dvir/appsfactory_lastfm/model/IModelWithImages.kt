package com.hotmail.or_dvir.appsfactory_lastfm.model

import com.hotmail.or_dvir.appsfactory_lastfm.model.Image.Companion.Size

interface IModelWithImages
{
    fun getImageList(): List<Image>

    fun getImageUrl() = getImageList().filterNot { it.url.isNullOrBlank() }.let { images ->
        //first look for the large image, and if not available, look for the medium image.
        //for the purposes of this demo app it's good enough
        images.find { it.size == Size.LARGE } ?: images.find { it.size == Size.MEDIUM }
    }?.url
}