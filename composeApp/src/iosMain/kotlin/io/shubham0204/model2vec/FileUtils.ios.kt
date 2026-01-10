package io.shubham0204.model2vec

actual class FileUtils {
    actual fun getReadableFileFromResFileUri(resFileUri: String): String {
        return resFileUri.replace("file://", "")
    }
}