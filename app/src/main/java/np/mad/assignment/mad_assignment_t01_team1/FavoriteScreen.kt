package np.mad.assignment.mad_assignment_t01_team1
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.outlined.Star
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedCard
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.contentColorFor
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.launch
import np.mad.assignment.mad_assignment_t01_team1.data.db.AppDatabase
import np.mad.assignment.mad_assignment_t01_team1.model.CanteenGroupUi
import np.mad.assignment.mad_assignment_t01_team1.model.FavoriteStallUi
import np.mad.assignment.mad_assignment_t01_team1.ui.theme.MAD_Assignment_T01_Team1Theme
import androidx.compose.material3.*


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FavoriteScreen(
    userId: Long,
    onStallClick: (FavoriteStallUi) -> Unit = {},
    onLoginClick:()-> Unit
){
    val context= LocalContext.current
    val  db =remember(context){
        AppDatabase.get(context)}
    val favoritesDao = remember { db.favoriteDao() }
    val scope = rememberCoroutineScope()


    val favorites: List<FavoriteStallUi> by favoritesDao.getFavoriteStallsForUser(userId).collectAsState(initial = emptyList())


    val groups: List<CanteenGroupUi> = remember(favorites){
        favorites
            .groupBy {it.canteenId to it.canteenName}
            .map{(key,grouped)->
                val(canteenId,canteenName) = key
                CanteenGroupUi(
                    canteenId = canteenId,
                    canteenName = canteenName,
                    stalls =  grouped.sortedBy {it.stallName}
                )
            }.sortedBy {it.canteenName}
    }


    var expanded by remember(groups) { mutableStateOf(groups.map {it.canteenId }.toSet())}
    Box(Modifier.fillMaxSize()){
        Row(
            modifier = Modifier.align(Alignment.TopStart).padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ){
            Icon(
                imageVector =  Icons.Filled.Favorite,
                contentDescription = "Favorites",
                tint = MaterialTheme.colorScheme.primary
            )
            Spacer(Modifier.width(8.dp))
            Text(
                text = "Favorites",
                style = MaterialTheme.typography.titleLarge,

            )
        }
        if(userId <= 0L){
            NotLoggedInState(
                title = "Sign in to view your favorites",
                subtitle = "Create an account or sign in to save and sync favorites.",
                onLoginClick = onLoginClick
            )
            return@Box
        }
        if (favorites.isEmpty()){
            EmptyState(
                title = "No favorites yet",
                subtitle = "Tap on the ☆ on a stall to save it here.",
            )
            return
        }
    }


    LazyColumn(
        modifier = Modifier.fillMaxSize().padding(top = 56.dp,),
        contentPadding = PaddingValues(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        items(groups, {it.canteenId}){group->
            Card(
                modifier = Modifier.fillMaxSize().padding(bottom = 16.dp),
                shape = RoundedCornerShape(12.dp)
            ){
                val headerColor = canteenColorOf(group.canteenName)
                val headerContentColor = contentColorFor(headerColor)

                Row(
                    modifier = Modifier.fillMaxWidth().clip(RoundedCornerShape(topStart = 12.dp, topEnd = 12.dp)).background(headerColor).clickable{
                        expanded = if (expanded.contains(group.canteenId)){
                            expanded - group.canteenId
                        }
                        else {
                            expanded + group.canteenId
                        }
                    }.padding(horizontal = 16.dp, vertical = 12.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = group.canteenName,
                        style = MaterialTheme.typography.titleMedium,
                        color = headerContentColor,
                        modifier = Modifier.weight(1f)
                    )
                }
                    if (expanded.contains(group.canteenId)){
                        Column(
                            modifier = Modifier.fillMaxWidth().padding(12.dp),
                            verticalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            group.stalls.forEach { stall ->
                                StallItem(
                                    stall = stall,
                                    onClick = {onStallClick(stall)},
                                    onUnfavorite = {
                                        scope.launch {
                                            favoritesDao.removeFavoriteById(stall.favoriteId)
                                        }
                                    }

                                )
                            }
                        }
                        }
                    }
                }
            }
        }





@Composable
private fun StallItem(
    stall: FavoriteStallUi,
    onClick: ()-> Unit,
    onUnfavorite: () -> Unit
){
    OutlinedCard(
        onClick = onClick,
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(10.dp),
        colors = CardDefaults.outlinedCardColors(
            containerColor = MaterialTheme.colorScheme.surface
        )
    ){
    Row(
        modifier = Modifier.fillMaxWidth().padding(horizontal = 12.dp, vertical = 10.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Column(Modifier.weight(1f)) {
            Text(
                text = stall.stallName,
                style = MaterialTheme.typography.bodyLarge,
            )
            if(stall.halal){
                Text(
                    text = "Halal",
                    style = MaterialTheme.typography.bodyMedium
                )
            }
        }

        IconButton(onClick = onUnfavorite) {
            Icon(
                imageVector =  Icons.Filled.Favorite,
                contentDescription = "Remove from favorites",
                tint = MaterialTheme.colorScheme.primary
            )
        }
        }
    }
}
@Composable
private fun canteenColorOf(canteenName: String): Color{
    return when (canteenName){
        "Food Club" -> Color(0xFF42A5F5)
        "Makan Place" -> Color(0xFFFF7043)
        "Munch"  -> Color(0xFF66BB6A)
        else -> MaterialTheme.colorScheme.primaryContainer
    }
}


@Composable
private fun EmptyState(title: String, subtitle: String) {
    Column(
        modifier = Modifier.fillMaxSize().padding(24.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(title, style = MaterialTheme.typography.titleMedium)
        Spacer(Modifier.height(8.dp))
        Text(subtitle, style = MaterialTheme.typography.bodyMedium)
        Spacer(Modifier.height(8.dp))
    }
}

@Composable
private fun NotLoggedInState(
    title: String,
    subtitle: String,
    onLoginClick: () -> Unit
){
    Column(
        modifier = Modifier.fillMaxSize().padding(24.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(title, style = MaterialTheme.typography.titleMedium)
        Spacer(Modifier.height(8.dp))
        Text(subtitle, style = MaterialTheme.typography.bodyMedium)
        Spacer(Modifier.height(16.dp))
        Button(onLoginClick) {
            Text("Sign in")
        }
    }
}