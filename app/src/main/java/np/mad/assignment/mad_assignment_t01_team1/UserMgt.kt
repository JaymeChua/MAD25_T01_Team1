package np.mad.assignment.mad_assignment_t01_team1

import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.runtime.Composable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import np.mad.assignment.mad_assignment_t01_team1.data.db.AppDatabase
import np.mad.assignment.mad_assignment_t01_team1.data.entity.ReviewEntity
import np.mad.assignment.mad_assignment_t01_team1.data.entity.UserEntity
import np.mad.assignment.mad_assignment_t01_team1.util.SecurityUtils
import java.time.LocalDate

val UserMgmtDarkBlue = Color(0xFF1565C0)
val UserMgmtLightBlue = Color(0xFFE3F2FD)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun UserManagementScreen(
    navController: NavController,
    currentAdminId: Long,
) {
    val context= LocalContext.current
    val  db =remember(context){
        AppDatabase.get(context)}
    val userDao = remember { db.userDao() }
    val scope = rememberCoroutineScope()
    val allUsers by userDao.getAllUsersExceptAdmin(currentAdminId).collectAsState(initial = emptyList<UserEntity>())
    var showDialog by rememberSaveable { mutableStateOf(false) }
    var newUsernameText by rememberSaveable { mutableStateOf("") }
    var newUserPasswordText by rememberSaveable { mutableStateOf("") }
    var newUserRoleText by rememberSaveable { mutableStateOf("") }
    val roles = listOf("USER", "ADMIN")
    var expanded by remember { mutableStateOf(false) }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("User Management") },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "Back")
                    }
                },
                actions = {
                    IconButton(onClick = { showDialog = true }) {
                        Icon(Icons.Default.Add, contentDescription = "Add User")
                    }
                }
            )
        }
    ) { padding ->
        LazyColumn(modifier = Modifier.padding(padding)) {
             items(allUsers){user ->
                 UserItem(
                     user = user,
                     onDeleteClick = { userToDelete ->
                         scope.launch {
                             userDao.deleteUser(userToDelete)
                         }
                     }
                 )
             }
        }
        if (showDialog) {
            AlertDialog(
                onDismissRequest = { showDialog = false },
                title = { Text("Create User") },
                text = {
                    Column {
                        OutlinedTextField(
                            value = newUsernameText,
                            onValueChange = { newUsernameText = it },
                            label = { Text("Username.") },
                            modifier = Modifier.fillMaxWidth(),
                            maxLines = 3
                        )

                        Spacer(modifier = Modifier.height(8.dp))

                        OutlinedTextField(
                            value = newUserPasswordText,
                            onValueChange = { newUserPasswordText = it },
                            label = { Text("Password.") },
                            modifier = Modifier.fillMaxWidth(),
                            maxLines = 3
                        )

                        Spacer(modifier = Modifier.height(8.dp))

                        ExposedDropdownMenuBox(
                            expanded = expanded,
                            onExpandedChange = { expanded = !expanded },
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            OutlinedTextField(
                                value = newUserRoleText,
                                onValueChange = {},
                                readOnly = true,
                                label = { Text("Select Role") },
                                modifier = Modifier
                                    .menuAnchor(MenuAnchorType.PrimaryNotEditable)
                                    .fillMaxWidth()
                            )

                            ExposedDropdownMenu(
                                expanded = expanded,
                                onDismissRequest = { expanded = false }
                            ) {
                                roles.forEach { role ->
                                    DropdownMenuItem(
                                        text = { Text(role) },
                                        onClick = {
                                            newUserRoleText = role
                                            expanded = false
                                        },
                                    )
                                }
                            }
                        }
                    }
                },
                confirmButton = {
                    Button(
                        onClick = {
                            if (newUsernameText.isNotBlank() && newUserPasswordText.isNotBlank() && newUserRoleText.isNotBlank()) {
                                scope.launch(Dispatchers.IO) {
                                    val hashedPass = SecurityUtils.sha256(newUserPasswordText)

                                    val newUser = UserEntity(
                                        name = newUsernameText,
                                        password = hashedPass,
                                        role = newUserRoleText
                                    )
                                    userDao.upsert(newUser)
                                    launch(Dispatchers.Main) {
                                        newUsernameText = ""
                                        newUserPasswordText = ""
                                        newUserRoleText = ""
                                        showDialog = false
                                    }
                                }
                            }
                        },
                        colors = ButtonDefaults.buttonColors(containerColor = Color.Black)
                    ) {
                        Text("Create User")
                    }
                },
            )
        }
    }
}
@Composable
fun UserItem(
    user: UserEntity,
    onDeleteClick: (UserEntity) -> Unit,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 8.dp),
        colors = CardDefaults.cardColors(
            containerColor = UserMgmtLightBlue
        ),
        shape = RoundedCornerShape(12.dp)
    ) {
        ListItem(
            headlineContent = {
                Text(text = user.name, style = MaterialTheme.typography.titleMedium)
            },
            supportingContent = {
                Column {
                    Text(text = "ID: ${user.userId}")
                    Text(text = "Role: ${user.role}")
                    Text(text = "Created Date: ${user.createdDate}")
                }
            },
            leadingContent = {
                Icon(
                    imageVector = Icons.Default.Person,
                    contentDescription = null,
                    tint = UserMgmtDarkBlue
                )
            },
            trailingContent = {
                IconButton(onClick = { onDeleteClick(user) }) {
                    Icon(
                        imageVector = Icons.Default.Delete,
                        contentDescription = "Delete User",
                        tint = MaterialTheme.colorScheme.error
                    )
                }
            },
            colors = ListItemDefaults.colors(containerColor = Color.Transparent)
        )
    }
}