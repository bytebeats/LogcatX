package me.bytebeats.logcatx

import java.io.BufferedReader
import java.io.IOException
import java.io.InputStreamReader
import kotlin.concurrent.thread

/**
 * @Author bytebeats
 * @Email <happychinapc@gmail.com>
 * @Github https://github.com/bytebeats
 * @Created at 2021/10/9 14:47
 * @Version 1.0
 * @Description TO-DO
 */

object LogcatX {

    private class LogTask : Runnable {
        override fun run() {
            var reader: BufferedReader? = null
            try {
                val process = ProcessBuilder("logcat", "-v", "threadtime").start()
                reader = BufferedReader(InputStreamReader(process.inputStream))
                var line: String? = reader.readLine()
                while (line != null) {
                    synchronized(LogcatX::class.java) {
                        if (IGNORED_LOGS.contains(line)) {
                            return@synchronized
                        }
                        val logcatItem = LogcatItem.create(line) ?: return@synchronized
                        if (!isWorking) {
                            BACKUP_LOGS.add(logcatItem)
                            return@synchronized
                        }
                        sListener?.onLogReceived(logcatItem)
                    }
                    line = reader.readLine()
                }
                pause()
            } catch (ignore: IOException) {
                pause()
            } finally {
                try {
                    reader?.close()
                } catch (ignore: IOException) {
                }
            }
        }

        companion object {
            fun start() {
                LogTask().run()
            }
        }
    }

    interface OnLogReceivedListener {
        fun onLogReceived(logcatItem: LogcatItem)
    }

    @Volatile
    private var sListener: OnLogReceivedListener? = null

    @Volatile
    private var isWorking: Boolean = false

    private val BACKUP_LOGS = mutableListOf<LogcatItem?>()

    fun start(listener: OnLogReceivedListener?) {
        isWorking = true
        thread { LogTask.start() }
        sListener = listener
    }

    fun resume() {
        isWorking = true
        if (BACKUP_LOGS.isNotEmpty()) {
            BACKUP_LOGS.forEach { logcatItem -> logcatItem?.let { sListener?.onLogReceived(it) } }
        }
        BACKUP_LOGS.clear()
    }

    fun pause() {
        isWorking = false
    }

    fun destroy() {
        isWorking = false
        sListener = null
    }

    fun clear() {
        try {
            ProcessBuilder("logcat", "-c").start()
            isWorking = true
            thread { LogTask.start() }
        } catch (ignored: IOException) {
        }
    }
}