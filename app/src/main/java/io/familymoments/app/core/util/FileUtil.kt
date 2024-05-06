package io.familymoments.app.core.util

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.ImageDecoder
import android.net.Uri
import android.os.Build
import android.provider.MediaStore
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import timber.log.Timber
import java.io.File
import java.io.FileOutputStream
import java.net.URL
import kotlin.math.roundToInt

object FileUtil {
    private const val COMPRESS_QUALITY = 50
    private const val URI_SCHEME_HTTPS = "https"
    private const val URI_SCHEME_CONTENT = "content"

    suspend fun imageFilesResize(
        context: Context,
        uriList: List<Uri>
    ): List<File> {
        val pathHashMap = hashMapOf<Int, String?>()

        CoroutineScope(Dispatchers.IO).launch {
            uriList.forEachIndexed { index, uri ->
                launch {
                    val path = optimizeBitmap(context, uri, index)
                    pathHashMap[index] = path
                }
            }
        }.join()

        return pathHashMap.map { File(it.value!!) }
    }

    suspend fun imageFileResize(
        context: Context,
        uri: Uri
    ): File {
        return withContext(Dispatchers.IO) {
            val path = optimizeBitmap(context, uri, 0)
            File(path!!)
        }
    }

    private fun optimizeBitmap(context: Context, uri: Uri, index: Int): String? {
        try {
            val tempFile = File(context.cacheDir, "image$index.jpg")
            tempFile.createNewFile() // 임시 파일 생성

            // 지정된 이름을 가진 파일에 쓸 파일 출력 스트림을 만든다.
            val fos = FileOutputStream(tempFile)

            convertUriToBitmap(uri, context).apply {
                compress(Bitmap.CompressFormat.JPEG, COMPRESS_QUALITY, fos)
                recycle()
            }

            fos.flush()
            fos.close()

            return tempFile.absolutePath // 임시파일 저장경로 리턴

        } catch (e: Exception) {
            Timber.tag("hkhk").e("FileUtil - ${e.message}")
        }

        return null
    }

    @Suppress("DEPRECATION")
    fun convertUriToBitmap(uri: Uri?, context: Context): Bitmap {
        var bitmap: Bitmap? = null

        /*
        uri 의 scheme 에 따라 비트맵 변환하는 과정이 다름
        - 갤러리에서 불러온 이미지 uri 의 scheme -> content
        - app resource uri 의 scheme -> android.resource
        - url 을 변환한 uri 의 scheme -> https
        */
        if (uri?.scheme == URI_SCHEME_HTTPS) {
            val url = URL(uri.toString())
            bitmap = BitmapFactory.decodeStream(url.openConnection().getInputStream())
        }
        if (uri?.scheme == URI_SCHEME_CONTENT || uri?.scheme == URI_SCHEME_RESOURCE) {
            bitmap = if (Build.VERSION.SDK_INT < 28) {
                MediaStore.Images
                    .Media.getBitmap(context.contentResolver, uri)
            } else {
                val source = ImageDecoder
                    .createSource(context.contentResolver, uri)
                ImageDecoder.decodeBitmap(source)
            }
        }
        return bitmap ?: throw NullPointerException()
    }

    fun convertBitmapToFile(bitmap: Bitmap, context: Context): File {
        val file = File(context.cacheDir, "image.jpg")
        file.outputStream().use { outputStream ->
            bitmap.compress(Bitmap.CompressFormat.JPEG, COMPRESS_QUALITY, outputStream)
        }
        return file
    }

    fun resizeBitmap(bitmap: Bitmap, density: Float, height: Int): Bitmap {
        val aspectRatio = bitmap.width.toFloat() / bitmap.height.toFloat()
        val desiredHeightPx = (height.toFloat() * density).roundToInt()
        val scaledWidthPx = (desiredHeightPx * aspectRatio).roundToInt()
        return Bitmap.createScaledBitmap(bitmap, scaledWidthPx, desiredHeightPx, true)
    }
}
