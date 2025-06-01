package si.uni_lj.fri.pbd.classproject3.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import si.uni_lj.fri.pbd.classproject3.R
import si.uni_lj.fri.pbd.classproject3.ui.theme.ClassProject3Theme
import si.uni_lj.fri.pbd.classproject3.ui.theme.Purple40
import kotlin.random.Random

@Composable
fun SplashScreen() {
    val context = LocalContext.current
    var randomPun by remember { mutableStateOf<String?>(null) }

    LaunchedEffect(Unit) {
        val punsArray = context.resources.getStringArray(R.array.cooking_puns)
        if (punsArray.isNotEmpty()) {
            randomPun = punsArray[Random.nextInt(punsArray.size)]
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Purple40)
            .padding(16.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Image(
            painter = painterResource(id = R.drawable.ic_launcher_foreground),
            contentDescription = "App Logo",
            modifier = Modifier.size(120.dp)
        )
        Spacer(modifier = Modifier.height(24.dp))
        Text(
            text = "Loading recipes!",
            fontSize = 28.sp,
            color = Color.White,
            style = MaterialTheme.typography.headlineSmall
        )
        Spacer(modifier = Modifier.height(32.dp))

        randomPun?.let { pun ->
            Text(
                text = pun,
                fontSize = 18.sp,
                color = Color.White.copy(alpha = 0.85f),
                textAlign = TextAlign.Center,
                style = MaterialTheme.typography.bodyLarge,
                modifier = Modifier.padding(horizontal = 16.dp)
            )
            Spacer(modifier = Modifier.height(32.dp))
        }

        CircularProgressIndicator(color = Color.White)
    }
}

@Preview(showBackground = true)
@Composable
fun SplashScreenPreview() {
    ClassProject3Theme {
        SplashScreen()
    }
}