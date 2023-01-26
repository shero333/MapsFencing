package com.example.taskday1application.main

import android.graphics.drawable.ColorDrawable
import android.os.Build
import android.os.Bundle
import android.view.WindowManager
import androidx.appcompat.app.ActionBar
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.Navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.NavigationUI.setupActionBarWithNavController
import androidx.navigation.ui.NavigationUI.setupWithNavController
import com.example.taskday1application.R
import com.example.taskday1application.databinding.ActivityMainBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {
    private var binding: ActivityMainBinding? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding!!.root)

        val window = this.window
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS)
        window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS)
        window.statusBarColor = this.resources.getColor(R.color.icon_splash_background)

        val actionBar: ActionBar?
        actionBar = supportActionBar
        var colorDrawable: ColorDrawable? = null
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            colorDrawable = ColorDrawable(getColor(R.color.icon_splash_background))
        }
        assert(actionBar != null)
        actionBar!!.setBackgroundDrawable(colorDrawable)

        val appBarConfiguration = AppBarConfiguration(
            setOf(
                R.id.navigation_todoList, R.id.navigation_addTodo, R.id.navigation_otherOperations
            )
        )

        val navController = findNavController(this, R.id.nav_host_fragment_activity_main)
        setupActionBarWithNavController(this, navController, appBarConfiguration)
        setupWithNavController(binding!!.navView, navController)

    }
}