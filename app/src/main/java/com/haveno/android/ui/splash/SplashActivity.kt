package com.haveno.android.ui.splash

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.haveno.android.ui.main.MainActivity
import kotlinx.coroutines.*

class SplashActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        
        // Mock splash delay, then go to main activity
        CoroutineScope(Dispatchers.Main).launch {
            delay(1000) // 1 second splash
            startActivity(Intent(this@SplashActivity, MainActivity::class.java))
            finish()
        }
    }
}