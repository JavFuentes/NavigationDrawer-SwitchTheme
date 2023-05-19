package com.bootcamp.navigationdrawerswitchtheme

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.Modifier
import com.bootcamp.navigationdrawerswitchtheme.ui.theme.NavigationDrawerSwitchThemeTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.saveable.rememberSaveable

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            // Utiliza rememberSaveable para manejar el estado del tema
            val isDarkTheme = rememberSaveable { mutableStateOf(false) }

            // Usa el estado en tu tema
            NavigationDrawerSwitchThemeTheme(darkTheme = isDarkTheme.value) {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                ) {
                    // Pasa el estado a MyNavDrawerApp
                    MyNavDrawerApp(isDarkTheme)
                }
            }
        }
    }
}
