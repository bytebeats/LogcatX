package me.bytebeats.logcatx.ui.recyclerview

import android.view.GestureDetector
import android.view.MotionEvent
import androidx.recyclerview.widget.RecyclerView

/**
 * @Author bytebeats
 * @Email <happychinapc@gmail.com>
 * @Github https://github.com/bytebeats
 * @Created at 2021/10/9 16:56
 * @Version 1.0
 * @Description A RecyclerView.OnItemTouchListener to bind OnItemSingleTapListener/OnItemLongPressListener/OnItemDoubleTapListener into RecyclerView; Don't forget to call RecyclerView.addOnItemTouchListener(this)
 */

class RecyclerViewClickBinder(
    val recyclerView: RecyclerView,
    val singleClickListener: OnItemSingleTapListener? = null,
    val longClickListener: OnItemLongPressListener? = null,
    val doubleClickListener: OnItemDoubleTapListener? = null
) : RecyclerView.OnItemTouchListener {
    private val mGestureDetector =
        GestureDetector(recyclerView.context, object : GestureDetector.SimpleOnGestureListener() {
            override fun onSingleTapUp(e: MotionEvent?): Boolean {
                val child = e?.let { recyclerView.findChildViewUnder(it.x, it.y) }
                if (child != null && singleClickListener != null) {
                    singleClickListener.onItemSingleTap(
                        recyclerView,
                        child,
                        recyclerView.getChildLayoutPosition(child)
                    )
                    return true
                }
                return false
            }

            override fun onLongPress(e: MotionEvent?) {
                val child = e?.let { recyclerView.findChildViewUnder(it.x, it.y) }
                if (child != null && longClickListener != null) {
                    longClickListener.onItemLongPress(
                        recyclerView,
                        child,
                        recyclerView.getChildLayoutPosition(child)
                    )
                }
            }

            override fun onDoubleTap(e: MotionEvent?): Boolean {
                val child = e?.let { recyclerView.findChildViewUnder(it.x, it.y) }
                if (child != null && doubleClickListener != null) {
                    doubleClickListener.onItemDoubleTap(
                        recyclerView,
                        child,
                        recyclerView.getChildLayoutPosition(child)
                    )
                    return true
                }
                return false
            }
        })

    override fun onInterceptTouchEvent(rv: RecyclerView, e: MotionEvent): Boolean = mGestureDetector.onTouchEvent(e)

    override fun onTouchEvent(rv: RecyclerView, e: MotionEvent) {
    }

    override fun onRequestDisallowInterceptTouchEvent(disallowIntercept: Boolean) {
    }
}