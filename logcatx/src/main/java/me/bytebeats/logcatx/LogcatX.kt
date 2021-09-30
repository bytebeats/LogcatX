package me.bytebeats.logcatx

import android.content.Context
import android.content.SharedPreferences

object LogcatX {
    private lateinit var mConfig: SharedPreferences

    fun init(context: Context) {
        mConfig = context.getSharedPreferences("logcatx", Context.MODE_PRIVATE)
    }
}