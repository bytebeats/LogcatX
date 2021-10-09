package me.bytebeats.logcatx.ui

import android.app.Activity
import android.content.Intent
import android.util.TypedValue
import android.view.Gravity
import android.view.View
import android.widget.ImageView
import com.hjq.xtoast.XToast
import com.hjq.xtoast.draggable.SpringDraggable
import me.bytebeats.logcatx.R

/**
 * @Author bytebeats
 * @Email <happychinapc@gmail.com>
 * @Github https://github.com/bytebeats
 * @Created at 2021/10/9 14:41
 * @Version 1.0
 * @Description TO-DO
 */

class FloatingWindow private constructor(activity: Activity) : XToast<FloatingWindow>(activity),
        XToast.OnClickListener<View> {

    init {
        contentView = ImageView(activity.applicationContext).apply {
            id = android.R.id.icon
            setImageResource(R.drawable.logcat_selector_floating)
        }
        val size =
            TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 45F, activity.resources.displayMetrics).toInt()
        setWidth(size)
        setHeight(size)

        setAnimStyle(android.R.style.Animation_Toast)
        setDraggable(SpringDraggable())
        setGravity(Gravity.END or Gravity.CENTER_VERTICAL)
        setOnClickListener(android.R.id.icon, this)
    }

    override fun onClick(toast: XToast<*>?, view: View?) {
        startActivity(Intent(context, LogcatXActivity::class.java))
    }


    companion object {
        fun with(activity: Activity): FloatingWindow = FloatingWindow(activity)
    }
}