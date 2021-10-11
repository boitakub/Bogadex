package fr.boitakub.bogadex.tests.tools

import androidx.test.platform.app.InstrumentationRegistry
import java.io.InputStreamReader

object FileReader {
    fun readStringFromFile(fileName: String): String {
        val inputStream =
            InstrumentationRegistry.getInstrumentation().context.assets.open(
                "data/$fileName"
            )
        val builder = StringBuilder()
        val reader = InputStreamReader(inputStream, "UTF-8")
        reader.readLines().forEach {
            builder.append(it)
        }
        return builder.toString()
    }
}
