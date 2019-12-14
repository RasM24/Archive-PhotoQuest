package ru.endroad.panorama

import android.content.Context
import androidx.annotation.RawRes
import java.io.BufferedReader
import java.io.IOException
import java.io.InputStreamReader

internal fun Context.readTextFileFromRawResource(@RawRes resourceId: Int): String? {
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