package com.example.ucppamkia

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import com.example.ucppamkia.ui.AppNavigation
import com.example.ucppamkia.ui.theme.UcppamkiaTheme
import com.example.ucppamkia.viewmodel.AppViewModel

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val viewModel: AppViewModel by viewModels()
        setContent {
            UcppamkiaTheme {
                AppNavigation(viewModel)
            }
        }
    }
}