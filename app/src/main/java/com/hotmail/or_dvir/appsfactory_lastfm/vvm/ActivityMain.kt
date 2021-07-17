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
    // go over EVERY SINGLE FILE and auto arrange code and imports
    // go over EVERY SINGLE FILE and check ALL warnings
    // some entire project folders were copied from other projects. remove all unused files!!!
    // think about obvious features you did not add, and add comment what you would also do
    //      e.g. remove album from favorites from the "favorites" screen
    // make basic tests in landscape mode
    // for all network requests, handle timeouts (already have exception handlers, show specific message)

    //General Notes:
    //
    //for simplicity, errors are handled in a generic way (simple, non-specific error messages).
    //in a real app, we would handle specific errors appropriately and display more meaningful
    //messages to the user.
    //
    //it seems that all artist images are the same (image of a star icon).
    //due to time constraints, and given that this is an issue with the api, there is nothing
    //we can do about it.
    //
    //even though all fields in all classes are either nullable or have default values,
    //its possible that the lastFM api still holds some surprises for us... for example
    //we know for sure that some strings have the value "(null)"
    //
    //for simplicity, no special attention was given to landscape mode
    //(or small/large screen sizes for that matter
    //
    //the lastFM api is inconsistent!!! this can lead to crashes for specific artists/albums!
    //for example, the album "Believe" by Cher contains a LIST of tracks, where as
    //the album "Empty & Alone" by Tootiredtocare contains a single track OBJECT.
    //this causes Moshi to crash because it was expecting an array but got an object.
    //the correct thing to do (in terms of the api), of course, is that albums with only
    //a single track should have that track inside a list of size 1.
    //this is an api issue! considering this is just a demo app, and due to time constraints,
    //creating a mechanism to overcome this would be too complicated.

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