package com.example.openinapptask.views.activity

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.findNavController
import androidx.navigation.ui.setupWithNavController
import com.example.openinapptask.R
import com.example.openinapptask.databinding.ActivityDashboardBinding
import com.google.android.material.bottomnavigation.BottomNavigationView


class DashboardActivity : AppCompatActivity() {

    lateinit var dashBinding: ActivityDashboardBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        dashBinding = ActivityDashboardBinding.inflate(layoutInflater)
        val view = dashBinding.root
        setContentView(view)
        //Setting Navigation for fragments
        val navView:BottomNavigationView = dashBinding.bottomNav
        val navController = findNavController(R.id.nav_host_fragment_activity_dash)
        navView.setupWithNavController(navController)

    }



}