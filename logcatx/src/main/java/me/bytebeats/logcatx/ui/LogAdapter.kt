package me.bytebeats.logcatx.ui

import android.content.Context
import android.graphics.Color
import android.os.Build
import android.text.SpannableString
import android.text.Spanned
import android.text.style.BackgroundColorSpan
import android.text.style.ForegroundColorSpan
import android.util.SparseBooleanArray
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import me.bytebeats.logcatx.LogcatItem
import me.bytebeats.logcatx.R

/**
 * @Author bytebeats
 * @Email <happychinapc@gmail.com>
 * @Github https://github.com/bytebeats
 * @Created at 2021/10/9 15:53
 * @Version 1.0
 * @Description TO-DO
 */

class LogAdapter(val context: Context) : RecyclerView.Adapter<LogAdapter.LogViewHolder>() {
    private val mExpanded = SparseBooleanArray()
    private val mLogs = mutableListOf<LogcatItem>()
    private var mKey: String? = null

    fun logs(): MutableList<LogcatItem> = mLogs

    fun lastIndex(): Int = if (mLogs.isEmpty()) 0 else itemCount - 1

    fun addItem(item: LogcatItem) {
        mLogs.add(item)
        notifyItemInserted(mLogs.size)
    }

    fun removeAt(position: Int) {
        mLogs.removeAt(position)
        notifyItemRemoved(position)
    }

    fun clear() {
        mExpanded.clear()
        mLogs.clear()
        mKey = ""
        notifyDataSetChanged()
    }

    fun clickAt(position: Int) {
        mExpanded.put(position, !mExpanded.get(position))
        notifyItemChanged(position)
    }

    fun notifyLastItemChanged() {
        notifyItemChanged(itemCount - 1)
    }

    fun setKeyword(key: String?) {
        mKey = key
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): LogViewHolder {
        return LogViewHolder(LayoutInflater.from(context).inflate(R.layout.logcatx_item_logcat, parent, false))
    }

    override fun onBindViewHolder(holder: LogViewHolder, position: Int) {
        holder.onBind(item(position), position)
    }

    override fun getItemCount(): Int = mLogs.size

    fun item(position: Int): LogcatItem = mLogs[position]

    inner class LogViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        private val contentView by lazy { view.findViewById<TextView>(R.id.tv_log_content) }
        private val indexView by lazy { view.findViewById<TextView>(R.id.tv_log_index) }
        private val expandView by lazy { view.findViewById<ImageView>(R.id.iv_log_expand) }
        private val dividerView by lazy { view.findViewById<View>(R.id.v_log_divider) }

        fun onBind(logcatItem: LogcatItem, position: Int) {
            val logContent = logcatItem.string()
            var text: CharSequence = logContent
            if (!mKey.isNullOrEmpty()) {
                var keyIdx = logContent.indexOf(mKey!!)
                if (keyIdx == -1) {
                    keyIdx = logContent.lowercase().indexOf(mKey!!.lowercase())
                }
                val spannable = SpannableString(logContent)
                while (keyIdx > -1) {
                    val start = keyIdx
                    val end = keyIdx + mKey!!.length
                    spannable.setSpan(BackgroundColorSpan(Color.WHITE), start, end, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE)
                    spannable.setSpan(ForegroundColorSpan(Color.BLACK), start, end, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE)
                    keyIdx = logContent.indexOf(mKey!!, end)
                    if (keyIdx == -1) {
                        keyIdx = logContent.lowercase().indexOf(mKey!!.lowercase(), end)
                    }
                    text = spannable
                }
            }
            contentView.text = text
            val logColorRes = when (logcatItem.level) {
                "V" -> R.color.logcatx_level_verbose_color
                "D" -> R.color.logcatx_level_debug_color
                "I" -> R.color.logcatx_level_info_color
                "W" -> R.color.logcatx_level_warn_color
                "E" -> R.color.logcatx_level_error_color
                else -> R.color.logcatx_level_other_color
            }
            val logTextColor = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                context.resources.getColor(logColorRes, context.theme)
            } else {
                context.resources.getColor(logColorRes)
            }
            contentView.setTextColor(logTextColor)
            dividerView.visibility = if (position == itemCount - 1) View.GONE else View.VISIBLE

            if (contentView.lineCount > MAX_LINE + 1) {
                if (mExpanded.get(position)) {
                    if (contentView.maxLines != Int.MAX_VALUE) {
                        contentView.maxLines = Int.MAX_VALUE
                        expandView.setImageResource(R.drawable.logcatx_ic_arrows_up)
                    }
                } else {
                    if (contentView.maxLines != MAX_LINE) {
                        contentView.maxLines = MAX_LINE
                        expandView.setImageResource(R.drawable.logcatx_ic_arrows_down)
                    }
                }

                expandView.visibility = View.VISIBLE
                indexView.visibility = View.GONE
            } else {
                contentView.maxLines = Int.MAX_VALUE
                indexView.text = (position + 1).toString()
                expandView.visibility = View.GONE
                indexView.visibility = View.VISIBLE
            }
        }
    }

    private companion object {
        const val MAX_LINE = 4
    }
}