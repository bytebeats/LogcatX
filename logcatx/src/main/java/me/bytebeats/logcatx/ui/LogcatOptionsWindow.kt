package me.bytebeats.logcatx.ui

import android.app.Activity
import android.view.Gravity
import android.view.View
import android.view.WindowManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.hjq.xtoast.XToast
import me.bytebeats.logcatx.R
import me.bytebeats.logcatx.ui.recyclerview.OnItemSingleTapListener
import me.bytebeats.logcatx.ui.recyclerview.RecyclerViewClickBinder

/**
 * @Author bytebeats
 * @Email <happychinapc@gmail.com>
 * @Github https://github.com/bytebeats
 * @Created at 2021/10/9 20:20
 * @Version 1.0
 * @Description LogcatOptionsWindow show options for logs
 */

class LogcatOptionsWindow(activity: Activity) : XToast<LogcatOptionsWindow>(activity), XToast.OnClickListener<View> {

    private val mRecyclerView by lazy { findViewById<RecyclerView>(R.id.lv_choose_recycler) }
    private val mAdapter by lazy { LogcatOptionsAdapter(context) }

    private var mItemClickListener: OnItemSingleTapListener? = null

    init {
        setContentView(R.layout.logcatx_window_logcat_options)
        setGravity(Gravity.CENTER)
        clearWindowFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE)
        mRecyclerView.layoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
        mRecyclerView.adapter = mAdapter
        mRecyclerView.addOnItemTouchListener(RecyclerViewClickBinder(mRecyclerView, object : OnItemSingleTapListener {
            override fun onItemSingleTap(recyclerView: RecyclerView, child: View, position: Int) {
                mItemClickListener?.onItemSingleTap(recyclerView, child, position)
                cancel()
            }
        }))

        setOnClickListener(R.id.fl_choose_root, this)
    }

    override fun onClick(toast: XToast<*>?, view: View?) {
        cancel()
    }

    fun setList(vararg data: Int): LogcatOptionsWindow {
        mAdapter.set((data.map { context.getString(it) }))
        return this
    }

    fun setList(vararg data: String): LogcatOptionsWindow {
        mAdapter.set(listOf(*data))
        return this
    }

    fun setList(data: List<String>): LogcatOptionsWindow {
        mAdapter.set(data)
        return this
    }

    fun setOnItemClickListener(listener: OnItemSingleTapListener): LogcatOptionsWindow {
        mItemClickListener = listener
        return this
    }
}