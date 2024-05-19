package io.familymoments.app.core.util

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.ImageDecoder
import android.net.Uri
import android.os.Build
import android.provider.MediaStore
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import timber.log.Timber
import java.io.File
import java.io.FileOutputStream
import java.net.URL

object FileUtil {
    private const val COMPRESS_QUALITY = 50
    private const val URI_SCHEME_HTTPS = "https"
    private const val URI_SCHEME_CONTENT = "content"

    fun imageFilesResize(
        context: Context,
        uriList: List<Uri>
    ): List<File> {
        val pathHashMap = hashMapOf<Int, String?>()

        uriList.forEachIndexed { index, uri ->
            val path = optimizeBitmap(context, uri, index)
            pathHashMap[index] = path
        }

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
            val tempFile = File(context.cacheDir, "image$index.webp")
            tempFile.createNewFile() // 임시 파일 생성

            // 지정된 이름을 가진 파일에 쓸 파일 출력 스트림을 만든다.
            val fos = FileOutputStream(tempFile)

            convertUriToBitmap(uri, context).apply {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
                    compress(Bitmap.CompressFormat.WEBP_LOSSY, COMPRESS_QUALITY, fos)
                } else {
                    @Suppress("DEPRECATION")
                    compress(Bitmap.CompressFormat.WEBP, COMPRESS_QUALITY, fos)
                }
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

    fun convertBitmapToCompressedFile(bitmap: Bitmap, context: Context): File {
        val file = File(context.cacheDir, "image.webp")
        file.outputStream().use { outputStream ->
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
                bitmap.compress(Bitmap.CompressFormat.WEBP_LOSSY, COMPRESS_QUALITY, outputStream)
            } else {
                @Suppress("DEPRECATION")
                bitmap.compress(Bitmap.CompressFormat.WEBP, COMPRESS_QUALITY, outputStream)
            }
        }
        return file
    }

    fun uriToFile(uri: Uri, index: Int): File {
        val url = URL(uri.toString())
        val inputStream = url.openConnection().getInputStream()
        val file = File.createTempFile("temp$index", ".webp")
        inputStream?.let { input ->
            file.outputStream().use { output ->
                input.copyTo(output)
            }
        }
        inputStream?.close()
        return file
    }

}
