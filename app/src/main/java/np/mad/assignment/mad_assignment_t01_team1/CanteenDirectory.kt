package np.mad.assignment.mad_assignment_t01_team1

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.FavoriteBorder
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController

//Is it in main?
// Data class representing a canteen
data class Canteen(
    val name: String,
    val description: String,
    val imageResId: Int, // Resource ID for the image
)

// Sample data for canteens
fun getSampleCanteens(): List<Canteen> {
    return listOf(
        Canteen(
            name = "Food Club",
            description = "A spacious canteen with a variety of food options.",
            imageResId = R.drawable.food_club, // Replace with drawable resource ID
        ),
        Canteen(
            name = "Munch",
            description = "Quick bites and delicious snacks are available.",
            imageResId = R.drawable.munch, // Replace with drawable resource ID
        ),
        Canteen(
            name = "Makan Place",
            description = "A cozy place offering local and international dishes.",
            imageResId = R.drawable.makan_place, // Replace with drawable resource ID
        )
    )
}

// Main composable function to display the canteen directory
@Composable
fun CanteenDirectoryScreen(canteens: List<Canteen>, modifier: Modifier = Modifier, navController: NavController) {
    LazyColumn(modifier = modifier.padding(16.dp)) {
        items(canteens.size) { index ->
            CanteenCard(canteen = canteens[index], navController = navController)
        }
    }
}

// Composable for displaying individual canteen information in a card
@Composable
fun CanteenCard(canteen: Canteen, modifier: Modifier = Modifier, navController: NavController) {
    Card(
        modifier = modifier
            .fillMaxWidth()
            .padding(bottom = 16.dp),
        shape = RoundedCornerShape(16.dp),
    ) {
        Column(
            modifier = Modifier
                .padding(16.dp)
                .fillMaxWidth()
                .clickable {
                    // Navigate to the Stall Directory, passing the canteen name
                    navController.navigate("stallDirectory/${canteen.name}")
                }
        ) {
            // Image display with drawable resource and rounded corners
            Image(
                painter = painterResource(id = canteen.imageResId),
                contentDescription = "Canteen Image",
                modifier = Modifier
                    .fillMaxWidth()
                    .height(200.dp)
                    .padding(bottom = 8.dp)
                    .clip(RoundedCornerShape(16.dp)),
                contentScale = ContentScale.Crop
            )
            // Canteen name and description
            Text(
                text = canteen.name,
                style = MaterialTheme.typography.headlineSmall,
                color = Color.Black
            )
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = canteen.description,
                style = MaterialTheme.typography.bodyMedium,
                color = Color.Gray
            )
        }
    }
}

