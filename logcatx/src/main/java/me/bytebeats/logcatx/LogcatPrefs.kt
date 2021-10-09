package me.bytebeats.logcatx

import android.content.Context
import android.content.SharedPreferences

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