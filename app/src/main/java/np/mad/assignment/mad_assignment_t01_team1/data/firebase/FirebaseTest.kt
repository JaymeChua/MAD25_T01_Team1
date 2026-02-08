
package np.mad.assignment.mad_assignment_t01_team1.data.firebase

import android.util.Log
import com.google.firebase.Firebase
import com.google.firebase.FirebaseApp
import com.google.firebase.firestore.firestore

object FirebaseTest {

    fun test() {
        // 1) Log the resolved Firebase project
        val app = FirebaseApp.getInstance()
        val opts = app.options
        Log.d(
            "FIREBASE_TEST",
            "ProjectId=${opts.projectId}, AppId=${opts.applicationId}, DBUrl=${opts.databaseUrl}"
        )

        // 2) Do a write, then read the same doc back

        val db = Firebase.firestore
        val doc = db.collection("test_connection").document()
        val payload = mapOf("message" to "Hello Firebase!", "ts" to System.currentTimeMillis())

        doc.set(payload)
            .addOnSuccessListener {
                Log.d("FIREBASE_TEST", "WRITE_OK id=${doc.id}")
                doc.get()
                    .addOnSuccessListener { snap ->
                        Log.d("FIREBASE_TEST", "READ_OK exists=${snap.exists()} data=${snap.data}")
                    }
                    .addOnFailureListener { e ->
                        Log.e("FIREBASE_TEST", "READ_FAIL ${e.javaClass.simpleName}: ${e.message}", e)
                    }
            }
            .addOnFailureListener { e ->
                Log.e("FIREBASE_TEST", "WRITE_FAIL ${e.javaClass.simpleName}: ${e.message}", e)
            }



    }
}