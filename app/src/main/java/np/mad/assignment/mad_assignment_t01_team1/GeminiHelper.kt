package np.mad.assignment.mad_assignment_t01_team1

import android.graphics.Bitmap
import android.util.Log
import np.mad.assignment.mad_assignment_t01_team1.BuildConfig
import com.google.ai.client.generativeai.GenerativeModel
import com.google.ai.client.generativeai.type.content
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import org.json.JSONArray
import kotlin.math.roundToInt

data class ScannedDish(
    val name: String,
    val price: String,
    val croppedBitmap: Bitmap
)

object MenuScanner {

    private const val API_KEY = BuildConfig.GEMINI_API_KEY

    suspend fun scanAndCropMenu(fullMenuBitmap: Bitmap): List<ScannedDish> {
        return withContext(Dispatchers.IO) {
            val generativeModel = GenerativeModel(
                modelName = "gemini-2.5-flash-lite",
                apiKey = API_KEY
            )
            Log.d("GEMINI", "Response: ${fullMenuBitmap}")
            val prompt = """
                Analyze this menu image. Identify each distinct dish that has a visible photo.
                Return a JSON ARRAY. For each dish, provide:
                - "name": Dish name
                - "price": Price (as string, e.g. "4.50")
                - "box": The bounding box of the DISH IMAGE. 
                  Format: [ymin, xmin, ymax, xmax] as integers on a 0-1000 scale.
                
                Example Response:
                [
                  {"name":"Chicken Rice", "price":"4.00",  "box":[300, 100, 500, 400]},
                  {"name":"Laksa", "price":"5.50", "box":[500, 100, 800, 400]}
                ]
                Return ONLY raw JSON. No markdown.
                        """.trimIndent()

            try {
                val response = generativeModel.generateContent(
                    content {
                        image(fullMenuBitmap)
                        text(prompt)
                    }
                )
                Log.d("GEMINI", "Response: ${response.text}")
                val cleanJson = response.text?.replace("```json", "")
                    ?.replace("```", "")
                    ?.trim() ?: return@withContext emptyList()

                val jsonArray = JSONArray(cleanJson)
                val results = mutableListOf<ScannedDish>()

                for (i in 0 until jsonArray.length()) {
                    val item = jsonArray.getJSONObject(i)
                    val name = item.optString("name")
                    val price = item.optString("price")
                    val box = item.optJSONArray("box")
                    if (box != null && box.length() == 4) {
                        val ymin = box.getDouble(0).toFloat()
                        val xmin = box.getDouble(1).toFloat()
                        val ymax = box.getDouble(2).toFloat()
                        val xmax = box.getDouble(3).toFloat()

                        val cropped = cropBitmap(fullMenuBitmap, ymin, xmin, ymax, xmax)

                        if (cropped != null) {
                            results.add(ScannedDish(name, price, cropped))
                        }
                    }
                }
                results
            } catch (e: Exception) {
                e.printStackTrace()
                emptyList()
            }
        }
    }
    private fun cropBitmap(
        original: Bitmap,
        ymin: Float,
        xmin: Float,
        ymax: Float,
        xmax: Float
    ): Bitmap? {
        return try {
            val width = original.width.toFloat()
            val height = original.height.toFloat()


            var normXMin = xmin
            var normYMin = ymin
            var normXMax = xmax
            var normYMax = ymax

            // Divide values by 1000 for percentage
            normXMin = xmin / 1000f
            normYMin = ymin / 1000f
            normXMax = xmax / 1000f
            normYMax = ymax / 1000f

            // Multiply by original image size to get 4 corner for crop
            var finalX = normXMin * width
            var finalY = normYMin * height
            var finalMaxX = normXMax * width
            var finalMaxY = normYMax * height

            // Get width and height to crop image
            var cropW = finalMaxX - finalX
            var cropH = finalMaxY - finalY

            //Ensures location is not negeative or past the image size
            if (finalX < 0) finalX = 0f
            if (finalY < 0) finalY = 0f
            if (finalX + cropW > width) cropW = width - finalX
            if (finalY + cropH > height) cropH = height - finalY

            // Ensures box exists
            if (cropW <= 0 || cropH <= 0) return null

            Bitmap.createBitmap(
                original,
                finalX.toInt(),
                finalY.toInt(),
                cropW.toInt(),
                cropH.toInt()
            )
        } catch (e: Exception) {
            e.printStackTrace()
            null
        }
    }
}