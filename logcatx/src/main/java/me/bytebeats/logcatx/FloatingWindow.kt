package me.bytebeats.logcatx

import android.app.Activity

/**
 * @Author bytebeats
 * @Email <happychinapc@gmail.com>
 * @Github https://github.com/bytebeats
 * @Created at 2021/10/9 14:41
 * @Version 1.0
 * @Description TO-DO
 */

class FloatingWindow private constructor(activity: Activity) {

    fun display() {

    }

    companion object {
        fun with(activity: Activity): FloatingWindow = FloatingWindow(activity).apply {

        }
    }
}