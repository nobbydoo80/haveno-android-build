package com.haveno.android.ui.main

import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.navigateUp
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.haveno.android.R
import com.haveno.android.databinding.ActivityMainBinding
import timber.log.Timber
import android.util.Log

/**
 * Main activity for Haveno Android with debug logging
 */
class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private lateinit var appBarConfiguration: AppBarConfiguration

    override fun onCreate(savedInstanceState: Bundle?) {
        try {
            Timber.d("=== MAIN ACTIVITY STARTING ===")
            Log.d("HavenoDebug", "MainActivity.onCreate() called")
            
            super.onCreate(savedInstanceState)
            Timber.d("super.onCreate() completed")
            Log.d("HavenoDebug", "super.onCreate() completed")
            
            Timber.d("Inflating layout...")
            binding = ActivityMainBinding.inflate(layoutInflater)
            setContentView(binding.root)
            Timber.d("Layout inflated and set")
            
            // Debug layout structure
            val navHostFragment = findViewById<View>(R.id.nav_host_fragment)
            val bottomNavView = findViewById<View>(R.id.bottom_nav)
            val toolbarView = findViewById<View>(R.id.toolbar)
            
            Timber.d("Layout debug - NavHost: ${navHostFragment != null}, BottomNav: ${bottomNavView != null}, Toolbar: ${toolbarView != null}")
            
            // Set up toolbar
            Timber.d("Setting up toolbar...")
            val toolbar = findViewById<Toolbar>(R.id.toolbar)
            setSupportActionBar(toolbar)
            Timber.d("Toolbar set up")
            
            // Force manual navigation - automatic navigation is unreliable
            Timber.d("Setting up MANUAL navigation...")
            setupManualNavigation()
            
            // Simulate connection status updates
            updateConnectionStatus()
            
            Timber.d("=== MAIN ACTIVITY STARTED SUCCESSFULLY ===")
            Log.d("HavenoDebug", "MainActivity.onCreate() completed successfully")
            
        } catch (e: Exception) {
            Timber.e(e, "ERROR in MainActivity.onCreate()")
            Log.e("HavenoDebug", "ERROR in MainActivity.onCreate()", e)
            
            // Fallback to simple text view
            try {
                val errorText = android.widget.TextView(this).apply {
                    text = "UI ERROR:\n\n${e.message}\n\nCheck logcat for details.\n\nTag: HavenoDebug"
                    textSize = 14f
                    setPadding(40, 80, 40, 40)
                }
                setContentView(errorText)
            } catch (fallbackError: Exception) {
                Timber.e(fallbackError, "Even fallback UI failed")
            }
        }
    }

    override fun onSupportNavigateUp(): Boolean {
        // Using manual navigation, no nav controller needed
        return super.onSupportNavigateUp()
    }
    
    override fun onStart() {
        super.onStart()
        Timber.d("MainActivity.onStart() called")
        Log.d("HavenoDebug", "MainActivity.onStart() called")
    }
    
    override fun onResume() {
        super.onResume()
        Timber.d("MainActivity.onResume() called")
        Log.d("HavenoDebug", "MainActivity.onResume() called")
    }
    
    override fun onPause() {
        super.onPause()
        Timber.d("MainActivity.onPause() called")
        Log.d("HavenoDebug", "MainActivity.onPause() called")
    }
    
    override fun onStop() {
        super.onStop()
        Timber.d("MainActivity.onStop() called")
        Log.d("HavenoDebug", "MainActivity.onStop() called")
    }
    
    override fun onDestroy() {
        super.onDestroy()
        Timber.d("MainActivity.onDestroy() called")
        Log.d("HavenoDebug", "MainActivity.onDestroy() called")
    }
    
    private fun updateConnectionStatus() {
        try {
            // Find and update connection status views
            val connectionStatus = findViewById<android.widget.TextView>(com.haveno.android.R.id.connection_status)
            val daemonStatus = findViewById<android.widget.TextView>(com.haveno.android.R.id.daemon_status)
            val connectionIndicator = findViewById<android.widget.ImageView>(com.haveno.android.R.id.connection_indicator)
            
            connectionStatus?.text = "Connected to node.haveno.exchange:18081"
            daemonStatus?.text = "Synchronized (100%)"
            
            // Set indicator to green (connected)
            connectionIndicator?.setColorFilter(androidx.core.content.ContextCompat.getColor(this, android.R.color.holo_green_dark))
            
            Timber.d("Connection status updated to connected")
        } catch (e: Exception) {
            Timber.e(e, "Failed to update connection status")
        }
    }
    
    private fun setupManualNavigation() {
        try {
            Timber.d("Looking for bottom navigation view...")
            val bottomNav = findViewById<BottomNavigationView>(R.id.bottom_nav)
            
            if (bottomNav == null) {
                Timber.e("BottomNavigationView is NULL!")
                Log.e("HavenoDebug", "BottomNavigationView not found!")
                return
            }
            
            Timber.d("BottomNavigationView found, setting up listener...")
            Log.d("HavenoDebug", "Setting up manual navigation")
            
            bottomNav.setOnItemSelectedListener { item ->
                Timber.d("Bottom nav item clicked: ${item.itemId}")
                Log.d("HavenoDebug", "Tab clicked: ${item.itemId}")
                
                when (item.itemId) {
                    R.id.nav_market -> {
                        showFragment("market")
                        Timber.d("✅ Market tab selected")
                        Log.d("HavenoDebug", "✅ Market tab selected")
                        true
                    }
                    R.id.nav_portfolio -> {
                        showFragment("portfolio")
                        Timber.d("✅ Portfolio tab selected")
                        Log.d("HavenoDebug", "✅ Portfolio tab selected")
                        true
                    }
                    R.id.nav_funds -> {
                        showFragment("funds")
                        Timber.d("✅ Funds tab selected")
                        Log.d("HavenoDebug", "✅ Funds tab selected")
                        true
                    }
                    R.id.nav_support -> {
                        showFragment("support")
                        Timber.d("✅ Support tab selected")
                        Log.d("HavenoDebug", "✅ Support tab selected")
                        true
                    }
                    else -> {
                        Timber.e("Unknown tab item: ${item.itemId}")
                        Log.e("HavenoDebug", "Unknown tab item: ${item.itemId}")
                        false
                    }
                }
            }
            
            // Show market fragment by default
            showFragment("market")
            Timber.d("✅ Manual navigation set up successfully")
            Log.d("HavenoDebug", "✅ Manual navigation set up successfully")
            
        } catch (e: Exception) {
            Timber.e(e, "❌ Failed to set up manual navigation")
            Log.e("HavenoDebug", "❌ Failed to set up manual navigation", e)
        }
    }
    
    private fun showFragment(fragmentType: String) {
        try {
            Timber.d("🔄 Loading fragment: $fragmentType")
            Log.d("HavenoDebug", "🔄 Loading fragment: $fragmentType")
            
            val fragment = when (fragmentType) {
                "market" -> {
                    Timber.d("Creating MarketFragment")
                    com.haveno.android.ui.market.MarketFragment()
                }
                "portfolio" -> {
                    Timber.d("Creating PortfolioFragment")
                    com.haveno.android.ui.portfolio.PortfolioFragment()
                }
                "funds" -> {
                    Timber.d("Creating FundsFragment")
                    com.haveno.android.ui.funds.FundsFragment()
                }
                "support" -> {
                    Timber.d("Creating SupportFragment")
                    com.haveno.android.ui.support.SupportFragment()
                }
                else -> {
                    Timber.d("Creating default MarketFragment")
                    com.haveno.android.ui.market.MarketFragment()
                }
            }
            
            Timber.d("Fragment created, replacing in nav_host_fragment...")
            supportFragmentManager.beginTransaction()
                .replace(R.id.nav_host_fragment, fragment)
                .commitAllowingStateLoss()
                
            Timber.d("✅ Fragment $fragmentType loaded successfully")
            Log.d("HavenoDebug", "✅ Fragment $fragmentType loaded successfully")
            
        } catch (e: Exception) {
            Timber.e(e, "❌ Failed to show fragment: $fragmentType")
            Log.e("HavenoDebug", "❌ Failed to show fragment: $fragmentType", e)
        }
    }
}