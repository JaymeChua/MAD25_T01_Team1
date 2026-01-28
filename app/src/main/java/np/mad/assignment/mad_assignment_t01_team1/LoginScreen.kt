package np.mad.assignment.mad_assignment_t01_team1

import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import np.mad.assignment.mad_assignment_t01_team1.ui.theme.MAD_Assignment_T01_Team1Theme

@Composable
fun LoginScreenBye(
    onLoginSuccess: () -> Unit,
    modifier: Modifier = Modifier
){
    var username by rememberSaveable { mutableStateOf("")}
    var password by rememberSaveable {mutableStateOf("")}

    Box(
        modifier = modifier.fillMaxSize().padding(24.dp),
        contentAlignment = Alignment.Center
    ){
        Column(
            horizontalAlignment = Alignment.CenterHorizontally
        ){
            Text(text = "Login Page", style = MaterialTheme.typography.headlineLarge)
            Spacer(modifier = Modifier.padding(12.dp))
            OutlinedTextField(
                value = username,
                onValueChange = {username = it},
                label = { Text(text = "Username")},
                leadingIcon = { Icon(Icons.Default.Person, contentDescription = "Username") }
            )
            Spacer(modifier = Modifier.padding(12.dp))
            OutlinedTextField(
                value = password,
                onValueChange = {password = it},
                label = { Text(text = "Password")},
                leadingIcon = { Icon(Icons.Default.Lock, contentDescription = "Password") }
            )
            Spacer(modifier = Modifier.padding(12.dp))
            Button(
                onClick = {
                    if (validateLogin(username, password)){
                        onLoginSuccess()
                    }
                }
            ){
                Text(text = "Login")
            }
        }

    }

}

fun validateLogin(username: String, password: String): Boolean{
    val myUsername = "admin"
    val myPassword = "password"
    return username == myUsername && password == myPassword
}