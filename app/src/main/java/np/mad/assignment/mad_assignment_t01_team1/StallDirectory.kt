package np.mad.assignment.mad_assignment_t01_team1

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.FavoriteBorder
import androidx.compose.material3.*
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp

//Is it in main?
// Data class representing a food stall
data class FoodStall(
    val name: String,
    val cuisine: String,
    val imageResId: Int, // Resource ID for the image
    val description: String,
    val canteen: String
)

// Sample data for food stalls
fun getSampleFoodStalls(): List<FoodStall> {
    return listOf(
        FoodStall(
            name = "Mala Delicacy",
            cuisine = "Asian",
            imageResId = R.drawable.mala_xiangguo, // Replace with the drawable resource ID
            description = "Serving delicious mala xiang guo with a variety of ingredients.",
            canteen = "Food Club"
        ),
        FoodStall(
            name = "Western Delights",
            cuisine = "Italian",
            imageResId = R.drawable.western_food, // Replace with the drawable resource ID
            description = "Serving the best western dishes, including pastas, pizzas, and more.",
            canteen = "Munch"
        ),
        FoodStall(
            name = "Chicken Rice",
            cuisine = "Asian",
            imageResId = R.drawable.chicken_rice, // Replace with the drawable resource ID
            description = "A cozy chicken rice food stall offering tender chicken with fragrant rice.",
            canteen = "Makan Place"
        )
    )
}

// Main composable function to display the stall directory
@Composable
fun StallDirectoryScreen(canteen: Canteen, modifier: Modifier = Modifier) {
    // Filter food stalls based on the canteen's name
    val filteredStalls = getSampleFoodStalls().filter { stall ->
        stall.canteen == canteen.name // Filter based on the canteen name
    }

    LazyColumn(modifier = modifier.padding(16.dp)) {
        items(filteredStalls.size) { index ->
            StallCard(stall = filteredStalls[index])
        }
    }
}

// Composable for displaying individual stall information in a card
@Composable
fun StallCard(stall: FoodStall, modifier: Modifier = Modifier) {
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
        ) {
            // Image display with drawable resource and rounded corners
            Image(
                painter = painterResource(id = stall.imageResId),
                contentDescription = "Stall Image",
                modifier = Modifier
                    .fillMaxWidth()
                    .height(200.dp)
                    .padding(bottom = 8.dp)
                    .clip(RoundedCornerShape(16.dp)),
                contentScale = ContentScale.Crop
            )
            // Stall name, cuisine, and description
            Text(
                text = stall.name,
                style = MaterialTheme.typography.headlineSmall,
                color = Color.Black
            )
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = "Cuisine: ${stall.cuisine}",
                style = MaterialTheme.typography.bodyMedium,
                color = Color.Gray
            )
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = stall.description,
                style = MaterialTheme.typography.bodyMedium,
                color = Color.Black
            )
            // Favorite icon (heart)
            IconButton(
                onClick = { /* Handle favorite click */ },
                modifier = Modifier.align(Alignment.End)
            ) {
                Icon(
                    imageVector = Icons.Filled.FavoriteBorder,
                    contentDescription = "Favorite",
                    tint = Color.Red
                )
            }
        }
    }
}
