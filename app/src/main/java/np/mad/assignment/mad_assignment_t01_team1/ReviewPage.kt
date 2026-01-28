package np.mad.assignment.mad_assignment_t01_team1


import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import np.mad.assignment.mad_assignment_t01_team1.data.db.AppDatabase
import np.mad.assignment.mad_assignment_t01_team1.data.entity.ReviewEntity
import androidx.compose.material.icons.filled.Add
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.layout.ContentScale
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.time.LocalDate
import androidx.compose.foundation.lazy.items
import androidx.compose.runtime.LaunchedEffect
import kotlinx.coroutines.withContext
import np.mad.assignment.mad_assignment_t01_team1.data.entity.StallEntity
import np.mad.assignment.mad_assignment_t01_team1.data.entity.UserEntity

@Composable
fun ReviewPage(
    userId:Long,
    stallId: Long,
    onCloseClicked: () -> Unit = {},
) {
    val context= LocalContext.current
    val  db =remember(context){
        AppDatabase.get(context)}
    val reviewsDao = remember { db.reviewDao() }
    val stallDao = remember { db.stallDao() }
    val userDao = remember { db.userDao() }
    val stall by stallDao.getByIdFlow(stallId).collectAsState(initial = null)
    val scope = rememberCoroutineScope()
    val reviews: List<ReviewEntity> by reviewsDao.getAllReviewsForStall(stallId).collectAsState(initial = emptyList())
    val user by userDao.getById(userId).collectAsState(initial = null)

    val reviewCount = reviews.size
    val averageRating = if (reviewCount > 0) {
        reviews.map { it.rating }.average()
    } else {
        0.0
    }
    var showDialog by rememberSaveable { mutableStateOf(false) }
    var inputReviewText by rememberSaveable { mutableStateOf("") }
    var inputRating by rememberSaveable { mutableStateOf(5) }

    // Main Container
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFFF5F5F5))
    ) {

        LazyColumn (
            modifier = Modifier
                .fillMaxSize()
        ) {

            item {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(250.dp),
                    contentAlignment = Alignment.Center
                ) {
                    stall?.let { stallEntity ->
                        Image(
                            painter = painterResource(stallEntity.imageResId),
                            contentDescription = stallEntity.name,
                            contentScale = ContentScale.Crop,
                            modifier = Modifier.fillMaxSize()
                        )
                    }
                }

                Column(modifier = Modifier.padding(16.dp)) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(
                            imageVector = Icons.Default.Star,
                            contentDescription = "Rating",
                            tint = Color(0xFFF4B400),
                            modifier = Modifier.size(20.dp)
                        )
                        Spacer(modifier = Modifier.width(4.dp))
                        Text(
                            text = String.format("%.1f/5 (%d reviews)", averageRating, reviewCount),
                            fontWeight = FontWeight.Bold,
                            fontSize = 14.sp
                        )
                    }

                    Spacer(modifier = Modifier.height(8.dp))

                    Text(
                        text = "Reviews for ${stall?.name ?: "Loading..."}",
                        fontSize = 28.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color.Black
                    )

                    Text(
                        text = stall?.description ?: "",
                        fontSize = 16.sp,
                        color = Color.Gray,
                        modifier = Modifier.padding(top = 4.dp, bottom = 24.dp)
                    )
                }
            }

            items(reviews) { review ->
                Box(modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp)) {
                    ReviewCardItem(review)
                }
            }
        }

        SmallFloatingActionButton(
            onClick = { onCloseClicked() },
            modifier = Modifier
                .padding(top = 40.dp, start = 16.dp)
                .align(Alignment.TopStart),
            containerColor = Color.White,
            contentColor = Color(0xFFF4B400)
        ) {
            Icon(Icons.Default.Close, contentDescription = "Close")
        }
        FloatingActionButton(
            onClick = { showDialog = true },
            containerColor = Color(0xFFF4B400),
            contentColor = Color.White,
            modifier = Modifier
                .align(Alignment.BottomEnd)
                .padding(24.dp)
        ) {
            Row(
                modifier = Modifier.padding(horizontal = 16.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(Icons.Default.Add, contentDescription = "Add Review")
                Spacer(modifier = Modifier.width(8.dp))
                Text("Review", fontWeight = FontWeight.Bold)
            }
        }

        if (showDialog) {
            AlertDialog(
                onDismissRequest = { showDialog = false },
                title = { Text("Write a Review") },
                text = {
                    Column {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.Center
                        ) {
                            for (i in 1..5) {
                                IconButton(
                                    onClick = { inputRating = i }
                                ) {
                                    Icon(
                                        imageVector = Icons.Default.Star,
                                        contentDescription = "Star $i",
                                        tint = if (i <= inputRating) { //LLM
                                            Color(0xFFF4B400)
                                        } else {
                                            Color(0xFFF4B400).copy(alpha = 0.2f)
                                        }
                                    )
                                }
                            }
                        }

                        Spacer(modifier = Modifier.height(16.dp))

                        OutlinedTextField(
                            value = inputReviewText,
                            onValueChange = { inputReviewText = it },
                            label = { Text("Write something.") },
                            modifier = Modifier.fillMaxWidth(),
                            maxLines = 3
                        )
                    }
                },
                confirmButton = {
                    Button(
                        onClick = {
                            if (inputReviewText.isNotBlank()) {
                                scope.launch(Dispatchers.IO) {
                                    val currentUserName = user?.name ?: "Loading."

                                    val newReview = ReviewEntity(
                                        userId = userId,
                                        username = currentUserName,
                                        stallId = stallId,
                                        review = inputReviewText,
                                        rating = inputRating,
                                        date = LocalDate.now().toString()
                                    )
                                    reviewsDao.addReview(newReview)
                                    launch(Dispatchers.Main) { //LLM
                                        inputReviewText = ""
                                        inputRating = 5
                                        showDialog = false
                                    }
                                }
                            }
                        },
                        colors = ButtonDefaults.buttonColors(containerColor = Color.Black)
                    ) {
                        Text("Submit")
                    }
                },
                dismissButton = {
                    TextButton(onClick = { showDialog = false }) {
                        Text("Cancel", color = Color.Gray)
                    }
                }
            )
        }
    }
}

@Composable
fun ReviewCardItem(review: ReviewEntity) {
    val reviewBackgroundColor = when (review.rating) {
        1 -> Color(0xFFFAE0E3)
        5 -> Color(0xFFDCEFD9)
        else -> Color.White
    }
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(20.dp),
        colors = CardDefaults.cardColors(containerColor = reviewBackgroundColor),
        elevation = CardDefaults.cardElevation(defaultElevation = 6.dp)
    ) {
        Column(
            modifier = Modifier.padding(20.dp)
        ) {

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = review.username,
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold
                )

                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(
                        imageVector = Icons.Default.Star,
                        contentDescription = null,
                        tint = Color(0xFFF4B400),
                        modifier = Modifier.size(18.dp)
                    )
                    Spacer(modifier = Modifier.width(6.dp))
                    Text(
                        text = "${review.rating}/5",
                        style = MaterialTheme.typography.bodyMedium,
                        fontWeight = FontWeight.SemiBold
                    )
                }
            }

            Spacer(modifier = Modifier.height(12.dp))

            // Comment
            Text(
                text = review.review,
                style = MaterialTheme.typography.bodyLarge,
                lineHeight = 22.sp,
                modifier = Modifier.padding(bottom = 12.dp)
            )

            // Date
            Text(
                text = review.date.toString(),
                style = MaterialTheme.typography.labelSmall,
                color = Color.Gray.copy(alpha = 0.7f)
            )
        }
    }
}