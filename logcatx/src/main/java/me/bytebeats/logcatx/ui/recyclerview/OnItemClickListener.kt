package me.bytebeats.logcatx.ui.recyclerview

import android.view.View
import androidx.recyclerview.widget.RecyclerView

/**
 * @Author bytebeats
 * @Email <happychinapc@gmail.com>
 * @Github https://github.com/bytebeats
 * @Created at 2021/10/9 16:55
 * @Version 1.0
 * @Description TO-DO
 */

interface OnItemClickListener {
    fun onItemSingleTap(recyclerView: RecyclerView, child: View, position: Int)
    fun onItemDoubleTap(recyclerView: RecyclerView, child: View, position: Int)
    fun onItemLongClick(recyclerView: RecyclerView, child: View, position: Int)
}