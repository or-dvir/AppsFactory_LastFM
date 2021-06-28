package com.hotmail.or_dvir.appsfactory_lastfm

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupWithNavController
import com.hotmail.or_dvir.appsfactory_lastfm.databinding.ActivityMainBinding

class ActivityMain : AppCompatActivity()
{
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