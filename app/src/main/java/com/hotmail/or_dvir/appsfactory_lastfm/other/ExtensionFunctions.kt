package com.hotmail.or_dvir.appsfactory_lastfm.other

import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkInfo
import android.view.View
import android.widget.ImageView
import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import com.google.android.material.snackbar.Snackbar
import com.squareup.picasso.Picasso
import or_dvir.hotmail.com.dxutils.hideKeyBoard

/**
 * shows a [Snackbar] for [Snackbar.LENGTH_SHORT] duration, with the given [text]
 */
fun View.snackbar(text: String)
{
    hideKeyBoard(this, 0)
    Snackbar.make(this, text, Snackbar.LENGTH_SHORT).show()
}

/**
 * a convenience function for [snackbar]
 */
fun View.snackbar(@StringRes textRes: Int)
{
    hideKeyBoard(this, 0)
    snackbar(resources.getString(textRes))
}

/**
 * shows a [Snackbar] for [Snackbar.LENGTH_LONG] duration, with the given [text]
 */
fun View.longSnackbar(text: String)
{
    hideKeyBoard(this, 0)
    Snackbar.make(this, text, Snackbar.LENGTH_LONG).show()
}

/**
 * a convenience function for [longSnackbar]
 */
fun View.longSnackbar(@StringRes textRes: Int)
{
    hideKeyBoard(this, 0)
    longSnackbar(resources.getString(textRes))
}

/**
 * returns whether or not the device has internet connection at the moment this function
 * was called
 */
fun Context.hasInternetConnection(): Boolean
{
    //these classes/methods were only deprecated in API 29.
    //for the purposes of this demo app, its good enough to still use them
    val cm = this.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
    val activeNetwork: NetworkInfo? = cm.activeNetworkInfo

    return activeNetwork?.isConnectedOrConnecting == true
}

/**
 * a helper class using the Picasso library to load an image into this [ImageView].
 *
 * @param url String? the url of the image to load. if the value is null, the given error image
 * will be shown
 * @param errorRes Int the resource ID of an image to show in case the loading of [url] failed
 */
fun ImageView.loadWithPicasso(url: String?, @DrawableRes errorRes: Int)
{
    //p for picasso
    var pUrl = url

    if (pUrl.isNullOrBlank())
    {
        //any string to make picasso fail and load the error image.
        //cannot be empty string (picasso crashes)
        pUrl = "123"
    }

    Picasso.get().load(pUrl).error(errorRes).into(this)
}
