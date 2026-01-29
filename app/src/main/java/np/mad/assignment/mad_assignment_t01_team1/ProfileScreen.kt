package np.mad.assignment.mad_assignment_t01_team1

import android.content.Context
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import np.mad.assignment.mad_assignment_t01_team1.data.db.AppDatabase
import np.mad.assignment.mad_assignment_t01_team1.data.entity.UserEntity

@Composable
fun ProfileScreen(
    userId: Long,
    onLogout: () -> Unit
) {
    val context = LocalContext.current
    val db = remember { AppDatabase.get(context) }
    val userDao = remember { db.userDao() }
    val reviewsDao = remember { db.reviewDao() }
    val favoritesDao = remember { db.favoriteDao() }
    val scope = rememberCoroutineScope()

    // Data Observables
    val user by userDao.getUserById(userId).collectAsState(initial = null)
    val reviewCount by reviewsDao.getReviewCountForUser(userId).collectAsState(initial = 0)
    val favouriteCount by favoritesDao.getFavoriteCountForUser(userId).collectAsState(initial = 0)
    val userReviews by reviewsDao.getAllReviewsByUserId(userId).collectAsState(initial = emptyList())

    // UI State
    var isEditing by remember { mutableStateOf(false) }
    var editedName by rememberSaveable(user) { mutableStateOf(user?.name ?: "") }
    var showDeleteDialog by remember { mutableStateOf(false) }

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        // --- TOP ROW: PROFILE & LOGOUT ---
        item {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 24.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(
                    imageVector = Icons.Default.Person,
                    contentDescription = "Profile Icon",
                    modifier = Modifier.size(48.dp),
                    tint = Color(0xFF1E88E5)
                )

                Spacer(modifier = Modifier.width(12.dp))

                if (isEditing) {
                    OutlinedTextField(
                        value = editedName,
                        onValueChange = { editedName = it },
                        modifier = Modifier.weight(1f).height(56.dp),
                        singleLine = true
                    )
                    IconButton(onClick = {
                        user?.let {
                            scope.launch(Dispatchers.IO) {
                                userDao.updateUser(it.copy(name = editedName))
                                isEditing = false
                            }
                        }
                    }) {
                        Icon(Icons.Default.Check, contentDescription = "Save", tint = Color.Green)
                    }
                } else {
                    Text(
                        text = user?.name ?: "Loading...",
                        fontSize = 20.sp,
                        fontWeight = FontWeight.Bold
                    )
                    IconButton(onClick = {
                        editedName = user?.name ?: ""
                        isEditing = true
                    }) {
                        Icon(Icons.Default.Edit, contentDescription = "Edit Name", modifier = Modifier.size(20.dp))
                    }
                    Spacer(modifier = Modifier.weight(1f))
                }

                TextButton(
                    onClick = {
                        val prefs = context.getSharedPreferences("user_prefs", Context.MODE_PRIVATE)
                        prefs.edit().clear().apply()
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
        }

        // --- STATS ROW ---
        item {
            Row(
                modifier = Modifier.fillMaxWidth().padding(vertical = 24.dp),
                horizontalArrangement = Arrangement.SpaceEvenly
            ) {
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Text(text = favouriteCount.toString(), fontSize = 32.sp, fontWeight = FontWeight.Bold)
                    Text(text = "Favorites", fontSize = 14.sp, color = Color.Gray)
                }
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Text(text = reviewCount.toString(), fontSize = 32.sp, fontWeight = FontWeight.Bold)
                    Text(text = "Reviews", fontSize = 14.sp, color = Color.Gray)
                }
            }
            Divider(modifier = Modifier.padding(vertical = 8.dp))
        }

        // --- REVIEW HISTORY SECTION ---
        item {
            Text(
                text = "My Review History",
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.padding(vertical = 16.dp)
            )
        }

        if (userReviews.isEmpty()) {
            item {
                Text("You haven't written any reviews yet.", color = Color.Gray, modifier = Modifier.padding(8.dp))
            }
        } else {
            items(userReviews) { review ->
                ReviewCardItem(review = review)
                Spacer(modifier = Modifier.height(12.dp))
            }
        }

        // --- ACCOUNT DELETION ---
        item {
            Spacer(modifier = Modifier.height(40.dp))
            Button(
                onClick = { showDeleteDialog = true },
                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFFFEBEE)),
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Delete Account", color = Color.Red)
            }
        }
    }

    // Delete Confirmation Dialog
    if (showDeleteDialog) {
        AlertDialog(
            onDismissRequest = { showDeleteDialog = false },
            title = { Text("Delete Account?") },
            text = { Text("This will permanently remove your profile and reviews. This cannot be undone.") },
            confirmButton = {
                TextButton(onClick = {
                    user?.let {
                        scope.launch(Dispatchers.IO) {
                            userDao.deleteUser(it)
                            launch(Dispatchers.Main) { onLogout() }
                        }
                    }
                }) { Text("Confirm Delete", color = Color.Red) }
            },
            dismissButton = {
                TextButton(onClick = { showDeleteDialog = false }) { Text("Cancel") }
            }
        )
    }
}