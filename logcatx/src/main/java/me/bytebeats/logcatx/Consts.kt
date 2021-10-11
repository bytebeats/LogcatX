package me.bytebeats.logcatx

import android.os.Build
import android.view.View
import androidx.annotation.IdRes
import androidx.appcompat.app.AppCompatActivity
import java.nio.charset.Charset
import java.nio.charset.StandardCharsets

/**
 * @Author bytebeats
 * @Email <happychinapc@gmail.com>
 * @Github https://github.com/bytebeats
 * @Created at 2021/10/9 14:07
 * @Version 1.0
 * @Description TO-DO
 */

internal const val LOGCATX_LEVEL = "logcatx_level"
internal const val LOGCATX_TEXT = "logcatx_text"
internal const val LINE_SPACE = "\n    "
internal val IGNORED_LOGS =
    listOf("--------- beginning of crash", "--------- beginning of main", "--------- beginning of system")

internal val UTF_8 =
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) StandardCharsets.UTF_8 else Charset.forName("UTF-8")

internal val LOG_LEVELS = listOf("Verbose", "Debug", "Info", "Warn", "Error")

fun <T : View> AppCompatActivity.lazyFind(@IdRes viewId: Int): Lazy<T> =
    lazy(LazyThreadSafetyMode.SYNCHRONIZED) {
        findViewById(viewId)
    }

fun View?.gone(): Unit {
    this?.visibility = View.GONE
}

fun View?.visible(): Unit {
    this?.visibility = View.VISIBLE
}