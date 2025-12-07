package np.mad.assignment.mad_assignment_t01_team1

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.FavoriteBorder
import androidx.compose.material3.*
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import kotlinx.coroutines.launch
import np.mad.assignment.mad_assignment_t01_team1.data.db.AppDatabase
import np.mad.assignment.mad_assignment_t01_team1.data.entity.FavoriteEntity
import np.mad.assignment.mad_assignment_t01_team1.data.entity.ReviewEntity
import np.mad.assignment.mad_assignment_t01_team1.data.entity.StallEntity

//Is it in main?
// Data class representing a food stall
data class FoodStall(
    val stallId: Long,
    val name: String,
    val cuisine: String,
    val imageResId: Int, // Resource ID for the image
    val description: String,
    val canteen: String
)
fun StallEntity.toFoodStall(): FoodStall {
    return FoodStall(
        stallId = this.stallId,
        name = this.name,
        cuisine = this.cuisine,
        imageResId = this.imageResId,
        description = this.description,
        canteen = "" // not needed since we use canteenId now
    )
}


// Main composable function to display the stall directory
@Composable
fun StallDirectoryScreen(
    userId: Long,
    canteen: Canteen,
    modifier: Modifier = Modifier,
    navController: NavController
) {
    val context= LocalContext.current
    val  db =remember(context){
        AppDatabase.get(context)}
    val stallsDao = remember { db.stallDao() }
    val favoriteDao = remember { db.favoriteDao() }
    val scope = rememberCoroutineScope()
    val stallsFlow = stallsDao.getByCanteenName(canteen.name)
    val stalls by stallsFlow.collectAsState(initial = emptyList())    // Filter food stalls based on the canteen's name
    val favoriteStallIds by favoriteDao.getFavoriteStallIdsForUser(userId).collectAsState(initial = emptyList<Long>())
    LazyColumn(modifier = modifier.padding(16.dp)) {
        item {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                IconButton(onClick = { navController.popBackStack() }) {
                    Icon(
                        imageVector = Icons.Default.ArrowBack,
                        contentDescription = "Back",
                        tint = Color.Black
                    )
                }

                Text(
                    text = canteen.name,
                    style = MaterialTheme.typography.headlineSmall,
                    modifier = Modifier.padding(start = 8.dp)
                )
            }
        }
        items(stalls.size) { index ->
            val stall = stalls[index].toFoodStall()
            val isFavorite = favoriteStallIds.contains(stall.stallId)

            StallCard(
                stall = stall,
                isFavorite = isFavorite,
                onStallClick = { stallId ->
                    navController.navigate("menu/$stallId")
                },
                onFavoriteClick = {
                    scope.launch {
                        if (isFavorite) {
                            favoriteDao.removeFavoriteByUserAndStall(userId, stall.stallId)
                        } else {
                            favoriteDao.addFavorite(
                                FavoriteEntity(userId = userId, stallId = stall.stallId)
                            )
                        }
                    }
                }
            )
        }
    }
}

// Composable for displaying individual stall information in a card
@Composable
fun StallCard(
    stall: FoodStall,
    isFavorite: Boolean,
    modifier: Modifier = Modifier,
    onStallClick: (Long) -> Unit,
    onFavoriteClick: () -> Unit
) {
    Card(
        onClick = { onStallClick(stall.stallId) },
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
            IconButton(
                onClick = { onFavoriteClick() },
                modifier = Modifier.align(Alignment.End)
            ) {
                Icon(
                    imageVector = Icons.Default.Favorite,

                    contentDescription = "Favorite",

                    tint = if (isFavorite) Color.Red else Color.LightGray
                )
            }
        }
    }
}
