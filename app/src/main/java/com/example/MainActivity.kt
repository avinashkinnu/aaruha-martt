package com.example

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.compose.runtime.getValue
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.ui.AaruhaApp
import com.example.ui.AaruhaViewModel
import com.example.ui.theme.MyApplicationTheme

class MainActivity : ComponentActivity() {
  private val viewModel: AaruhaViewModel by viewModels()

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    enableEdgeToEdge()
    setContent {
      val userProfile by viewModel.userProfile.collectAsStateWithLifecycle()
      MyApplicationTheme(darkTheme = userProfile.isDarkMode) {
        AaruhaApp(viewModel = viewModel)
      }
    }
  }
}

