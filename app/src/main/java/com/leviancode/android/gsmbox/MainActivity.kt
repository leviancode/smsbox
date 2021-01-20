package com.leviancode.android.gsmbox

import android.graphics.Color
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import androidx.appcompat.app.AppCompatDelegate.setDefaultNightMode
import androidx.databinding.DataBindingUtil
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.leviancode.android.gsmbox.databinding.ActivityMainBinding
import com.leviancode.android.gsmbox.model.TemplateGroup
import com.leviancode.android.gsmbox.repository.TemplatesRepository
import com.leviancode.android.gsmbox.ui.dialogs.NewItemDialogFragment

class MainActivity : AppCompatActivity(), NewItemDialogFragment.ItemClickListener {
    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_main)

        val navView: BottomNavigationView = findViewById(R.id.nav_view)
        val navController = findNavController(R.id.nav_host_fragment)

        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        val appBarConfiguration = AppBarConfiguration(
            setOf(
                R.id.navigation_favorites,
                R.id.navigation_templates,
                R.id.navigation_devices,
                R.id.navigation_settings
            )
        )
        setupActionBarWithNavController(navController, appBarConfiguration)
        navView.setupWithNavController(navController)
    }

    fun onAddTemplate(view: View) {
        NewItemDialogFragment().show(supportFragmentManager, NewItemDialogFragment.TAG)
    }

    override fun onItemClick(itemTag: String) {
       when(itemTag){
           NewItemDialogFragment.TEMPLATE_TAG -> {
               TemplatesRepository.addGroup(TemplateGroup(name = "Гараж", description =  "отопление"))
               Toast.makeText(this, "Add template", Toast.LENGTH_SHORT).show()
           }
           NewItemDialogFragment.DEVICE_TAG -> {
               Toast.makeText(this, "Add device", Toast.LENGTH_SHORT).show()
           }
       }
    }
}