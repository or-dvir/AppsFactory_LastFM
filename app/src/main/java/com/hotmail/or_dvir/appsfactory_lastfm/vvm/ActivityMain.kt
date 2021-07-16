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
    // go over EVERY SINGLE FILE and check ALL warnings
    // in every fragment/activity, check the error messages and make them more specific
    // some entire project folders were copied from other projects. remove all unused files!!!
    // i made all fields in all classes nullable or have default values.
    //      its possible there will be side-effects (e.g. null strings will show as "null").
    //      check app thoroughly
    // make comments like "i didnt add this obvious feature because it was not in the instructions"
    // think about obvious features you did not add, and add comment what you would also do
    //      e.g. remove album from favorites from the "favorites" screen
    // add note saying that no special attention was given to landscape mode
    //      make basic tests in landscape mode!!!
    // for all network requests, handle timeouts (already have exception handlers, show specific message)

    //general notes:
    //for simplicity, errors are handled in a generic way (simple, non-specific error messages).
    //in a real app, we would handle specific errors appropriately and display more meaningful
    //messages to the user.
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