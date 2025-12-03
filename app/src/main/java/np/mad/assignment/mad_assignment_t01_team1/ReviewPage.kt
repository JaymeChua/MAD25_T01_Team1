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
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

data class Review(
    val name: String,
    val comment: String,
    val rating: String,
    val date: String
)

class ReviewActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            MaterialTheme {
                ReviewPage()
            }
        }
    }
}

@Composable
fun ReviewPage(
    onCloseClicked: () -> Unit = {},
) {
    // Mock Data based on your image
    val reviews = listOf(
        Review("KaiJie", "Yoo this food is bussin. Unc locked in", "5/5", "23/11/2025"),
        Review("JieKai", "It was half-uncooked bro. This uncle trolling", "1/5", "22/11/2025")
    )

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
    }
}

@Composable
fun ReviewCardItem(review: Review) {
    val reviewBackgroundColor = when (review.rating) {
        "1/5" -> Color(0xFFFAE0E3)
        "5/5" -> Color(0xFFDCEFD9)
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
                    text = review.name,
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
                        text = review.rating,
                        style = MaterialTheme.typography.bodyMedium,
                        fontWeight = FontWeight.SemiBold
                    )
                }
            }

            Spacer(modifier = Modifier.height(12.dp))

            // Comment
            Text(
                text = review.comment,
                style = MaterialTheme.typography.bodyLarge,
                lineHeight = 22.sp,
                modifier = Modifier.padding(bottom = 12.dp)
            )

            // Date
            Text(
                text = review.date,
                style = MaterialTheme.typography.labelSmall,
                color = Color.Gray.copy(alpha = 0.7f)
            )
        }
    }
}