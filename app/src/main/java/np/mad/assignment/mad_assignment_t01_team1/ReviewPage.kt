package np.mad.assignment.mad_assignment_t01_team1


import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
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
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.time.LocalDate

//class ReviewActivity : ComponentActivity() {
//    override fun onCreate(savedInstanceState: Bundle?) {
//        super.onCreate(savedInstanceState)
//        setContent {
//            MaterialTheme {
//                ReviewPage()
//            }
//        }
//    }
//}

@Composable
fun ReviewPage(
    stallId: Long,
    onCloseClicked: () -> Unit = {},
) {
    val context= LocalContext.current
    val  db =remember(context){
        AppDatabase.get(context)}
    val reviewsDao = remember { db.reviewDao() }
    val scope = rememberCoroutineScope()
    val reviews: List<ReviewEntity> by reviewsDao.getAllReviewsForStall(stallId).collectAsState(initial = emptyList())

    var showDialog by rememberSaveable { mutableStateOf(false) }
    var inputReviewText by rememberSaveable { mutableStateOf("") }
    var inputRating by rememberSaveable { mutableIntStateOf(5) }

    // Main Container
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFFF5F5F5))
    ) {

        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
        ) {

            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(250.dp),
                contentAlignment = Alignment.Center
            ) {
                Image(
                    painter = painterResource(R.drawable.pngegg3),
                    contentDescription = null,
//                    contentScale = ContentScale.FillBounds,
                    modifier = Modifier.fillMaxSize().fillMaxWidth())
                //Text("Food Image Here", color = Color.White)
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
                        text = "3/5 (2 reviews)",
                        fontWeight = FontWeight.Bold,
                        fontSize = 14.sp
                    )
                }

                Spacer(modifier = Modifier.height(8.dp))

                Text(
                    text = "Chicken Rice",
                    fontSize = 28.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.Black
                )

                Text(
                    text = "Bussin chicken rice -Uncle",
                    fontSize = 16.sp,
                    color = Color.Gray,
                    modifier = Modifier.padding(top = 4.dp, bottom = 24.dp)
                )

                reviews.forEach { review ->
                    ReviewCardItem(review)
                    Spacer(modifier = Modifier.height(24.dp))
                }
            }
        }

        IconButton(
            onClick = { onCloseClicked() },
            modifier = Modifier
                .padding(16.dp)
                .align(Alignment.TopStart)
                .background(Color.White, shape = CircleShape)
                .size(40.dp)
                .padding(4.dp) // Inner padding
        ) {
            Icon(Icons.Default.Close, contentDescription = "Close", tint = Color.Gray)
        }
        FloatingActionButton(
            onClick = { showDialog = true },
            containerColor = Color(0xFFF4B400),
            contentColor = Color.White,
            modifier = Modifier
                .align(Alignment.BottomEnd)
                .padding(24.dp)
        ) {
            Row(modifier = Modifier.padding(horizontal = 16.dp)) {
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
                                IconButton(onClick = { inputRating = i }) {
                                    Icon(
                                        imageVector = if (i <= inputRating) Icons.Default.Star else Icons.Default.Star,
                                        contentDescription = "Star $i",
                                        tint = Color(0xFFF4B400)
                                    )
                                }
                            }
                        }

                        Spacer(modifier = Modifier.height(16.dp))

                        OutlinedTextField(
                            value = inputReviewText,
                            onValueChange = { inputReviewText = it },
                            label = { Text("Share your thoughts...") },
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
                                    // TODO: Later, get the REAL User ID and Name from login session
                                    val currentUserId = 1L
                                    val currentUserName = "demo"

                                    val newReview = ReviewEntity(
                                        userId = currentUserId,
                                        username = currentUserName,
                                        stallId = stallId,
                                        review = inputReviewText,
                                        rating = inputRating,
                                        date = LocalDate.now()
                                    )
                                    reviewsDao.addReview(newReview)
                                }
                                // Reset and Close
                                inputReviewText = ""
                                inputRating = 5
                                showDialog = false
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