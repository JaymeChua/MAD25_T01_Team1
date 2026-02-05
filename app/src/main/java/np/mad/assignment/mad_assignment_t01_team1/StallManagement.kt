package np.mad.assignment.mad_assignment_t01_team1

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
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
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import np.mad.assignment.mad_assignment_t01_team1.data.db.AppDatabase
import np.mad.assignment.mad_assignment_t01_team1.data.entity.CanteenEntity
import np.mad.assignment.mad_assignment_t01_team1.data.entity.StallEntity

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
            Image(
                painter = painterResource(id = stall.imageResId),
                contentDescription = null,
                contentScale = ContentScale.Crop,
                modifier = Modifier
                    .size(70.dp)
                    .clip(RoundedCornerShape(8.dp))
                    .background(Color.Gray)
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
    var name by remember { mutableStateOf(stallToEdit?.name ?: "") }
    var cuisine by remember { mutableStateOf(stallToEdit?.cuisine ?: "") }
    var description by remember { mutableStateOf(stallToEdit?.description ?: "") }
    var canteenName by remember {
        mutableStateOf(
            if (stallToEdit != null) {
                availableCanteens.find { it.canteenId == stallToEdit.canteenId }?.name ?: ""
            } else {
                ""
            }
        )
    }
    val availableImages = mapOf(
        "Standard Food" to R.drawable.ic_launcher_background,
        "Fast Food" to R.drawable.ic_launcher_foreground,
    )

    var selectedImageId by remember {
        mutableStateOf(stallToEdit?.imageResId ?: availableImages.values.first())
    }

    var expandedCanteen by remember { mutableStateOf(false) }
    var expandedImage by remember { mutableStateOf(false) }

    val canteens = listOf("Makan Place", "Food Club", "Munch")

    AlertDialog(
        onDismissRequest = onDismiss,
        title = {
            Text(
                text = if (stallToEdit == null) "Add New Stall" else "Edit Stall",
                fontWeight = FontWeight.Bold,
                color = AdminPrimary
            )
        },
        text = {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 8.dp),
                verticalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                OutlinedTextField(
                    value = name,
                    onValueChange = { name = it },
                    label = { Text("Stall Name") },
                    modifier = Modifier.fillMaxWidth()
                )

                OutlinedTextField(
                    value = cuisine,
                    onValueChange = { cuisine = it },
                    label = { Text("Cuisine (e.g. Western)") },
                    modifier = Modifier.fillMaxWidth()
                )

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
                        canteens.forEach { item ->
                            DropdownMenuItem(
                                text = { Text(item) },
                                onClick = {
                                    canteenName = item
                                    expandedCanteen = false
                                }
                            )
                        }
                    }
                }

                // Image Selector Dropdown (Crucial for ResId)
                ExposedDropdownMenuBox(
                    expanded = expandedImage,
                    onExpandedChange = { expandedImage = !expandedImage }
                ) {
                    // Find the name key for the current selected ID
                    val selectedName = availableImages.entries.find { it.value == selectedImageId }?.key ?: "Select Image"

                    OutlinedTextField(
                        value = selectedName,
                        onValueChange = {},
                        readOnly = true,
                        label = { Text("Stall Image") },
                        trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = expandedImage) },
                        modifier = Modifier.menuAnchor().fillMaxWidth()
                    )
                    ExposedDropdownMenu(
                        expanded = expandedImage,
                        onDismissRequest = { expandedImage = false }
                    ) {
                        availableImages.forEach { (imgName, resId) ->
                            DropdownMenuItem(
                                text = {
                                    Row(verticalAlignment = Alignment.CenterVertically) {
                                        Image(
                                            painter = painterResource(id = resId),
                                            contentDescription = null,
                                            modifier = Modifier.size(24.dp)
                                        )
                                        Spacer(modifier = Modifier.width(8.dp))
                                        Text(imgName)
                                    }
                                },
                                onClick = {
                                    selectedImageId = resId
                                    expandedImage = false
                                }
                            )
                        }
                    }
                }

                OutlinedTextField(
                    value = description,
                    onValueChange = { description = it },
                    label = { Text("Description") },
                    modifier = Modifier.fillMaxWidth(),
                    maxLines = 3
                )
            }
        },
        confirmButton = {
            Button(
                colors = ButtonDefaults.buttonColors(containerColor = AdminPrimary),
                onClick = {
                    if (name.isNotBlank() && cuisine.isNotBlank()) {
                        val matchedCanteen = availableCanteens.find { it.name == canteenName }
                        val matchedId = matchedCanteen?.canteenId ?: 0L
                        val newStall = StallEntity(
                            stallId = stallToEdit?.stallId ?: 0,
                            name = name,
                            cuisine = cuisine,
                            description = description,
                            imageResId = selectedImageId,
                            canteenName = canteenName,
                            canteenId = matchedId,
                        )
                        onConfirm(newStall)
                    }
                }
            ) {
                Text("Save")
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text("Cancel", color = AdminPrimary)
            }
        }
    )
}