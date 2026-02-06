    package np.mad.assignment.mad_assignment_t01_team1

    import androidx.compose.foundation.Image
    import androidx.compose.foundation.background
    import androidx.compose.foundation.clickable
    import androidx.compose.foundation.layout.*
    import androidx.compose.foundation.lazy.LazyColumn
    import androidx.compose.foundation.lazy.LazyRow
    import androidx.compose.foundation.lazy.items
    import androidx.compose.foundation.shape.RoundedCornerShape
    import androidx.compose.material.icons.Icons
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
    import coil.compose.AsyncImage
    import np.mad.assignment.mad_assignment_t01_team1.data.db.AppDatabase // Check your imports
    import np.mad.assignment.mad_assignment_t01_team1.data.entity.CanteenEntity
    import np.mad.assignment.mad_assignment_t01_team1.data.entity.StallEntity
    import java.io.File

    // Colors from your design
    val HeaderBlue = Color(0xFF0F3460)
    val CardOrange = Color(0xFFFFA726)

    @Composable
    fun HomeScreen(
        onNavigateToCanteens: (String) -> Unit,
        onNavigateToStall: (Long) -> Unit,
    ) {
        val context= LocalContext.current
        val  db =remember(context){
            AppDatabase.get(context)}
        val canteenDao = remember { db.canteenDao() }
        val stallDao = remember { db.stallDao() }


        val canteens by canteenDao.getALL().collectAsState(initial = emptyList())

        val popularStalls by stallDao.getStallsOrderedByPopularity().collectAsState(initial = emptyList())

        val cuisines by stallDao.getAllCuisines().collectAsState(initial = emptyList())

        Scaffold(

        ) { padding ->
            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(padding)
                    .padding(horizontal = 16.dp),
                verticalArrangement = Arrangement.spacedBy(24.dp)
            ) {

                item {
                    SectionHeader(title = "Popular Canteens", onSeeMore = { })
                    Spacer(modifier = Modifier.height(8.dp))

                    // Show the first canteen as the "Featured" one
                    if (canteens.isNotEmpty()) {
                        FeaturedCanteenCard(
                            canteen = canteens.first(),
                            onClick = { onNavigateToCanteens(canteens.first().name) }
                        )
                    }
                }

                item {
                    SectionHeader(title = "Popular Foods", onSeeMore = {onNavigateToCanteens("Food Club")})
                    Spacer(modifier = Modifier.height(8.dp))
                    LazyRow(horizontalArrangement = Arrangement.spacedBy(16.dp)) {
                        items(popularStalls) { stall ->
                            PopularFoodItem(stall = stall, onClick = { onNavigateToStall(stall.stallId) })
                        }
                    }
                }

                item {
                    SectionHeader(title = "Popular Categories", onSeeMore = {onNavigateToCanteens("Food Club")})
                    Spacer(modifier = Modifier.height(8.dp))
                    LazyRow(horizontalArrangement = Arrangement.spacedBy(16.dp)) {
                        items(cuisines) { cuisine ->
                            val matchingStall = popularStalls.find { it.cuisine == cuisine }

                            val imageToDisplay = matchingStall?.imageResId ?: R.drawable.chickencutlet

                            CategoryItem(cuisineName = cuisine, imageResId = imageToDisplay)
                        }
                    }
                }

                item { Spacer(modifier = Modifier.height(50.dp)) }
            }
        }
    }


    @Composable
    fun SectionHeader(title: String, onSeeMore: () -> Unit) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(text = title, fontSize = 20.sp, fontWeight = FontWeight.Bold)
            Text(
                text = "See more >",
                fontSize = 14.sp,
                modifier = Modifier.clickable { onSeeMore() }
            )
        }
    }

    @Composable
    fun FeaturedCanteenCard(canteen: CanteenEntity, onClick: () -> Unit) {
        Card(
            shape = RoundedCornerShape(16.dp),
            modifier = Modifier
                .fillMaxWidth()
                .height(180.dp)
                .clickable { onClick() }
        ) {
            Box(modifier = Modifier.fillMaxSize()) {
                Image(
                    painter = painterResource(id = R.drawable.food_club),
                    contentDescription = null,
                    contentScale = ContentScale.Crop,
                    modifier = Modifier.fillMaxSize()
                )

                Text(
                    text = canteen.name,
                    color = Color.White,
                    fontSize = 28.sp,
                    fontWeight = FontWeight.ExtraBold,
                    modifier = Modifier
                        .align(Alignment.BottomStart)
                        .padding(16.dp)
                )
            }
        }
    }

    @Composable
    fun PopularFoodItem(stall: StallEntity, onClick: () -> Unit) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier.clickable { onClick() }
        ) {
            Card(
                shape = RoundedCornerShape(12.dp),
                colors = CardDefaults.cardColors(containerColor = CardOrange),
                modifier = Modifier.size(100.dp)
            ) {
                val imageModel = if (stall.imagePath != null) {
                    File(stall.imagePath)
                } else {
                    stall.imageResId ?: R.drawable.ic_launcher_foreground
                }

                AsyncImage(
                    model = imageModel,
                    contentDescription = "Stall Image",
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(200.dp)
                        .padding(bottom = 8.dp)
                        .clip(RoundedCornerShape(16.dp))
                        .background(Color.Gray),
                    contentScale = ContentScale.Crop,
                    error = painterResource(R.drawable.ic_launcher_foreground)
                )
            }
            Spacer(modifier = Modifier.height(4.dp))
            Text(text = stall.name, fontWeight = FontWeight.Medium, fontSize = 14.sp)
            Text(text = stall.cuisine, color = Color.Gray, fontSize = 12.sp)
        }
    }

    @Composable
    fun CategoryItem(cuisineName: String, imageResId: Int) {
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            Card(
                shape = RoundedCornerShape(12.dp),
                colors = CardDefaults.cardColors(containerColor = CardOrange.copy(alpha = 0.8f)),
                modifier = Modifier.size(100.dp)
            ) {
                // Using a generic image for categories, or logic to pick one based on name
                Image(
                    painter = painterResource(id = imageResId),
                    contentDescription = cuisineName,
                    contentScale = ContentScale.Crop,
                    modifier = Modifier.fillMaxSize()
                )
            }
            Spacer(modifier = Modifier.height(4.dp))
            Text(text = cuisineName, fontWeight = FontWeight.Medium, fontSize = 14.sp)
        }
    }