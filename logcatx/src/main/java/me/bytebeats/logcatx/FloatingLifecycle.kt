package me.bytebeats.logcatx

import android.app.Activity
import android.app.Application
import android.os.Bundle
import android.view.WindowManager
import me.bytebeats.logcatx.ui.LogcatXActivity

/**
 * @Author bytebeats
 * @Email <happychinapc@gmail.com>
 * @Github https://github.com/bytebeats
 * @Created at 2021/10/9 14:37
 * @Version 1.0
 * @Description TO-DO
 */

class FloatingLifecycle : Application.ActivityLifecycleCallbacks {

    companion object {
        fun with(application: Application) {
            application.registerActivityLifecycleCallbacks(FloatingLifecycle())
        }
    }

    override fun onActivityCreated(activity: Activity, savedInstanceState: Bundle?) {
        if (activity is LogcatXActivity) {
            activity.window.addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN)
            return
        }
        FloatingWindow.with(activity).display()
    }

    override fun onActivityStarted(activity: Activity) {}

    override fun onActivityResumed(activity: Activity) {}

    override fun onActivityPaused(activity: Activity) {}

    override fun onActivityStopped(activity: Activity) {}

    override fun onActivitySaveInstanceState(activity: Activity, outState: Bundle) {}

    override fun onActivityDestroyed(activity: Activity) {}
}