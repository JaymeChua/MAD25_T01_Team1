package np.mad.assignment.mad_assignment_t01_team1

import android.os.Bundle
import android.util.Log
import android.util.Patterns
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import com.google.firebase.auth.FirebaseAuth
import np.mad.assignment.mad_assignment_t01_team1.ui.theme.MAD_Assignment_T01_Team1Theme

class ForgotPasswordActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        setContent {
            MAD_Assignment_T01_Team1Theme {
                Surface(modifier = Modifier.fillMaxSize()) {
                    ForgotPasswordScreen(
                        onBack = { finish() }
                    )
                }
            }
        }
    }
}

@Composable
private fun ForgotPasswordScreen(
    onBack: () -> Unit
) {
    val context = LocalContext.current
    var email by rememberSaveable { mutableStateOf("") }
    var isSending by rememberSaveable { mutableStateOf(false) }
    var emailError by rememberSaveable { mutableStateOf<String?>(null) }

    fun validateEmail(input: String): Boolean {
        val trimmed = input.trim()
        if (trimmed.isEmpty()) {
            emailError = "Email cannot be empty"
            return false
        }
        if (!Patterns.EMAIL_ADDRESS.matcher(trimmed).matches()) {
            emailError = "Please enter a valid email address"
            return false
        }
        emailError = null
        return true
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(24.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text("Forgot Password", style = MaterialTheme.typography.headlineMedium)

        Spacer(modifier = Modifier.height(12.dp))

        Text(
            text = "Enter your account email. We’ll send a password reset link.",
            style = MaterialTheme.typography.bodyMedium
        )

        Spacer(modifier = Modifier.height(16.dp))

        OutlinedTextField(
            value = email,
            onValueChange = {
                email = it
                if (emailError != null) validateEmail(it)
            },
            label = { Text("Email") },
            singleLine = true,
            isError = emailError != null,
            supportingText = { if (emailError != null) Text(emailError!!) },
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email),
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(16.dp))

        Button(
            onClick = {
                val trimmedEmail = email.trim()
                if (!validateEmail(trimmedEmail)) return@Button
                if (isSending) return@Button

                isSending = true

                FirebaseAuth.getInstance()
                    .sendPasswordResetEmail(trimmedEmail)
                    .addOnCompleteListener { task ->
                        isSending = false
                        if (task.isSuccessful) {
                            Toast.makeText(
                                context,
                                "Reset link sent. Check inbox/spam.",
                                Toast.LENGTH_LONG
                            ).show()
                            onBack()
                        } else {
                            val e = task.exception
                            Log.e("ForgotPassword", "sendPasswordResetEmail failed", e)
                            Toast.makeText(
                                context,
                                e?.message ?: "Failed to send reset link.",
                                Toast.LENGTH_LONG
                            ).show()
                        }
                    }
            },
            enabled = !isSending,
            modifier = Modifier.fillMaxWidth()
        ) {
            Text(if (isSending) "Sending..." else "Send Reset Link")
        }

        Spacer(modifier = Modifier.height(8.dp))

        OutlinedButton(
            onClick = onBack,
            enabled = !isSending,
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Back to Login")
        }
    }
}