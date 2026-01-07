package io.shubham0204.model2vec

actual object FileUtils {
    actual fun getReadableFileFromResFileUri(resFileUri: String): String {
        return resFileUri.replace("file://", "")
    }
}