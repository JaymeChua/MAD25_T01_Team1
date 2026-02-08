package np.mad.assignment.mad_assignment_t01_team1

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Info
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import np.mad.assignment.mad_assignment_t01_team1.data.db.AppDatabase
import np.mad.assignment.mad_assignment_t01_team1.data.entity.DishEntity
import androidx.core.content.FileProvider
import java.util.Date
import java.io.File
import java.io.FileOutputStream
import android.Manifest
import android.content.pm.PackageManager
import androidx.core.content.ContextCompat

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BulkAddDishesScreen(
    navController: NavController,
    stallId: Long
) {
    val context = LocalContext.current
    val scope = rememberCoroutineScope()
    var scannedDishes by remember { mutableStateOf<List<ScannedDish>>(emptyList()) }
    var isLoading by remember { mutableStateOf(false) }

    val db = remember { AppDatabase.get(context) }
    val dishDao = remember { db.dishDao() }

    var tempCameraUri by remember { mutableStateOf<Uri?>(null) }

    fun createTempPictureUri(): Uri {
        val tempFile = File.createTempFile(
            "menu_scan_${System.currentTimeMillis()}",
            ".jpg",
            context.cacheDir
        ).apply { createNewFile() }

        return androidx.core.content.FileProvider.getUriForFile(
            context,
            "${context.packageName}.provider",
            tempFile
        )
    }

    // --- 2. CAMERA LAUNCHER ---
    val takePictureLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.TakePicture()
    ) { success ->
        if (success && tempCameraUri != null) {
            isLoading = true
            scope.launch {
                try {
                    val inputStream = context.contentResolver.openInputStream(tempCameraUri!!)
                    val bitmap = BitmapFactory.decodeStream(inputStream)
                    scannedDishes = MenuScanner.scanAndCropMenu(bitmap)
                    if (scannedDishes.isEmpty()) {
                        Toast.makeText(context, "No dishes found", Toast.LENGTH_SHORT).show()
                    }
                } catch (e: Exception) {
                    e.printStackTrace()
                    Toast.makeText(context, "Error processing photo", Toast.LENGTH_SHORT).show()
                } finally {
                    isLoading = false
                }
            }
        }
    }

    val permissionLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestPermission()
    ) { isGranted ->
        if (isGranted) {
            val uri = createTempPictureUri()
            tempCameraUri = uri
            takePictureLauncher.launch(uri)
        } else {
            Toast.makeText(context, "Camera permission required", Toast.LENGTH_SHORT).show()
        }
    }

    val galleryLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.PickVisualMedia()
    ) { uri ->
        if (uri != null) {
            isLoading = true
            scope.launch {
                try {
                    val inputStream = context.contentResolver.openInputStream(uri)
                    val bitmap = BitmapFactory.decodeStream(inputStream)
                    scannedDishes = MenuScanner.scanAndCropMenu(bitmap)
                    if (scannedDishes.isEmpty()) Toast.makeText(context, "No dishes found", Toast.LENGTH_SHORT).show()
                } catch (e: Exception) {
                    Toast.makeText(context, "Scan failed", Toast.LENGTH_SHORT).show()
                } finally {
                    isLoading = false
                }
            }
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("AI Menu Scanner") },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "Back")
                    }
                }
            )
        }
    ) { padding ->
        Column(modifier = Modifier.padding(padding).padding(16.dp)) {

            Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                Button(
                    modifier = Modifier.weight(1f),
                    enabled = !isLoading,
                    onClick = {
                        val permissionCheck = ContextCompat.checkSelfPermission(
                            context,
                            Manifest.permission.CAMERA
                        )

                        if (permissionCheck == PackageManager.PERMISSION_GRANTED) {
                            val uri = createTempPictureUri()
                            tempCameraUri = uri
                            takePictureLauncher.launch(uri)
                        } else {
                            permissionLauncher.launch(Manifest.permission.CAMERA)
                        }
                    }
                ) {
                    Icon(Icons.Default.Add, contentDescription = null) // Changed from CameraAlt to avoid import error
                    Spacer(Modifier.width(8.dp))
                    Text("Camera")
                }

                OutlinedButton(
                    modifier = Modifier.weight(1f),
                    enabled = !isLoading,
                    onClick = {
                        galleryLauncher.launch(PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly))
                    }
                ) {
                    Icon(Icons.Default.Info, contentDescription = null)
                    Spacer(Modifier.width(8.dp))
                    Text("Gallery")
                }
            }

            if (isLoading) {
                LinearProgressIndicator(modifier = Modifier.fillMaxWidth().padding(top = 16.dp))
                Text("Gemini is analyzing...", modifier = Modifier.padding(top = 8.dp), color = Color.Gray)
            }

            LazyColumn(
                verticalArrangement = Arrangement.spacedBy(12.dp),
                modifier = Modifier.padding(top = 16.dp)
            ) {
                items(scannedDishes) { dish ->
                    DishReviewCard(dish) {
                        scope.launch(Dispatchers.IO) {
                            val file = File(context.filesDir, "crop_${System.currentTimeMillis()}.jpg")
                            FileOutputStream(file).use { out ->
                                dish.croppedBitmap.compress(Bitmap.CompressFormat.JPEG, 90, out)
                            }

                            val newDish = DishEntity(
                                dishName = dish.name,
                                dishPrice = dish.price.replace("$", ""),
                                imagePath = file.absolutePath,
                                imageResId = null,
                                stallId = stallId,
                            )
                            dishDao.insert(newDish)
                            withContext(Dispatchers.Main) {
                                scannedDishes = scannedDishes - dish
                                Toast.makeText(context, "Added ${dish.name}", Toast.LENGTH_SHORT).show()
                            }
                        }
                    }
                }
            }
        }
    }
}
@Composable
fun DishReviewCard(dish: ScannedDish, onAdd: () -> Unit) {
    Card(elevation = CardDefaults.cardElevation(4.dp)) {
        Row(modifier = Modifier.padding(12.dp), verticalAlignment = Alignment.CenterVertically) {
            Image(
                bitmap = dish.croppedBitmap.asImageBitmap(),
                contentDescription = null,
                contentScale = ContentScale.Crop,
                modifier = Modifier.size(80.dp).clip(RoundedCornerShape(8.dp))
            )
            Spacer(modifier = Modifier.width(12.dp))
            Column(modifier = Modifier.weight(1f)) {
                Text(dish.name, fontWeight = FontWeight.Bold)
                Text("$${dish.price}", color = Color.Gray)
            }
            IconButton(onClick = onAdd) { Icon(Icons.Default.Add, contentDescription = "Add") }
        }
    }
}