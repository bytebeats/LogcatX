package me.bytebeats.logcatx.ui

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import me.bytebeats.logcatx.R

/**
 * @Author bytebeats
 * @Email <happychinapc@gmail.com>
 * @Github https://github.com/bytebeats
 * @Created at 2021/10/9 20:13
 * @Version 1.0
 * @Description Options to logs, e.g.: copy, share, delete and shield
 */

class LogcatOptionsAdapter(val context: Context) : RecyclerView.Adapter<LogcatOptionsAdapter.ChooseViewHolder>() {

    private val mDataSet = mutableListOf<String>()

    fun set(data: List<String>) {
        mDataSet.clear()
        mDataSet.addAll(data)
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ChooseViewHolder {
        return ChooseViewHolder(LayoutInflater.from(context).inflate(R.layout.logcatx_item_logcat_options, parent, false))
    }

    override fun onBindViewHolder(holder: ChooseViewHolder, position: Int) {
        holder.onBind(item(position), position == itemCount - 1)
    }

    override fun getItemCount(): Int = mDataSet.size

    fun item(position: Int): String = mDataSet[position]

    class ChooseViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        private val contentView = view.findViewById<TextView>(R.id.tv_choose_content)
        private val dividerView = view.findViewById<View>(R.id.v_choose_divider)

        fun onBind(text: String, last: Boolean) {
            contentView.text = text
            dividerView.visibility = if (last) View.GONE else View.VISIBLE
        }
    }
}