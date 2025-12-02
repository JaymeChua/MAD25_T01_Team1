package np.mad.assignment.mad_assignment_t01_team1

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.lifecycle.lifecycleScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import np.mad.assignment.mad_assignment_t01_team1.data.db.AppDatabase
import np.mad.assignment.mad_assignment_t01_team1.data.db.seedMockData
import np.mad.assignment.mad_assignment_t01_team1.ui.theme.MAD_Assignment_T01_Team1Theme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val db = AppDatabase.get(this)

        val prefs = getSharedPreferences("seed_prefs",MODE_PRIVATE)
        if(!prefs.getBoolean("mock_seed_done",false)){
            lifecycleScope.launch(Dispatchers.IO){
                seedMockData(db)
                prefs.edit().putBoolean("mock_seed_done",true).apply()
            }
        }
        enableEdgeToEdge()
        setContent {
            MAD_Assignment_T01_Team1Theme{
                MainNavigation()
            }
        }
    }
}
