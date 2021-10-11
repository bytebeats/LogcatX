package me.bytebeats.logcatx.ui

import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.os.Process
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.view.WindowInsets
import android.widget.*
import androidx.activity.OnBackPressedCallback
import androidx.annotation.StringRes
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.WindowCompat
import androidx.core.view.WindowInsetsControllerCompat
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import androidx.lifecycle.LifecycleOwner
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import me.bytebeats.logcatx.*
import me.bytebeats.logcatx.ui.recyclerview.OnItemLongClickListener
import me.bytebeats.logcatx.ui.recyclerview.OnItemSingleClickListener
import me.bytebeats.logcatx.ui.recyclerview.RecyclerViewClickBinder
import java.io.*
import java.text.SimpleDateFormat
import java.util.*

/**
 * @Author bytebeats
 * @Email <happychinapc@gmail.com>
 * @Github https://github.com/bytebeats
 * @Created at 2021/10/9 14:40
 * @Version 1.0
 * @Description TO-DO
 */

internal class LogcatXActivity : AppCompatActivity(), TextWatcher, View.OnClickListener, View.OnLongClickListener,
        CompoundButton.OnCheckedChangeListener, LogcatX.OnLogReceivedListener, Runnable {

    private val mSwitchView by lazyFind<CheckBox>(R.id.iv_log_switch)
    private val mSaveView by lazyFind<View>(R.id.iv_log_save)
    private val mLevelView by lazyFind<TextView>(R.id.tv_log_level)
    private val mSearchView by lazyFind<EditText>(R.id.et_log_search)
    private val mEmptyView by lazyFind<View>(R.id.iv_log_clear)
    private val mCleanView by lazyFind<View>(R.id.iv_log_clean)
    private val mCloseView by lazyFind<View>(R.id.iv_log_close)
    private val mRecyclerview by lazyFind<RecyclerView>(R.id.lv_log_recycler)
    private val mDownView by lazyFind<View>(R.id.ib_log_down)

    private val mAdapter by lazy {
        LogAdapter(this)
    }

    private val mTagFilter = mutableListOf<String>()

    private var mLevel = "V"

    private val mLogs = mutableListOf<LogcatItem>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.logcatx_window_logcat)
        RecyclerViewClickBinder(mRecyclerview, object : OnItemSingleClickListener {
            override fun onItemSingleTap(recyclerView: RecyclerView, child: View, position: Int) {
                mAdapter.clickAt(position)
            }
        }, object : OnItemLongClickListener {
            override fun onItemLongClick(recyclerView: RecyclerView, child: View, position: Int) {
                ChooseWindow(this@LogcatXActivity)
                    .setList(
                        R.string.logcatx_options_copy,
                        R.string.logcatx_options_share,
                        R.string.logcatx_options_delete,
                        R.string.logcatx_options_shield
                    )
                    .setOnItemClickListener(object : OnItemSingleClickListener {
                        override fun onItemSingleTap(recyclerView: RecyclerView, child: View, position: Int) {
                            when (position) {
                                0 -> {
                                    copyIntoClipboard(position)
                                }
                                1 -> {
                                    smartShare(position)
                                }
                                2 -> {
                                    mLogs.remove(mAdapter.item(position))
                                    mAdapter.removeAt(position)
                                }
                                3 -> {
                                    addFilter(mAdapter.item(position).tag)
                                }
                                else -> {
                                }
                            }
                        }
                    })
                    .show()
            }
        }, null)
        mRecyclerview.layoutManager = LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)
        mRecyclerview.adapter = mAdapter

        mSwitchView.setOnCheckedChangeListener(this)
        mSearchView.addTextChangedListener(this)
        mSearchView.setText(LogcatPrefs.logcatLevel)
        logLevel(LogcatPrefs.logcatLevel)

        mSaveView.setOnClickListener(this)
        mLevelView.setOnClickListener(this)
        mEmptyView.setOnClickListener(this)
        mCleanView.setOnClickListener(this)
        mCloseView.setOnClickListener(this)
        mDownView.setOnClickListener(this)

        mSaveView.setOnLongClickListener(this)
        mLevelView.setOnLongClickListener(this)
        mSwitchView.setOnLongClickListener(this)
        mCleanView.setOnLongClickListener(this)
        mCloseView.setOnLongClickListener(this)

        lifecycle.addObserver(object : LifecycleEventObserver {
            override fun onStateChanged(source: LifecycleOwner, event: Lifecycle.Event) {
                if (event == Lifecycle.Event.ON_CREATE) {
                    LogcatX.start(this@LogcatXActivity)
                } else if (event == Lifecycle.Event.ON_RESUME) {
                    LogcatX.resume()
                } else if (event == Lifecycle.Event.ON_PAUSE) {
                    LogcatX.pause()
                } else if (event == Lifecycle.Event.ON_DESTROY) {
                    LogcatX.destroy()
                    mSearchView.removeCallbacks(this@LogcatXActivity)
                }
            }
        })

        mRecyclerview.postDelayed({ mRecyclerview.scrollToPosition(mAdapter.lastIndex()) }, 500)

        initFilter()
        onBackPressedDispatcher.addCallback(this, object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                mSearchView.clearFocus()
                moveTaskToBack(false)
                finish()
            }
        })
    }

    override fun onWindowFocusChanged(hasFocus: Boolean) {
        super.onWindowFocusChanged(hasFocus)
        if (hasFocus) hideSystemUI() else showSystemUI()
    }

    private fun hideSystemUI() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            WindowCompat.setDecorFitsSystemWindows(window, false)
            WindowInsetsControllerCompat(window, window.decorView).let { controller ->
                controller.hide(WindowInsets.Type.systemBars())
                controller.hide(WindowInsets.Type.navigationBars())
                controller.hide(WindowInsets.Type.statusBars())
                controller.hide(WindowInsets.Type.captionBar())
                controller.systemBarsBehavior = WindowInsetsControllerCompat.BEHAVIOR_SHOW_TRANSIENT_BARS_BY_SWIPE
            }
        } else {
            // Enables regular immersive mode.
            // For "lean back" mode, remove SYSTEM_UI_FLAG_IMMERSIVE.
            // Or for "sticky immersive," replace it with SYSTEM_UI_FLAG_IMMERSIVE_STICKY
            window.decorView.systemUiVisibility = (View.SYSTEM_UI_FLAG_IMMERSIVE
                    // Set the content to appear under the system bars so that the
                    // content doesn't resize when the system bars hide and show.
                    or View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                    or View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                    or View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                    // Hide the nav bar and status bar
                    or View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                    or View.SYSTEM_UI_FLAG_FULLSCREEN)
        }
    }

    // Shows the system bars by removing all the flags
// except for the ones that make the content appear under the system bars.
    private fun showSystemUI() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            WindowCompat.setDecorFitsSystemWindows(window, true)
            WindowInsetsControllerCompat(window, window.decorView).let { controller ->
                controller.show(WindowInsets.Type.systemBars())
                controller.show(WindowInsets.Type.navigationBars())
                controller.show(WindowInsets.Type.statusBars())
                controller.show(WindowInsets.Type.captionBar())
            }
        } else {
            window.decorView.systemUiVisibility = (View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                    or View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                    or View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN)
        }
    }

    override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {

    }

    override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
    }

    override fun afterTextChanged(s: Editable?) {
        mSearchView.removeTextChangedListener(this)
        mSearchView.postDelayed(this, 600)
    }

    override fun onLogReceived(logcatItem: LogcatItem) {
        if (logcatItem.pid.toInt() != Process.myPid()) {
            return
        }
        if (!mTagFilter.contains(logcatItem.tag)) {
            mRecyclerview.post(LogTask(logcatItem))
        }
    }

    override fun onClick(v: View?) {
        when (v?.id) {
            R.id.iv_log_save -> saveLogsToLocal()
            R.id.tv_log_level -> {
            }
            R.id.iv_log_clear -> mSearchView.setText("")
            R.id.iv_log_clean -> {
                LogcatX.clear()
                mAdapter.clear()
            }
            R.id.iv_log_close -> onBackPressed()
            R.id.ib_log_down -> mRecyclerview.scrollToPosition(mAdapter.lastIndex())
        }
    }

    override fun onLongClick(v: View?): Boolean {
        val toastRes = when (v?.id) {
            R.id.iv_log_switch -> R.string.logcatx_capture
            R.id.iv_log_save -> R.string.logcatx_save
            R.id.tv_log_level -> R.string.logcatx_level
            R.id.iv_log_clean -> R.string.logcatx_empty
            R.id.iv_log_close -> R.string.logcatx_close
            else -> null
        }
        if (toastRes != null) {
            toast(toastRes)
        }
        return true
    }

    override fun onCheckedChanged(buttonView: CompoundButton?, isChecked: Boolean) {
        if (isChecked) {
            toast(R.string.logcatx_capture_pause)
            LogcatX.pause()
        } else {
            LogcatX.resume()
        }
    }

    override fun run() {
        val keyword = mSearchView.text.toString().trim()
        LogcatPrefs.logcatText = keyword
        mAdapter.setKeyword(keyword)
        mAdapter.clear()
        mLogs.forEach {
            if (mLevel == "V" || it.level == mLevel) {
                if ("" != keyword) {
                    if (it.matches(keyword)) {
                        mAdapter.addItem(it)
                    }
                } else {
                    mAdapter.addItem(it)
                }
            }
        }
        mRecyclerview.scrollToPosition(mAdapter.lastIndex())
        mEmptyView.visibility = if (keyword.isEmpty()) View.VISIBLE else View.GONE
    }

    private fun logLevel(level: String) {
        if (level == mLevel) return
        mLevel = level
        LogcatPrefs.logcatLevel = mLevel
        afterTextChanged(mSearchView.text)
        val textLevel = when (mLevel) {
            "V" -> LOG_LEVELS[0]
            "D" -> LOG_LEVELS[1]
            "I" -> LOG_LEVELS[2]
            "W" -> LOG_LEVELS[3]
            "E" -> LOG_LEVELS[4]
            else -> LOG_LEVELS[0]
        }
    }

    private fun initFilter() {
        val file = File(getExternalFilesDir(FILE_TYPE), LOGCATX_TAG_FILTER_FILE)
        if (file.exists() && file.isFile) {
            var reader: BufferedReader? = null
            try {
                reader = BufferedReader(InputStreamReader(FileInputStream(file), UTF_8))
                var tag = reader.readLine()
                while (tag != null) {
                    mTagFilter.add(tag)
                    tag = reader.readLine()
                }
            } catch (ignored: IOException) {
                toast(R.string.logcatx_read_config_fail)
            } finally {
                try {
                    reader?.close()
                } catch (ignored: IOException) {

                }
            }
        }
    }

    private fun addFilter(tag: String) {
        mTagFilter.add(tag)
        var writer: BufferedWriter? = null
        try {
            val file = File(getExternalFilesDir(FILE_TYPE), LOGCATX_TAG_FILTER_FILE)
            if (!file.isFile) {
                file.delete()
            }
            if (!file.exists()) {
                file.createNewFile()
            }
            writer = BufferedWriter(OutputStreamWriter(FileOutputStream(file, false), UTF_8))
            mTagFilter.forEach { writer.write("$it\r\n") }
            writer.flush()
            mAdapter.logs().filter { it.tag == tag }
            mAdapter.notifyDataSetChanged()
            toast("${getString(R.string.logcatx_shield_succeed)} ${file.path}")
        } catch (ignored: IOException) {
            toast(R.string.logcatx_shield_fail)
        } finally {
            try {
                writer?.close()
            } catch (ignored: IOException) {

            }
        }
    }

    private fun saveLogsToLocal() {
        var writer: BufferedWriter? = null
        try {
            val dir = getExternalFilesDir(FILE_TYPE) ?: return
            if (!dir.isDirectory) {
                dir.delete()
            }
            if (!dir.exists()) {
                dir.mkdirs()
            }
            val file = File(dir, "${SimpleDateFormat("yyyyMMdd_HHmmss", Locale.getDefault()).format(Date())}.txt")
            if (!file.isFile) {
                file.delete()
            }
            if (!file.exists()) {
                file.createNewFile()
            }
            writer = BufferedWriter(OutputStreamWriter(FileOutputStream(file, false), UTF_8))
            mAdapter.logs().forEach {
                writer.write("${it.string().replace("\n", "\r\n")}\r\n\r\n")
            }
            writer.flush()

            toast("${getString(R.string.logcatx_save_succeed)} ${file.path}")
        } catch (ignored: IOException) {
            toast(R.string.logcatx_save_fail)
        } finally {
            try {
                writer?.close()
            } catch (ignored: IOException) {

            }
        }
    }

    private fun toast(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
    }

    private fun toast(@StringRes msgRes: Int) {
        toast(getString(msgRes))
    }

    private fun copyIntoClipboard(position: Int) {
        val manager = getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
        if (manager == null) {
            toast(R.string.logcatx_copy_fail)
        } else {
            manager.setPrimaryClip(
                ClipData.newPlainText(
                    "logcatX",
                    mAdapter.item(position).log
                )
            )
            toast(R.string.logcatx_copy_succeed)
        }
    }

    private fun smartShare(position: Int) {
        val intent = Intent(Intent.ACTION_SEND)
        intent.type = "text/plain"
        intent.putExtra(Intent.EXTRA_TEXT, mAdapter.item(position).log)
        startActivity(
            Intent.createChooser(
                intent,
                getString(R.string.logcatx_options_share)
            )
        )
    }

    private inner class LogTask(private val item: LogcatItem) : Runnable {
        override fun run() {
            if (mLogs.isNotEmpty()) {
                val lastLogItem = mLogs.last()
                if (lastLogItem.level == item.level && lastLogItem.tag == item.tag) {
                    lastLogItem.append(item.log)
                    mAdapter.notifyLastItemChanged()
                    return
                }
            }

            mLogs.add(item)

            val query = mSearchView.text.toString()
            if (query.isEmpty() && mLevel == "V") {
                mAdapter.addItem(item)
                return
            }

            if (item.level == mLevel && item.matches(query)) {
                mAdapter.addItem(item)
            }
        }
    }

    companion object {
        private const val FILE_TYPE = "LogcatX"
        private const val LOGCATX_TAG_FILTER_FILE = "logcatx_tag_filter.txt"
    }
}