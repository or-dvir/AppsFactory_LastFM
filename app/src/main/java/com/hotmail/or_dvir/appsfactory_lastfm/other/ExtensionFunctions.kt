package com.hotmail.or_dvir.appsfactory_lastfm.other

import android.view.View
import androidx.annotation.StringRes
import com.google.android.material.snackbar.Snackbar

//todo when done, delete unused functions

fun View.snackbar(text: String) = Snackbar.make(this, text, Snackbar.LENGTH_SHORT).show()
fun View.snackbar(@StringRes textRes: Int) = snackbar(resources.getString(textRes))

fun View.longSnackbar(text: String) = Snackbar.make(this, text, Snackbar.LENGTH_LONG).show()
fun View.longSnackbar(@StringRes textRes: Int) = longSnackbar(resources.getString(textRes))

/**
 * checks if this string is blank or equals to the given [str] (ignoring the case)
 */
fun String.isBlankOrEquals(str: String) = this.isBlank() || this.equals(str, true)