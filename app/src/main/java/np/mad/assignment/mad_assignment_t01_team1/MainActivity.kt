package np.mad.assignment.mad_assignment_t01_team1

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import np.mad.assignment.mad_assignment_t01_team1.ui.theme.MAD_Assignment_T01_Team1Theme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            MAD_Assignment_T01_Team1Theme{
                MainNavigation()
            }
        }
    }
}