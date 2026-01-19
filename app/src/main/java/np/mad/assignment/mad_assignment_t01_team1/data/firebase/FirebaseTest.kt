
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
        val col = db.collection("test_connection")
        val doc = col.document() // random ID
        val payload = mapOf(
            "message" to "Hello Firebase!",
            "ts" to System.currentTimeMillis()
        )

        doc.set(payload)
            .addOnSuccessListener {
                Log.d("FIREBASE_TEST", "WRITE_OK docId=${doc.id}")
                doc.get()
                    .addOnSuccessListener { snap ->
                        Log.d("FIREBASE_TEST", "READ_OK exists=${snap.exists()} data=${snap.data}")
                    }
            }

    }
}