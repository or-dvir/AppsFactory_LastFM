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
    // in every fragment/activity, check the error messages and make them more specific
    // look in the instructions, and wherever appropriate, add "no internet" error
    // some entire project folders were copied from other projects. remove all unused files!!!
    // i made all fields in all classes nullable or have default values.
    //      its possible there will be side-effects (e.g. null strings will show as "null").
    //      check app thoroughly
    // add note that since "id" field is unreliable (can be missing or empty), we use names instead
    // as "unique" identifiers. they may also be missing or empty, but then its just the same as id,
    //      so we don't really need this extra field
    // add the instructional pdf file to the repository so you can make comments like
    //      "i didnt add this obvious feature because it was not in the instructions"
    // think about obvious features you did not add, and add comment what you would also do
    //      e.g. remove album from favorites from the "favorites" screen
    // add note saying that no special attention was given to landscape mode
    //      make basic tests in landscape mode!!!
    // for all network requests, add exception handler! especially for timeouts!

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