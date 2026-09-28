package com.example.gymcanamaster

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import com.example.gymcanamaster.navigation.AppNavigation
import com.example.gymcanamaster.ui.theme.GymcanaMasterTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            GymcanaMasterTheme {
                AppNavigation()
            }
        }
    }
}