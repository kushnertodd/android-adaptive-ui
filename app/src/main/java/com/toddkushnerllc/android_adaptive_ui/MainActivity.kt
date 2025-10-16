package com.toddkushnerllc.android_adaptive_ui

import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.ui.Modifier
import com.toddkushnerllc.android_adaptive_ui.ui.theme.AndroidadaptiveuiTheme

private const val MAIN_TAG = "MainActivity"

// this is a change
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            AndroidadaptiveuiTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    //DrawingApp()
                    LogPointerEvents()
                }
            }
        }
    }

    override fun onStart() {
        super.onStart()
        Log.d(MAIN_TAG, "onStart")
    }

    override fun onResume() {
        super.onResume()
        Log.d(MAIN_TAG, "onResume")
    }

    override fun onPause() {
        super.onPause()
        Log.d(MAIN_TAG, "onPause")
    }

    override fun onStop() {
        super.onStop()
        Log.d(MAIN_TAG, "onStop")
    }

    override fun onDestroy() {
        super.onDestroy()
        Log.d(MAIN_TAG, "onDestroy")
    }

}
