package si.uni_lj.fri.pbd.classproject3

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import kotlinx.coroutines.delay
import si.uni_lj.fri.pbd.classproject3.screens.MainScreen
import si.uni_lj.fri.pbd.classproject3.screens.SplashScreen
import si.uni_lj.fri.pbd.classproject3.ui.theme.ClassProject3Theme
import si.uni_lj.fri.pbd.classproject3.viewmodels.SearchViewModel

class MainActivity : ComponentActivity() {

    private val searchViewModel: SearchViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        setContent {
            ClassProject3Theme {
                var showSplashScreen by remember { mutableStateOf(true) }

                LaunchedEffect(Unit) {
                    delay(3000)
                    showSplashScreen = false
                }

                if (showSplashScreen) {
                    SplashScreen()
                } else {
                    MainScreen(searchViewModel = searchViewModel)
                }
            }
        }
    }
}