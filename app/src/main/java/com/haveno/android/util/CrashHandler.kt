package com.haveno.android.util

import android.content.Context
import android.content.Intent
import android.util.Log
import timber.log.Timber
import java.io.PrintWriter
import java.io.StringWriter

class CrashHandler(private val context: Context) : Thread.UncaughtExceptionHandler {
    
    private val defaultHandler = Thread.getDefaultUncaughtExceptionHandler()
    
    override fun uncaughtException(thread: Thread, exception: Throwable) {
        try {
            // Log the crash with all available information
            val stackTrace = getStackTrace(exception)
            
            Timber.e(exception, "=== UNCAUGHT EXCEPTION ===")
            Log.e("HavenoCrash", "=== UNCAUGHT EXCEPTION ===")
            Log.e("HavenoCrash", "Thread: ${thread.name}")
            Log.e("HavenoCrash", "Exception: ${exception.javaClass.simpleName}")
            Log.e("HavenoCrash", "Message: ${exception.message}")
            Log.e("HavenoCrash", "Stack trace:")
            Log.e("HavenoCrash", stackTrace)
            
            // Try to show error activity
            showErrorActivity(exception, stackTrace)
            
        } catch (e: Exception) {
            Log.e("HavenoCrash", "Error in crash handler", e)
        }
        
        // Call the default handler
        defaultHandler?.uncaughtException(thread, exception)
    }
    
    private fun getStackTrace(throwable: Throwable): String {
        val stringWriter = StringWriter()
        val printWriter = PrintWriter(stringWriter)
        throwable.printStackTrace(printWriter)
        return stringWriter.toString()
    }
    
    private fun showErrorActivity(exception: Throwable, stackTrace: String) {
        try {
            val intent = Intent(context, CrashActivity::class.java).apply {
                putExtra("error_message", exception.message ?: "Unknown error")
                putExtra("stack_trace", stackTrace)
                addFlags(Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK)
            }
            context.startActivity(intent)
        } catch (e: Exception) {
            Log.e("HavenoCrash", "Failed to start crash activity", e)
        }
    }
}