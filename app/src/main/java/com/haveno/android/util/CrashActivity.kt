package com.haveno.android.util

import android.os.Bundle
import android.widget.ScrollView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity

class CrashActivity : AppCompatActivity() {
    
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        
        val errorMessage = intent.getStringExtra("error_message") ?: "Unknown error"
        val stackTrace = intent.getStringExtra("stack_trace") ?: "No stack trace available"
        
        val textView = TextView(this).apply {
            text = "HAVENO ANDROID CRASH REPORT\n\n" +
                   "Error: $errorMessage\n\n" +
                   "Full Stack Trace:\n" +
                   "==================\n" +
                   stackTrace
            textSize = 11f
            setPadding(20, 40, 20, 40)
            setTextIsSelectable(true)
        }
        
        val scrollView = ScrollView(this).apply {
            addView(textView)
        }
        
        setContentView(scrollView)
    }
}