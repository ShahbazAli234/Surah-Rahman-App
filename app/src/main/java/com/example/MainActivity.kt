package com.example

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.BackHandler
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.ui.navigation.SurahRahmanBottomNav
import com.example.ui.screens.HomeScreen
import com.example.ui.screens.IslamicToolsScreen
import com.example.ui.screens.ListenScreen
import com.example.ui.screens.ReadScreen
import com.example.ui.screens.SettingsScreen
import com.example.ui.screens.SplashScreen
import com.example.ui.theme.SurahRahmanTheme
import com.example.ui.viewmodel.ScreenTab
import com.example.ui.viewmodel.SurahRahmanViewModel

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            val viewModel: SurahRahmanViewModel = viewModel()
            val appTheme by viewModel.appTheme.collectAsState()

            val isDarkTheme = when (appTheme) {
                "DARK" -> true
                "LIGHT" -> false
                else -> isSystemInDarkTheme()
            }

            SurahRahmanTheme(darkTheme = isDarkTheme) {
                SurahRahmanApp(viewModel = viewModel)
            }
        }
    }
}

@Composable
fun SurahRahmanApp(
    viewModel: SurahRahmanViewModel
) {
    var showSplash by remember { mutableStateOf(true) }
    val currentTab by viewModel.currentTab.collectAsState()
    val isDistractionFree by viewModel.isDistractionFreeMode.collectAsState()

    // Handle back button: if not on Home, return to Home; if in distraction-free, exit it
    BackHandler(enabled = !showSplash && (isDistractionFree || currentTab != ScreenTab.HOME)) {
        if (isDistractionFree) {
            viewModel.toggleDistractionFreeMode()
        } else if (currentTab != ScreenTab.HOME) {
            viewModel.navigateToTab(ScreenTab.HOME)
        }
    }

    if (showSplash) {
        SplashScreen(
            onSplashFinished = { showSplash = false }
        )
    } else {
        Scaffold(
            modifier = Modifier.fillMaxSize(),
            bottomBar = {
                if (!isDistractionFree) {
                    SurahRahmanBottomNav(
                        currentTab = currentTab,
                        onTabSelected = { tab ->
                            viewModel.navigateToTab(tab)
                        }
                    )
                }
            }
        ) { innerPadding ->
            AnimatedContent(
                targetState = currentTab,
                transitionSpec = { fadeIn() togetherWith fadeOut() },
                label = "screen_transition"
            ) { targetTab ->
                when (targetTab) {
                    ScreenTab.HOME -> HomeScreen(
                        viewModel = viewModel,
                        contentPadding = innerPadding
                    )
                    ScreenTab.READ -> ReadScreen(
                        viewModel = viewModel,
                        contentPadding = innerPadding
                    )
                    ScreenTab.LISTEN -> ListenScreen(
                        viewModel = viewModel,
                        contentPadding = innerPadding
                    )
                    ScreenTab.TOOLS -> IslamicToolsScreen(
                        viewModel = viewModel,
                        contentPadding = innerPadding
                    )
                    ScreenTab.SETTINGS -> SettingsScreen(
                        viewModel = viewModel,
                        contentPadding = innerPadding
                    )
                }
            }
        }
    }
}
