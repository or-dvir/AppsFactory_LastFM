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

fun View.snackbar(text: String)
{
    hideKeyBoard(this, 0)
    Snackbar.make(this, text, Snackbar.LENGTH_SHORT).show()
}

fun View.snackbar(@StringRes textRes: Int)
{
    hideKeyBoard(this, 0)
    snackbar(resources.getString(textRes))
}

fun View.longSnackbar(text: String)
{
    hideKeyBoard(this, 0)
    Snackbar.make(this, text, Snackbar.LENGTH_LONG).show()
}

fun View.longSnackbar(@StringRes textRes: Int)
{
    hideKeyBoard(this, 0)
    longSnackbar(resources.getString(textRes))
}

fun Context.hasInternetConnection(): Boolean
{
    //these classes/methods were only deprecated in API 29.
    //for the purposes of this demo app, its good enough to still use them
    val cm = this.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
    val activeNetwork: NetworkInfo? = cm.activeNetworkInfo

    return activeNetwork?.isConnectedOrConnecting == true
}

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
