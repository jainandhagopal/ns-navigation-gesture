package com.nscreation.navigationgesture

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Surface
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import androidx.lifecycle.compose.LocalLifecycleOwner
import com.nscreation.navigationgesture.modules.gesture.presentation.MainScreen
import com.nscreation.navigationgesture.modules.gesture.presentation.MainViewModel
import com.nscreation.navigationgesture.modules.welcome.presentation.WelcomeScreen
import com.nscreation.navigationgesture.ui.theme.NSNavigationGestureTheme

class MainActivity : ComponentActivity() {

    private val viewModel: MainViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            NSNavigationGestureTheme(themeMode = viewModel.themeMode) {
                val context = LocalContext.current
                val lifecycleOwner = LocalLifecycleOwner.current
                var showSplash by remember { mutableStateOf(true) }

                DisposableEffect(lifecycleOwner) {
                    val observer = LifecycleEventObserver { _, event ->
                        if (event == Lifecycle.Event.ON_RESUME) {
                            viewModel.checkServiceStatus(context)
                        }
                    }
                    lifecycleOwner.lifecycle.addObserver(observer)
                    onDispose {
                        lifecycleOwner.lifecycle.removeObserver(observer)
                    }
                }

                Surface(
                    modifier = Modifier.fillMaxSize()
                ) {
                    if (showSplash) {
                        WelcomeScreen(
                            onTimeout = {
                                showSplash = false
                            }
                        )
                    } else {
                        MainScreen(viewModel = viewModel)
                    }
                }
            }
        }
    }
}
