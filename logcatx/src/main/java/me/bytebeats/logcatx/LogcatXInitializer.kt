package me.bytebeats.logcatx

import android.app.Application
import android.content.ContentProvider
import android.content.ContentValues
import android.database.Cursor
import android.net.Uri
import android.widget.Toast

/**
 * @Author bytebeats
 * @Email <happychinapc@gmail.com>
 * @Github https://github.com/bytebeats
 * @Created at 2021/10/9 15:23
 * @Version 1.0
 * @Description TO-DO
 */

class LogcatXInitializer : ContentProvider() {
    override fun onCreate(): Boolean {
        context?.let {
            LogcatPrefs.init(it)
            if (it is Application) {
                FloatingLifecycle.with(it)
            } else {
                Toast.makeText(it, R.string.logcatx_launch_error, Toast.LENGTH_SHORT).show()
            }
        }
        return true
    }

    override fun query(
        uri: Uri,
        projection: Array<out String>?,
        selection: String?,
        selectionArgs: Array<out String>?,
        sortOrder: String?
    ): Cursor? = null

    override fun getType(uri: Uri): String? = null

    override fun insert(uri: Uri, values: ContentValues?): Uri? = null

    override fun delete(uri: Uri, selection: String?, selectionArgs: Array<out String>?): Int = 0

    override fun update(uri: Uri, values: ContentValues?, selection: String?, selectionArgs: Array<out String>?): Int =
        0
}