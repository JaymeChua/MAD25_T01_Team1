package np.mad.assignment.mad_assignment_t01_team1.util

import java.security.MessageDigest

object SecurityUtils {

    fun sha256(input: String): String {
        val bytes = MessageDigest.getInstance("SHA-256").digest(input.toByteArray())
        return bytes.joinToString("") { "%02x".format(it) }
    }
}
