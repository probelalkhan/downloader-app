package net.simplifiedcoding.ui.utils

import android.content.Context
import android.os.Environment
import androidx.core.content.ContextCompat
import java.io.File


fun getRootDirPath(context: Context): String {
    return if (Environment.MEDIA_MOUNTED == Environment.getExternalStorageState()) {
        val file: File = ContextCompat.getExternalFilesDirs(
            context.applicationContext,
            null
        )[0]
        file.absolutePath
    } else {
        context.applicationContext.filesDir.absolutePath
    }
}
