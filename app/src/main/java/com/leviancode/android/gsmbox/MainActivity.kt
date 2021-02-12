package com.leviancode.android.gsmbox

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import androidx.appcompat.app.AppCompatDelegate.setDefaultNightMode
import androidx.databinding.DataBindingUtil
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupWithNavController
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.leviancode.android.gsmbox.data.dao.AppDatabase
import com.leviancode.android.gsmbox.databinding.ActivityMainBinding


class MainActivity : AppCompatActivity(){

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
        AppDatabase.init(applicationContext)
        val binding: ActivityMainBinding = DataBindingUtil.setContentView(this, R.layout.activity_main)

        val navView: BottomNavigationView = binding.navView
        val navController = findNavController(R.id.nav_host_fragment)

        val appBarConfiguration = AppBarConfiguration(
            setOf(
                R.id.nav_template_group_list,
                R.id.nav_favorites,
                R.id.nav_recipients,
                R.id.nav_settings
            )
        )
         binding.toolbarMain.setupWithNavController(navController, appBarConfiguration)
        navView.setupWithNavController(navController)
    }
}