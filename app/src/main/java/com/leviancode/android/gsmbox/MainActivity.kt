package com.leviancode.android.gsmbox

import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import androidx.appcompat.app.AppCompatDelegate.setDefaultNightMode
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.leviancode.android.gsmbox.R
import com.leviancode.android.gsmbox.ui.view.dialog.NewTemplateDialog
import com.leviancode.android.gsmbox.ui.view.dialog.BottomSheetDialog

class MainActivity : AppCompatActivity(), BottomSheetDialog.ItemClickListener {
    private var isLargeLayout = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
        setContentView(R.layout.activity_main)

        isLargeLayout = resources.getBoolean(R.bool.large_layout)

        val navView: BottomNavigationView = findViewById(R.id.nav_view)
        val navController = findNavController(R.id.nav_host_fragment)

        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        val appBarConfiguration = AppBarConfiguration(
            setOf(
                R.id.navigation_templates,
                R.id.navigation_favorites,
                R.id.navigation_recipients,
                R.id.navigation_settings
            )
        )
        setupActionBarWithNavController(navController, appBarConfiguration)
        navView.setupWithNavController(navController)
    }

    fun onCreateItem(view: View) {
        BottomSheetDialog().show(supportFragmentManager, BottomSheetDialog.TAG)
    }

    override fun onItemClick(itemTag: String) {
       when(itemTag){
           BottomSheetDialog.TEMPLATE_TAG -> {
               showNewTemplateDialog()
           }
           BottomSheetDialog.RECIPIENT_TAG -> {
               Toast.makeText(this, "Add device", Toast.LENGTH_SHORT).show()
           }
       }
    }

    private fun showNewTemplateDialog(){
        NewTemplateDialog.display(supportFragmentManager)
    }
}