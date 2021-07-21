package com.hotmail.or_dvir.appsfactory_lastfm.model

import com.hotmail.or_dvir.appsfactory_lastfm.model.Image.Companion.Size

/**
 * an interface representing all objects which may contain an image (e.g. [Album], [Artist])
 */
interface IModelWithImages
{
    /**
     * returns the list of [Image]s of the object implementing this interface
     */
    fun getImageList(): List<Image>

    /**
     * return the url of an [Image] for the object implementing this interface.
     * this function returns the url for the [Size.LARGE] image.
     * If that url is not valid, this function returns thr url for the [Size.MEDIUM] image.
     * if that url is also not valid, the function returns null.
     */
    fun getImageUrl() = getImageList().filterNot { it.url.isNullOrBlank() }.let { images ->
        //first look for the large image, and if not available, look for the medium image.
        //for the purposes of this demo app it's good enough
        images.find { it.size == Size.LARGE } ?: images.find { it.size == Size.MEDIUM }
    }?.url
}