package com.lintang.androidapp

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.navigation.compose.rememberNavController
import com.lintang.androidapp.ui.theme.AppTheme
import com.lintang.androidapp.navigation.setUpNavGraph

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
          AppTheme {
                val navController = rememberNavController()
                setUpNavGraph(navController = navController)
            }
        }
    }
}


