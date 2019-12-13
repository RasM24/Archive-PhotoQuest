package endroad.photoquest.places.panoram

import android.content.Context
import androidx.annotation.RawRes
import java.io.BufferedReader
import java.io.IOException
import java.io.InputStreamReader

fun Context.readTextFileFromRawResource(@RawRes resourceId: Int): String? {
	val bufferedReader = resources.openRawResource(resourceId)
		.run(::InputStreamReader)
		.run(::BufferedReader)

	return StringBuilder().apply {
		try {
			bufferedReader.readLines().forEach { line -> appendln(line) }
		} catch (e: IOException) {
			return null
		}
	}.toString()
}