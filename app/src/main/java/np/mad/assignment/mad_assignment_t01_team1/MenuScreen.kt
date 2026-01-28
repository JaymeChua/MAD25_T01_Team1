package np.mad.assignment.mad_assignment_t01_team1

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.GridItemSpan
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowRight
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material.icons.filled.KeyboardArrowRight
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.Star
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
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import np.mad.assignment.mad_assignment_t01_team1.data.db.AppDatabase
import np.mad.assignment.mad_assignment_t01_team1.data.entity.DishEntity
import np.mad.assignment.mad_assignment_t01_team1.data.entity.ReviewEntity
import np.mad.assignment.mad_assignment_t01_team1.data.entity.StallEntity







@Composable
fun MenuScreen(
    stallId: Long,
    onBackClick: () -> Unit = {},
    onReviewClick: () -> Unit
) {
    val context= LocalContext.current
    val  db =remember(context){
        AppDatabase.get(context)}
    val dishDao = remember { db.dishDao() }
    val reviewsDao = remember { db.reviewDao() }
    val stallDao = remember {db.stallDao()}
    val stall by stallDao.getByIdFlow(stallId).collectAsState(initial = null)
    val dishes: List<DishEntity> by dishDao.getAllDishesForStall(stallId).collectAsState(initial = emptyList())
    val reviews: List<ReviewEntity> by reviewsDao.getAllReviewsForStall(stallId).collectAsState(initial = emptyList())
    val reviewCount = reviews.size
    val averageRating = if (reviewCount > 0) {
        reviews.map { it.rating }.average()
    } else {
        0.0
    }
    LazyVerticalGrid(
        columns = GridCells.Fixed(2),
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFFFAFAFA)),
        contentPadding = PaddingValues(bottom = 16.dp),
        horizontalArrangement = Arrangement.spacedBy(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        item(span = { GridItemSpan(2) }) {
            Box {
                Image(
                    painter = painterResource(id = stall?.imageResId ?: R.drawable.ayampenyat),
                    contentDescription = "Stall Header",
                    contentScale = ContentScale.Crop,
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(250.dp)
                )
                //Back Button to Stall Page
                SmallFloatingActionButton(
                    onClick = onBackClick,
                    modifier = Modifier
                        .padding(top = 40.dp, start = 16.dp)
                        .align(Alignment.TopStart),
                    containerColor = Color.White,
                    contentColor = Color(0xFFF4B400)
                ) {
                    Icon(imageVector = Icons.Default.ArrowBack, contentDescription = "Back")
                }

                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 200.dp, start = 16.dp, end = 16.dp)
                ) {
                    Card(
                        colors = CardDefaults.cardColors(containerColor = Color.White),
                        elevation = CardDefaults.cardElevation(defaultElevation = 6.dp),
                        shape = RoundedCornerShape(12.dp)
                    ) {
                        Column(
                            modifier = Modifier.padding(16.dp),
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                                CategoryChip(text = "Price Range: $2.50-$5.00", color = Color(0xFFFFF3E0))
                                CategoryChip(
                                    text = stall?.cuisine ?: "Loading...",
                                    color = Color(0xFFE8F5E9)
                                )                            }

                            Spacer(modifier = Modifier.height(8.dp))

                            Text(
                                text = stall?.name ?: "Loading...",
                                style = MaterialTheme.typography.headlineSmall,
                                fontWeight = FontWeight.Bold
                            )

                            Spacer(modifier = Modifier.height(16.dp))

                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceEvenly
                            ) {
                                InfoItem(icon = Icons.Default.DateRange, text = "9am - 6pm")
                                InfoItem(icon = Icons.Default.LocationOn, stall?.canteenName ?: "Loading...")
                            }

                            Spacer(modifier = Modifier.height(12.dp))

                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .clickable { onReviewClick() }
                                    .padding(16.dp),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Row(verticalAlignment = Alignment.CenterVertically) {
                                    Icon(
                                        imageVector = Icons.Default.Star,
                                        contentDescription = "Rating",
                                        tint = Color(0xFFFFC107),
                                        modifier = Modifier.size(24.dp)
                                    )
                                    Spacer(modifier = Modifier.width(8.dp))
                                    Text(
                                        text = String.format("%.1f(%d reviews)", averageRating, reviewCount),
                                        style = MaterialTheme.typography.bodyLarge,
                                        fontWeight = FontWeight.Normal,
                                        color = Color.Black
                                    )
                                }

                                Icon(
                                    imageVector = Icons.Default.KeyboardArrowRight,
                                    contentDescription = "View Details",
                                    tint = Color.Gray
                                )
                            }
                        }
                    }
                }
            }
        }

        item(span = { GridItemSpan(2) }) {
            Text(
                text = "For You",
                style = MaterialTheme.typography.titleLarge,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.padding(horizontal = 16.dp)
            )
        }

        items(dishes) { dish ->
            Box(modifier = Modifier.padding(horizontal = 8.dp)) {
                DishCard(dish)
            }
        }
    }
}

@Composable
fun DishCard(dish: DishEntity) {
    Card(
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
        shape = RoundedCornerShape(12.dp),
        modifier = Modifier.fillMaxWidth()
    ) {
        Column {
            Image(
                painter = painterResource(id = dish.imageResId),
                contentDescription = dish.dishName,
                contentScale = ContentScale.Crop,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(120.dp)
                    .background(Color.LightGray)
            )

            Column(modifier = Modifier.padding(12.dp)) {
                Text(
                    text = dish.dishName,
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )

                Spacer(modifier = Modifier.height(4.dp))

                Text(
                    text = "from S$ ${dish.dishPrice}",
                    style = MaterialTheme.typography.bodyMedium,
                    color = Color.DarkGray
                )

            }
        }
    }
}

@Composable
fun InfoItem(icon: androidx.compose.ui.graphics.vector.ImageVector, text: String) {
    Row(verticalAlignment = Alignment.CenterVertically) {
        Icon(
            imageVector = icon,
            contentDescription = null,
            modifier = Modifier.size(16.dp),
            tint = Color.Black
        )
        Spacer(modifier = Modifier.width(4.dp))
        Text(text = text, style = MaterialTheme.typography.bodySmall, color = Color.Black)
    }
}

@Composable
fun CategoryChip(text: String, color: Color) {
    Surface(
        color = color,
        shape = RoundedCornerShape(16.dp)
    ) {
        Text(
            text = text,
            modifier = Modifier.padding(horizontal = 12.dp, vertical = 4.dp),
            style = MaterialTheme.typography.labelSmall,
            color = Color.Black
        )
    }
}