package np.mad.assignment.mad_assignment_t01_team1

import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import coil.compose.AsyncImage
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import np.mad.assignment.mad_assignment_t01_team1.data.db.AppDatabase
import np.mad.assignment.mad_assignment_t01_team1.data.entity.CanteenEntity
import np.mad.assignment.mad_assignment_t01_team1.data.entity.StallEntity
import java.io.File

val AdminPrimary = Color(0xFF181F4D)
val AdminBackground = Color(0xFFE3F2FD)
val AdminText = Color(0xFF212121)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AdminStallManagementScreen(
    navController: NavController
) {
    val context = LocalContext.current
    val db = remember(context) { AppDatabase.get(context) }
    val stallDao = remember { db.stallDao() }
    val canteenDao = remember { db.canteenDao() }
    val scope = rememberCoroutineScope()

    val stallsFlow = stallDao.getAllStalls()
    val allStalls by stallsFlow.collectAsState(initial = emptyList<StallEntity>())
    val allCanteens by canteenDao.getALL().collectAsState(initial = emptyList())
    var showDialog by remember { mutableStateOf(false) }
    var stallToEdit by remember { mutableStateOf<StallEntity?>(null) }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Manage Stalls", fontWeight = FontWeight.Bold) },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = AdminPrimary,
                    titleContentColor = Color.White,
                    navigationIconContentColor = Color.White,
                    actionIconContentColor = Color.White
                ),
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "Back")
                    }
                },
                actions = {
                    IconButton(onClick = {
                        stallToEdit = null
                        showDialog = true
                    }) {
                        Icon(Icons.Default.Add, contentDescription = "Add Stall")
                    }
                }
            )
        }
    ) { padding ->
        Box(modifier = Modifier.padding(padding).fillMaxSize()) {
            LazyColumn(
                contentPadding = PaddingValues(16.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                items(allStalls) { stall ->
                    AdminStallItem(
                        stall = stall,
                        onEdit = {
                            stallToEdit = stall
                            showDialog = true
                        },
                        onDelete = {
                            scope.launch(Dispatchers.IO) {
                                stallDao.deleteStall(stall)
                            }
                        }
                    )
                }
            }
        }

        if (showDialog) {
            StallDialog(
                stallToEdit = stallToEdit,
                availableCanteens = allCanteens,
                onDismiss = { showDialog = false },
                onConfirm = { stall ->
                    scope.launch(Dispatchers.IO) {
                        if (stall.stallId == 0L) {
                            stallDao.insert(stall)
                        } else {
                            stallDao.updateStall(stall)
                        }
                    }
                    showDialog = false
                }
            )
        }
    }
}

@Composable
fun AdminStallItem(
    stall: StallEntity,
    onEdit: () -> Unit,
    onDelete: () -> Unit
) {
    Card(
        colors = CardDefaults.cardColors(containerColor = AdminBackground),
        shape = RoundedCornerShape(12.dp),
        elevation = CardDefaults.cardElevation(2.dp),
        modifier = Modifier.fillMaxWidth()
    ) {
        Row(
            modifier = Modifier
                .padding(12.dp)
                .fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            val imageModel = if (stall.imagePath != null) {
                File(stall.imagePath)
            } else {
                stall.imageResId ?: R.drawable.ic_launcher_foreground
            }

            AsyncImage(
                model = imageModel,
                contentDescription = null,
                contentScale = ContentScale.Crop,
                modifier = Modifier
                    .size(70.dp)
                    .clip(RoundedCornerShape(8.dp))
                    .background(Color.Gray),
                error = painterResource(R.drawable.ic_launcher_foreground)
            )

            Spacer(modifier = Modifier.width(16.dp))

            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = stall.name,
                    fontWeight = FontWeight.Bold,
                    fontSize = 18.sp,
                    color = AdminText
                )
                Text(
                    text = stall.cuisine,
                    fontSize = 14.sp,
                    color = Color.Gray
                )
                Text(
                    text = "Canteen: ${stall.canteenName}",
                    fontSize = 12.sp,
                    color = AdminPrimary
                )
            }

            Column {
                IconButton(onClick = onEdit) {
                    Icon(Icons.Default.Edit, contentDescription = "Edit", tint = AdminPrimary)
                }
                IconButton(onClick = onDelete) {
                    Icon(Icons.Default.Delete, contentDescription = "Delete", tint = Color.Red)
                }
            }
        }
    }
}
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun StallDialog(
    stallToEdit: StallEntity?,
    availableCanteens: List<CanteenEntity>,
    onDismiss: () -> Unit,
    onConfirm: (StallEntity) -> Unit
) {
    val context = LocalContext.current
    var name by remember { mutableStateOf(stallToEdit?.name ?: "") }
    var cuisine by remember { mutableStateOf(stallToEdit?.cuisine ?: "") }
    var description by remember { mutableStateOf(stallToEdit?.description ?: "") }

    var currentImagePath by remember { mutableStateOf(stallToEdit?.imagePath) }
    var selectedImageUri by remember { mutableStateOf<Uri?>(null) }

    var canteenName by remember {
        mutableStateOf(
            if (stallToEdit != null) {
                availableCanteens.find { it.canteenId == stallToEdit.canteenId }?.name ?: ""
            } else ""
        )
    }
    var expandedCanteen by remember { mutableStateOf(false) }

    val photoPickerLauncher = rememberLauncherForActivityResult(ActivityResultContracts.PickVisualMedia())
    { uri ->
        uri?.let {
            selectedImageUri = it
        }
    }


    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text(if (stallToEdit == null) "Add New Stall" else "Edit Stall") },
        text = {
            Column(
                modifier = Modifier.verticalScroll(rememberScrollState()), // Make scrollable
                verticalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(150.dp)
                        .clip(RoundedCornerShape(8.dp))
                        .background(Color.LightGray)
                        .clickable {
                            photoPickerLauncher.launch(
                                PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly)
                            )
                        },
                    contentAlignment = Alignment.Center
                ) {
                    if (selectedImageUri != null) {
                        AsyncImage(
                            model = selectedImageUri,
                            contentDescription = "Selected Image",
                            contentScale = ContentScale.Crop,
                            modifier = Modifier.fillMaxSize()
                        )
                    } else if (currentImagePath != null) {
                        AsyncImage(
                            model = File(currentImagePath!!),
                            contentDescription = "Saved Image",
                            contentScale = ContentScale.Crop,
                            modifier = Modifier.fillMaxSize()
                        )
                    } else {
                        // Show placeholder
                        Column(horizontalAlignment = Alignment.CenterHorizontally) {
                            Icon(Icons.Default.Add, contentDescription = null, tint = Color.Gray)
                            Text("Tap to upload image", color = Color.Gray)
                        }
                    }
                }

                OutlinedTextField(value = name, onValueChange = { name = it }, label = { Text("Name") })
                OutlinedTextField(value = cuisine, onValueChange = { cuisine = it }, label = { Text("Cuisine") })

                ExposedDropdownMenuBox(
                    expanded = expandedCanteen,
                    onExpandedChange = { expandedCanteen = !expandedCanteen }
                ) {
                    OutlinedTextField(
                        value = canteenName,
                        onValueChange = {},
                        readOnly = true,
                        label = { Text("Select Canteen") },
                        trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = expandedCanteen) },
                        modifier = Modifier.menuAnchor().fillMaxWidth()
                    )
                    ExposedDropdownMenu(
                        expanded = expandedCanteen,
                        onDismissRequest = { expandedCanteen = false }
                    ) {
                        availableCanteens.forEach { item ->
                            DropdownMenuItem(
                                text = { Text(item.name) },
                                onClick = {
                                    canteenName = item.name
                                    expandedCanteen = false
                                }
                            )
                        }
                    }
                }

                OutlinedTextField(value = description, onValueChange = { description = it }, label = { Text("Description") })
            }
        },
        confirmButton = {
            Button(onClick = {
                if (name.isNotBlank()) {
                    var finalPath = currentImagePath

                    if (selectedImageUri != null) {
                        finalPath = saveImageToInternalStorage(context, selectedImageUri!!)
                    }

                    val matchedCanteen = availableCanteens.find { it.name == canteenName }

                    val newStall = StallEntity(
                        stallId = stallToEdit?.stallId ?: 0,
                        name = name,
                        cuisine = cuisine,
                        description = description,
                        imagePath = finalPath,
                        imageResId = null,
                        canteenName = canteenName,
                        canteenId = matchedCanteen?.canteenId ?: 0L,
                    )
                    onConfirm(newStall)
                }
            }) { Text("Save") }
        },
        dismissButton = { TextButton(onClick = onDismiss) { Text("Cancel") } }
    )
}

fun saveImageToInternalStorage(context: android.content.Context, uri: Uri): String? {
    return try {
        val fileName = "stall_${System.currentTimeMillis()}.jpg"
        val file = File(context.filesDir, fileName)

        context.contentResolver.openInputStream(uri)?.use { input ->
            file.outputStream().use { output ->
                input.copyTo(output)
            }
        }
        file.absolutePath
    } catch (e: Exception) {
        e.printStackTrace()
        null
    }
}