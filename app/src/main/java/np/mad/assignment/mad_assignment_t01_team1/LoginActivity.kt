package np.mad.assignment.mad_assignment_t01_team1

import android.content.Intent
import android.util.Patterns
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import np.mad.assignment.mad_assignment_t01_team1.data.db.AppDatabase
import np.mad.assignment.mad_assignment_t01_team1.data.entity.UserEntity
import np.mad.assignment.mad_assignment_t01_team1.ui.theme.MAD_Assignment_T01_Team1Theme
import np.mad.assignment.mad_assignment_t01_team1.util.SecurityUtils

class LoginActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: android.os.Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        setContent {
            MAD_Assignment_T01_Team1Theme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    LoginScreen(
                        onLoginSuccess = { userId, role ->
                            val prefs = getSharedPreferences("auth", MODE_PRIVATE)
                            prefs.edit().putLong("logged_in_user", userId).putString("user_role", role).apply()

                            val intent = Intent(this@LoginActivity, MainActivity::class.java)
                            intent.putExtra("userId", userId)
                            startActivity(intent)
                            finish()
                        },
                        onRegisterClick = {
                            startActivity(Intent(this@LoginActivity, RegisterActivity::class.java))
                        },
                        contentPadding = innerPadding,
                        modifier = Modifier.fillMaxSize()
                    )
                }
            }
        }
    }
}

@Composable
fun LoginScreen(
    onLoginSuccess: (Long, String) -> Unit,
    onRegisterClick: () -> Unit,
    contentPadding: PaddingValues = PaddingValues(0.dp),
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current
    val coroutineScope = rememberCoroutineScope()

    var usernameOrEmail by rememberSaveable { mutableStateOf("") }
    var password by rememberSaveable { mutableStateOf("") }

    val auth = FirebaseAuth.getInstance()
    val firestore = FirebaseFirestore.getInstance()

    fun toast(msg: String) = Toast.makeText(context, msg, Toast.LENGTH_LONG).show()

    suspend fun ensureLocalUser(username: String, role: String, passwordForHash: String): UserEntity? {
        val db = AppDatabase.get(context)

        val existing = withContext(Dispatchers.IO) {
            db.userDao().getByName(username)
        }

        if (existing != null) return existing

        // Create a local record for session/role purposes (password hash here is NOT used for auth anymore)
        val hashed = SecurityUtils.sha256(passwordForHash)
        val newUser = UserEntity(
            name = username,
            password = hashed,
            role = role
        )

        withContext(Dispatchers.IO) {
            db.userDao().upsert(newUser)
        }

        return withContext(Dispatchers.IO) {
            db.userDao().getByName(username)
        }
    }

    fun loginWithFirebase(email: String, passwordInput: String, resolvedUsername: String?, resolvedRole: String?) {
        auth.signInWithEmailAndPassword(email, passwordInput)
            .addOnCompleteListener { task ->
                if (!task.isSuccessful) {
                    val msg = task.exception?.message ?: "Firebase login failed"
                    toast(msg)
                    return@addOnCompleteListener
                }

                // After Firebase login success, we still want a local userId/role for your existing session logic
                val finalUsername = resolvedUsername ?: email.substringBefore("@")
                val finalRole = resolvedRole ?: "user"

                coroutineScope.launch {
                    val localUser = ensureLocalUser(finalUsername, finalRole, passwordInput)
                    if (localUser != null) {
                        onLoginSuccess(localUser.userId, localUser.role)
                    } else {
                        toast("Login succeeded but local user setup failed")
                    }
                }
            }
    }

    Box(
        modifier = modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        Image(
            painter = painterResource(id = R.drawable.loginbg),
            contentDescription = null,
            modifier = modifier.fillMaxSize(),
            contentScale = ContentScale.Crop
        )

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(contentPadding)
                .padding(top = 200.dp),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = "Welcome to",
                fontSize = 45.sp,
                fontWeight = FontWeight.SemiBold,
                color = Color.Black,
                textAlign = TextAlign.Center,
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(modifier = Modifier.height(12.dp))

            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.Center,
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(
                    text = "np",
                    fontSize = 64.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color(0xFFFFB041)
                )

                Spacer(modifier = Modifier.width(12.dp))

                Image(
                    painter = painterResource(id = R.drawable.npfoodielogo),
                    contentDescription = "NP Foodies Logo",
                    modifier = Modifier.size(96.dp)
                )
            }

            Text(
                text = "foodies",
                fontSize = 64.sp,
                fontWeight = FontWeight.Bold,
                color = Color.Black,
                textAlign = TextAlign.Center,
                modifier = Modifier.fillMaxWidth()
            )

            TextField(
                value = usernameOrEmail,
                onValueChange = { usernameOrEmail = it },
                label = { Text(text = "Username or Email") },
                leadingIcon = { Icon(Icons.Default.Person, contentDescription = "Username or Email") },
                colors = TextFieldDefaults.colors(
                    focusedContainerColor = Color.White,
                    unfocusedContainerColor = Color.White,
                    focusedIndicatorColor = Color.Transparent,
                    unfocusedIndicatorColor = Color.Transparent,
                    cursorColor = Color.Black
                ),
                shape = RoundedCornerShape(8.dp),
                modifier = Modifier
                    .fillMaxWidth(0.85f)
                    .padding(top = 50.dp)
            )

            Spacer(modifier = Modifier.padding(12.dp))

            TextField(
                value = password,
                onValueChange = { password = it },
                label = { Text(text = "Password") },
                leadingIcon = { Icon(Icons.Default.Lock, contentDescription = "Password") },
                colors = TextFieldDefaults.colors(
                    focusedContainerColor = Color.White,
                    unfocusedContainerColor = Color.White,
                    focusedIndicatorColor = Color.Transparent,
                    unfocusedIndicatorColor = Color.Transparent,
                    cursorColor = Color.Black
                ),
                shape = RoundedCornerShape(8.dp),
                modifier = Modifier.fillMaxWidth(0.85f)
            )

            Spacer(modifier = Modifier.padding(12.dp))

            Button(
                onClick = {
                    val identifier = usernameOrEmail.trim()
                    val pw = password.trim()

                    if (identifier.isEmpty() || pw.isEmpty()) {
                        toast("Please enter your credentials")
                        return@Button
                    }

                    // ============================
                    // OLD CODE (ROOM PASSWORD LOGIN) - COMMENTED OUT
                    // ============================
                    /*
                    coroutineScope.launch {
                        try {
                            val db = AppDatabase.get(context)
                            val user = withContext(Dispatchers.IO) {
                                db.userDao().getByName(identifier)
                            }

                            val hashedInput = SecurityUtils.sha256(pw)
                            if (user != null && user.password == hashedInput) {
                                onLoginSuccess(user.userId, user.role)
                            } else {
                                Toast.makeText(context, "Invalid username or password", Toast.LENGTH_SHORT).show()
                            }
                        } catch (e: Exception) {
                            Toast.makeText(context, "Login failed: ${e.message}", Toast.LENGTH_SHORT).show()
                        }
                    }
                    */

                    // ============================
                    // NEW CODE (USERNAME OR EMAIL -> FIRESTORE -> FIREBASE AUTH)
                    // ============================

                    // Case A: identifier is an email
                    if (Patterns.EMAIL_ADDRESS.matcher(identifier).matches()) {
                        // Find matching username/role via Firestore so your local session stays consistent
                        firestore.collection("users")
                            .whereEqualTo("email", identifier)
                            .limit(1)
                            .get()
                            .addOnSuccessListener { snap ->
                                if (snap.isEmpty) {
                                    // still try firebase login even if mapping not found
                                    loginWithFirebase(identifier, pw, resolvedUsername = null, resolvedRole = null)
                                    return@addOnSuccessListener
                                }

                                val doc = snap.documents.first()
                                val resolvedUsername = doc.id
                                val resolvedRole = doc.getString("role") ?: "user"

                                loginWithFirebase(identifier, pw, resolvedUsername, resolvedRole)
                            }
                            .addOnFailureListener { e ->
                                toast("Failed to lookup email: ${e.message}")
                            }

                        return@Button
                    }

                    // Case B: identifier is a username -> resolve email from users/{username}
                    firestore.collection("users")
                        .document(identifier)
                        .get()
                        .addOnSuccessListener { doc ->
                            if (!doc.exists()) {
                                toast("Username not found (Firestore)")
                                return@addOnSuccessListener
                            }

                            val email = doc.getString("email")?.trim()
                            if (email.isNullOrEmpty()) {
                                toast("No email linked to this username")
                                return@addOnSuccessListener
                            }

                            val role = doc.getString("role") ?: "user"
                            loginWithFirebase(email, pw, resolvedUsername = identifier, resolvedRole = role)
                        }
                        .addOnFailureListener { e ->
                            toast("Failed to lookup username: ${e.message}")
                        }
                },
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color.White,
                    contentColor = Color.Black
                ),
                modifier = Modifier
                    .padding(top = 50.dp)
                    .fillMaxWidth(0.85f)
                    .height(50.dp)
            ) {
                Text(text = "Login")
            }

            Spacer(modifier = Modifier.padding(8.dp))

            Button(
                onClick = { onRegisterClick() },
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color.White,
                    contentColor = Color.Black
                ),
                modifier = Modifier
                    .fillMaxWidth(0.85f)
                    .height(50.dp)
            ) {
                Text(text = "Register")
            }

            Spacer(modifier = Modifier.padding(8.dp))

            Button(
                onClick = { context.startActivity(Intent(context, ForgotPasswordActivity::class.java)) },
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color.White,
                    contentColor = Color.Black
                ),
                modifier = Modifier
                    .fillMaxWidth(0.85f)
                    .height(50.dp)
            ) {
                Text(text = "Forgot Password?")
            }
        }
    }
}
