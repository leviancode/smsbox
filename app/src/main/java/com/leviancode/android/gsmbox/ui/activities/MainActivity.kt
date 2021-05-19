package com.leviancode.android.gsmbox.ui.activities

import android.animation.Animator
import android.animation.AnimatorListenerAdapter
import android.content.Context
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import androidx.lifecycle.lifecycleScope
import androidx.navigation.findNavController
import androidx.navigation.ui.setupWithNavController
import com.leviancode.android.gsmbox.R
import com.leviancode.android.gsmbox.databinding.ActivityMainBinding
import com.leviancode.android.gsmbox.core.utils.PREF_KEY_DEFAULT_LANGUAGE
import com.leviancode.android.gsmbox.core.utils.managers.LanguageManager
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import java.util.*

val Context.dataStore by preferencesDataStore("settings")

class MainActivity : AppCompatActivity(){
    private lateinit var binding: ActivityMainBinding
    private var shortAnimationDuration: Int = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        showSplash()
        setAppLanguage()
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setupBottomNavigationView()
    }

    private fun setAppLanguage() {
        lifecycleScope.launchWhenStarted {
            dataStore.data.collect { pref ->
                val lang = pref[stringPreferencesKey(PREF_KEY_DEFAULT_LANGUAGE)]
                LanguageManager.updateAppLanguage(this@MainActivity, lang)
            }
        }
    }

    private fun setupBottomNavigationView() {
        val navController = findNavController(R.id.nav_host_fragment)
        binding.navView.setupWithNavController(navController)
    }


    private fun showSplash(){
        lifecycleScope.launch {
            delay(2000)
            hideSplashWithAnim()
        }
    }

    private fun hideSplashWithAnim() {
        shortAnimationDuration = resources.getInteger(android.R.integer.config_longAnimTime)
        binding.mainContainer.apply {
            alpha = 0f
            visibility = View.VISIBLE
            animate()
                .alpha(1f)
                .setDuration(shortAnimationDuration.toLong())
                .setListener(null)
        }
        binding.splash.root.animate()
            .alpha(0f)
            .setDuration(shortAnimationDuration.toLong())
            .setListener(object : AnimatorListenerAdapter() {
                override fun onAnimationEnd(animation: Animator) {
                    binding.splash.root.visibility = View.GONE
                }
            })
    }
}