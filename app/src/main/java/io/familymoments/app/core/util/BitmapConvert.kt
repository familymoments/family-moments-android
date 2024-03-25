package io.familymoments.app.core.util

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.ImageDecoder
import android.graphics.Matrix
import android.media.ExifInterface
import android.graphics.drawable.BitmapDrawable
import android.net.Uri
import android.os.Build
import android.provider.MediaStore
import coil.ImageLoader
import coil.request.ImageRequest
import coil.request.SuccessResult
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import timber.log.Timber
import java.io.BufferedInputStream
import java.io.File
import java.io.FileOutputStream
import java.util.UUID

object FileUtil {
    private const val MAX_WIDTH = 1280
    private const val MAX_HEIGHT = 960
    private const val COMPRESS_QUALITY = 50

    suspend fun bitmapResize(
        context: Context,
        uriList: List<Uri>
    ): List<File> {

        val pathHashMap = hashMapOf<Int, String?>()

        CoroutineScope(Dispatchers.IO).launch {
            uriList.forEachIndexed { index, uri ->
                launch {
                    val path = optimizeBitmap(context, uri)
                    pathHashMap[index] = path
                }
            }
        }.join()

        return pathHashMap.map { File(it.value!!) }
    }

    private fun optimizeBitmap(context: Context, uri: Uri): String? {
        try {
            val storage = context.cacheDir // 임시 파일 경로
            val fileName = String.format("%s.%s", UUID.randomUUID(), "jpg") // 임시 파일 이름

            val tempFile = File(storage, fileName)
            tempFile.createNewFile() // 임시 파일 생성

            // 지정된 이름을 가진 파일에 쓸 파일 출력 스트림을 만든다.
            val fos = FileOutputStream(tempFile)

            decodeBitmapFromUri(uri, context)?.apply {
                compress(Bitmap.CompressFormat.JPEG, COMPRESS_QUALITY, fos)
                recycle()
            } ?: throw NullPointerException()

            fos.flush()
            fos.close()

            return tempFile.absolutePath // 임시파일 저장경로 리턴

        } catch (e: Exception) {
            Timber.tag("hkhk").e("FileUtil - ${e.message}")
        }

        return null
    }

    // 최적화 bitmap 반환
    private fun decodeBitmapFromUri(uri: Uri, context: Context): Bitmap? {

        // 인자값으로 넘어온 입력 스트림을 나중에 사용하기 위해 저장하는 BufferedInputStream 사용
        val input = BufferedInputStream(context.contentResolver.openInputStream(uri))

        input.mark(input.available()) // 입력 스트림의 특정 위치를 기억

        var bitmap: Bitmap?

        BitmapFactory.Options().run {
            // inJustDecodeBounds를 true로 설정한 상태에서 디코딩한 다음 옵션을 전달
            inJustDecodeBounds = true
            bitmap = BitmapFactory.decodeStream(input, null, this)

            input.reset() // 입력 스트림의 마지막 mark 된 위치로 재설정

            // inSampleSize 값과 false로 설정한 inJustDecodeBounds를 사용하여 다시 디코딩
            inSampleSize = calculateInSampleSize(this)
            inJustDecodeBounds = false

            bitmap = BitmapFactory.decodeStream(input, null, this)?.apply {
                rotateImageIfRequired(context, this, uri)
            }
        }

        input.close()

        return bitmap

    }

    private fun rotateImageIfRequired(context: Context, bitmap: Bitmap, uri: Uri): Bitmap? {
        val input = context.contentResolver.openInputStream(uri) ?: return null

        val exif = if (Build.VERSION.SDK_INT > 23) {
            ExifInterface(input)
        } else {
            ExifInterface(uri.path!!)
        }

        return when (exif.getAttributeInt(ExifInterface.TAG_ORIENTATION, ExifInterface.ORIENTATION_NORMAL)) {
            ExifInterface.ORIENTATION_ROTATE_90 -> rotateImage(bitmap, 90)
            ExifInterface.ORIENTATION_ROTATE_180 -> rotateImage(bitmap, 180)
            ExifInterface.ORIENTATION_ROTATE_270 -> rotateImage(bitmap, 270)
            else -> bitmap
        }
    }

    private fun rotateImage(bitmap: Bitmap, degree: Int): Bitmap? {
        val matrix = Matrix()
        matrix.postRotate(degree.toFloat())
        return Bitmap.createBitmap(bitmap, 0, 0, bitmap.width, bitmap.height, matrix, true)
    }

    // 리샘플링 값 계산 : 타겟 너비와 높이를 기준으로 2의 거듭제곱인 샘플 크기 값을 계산
    private fun calculateInSampleSize(options: BitmapFactory.Options): Int {
        val (height: Int, width: Int) = options.run { outHeight to outWidth }
        var inSampleSize = 1

        if (height > MAX_HEIGHT || width > MAX_WIDTH) {

            val halfHeight: Int = height / 2
            val halfWidth: Int = width / 2

            while (halfHeight / inSampleSize >= MAX_HEIGHT && halfWidth / inSampleSize >= MAX_WIDTH) {
                inSampleSize *= 2
            }
        }

        return inSampleSize
    }

    @Suppress("DEPRECATION")
    fun convertUriToBitmap(uri: Uri?, context: Context): Bitmap? {
        var bitmap: Bitmap? = null
        uri?.let {
            bitmap = if (Build.VERSION.SDK_INT < 28) {
                MediaStore.Images
                    .Media.getBitmap(context.contentResolver, it)
            } else {
                val source = ImageDecoder
                    .createSource(context.contentResolver, it)
                ImageDecoder.decodeBitmap(source)
            }
        }
        return bitmap
    }
}

fun convertBitmapToFile(bitmap: Bitmap?): File {
    val file = File.createTempFile("profile_image", ".jpg") // 임시 파일 생성
    val outputStream = FileOutputStream(file)
    bitmap?.compress(Bitmap.CompressFormat.JPEG, 90, outputStream)
    outputStream.flush()
    outputStream.close()
    return file
}

suspend fun convertUrlToBitmap(url: String, context: Context): Bitmap? {
    return withContext(Dispatchers.IO) {
        try {
            val loader = ImageLoader(context)
            val request = ImageRequest.Builder(context)
                .data(url)
                .build()
            val result = (loader.execute(request) as SuccessResult).drawable
            (result as BitmapDrawable).bitmap
        } catch (e: Exception) {
            e.printStackTrace()
            null
        }
    }
}
