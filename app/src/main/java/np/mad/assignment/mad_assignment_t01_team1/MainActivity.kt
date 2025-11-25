package np.mad.assignment.mad_assignment_t01_team1

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountBox
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.Phone
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.adaptive.navigationsuite.NavigationSuiteScaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.PreviewScreenSizes
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.delay
import kotlin.random.Random
import np.mad.assignment.mad_assignment_t01_team1.ui.theme.MAD_Assignment_T01_Team1Theme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            MAD_Assignment_T01_Team1Theme (isSystemInDarkTheme()){
                LoginScreenApp()
            }
        }
    }
}

@PreviewScreenSizes
@Composable
fun LoginScreenApp() {
    var currentDestination by rememberSaveable { mutableStateOf(AppDestinations.OTP) }

    NavigationSuiteScaffold(
        navigationSuiteItems = {
            AppDestinations.entries.forEach {
                item(
                    icon = {
                        Icon(
                            it.icon,
                            contentDescription = it.label
                        )
                    },
                    label = { Text(it.label) },
                    selected = it == currentDestination,
                    onClick = { currentDestination = it }
                )
            }
        }
    ){
        when (currentDestination){
            AppDestinations.Timer -> Time()
            AppDestinations.OTP -> OTP()
        }
    }
}

enum class AppDestinations(
    val label: String,
    val icon: ImageVector,
) {
    OTP("OTP", Icons.Default.Lock),
    Timer("Time", Icons.Default.Phone),
}
@Composable
fun Time(){
    var countdowntime by remember { mutableStateOf(69) }
    var isRunning by remember { mutableStateOf(false) }
    var displayMinute = countdowntime/60
    var displaySecond = countdowntime%60

    LaunchedEffect(isRunning) {
        while (isRunning && countdowntime > 0){
            delay(1000)
            countdowntime--

            if (countdowntime <= 0){
                isRunning = false
                countdowntime = 67
            }
        }
    }

    Column (
        modifier = Modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text(text = "Time", style = MaterialTheme.typography.headlineLarge)
        Spacer(modifier = Modifier.padding(16.dp))
        Text(text = String.format(" %02d : %02d", displayMinute, displaySecond))
        Spacer(modifier = Modifier.padding(16.dp))
        Button(
            onClick = {
                if (countdowntime > 0){
                    isRunning = !isRunning
                }
            }
        ) {
            Text(if (isRunning) "Pause" else "Start")
        }
        Button(
            onClick = {
                isRunning = false
                countdowntime = 67
            }
        ) {
            Text(text = "Reset")
        }
    }
}

@Composable
fun OTP(){
    var OTPNumber by remember { mutableStateOf(generateOTP())}
    var seconds by remember {mutableStateOf(15)}

    LaunchedEffect(Unit) {
        while (true) {
            delay(1000)
            seconds--

            if (seconds <= 0) {
                seconds = 15
                OTPNumber = generateOTP()
            }
        }
    }

    Column (
        modifier = Modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text(text = "OTP", style = MaterialTheme.typography.headlineLarge)
        Spacer(modifier = Modifier.padding(16.dp))
        Text(text = OTPNumber, style = MaterialTheme.typography.headlineMedium)
        Text(text = "Reset in $seconds seconds")
    }
}

fun generateOTP():String{
    return Random.nextInt(100000,999999).toString()
}