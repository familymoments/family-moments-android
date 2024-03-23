package io.familymoments.app.core.util

import android.content.Context
import android.graphics.Bitmap
import java.io.File

fun bitmapToFile(bitmap: Bitmap, context: Context): File {
    val file = File(context.cacheDir, "profile_image.jpg")
    file.outputStream().use { outputStream ->
        bitmap.compress(Bitmap.CompressFormat.JPEG, 90, outputStream)
    }
    return file
}
