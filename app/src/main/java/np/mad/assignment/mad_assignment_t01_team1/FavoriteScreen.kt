package np.mad.assignment.mad_assignment_t01_team1
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Star
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedCard
import androidx.compose.material3.Text
import androidx.compose.material3.contentColorFor
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import np.mad.assignment.mad_assignment_t01_team1.ui.theme.MAD_Assignment_T01_Team1Theme


class FavoriteActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            MAD_Assignment_T01_Team1Theme { FavoriteScreen()
            }
        }
    }
}

data class FavoriteStall(
    val favoriteId: Long,
    val stallId: Long,
    val stallName: String,
    val canteenId: Long,
    val canteenName: String,
    val photoUrl: String? = null,
)
data class CanteenGroup(
    val canteenId: Long,
    val canteenName: String,
    val stalls: List<FavoriteStall>
)


@Composable
fun FavoriteScreen(
    onStallClick: (FavoriteStall) -> Unit = {},
    onUnfavorite: (FavoriteStall) -> Unit = {}
){
    var favorites by remember { mutableStateOf(
        listOf(
            FavoriteStall(1L,101L,"Hainan Chicken Rice",10L,"Food Club"),
            FavoriteStall(2L,202L,"Mala Hotpot",20L,"Makan Place"),
            FavoriteStall(3L,303L,"Ban Mian",10L,"Food Club"),
            FavoriteStall(4L,404L,"Western",30L,"Munch"),

            )
    )
    }
    val  groups = remember(favorites) {
        favorites.groupBy { it.canteenId }
            .map { (id,stalls) ->
                CanteenGroup(
                    canteenId = id,
                    canteenName = stalls.first().canteenName,
                    stalls = stalls.sortedBy { it.stallName }
                )
            }
    }
    var expanded by remember { mutableStateOf(groups.map {it.canteenId }.toSet())}
    if (favorites.isEmpty()){
        EmptyState(
            title = "No favorites yet",
            subtitle = "Tap on the ☆ on a stall to save it here."
        )
    }
    else {
    LazyColumn(
        modifier = Modifier.fillMaxSize().padding(top = 16.dp),
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
                                        favorites = favorites.filter { it.favoriteId != stall.favoriteId }
                                        onUnfavorite(stall)
                                    }

                                )
                            }
                        }
                        }
                    }
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
private fun StallItem(
    stall: FavoriteStall,
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

        Text(
            text = stall.stallName,
            style = MaterialTheme.typography.bodyLarge,
            modifier = Modifier.weight(1f)
        )
        IconButton(
            onClick = onUnfavorite
        ) {
            Icon(
                imageVector = Icons.Outlined.Star,
                contentDescription = "Remove from favorites",
                tint = MaterialTheme.colorScheme.primary
            )
        }
        }
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
    }
}
