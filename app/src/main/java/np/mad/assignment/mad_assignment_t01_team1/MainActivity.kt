package np.mad.assignment.mad_assignment_t01_team1

import android.content.Context
import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.runtime.*
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.lifecycleScope
import androidx.work.ExistingPeriodicWorkPolicy
import androidx.work.PeriodicWorkRequestBuilder
import androidx.work.WorkManager
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import np.mad.assignment.mad_assignment_t01_team1.data.db.AppDatabase
import np.mad.assignment.mad_assignment_t01_team1.data.db.seedMockData
import np.mad.assignment.mad_assignment_t01_team1.data.firebase.FirebaseTest
import np.mad.assignment.mad_assignment_t01_team1.data.sync.PeriodicSyncWorker
import np.mad.assignment.mad_assignment_t01_team1.ui.theme.MAD_Assignment_T01_Team1Theme
import java.util.concurrent.TimeUnit

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val db = AppDatabase.get(this)
        val prefs = getSharedPreferences("seed_prefs",MODE_PRIVATE)
        prefs.edit().remove("mock_seed_done").commit()
        if(!prefs.getBoolean("mock_seed_done",false)){
            lifecycleScope.launch(Dispatchers.IO){
                db.clearAllTables()
                seedMockData(db)
                prefs.edit().putBoolean("mock_seed_done",true).apply()
                Log.d("Seed", "IF Runned")
            }
        }
        enableEdgeToEdge()
        WorkManager.getInstance(this).enqueueUniquePeriodicWork(
            "SyncWorker",
            ExistingPeriodicWorkPolicy.KEEP,
            PeriodicWorkRequestBuilder<PeriodicSyncWorker>(
                15, TimeUnit.SECONDS
            ).build()
        )
        //FirebaseTest.test()
        setContent {
            MAD_Assignment_T01_Team1Theme {
                val context = LocalContext.current

                var loggedInUserId by remember {
                    mutableStateOf(
                        context.getSharedPreferences("auth", Context.MODE_PRIVATE)
                            .getLong("logged_in_user", -1L)
                    )
                }
                var userRole by remember {
                    mutableStateOf(
                        context.getSharedPreferences("auth", Context.MODE_PRIVATE)
                            .getString("user_role", "USER")
                    )
                }
                if (loggedInUserId == -1L && userRole == "USER") {
                    LoginScreen(
                        onLoginSuccess = { newId, role ->
                            loggedInUserId = newId
                            userRole = role
                        },
                        onRegisterClick = {
                            val intent = android.content.Intent(context, RegisterActivity::class.java)
                            context.startActivity(intent)
                        }
                    )
                } else {
                    MainNavigation(
                        userId = loggedInUserId,
                        userRole = userRole ?: "USER",
                        onLogout = {
                            context.getSharedPreferences("auth", Context.MODE_PRIVATE)
                                .edit().clear().apply()
                            loggedInUserId = -1L
                            userRole = "USER"
                        }
                    )
                }
            }
        }
    }
}