package np.mad.assignment.mad_assignment_t01_team1

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
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
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import np.mad.assignment.mad_assignment_t01_team1.data.db.AppDatabase
import np.mad.assignment.mad_assignment_t01_team1.ui.theme.MAD_Assignment_T01_Team1Theme

class LoginActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        setContent {
            MAD_Assignment_T01_Team1Theme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    LoginScreen(
                        onLoginSuccess = { userId ->
                            // Save userId to SharedPreferences (simple session)
                            val prefs = getSharedPreferences("auth", MODE_PRIVATE)
                            prefs.edit().putLong("logged_in_user", userId).apply()

                            // Launch MainActivity and finish LoginActivity
                            val intent = Intent(this@LoginActivity, MainActivity::class.java)
                            intent.putExtra("userId", userId)
                            startActivity(intent)
                            finish()
                        },
                        onRegisterClick = {
                            // Start RegisterActivity
                            startActivity(Intent(this@LoginActivity, RegisterActivity::class.java))
                        },
                        modifier = Modifier.padding(innerPadding)
                    )
                }
            }
        }
    }
}

@Composable
fun LoginScreen(
    onLoginSuccess: (Long) -> Unit,
    onRegisterClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current
    val coroutineScope = rememberCoroutineScope()

    var username by rememberSaveable { mutableStateOf("") }
    var password by rememberSaveable { mutableStateOf("") }

    Box(
        modifier = modifier
            .fillMaxSize()
            .padding(24.dp),
        contentAlignment = Alignment.Center
    ) {
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            Text(text = "Login Page", style = MaterialTheme.typography.headlineLarge)
            Spacer(modifier = Modifier.padding(12.dp))

            OutlinedTextField(
                value = username,
                onValueChange = { username = it },
                label = { Text(text = "Username") },
                leadingIcon = { Icon(Icons.Default.Person, contentDescription = "Username") }
            )

            Spacer(modifier = Modifier.padding(12.dp))

            OutlinedTextField(
                value = password,
                onValueChange = { password = it },
                label = { Text(text = "Password") },
                leadingIcon = { Icon(Icons.Default.Lock, contentDescription = "Password") }
            )

            Spacer(modifier = Modifier.padding(12.dp))

            // Login button: validate using Room DAO
            Button(onClick = {
                coroutineScope.launch {
                    try {
                        val db = AppDatabase.get(context)
                        // Your UserDao.getByName(name) exists in the code you provided.
                        val user = withContext(Dispatchers.IO) {
                            db.userDao().getByName(username)
                        }

                        if (user != null && user.password == password) {
                            // success: return real userId
                            onLoginSuccess(user.userId)
                        } else {
                            // show simple toast on failure
                            Toast.makeText(context, "Invalid username or password", Toast.LENGTH_SHORT).show()
                        }
                    } catch (e: Exception) {
                        Toast.makeText(context, "Login failed: ${e.message}", Toast.LENGTH_SHORT).show()
                    }
                }
            }) {
                Text(text = "Login")
            }

            Spacer(modifier = Modifier.padding(8.dp))

            // Register button
            Button(onClick = { onRegisterClick() }) {
                Text(text = "Register")
            }

            Spacer(modifier = Modifier.padding(8.dp))

            // Continue as Guest: use -1L to represent guest
            Button(onClick = {
                onLoginSuccess(-1L)
            }) {
                Text(text = "Continue as Guest")
            }
        }
    }
}
