package np.mad.assignment.mad_assignment_t01_team1

import android.content.Context
import android.content.Intent
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Person
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.platform.LocalContext
import np.mad.assignment.mad_assignment_t01_team1.data.db.AppDatabase
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.launch

@Composable
fun ProfileScreen(
    userId: Long,
    onLogout: () -> Unit
) {
    val context = LocalContext.current
    val db = remember { AppDatabase.get(context) }
    val reviewsDao = remember { db.reviewDao() }
    val favoritesDao = remember { db.favoriteDao() }
    var username by remember { mutableStateOf("Loading...") }
    val reviewCount by reviewsDao.getReviewCountForUser(userId).collectAsState(initial = 0)
    val favouriteCount by favoritesDao.getFavoriteCountForUser(userId).collectAsState(initial = 0)
    LaunchedEffect(userId) {
        try {
            val userFlow = db.userDao().getById(userId)
            val userEntity = userFlow.firstOrNull()
            username = userEntity?.name ?: "Unknown User"
        } catch (e: Exception) {
            username = "Error"
        }
    }

    // Top Row: Profile Icon, Username, Logout
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 24.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Profile Icon
            Icon(
                imageVector = Icons.Default.Person,
                contentDescription = "Profile Icon",
                modifier = Modifier.size(48.dp),
                tint = Color(0xFF1E88E5)
            )

            Spacer(modifier = Modifier.width(12.dp))

            // Username text (fetched from DB)
            Text(
                text = username,
                fontSize = 20.sp,
                fontWeight = FontWeight.Bold
            )

            Spacer(modifier = Modifier.weight(1f))

            // Logout button
            TextButton(
                onClick = {
                    // Clear saved session
                    val prefs = context.getSharedPreferences("user_prefs", Context.MODE_PRIVATE)
                    prefs.edit().clear().apply()

                    // Navigate back to login
                    onLogout()
                }
            ) {
                Image(
                    painter = painterResource(R.drawable.logout),
                    contentDescription = "Logout Icon",
                    modifier = Modifier.size(24.dp)
                    )
                Spacer(modifier = Modifier.width(4.dp))
                Text("Logout", color = Color.Red)
            }
        }

        Spacer(modifier = Modifier.height(48.dp))

        // Middle Row: Favorite Stall, Reviews Written
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceEvenly,
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Favorite Stall
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                Text(
                    text = favouriteCount.toString(), // Replace with actual count from DB
                    fontSize = 32.sp,
                    fontWeight = FontWeight.Bold
                )
                Text(text = "Favorite Stall")
            }

            // Reviews Written
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                Text(
                    text = reviewCount.toString(), // Replace with actual count from DB
                    fontSize = 32.sp,
                    fontWeight = FontWeight.Bold
                )
                Text(text = "Reviews Written")
            }
        }
    }
}
