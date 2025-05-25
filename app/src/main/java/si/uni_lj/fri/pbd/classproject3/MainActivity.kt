package si.uni_lj.fri.pbd.classproject3

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.material3.MaterialTheme
import dagger.hilt.android.AndroidEntryPoint
import si.uni_lj.fri.pbd.classproject3.ui.navigation.AppNavGraph
import si.uni_lj.fri.pbd.classproject3.ui.theme.ClassProject3Theme

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            ClassProject3Theme {
                AppNavGraph()
            }
        }
    }
}