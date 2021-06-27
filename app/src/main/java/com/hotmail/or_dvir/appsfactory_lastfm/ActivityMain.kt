package com.hotmail.or_dvir.appsfactory_lastfm

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle

class ActivityMain : AppCompatActivity()
{
    //todo setup a custom application class with koin
    // dont forget to add it to the manifest

    override fun onCreate(savedInstanceState: Bundle?)
    {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
    }
}