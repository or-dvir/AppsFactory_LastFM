package com.hotmail.or_dvir.appsfactory_lastfm.vvm

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupWithNavController
import com.hotmail.or_dvir.appsfactory_lastfm.R
import com.hotmail.or_dvir.appsfactory_lastfm.databinding.ActivityMainBinding

class ActivityMain : AppCompatActivity()
{
    //todo
    // go over ALL to-do notes!!!
    // add documentation for everything!!!
    // go over EVERY SINGLE FILE and check if there is any unnecessary/commented out code
    // go over EVERY SINGLE FILE and auto arrange code and imports
    // add note that you assume that no parameters are null from the server
    //      if you have time, handle nulls
    // in every fragment/activity, check the error messages and make them more specific
    // look in the instructions, and wherever appropriate, add "no internet" error
    // some entire folders were copied from other projects. remove all unused files!!!

    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?)
    {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setSupportActionBar(binding.toolbar)

        findNavController(R.id.nav_host_fragment).apply {
            binding.toolbar.setupWithNavController(this, AppBarConfiguration(graph))
        }
    }
}