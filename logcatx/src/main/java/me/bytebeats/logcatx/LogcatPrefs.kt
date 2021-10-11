package me.bytebeats.logcatx

import android.content.Context
import android.content.SharedPreferences

/**
 * @Author bytebeats
 * @Email <happychinapc@gmail.com>
 * @Github https://github.com/bytebeats
 * @Created at 2021/10/11 15:23
 * @Version 1.0
 * @Description LogcatPrefs to save and restore logcat's level and keyword through SharedPreferences
 */
object LogcatPrefs {
    private lateinit var mConfig: SharedPreferences

    fun init(context: Context) {
        mConfig = context.getSharedPreferences("logcatx", Context.MODE_PRIVATE)
    }

    var logcatLevel: String
        get() = if (::mConfig.isInitialized) {
            mConfig.getString(LOGCATX_LEVEL, "V")!!
        } else "V"
        set(value) {
            if (::mConfig.isInitialized) {
                mConfig.edit().putString(LOGCATX_LEVEL, value).apply()
            }
        }

    var logcatText: String
        get() = if (::mConfig.isInitialized) {
            mConfig.getString(LOGCATX_TEXT, "")!!
        } else ""
        set(value) {
            if (::mConfig.isInitialized) {
                mConfig.edit().putString(LOGCATX_TEXT, value).apply()
            }
        }
}