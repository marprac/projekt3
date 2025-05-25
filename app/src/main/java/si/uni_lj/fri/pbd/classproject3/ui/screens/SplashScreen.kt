package si.uni_lj.fri.pbd.classproject3.ui.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import kotlinx.coroutines.delay
import si.uni_lj.fri.pbd.classproject3.R
import si.uni_lj.fri.pbd.classproject3.ui.viewmodel.SplashViewModel

@Composable
fun SplashScreen(
    onTimeout: () -> Unit,
    viewModel: SplashViewModel = hiltViewModel()
) {
    val isLoading by viewModel.isLoading.collectAsState()
    val error by viewModel.error.collectAsState()

    LaunchedEffect(isLoading, error) {
        if (!isLoading) {
            delay(1000)
            onTimeout()
        } else {
            delay(3000)
            if (isLoading) {
                onTimeout()
            }
        }
    }

    Box(
        Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        Column(horizontalAlignment = Alignment.CenterHorizontally, verticalArrangement = Arrangement.Center) {
            Image(
                painter = painterResource(id = R.drawable.ic_launcher_foreground),
                contentDescription = null
            )
            if (isLoading) {
                CircularProgressIndicator(modifier = Modifier.padding(top = 16.dp))
                Text("Loading initial data...", style = MaterialTheme.typography.bodySmall, modifier = Modifier.padding(top=8.dp))
            }
            error?.let {
                Text(
                    text = it,
                    color = MaterialTheme.colorScheme.error,
                    modifier = Modifier.padding(16.dp)
                )
            }
        }
    }
}