package io.shubham0204.model2vec

import java.nio.file.Paths

private val applicationContext by lazy {
    AndroidContextInitializer.getContext()
}

actual object FileUtils {
    actual fun getReadableFileFromResFileUri(resFileUri: String): String {
        val dirPath = resFileUri.replace("file:///android_asset/", "")
        val assetsFileInputStream = applicationContext.assets.open(dirPath)
        val internalStorageFile = Paths.get(applicationContext.filesDir.absolutePath, dirPath).toFile()
        if (internalStorageFile.exists()) {
            return internalStorageFile.absolutePath
        }
        internalStorageFile.parentFile?.mkdirs()
        internalStorageFile.outputStream().use { outputStream ->
            assetsFileInputStream.copyTo(outputStream)
        }
        return internalStorageFile.absolutePath
    }
}