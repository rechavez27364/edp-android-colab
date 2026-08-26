package com.example.myapplication

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.lightColorScheme
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        
        // A warm, food-inspired color theme
        val recipeColorScheme = lightColorScheme(
            primary = Color(0xFFD84315), // Deep Orange 800
            onPrimary = Color.White,
            primaryContainer = Color(0xFFFFCCBC), // Deep Orange 100
            onPrimaryContainer = Color(0xFF3E0E00),
            secondary = Color(0xFF6D4C41), // Brown 600
            onSecondary = Color.White,
            background = Color(0xFFFFFBF7), // Warm white
            surface = Color(0xFFFFFBF7),
            onSurface = Color(0xFF1B1B1B)
        )

        setContent {
            MaterialTheme(colorScheme = recipeColorScheme) {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    RecipeApp() // <-- our whole app starts here
                }
            }
        }
    }
}
