package com.task.currencyapp.data.util

import java.io.ByteArrayOutputStream
import java.io.IOException
import java.io.InputStream

/**
 * Resource helper to read the file content from provided resource path
 *
 */

object ResourceHelper {

    fun getData(name: String?): String? {
        return getData(ResourceHelper::class.java.classLoader, name)
    }

    private fun getData(loader: ClassLoader?, name: String?): String? {
        if (loader == null) {
            return null
        }
        try {
            loader.getResourceAsStream(name).use { inputStream ->
                return getData(inputStream)
            }
        } catch (e: IOException) {
            return null
        } catch (e: NullPointerException) {
            return null
        }
    }

    private fun getData(inputStream: InputStream?): String? {
        if (inputStream == null) {
            return null
        }
        try {
            ByteArrayOutputStream().use { result ->
                val buffer = ByteArray(4096)
                var length: Int
                while (inputStream.read(buffer).also { length = it } > 0) {
                    result.write(buffer, 0, length)
                }
                return result.toString("UTF-8")
            }
        } catch (e: IOException) {
            return null
        }
    }
}