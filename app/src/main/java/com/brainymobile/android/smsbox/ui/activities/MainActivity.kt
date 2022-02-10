package com.brainymobile.android.smsbox.ui.activities

import android.animation.Animator
import android.animation.AnimatorListenerAdapter
import android.content.Context
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import androidx.lifecycle.lifecycleScope
import androidx.navigation.NavController
import androidx.navigation.findNavController
import androidx.navigation.ui.setupWithNavController
import com.brainymobile.android.smsbox.R
import com.brainymobile.android.smsbox.databinding.ActivityMainBinding
import com.brainymobile.android.smsbox.utils.PREF_KEY_DEFAULT_LANGUAGE
import com.brainymobile.android.smsbox.utils.extensions.observe
import com.brainymobile.android.smsbox.utils.managers.LanguageManager
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import javax.inject.Inject

val Context.dataStore by preferencesDataStore("settings")

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    @Inject
    lateinit var languageManager: LanguageManager
    private var shortAnimationDuration: Int = 0
    private lateinit var navController: NavController

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        showSplash()
        setAppLanguage()
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setupBottomNavigationView()
    }

    private fun setAppLanguage() {
        dataStore.data.observe(this) { pref ->
            val lang = pref[stringPreferencesKey(PREF_KEY_DEFAULT_LANGUAGE)]
            languageManager.updateAppLanguage(lang)
        }
    }

    fun setBottomNavVisibility(visibility: Int) {
        binding.navView.visibility = visibility
    }

    private fun setupBottomNavigationView() {
        navController = findNavController(R.id.nav_host_fragment)
        binding.navView.setupWithNavController(navController)
    }

    override fun onBackPressed() {
        navController.currentDestination?.let { currentDestination ->
            if (currentDestination.id == R.id.nav_template_group_list) {
                finish()
            } else {
                super.onBackPressed()
            }
        }
    }


    private fun showSplash() {
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