package me.bytebeats.logcatx

import android.app.Activity
import android.app.Application
import android.os.Bundle
import me.bytebeats.logcatx.ui.FloatingEntryWindow
import me.bytebeats.logcatx.ui.LogcatXActivity

/**
 * @Author bytebeats
 * @Email <happychinapc@gmail.com>
 * @Github https://github.com/bytebeats
 * @Created at 2021/10/9 14:37
 * @Version 1.0
 * @Description FloatingEntryLifecycle registered by user's Application and showed floating entry icon for every Activity when Activity was created
 */

class FloatingEntryLifecycle : Application.ActivityLifecycleCallbacks {

    companion object {
        fun with(application: Application) {
            application.registerActivityLifecycleCallbacks(FloatingEntryLifecycle())
        }
    }

    override fun onActivityCreated(activity: Activity, savedInstanceState: Bundle?) {
        if (activity is LogcatXActivity) {
            return
        }
        FloatingEntryWindow.with(activity).show()
    }

    override fun onActivityStarted(activity: Activity) {}

    override fun onActivityResumed(activity: Activity) {}

    override fun onActivityPaused(activity: Activity) {}

    override fun onActivityStopped(activity: Activity) {}

    override fun onActivitySaveInstanceState(activity: Activity, outState: Bundle) {}

    override fun onActivityDestroyed(activity: Activity) {}
}