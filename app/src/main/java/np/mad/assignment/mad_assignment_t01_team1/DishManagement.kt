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
import np.mad.assignment.mad_assignment_t01_team1.data.entity.DishEntity
import np.mad.assignment.mad_assignment_t01_team1.data.entity.StallEntity
import java.io.File
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.ui.graphics.graphicsLayer
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AdminDishManagementScreen(
    navController: NavController
) {
    val context = LocalContext.current
    val db = remember(context) { AppDatabase.get(context) }
    val stallDao = remember { db.stallDao() }
    val dishDao = remember { db.dishDao() }
    val scope = rememberCoroutineScope()

    val allStalls by stallDao.getAllStalls().collectAsState(initial = emptyList())
    val allDishes by dishDao.getAllDishes().collectAsState(initial = emptyList())

    val dishesByStall = remember(allDishes) { allDishes.groupBy { it.stallId } }

    var showDialog by remember { mutableStateOf(false) }
    var dishToEdit by remember { mutableStateOf<DishEntity?>(null) }
    var preSelectedStallId by remember { mutableStateOf<Long?>(null) }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Manage Dishes", fontWeight = FontWeight.Bold) },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = AdminPrimary,
                    titleContentColor = Color.White,
                    navigationIconContentColor = Color.White
                ),
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "Back")
                    }
                }
            )
        }
    ) { padding ->
        Box(modifier = Modifier.padding(padding).fillMaxSize()) {
            LazyColumn(
                contentPadding = PaddingValues(16.dp),
                verticalArrangement = Arrangement.spacedBy(24.dp)
            ) {
                items(allStalls) { stall ->
                    StallSection(
                        stall = stall,
                        dishes = dishesByStall[stall.stallId] ?: emptyList(),
                        onAddDish = {
                            dishToEdit = null
                            preSelectedStallId = stall.stallId
                            showDialog = true
                        },
                        onAiScan = {
                            navController.navigate("bulk_add/${stall.stallId}")
                        },
                        onEditDish = { dish ->
                            dishToEdit = dish
                            preSelectedStallId = stall.stallId
                            showDialog = true
                        },
                        onDeleteDish = { dish ->
                            scope.launch(Dispatchers.IO) {
                                dishDao.deleteDish(dish)
                            }
                        }
                    )
                }
            }
        }

        if (showDialog) {
            DishDialog(
                dishToEdit = dishToEdit,
                stallId = preSelectedStallId ?: 0L, // Pass the locked stall ID
                onDismiss = { showDialog = false },
                onConfirm = { dish ->
                    scope.launch(Dispatchers.IO) {
                        if (dish.dishId == 0L) {
                            dishDao.insert(dish)
                        } else {
                            dishDao.updateDish(dish)
                        }
                    }
                    showDialog = false
                }
            )
        }
    }
}
@Composable
fun StallSection(
    stall: StallEntity,
    dishes: List<DishEntity>,
    onAddDish: () -> Unit,
    onAiScan: () -> Unit,
    onEditDish: (DishEntity) -> Unit,
    onDeleteDish: (DishEntity) -> Unit
) {
    var isExpanded by remember { mutableStateOf(false) }

    val rotationState by animateFloatAsState(
        targetValue = if (isExpanded) 180f else 0f,
        label = "Arrow Rotation"
    )

    Column(modifier = Modifier.fillMaxWidth()) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 8.dp)
                .background(Color.White, RoundedCornerShape(8.dp))
                .padding(horizontal = 12.dp, vertical = 8.dp)
                .clickable { isExpanded = !isExpanded },
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(
                    imageVector = Icons.Default.KeyboardArrowDown,
                    contentDescription = "Expand",
                    tint = AdminPrimary,
                    modifier = Modifier.graphicsLayer(rotationZ = rotationState)
                )

                Spacer(modifier = Modifier.width(8.dp))

                Column {
                    Text(
                        text = stall.name,
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold,
                        color = AdminPrimary
                    )
                    Text(
                        text = stall.canteenName,
                        style = MaterialTheme.typography.bodySmall,
                        color = Color.Gray
                    )
                }
            }

            Row {
                IconButton(onClick = onAiScan) {
                    Icon(
                        Icons.Filled.Call,
                        contentDescription = "AI Scan",
                        tint = AdminPrimary
                    )
                }
                IconButton(onClick = onAddDish) {
                    Icon(
                        Icons.Default.AddCircle,
                        contentDescription = "Add Manual",
                        tint = AdminPrimary
                    )
                }
            }
        }


        AnimatedVisibility(visible = isExpanded) {
            if (dishes.isEmpty()) {
                Text(
                    "No dishes added yet.",
                    fontStyle = androidx.compose.ui.text.font.FontStyle.Italic,
                    color = Color.Gray,
                    modifier = Modifier.padding(start = 40.dp, bottom = 12.dp)
                )
            } else {
                Column(
                    verticalArrangement = Arrangement.spacedBy(8.dp),
                    modifier = Modifier.padding(bottom = 12.dp)
                ) {
                    dishes.forEach { dish ->
                        AdminDishItem(
                            dish = dish,
                            onEdit = { onEditDish(dish) },
                            onDelete = { onDeleteDish(dish) }
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun AdminDishItem(
    dish: DishEntity,
    onEdit: () -> Unit,
    onDelete: () -> Unit
) {
    Card(
        colors = CardDefaults.cardColors(containerColor = AdminBackground),
        shape = RoundedCornerShape(8.dp),
        elevation = CardDefaults.cardElevation(1.dp),
        modifier = Modifier.fillMaxWidth().padding(start = 12.dp)
    ) {
        Row(
            modifier = Modifier
                .padding(8.dp)
                .fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            val imageModel = if (dish.imagePath != null) {
                File(dish.imagePath)
            } else {
                dish.imageResId ?: R.drawable.ic_launcher_foreground
            }

            AsyncImage(
                model = imageModel,
                contentDescription = null,
                contentScale = ContentScale.Crop,
                modifier = Modifier
                    .size(50.dp)
                    .clip(RoundedCornerShape(6.dp))
                    .background(Color.Gray),
                error = painterResource(R.drawable.ic_launcher_foreground)
            )

            Spacer(modifier = Modifier.width(12.dp))

            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = dish.dishName,
                    fontWeight = FontWeight.Bold,
                    fontSize = 16.sp,
                    color = AdminText
                )
                Text(
                    text = "$${dish.dishPrice}",
                    fontSize = 14.sp,
                    color = AdminPrimary,
                    fontWeight = FontWeight.Medium
                )
            }

            IconButton(onClick = onEdit) {
                Icon(Icons.Default.Edit, contentDescription = "Edit", tint = AdminPrimary, modifier = Modifier.size(20.dp))
            }
            IconButton(onClick = onDelete) {
                Icon(Icons.Default.Delete, contentDescription = "Delete", tint = Color.Red, modifier = Modifier.size(20.dp))
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DishDialog(
    dishToEdit: DishEntity?,
    stallId: Long,
    onDismiss: () -> Unit,
    onConfirm: (DishEntity) -> Unit
) {
    val context = LocalContext.current
    var name by remember { mutableStateOf(dishToEdit?.dishName ?: "") }
    var priceStr by remember { mutableStateOf(dishToEdit?.dishPrice?.toString() ?: "") }

    var currentImagePath by remember { mutableStateOf(dishToEdit?.imagePath) }
    var selectedImageUri by remember { mutableStateOf<Uri?>(null) }

    val photoPickerLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.PickVisualMedia(),
        onResult = { uri -> uri?.let { selectedImageUri = it } }
    )

    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text(if (dishToEdit == null) "Add Dish" else "Edit Dish") },
        text = {
            Column(
                modifier = Modifier.verticalScroll(rememberScrollState()),
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
                        AsyncImage(model = selectedImageUri, contentDescription = null, contentScale = ContentScale.Crop, modifier = Modifier.fillMaxSize())
                    } else if (currentImagePath != null) {
                        AsyncImage(model = File(currentImagePath!!), contentDescription = null, contentScale = ContentScale.Crop, modifier = Modifier.fillMaxSize())
                    } else {
                        Column(horizontalAlignment = Alignment.CenterHorizontally) {
                            Icon(Icons.Default.Add, contentDescription = null, tint = Color.Gray)
                            Text("Tap to upload image", color = Color.Gray)
                        }
                    }
                }

                OutlinedTextField(value = name, onValueChange = { name = it }, label = { Text("Dish Name") })

                OutlinedTextField(
                    value = priceStr,
                    onValueChange = { priceStr = it },
                    label = { Text("Price ($)") },
                )

            }
        },
        confirmButton = {
            Button(onClick = {
                val price = priceStr.toDoubleOrNull()
                if (name.isNotBlank() && price != null) {
                    var finalPath = currentImagePath
                    if (selectedImageUri != null) {
                        finalPath = saveImageToInternalStorage(context, selectedImageUri!!)
                    }

                    val newDish = DishEntity(
                        dishId = dishToEdit?.dishId ?: 0,
                        dishName = name,
                        dishPrice = price.toString(),
                        imagePath = finalPath,
                        imageResId = null,
                        stallId = stallId
                    )
                    onConfirm(newDish)
                }
            }) { Text("Save") }
        },
        dismissButton = { TextButton(onClick = onDismiss) { Text("Cancel") } }
    )
}