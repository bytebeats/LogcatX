package me.bytebeats.logcatx

import java.util.regex.Pattern

/**
 * @Author bytebeats
 * @Email <happychinapc@gmail.com>
 * @Github https://github.com/bytebeats
 * @Created at 2021/10/9 20:31
 * @Version 1.0
 * @Description represent every log's details
 */

data class LogcatItem internal constructor(
    val time: String,
    val level: String,
    val tag: String,
    val pid: String,
    var log: String
) {

    fun matches(text: CharSequence): Boolean = tag.contains(text) || log.contains(text)

    fun append(text: String?) {
        log = "${if (log.startsWith(LINE_SPACE)) "" else LINE_SPACE}$log$LINE_SPACE$text"
    }

    fun string(): String = "$time/$pid $level/$tag: $log"

    companion object {

        private val LOG_PATTERN = Pattern.compile(
            "([0-9^-]+-[0-9^ ]+\\s[0-9^:]+:[0-9^:]+\\.[0-9]+)\\s+([0-9]+)\\s+([0-9]+)\\s([VDIWEF])\\s([^\\s]*)\\s*:\\s(.*)"
        )

        fun create(line: String?): LogcatItem? {
            if (line == null) return null
            val matcher = LOG_PATTERN.matcher(line)
            return if (!matcher.find()) null else LogcatItem(
                time = matcher.group(1).orEmpty(),
                level = matcher.group(4).orEmpty(),
                tag = matcher.group(5).orEmpty(),
                pid = matcher.group(2).orEmpty(),
                log = matcher.group(6).orEmpty()
            )
        }
    }
}
