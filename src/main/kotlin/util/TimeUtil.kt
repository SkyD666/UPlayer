package util

import java.text.SimpleDateFormat
import java.util.*

fun Long.toTimeString(
    pattern: String = "yyyy-MM-dd HH:mm:ss",
    locale: Locale = Locale.getDefault()
): String = Date(this).toTimeString(pattern, locale)

fun Date.toTimeString(
    pattern: String = "yyyy-MM-dd HH:mm:ss",
    locale: Locale = Locale.getDefault()
): String {
    val format = SimpleDateFormat(pattern, locale)
    return format.format(this)
}

/**
 * 计算距今时间
 */
fun Long.time2Now(): String {
    val nowTimeStamp = System.currentTimeMillis()
    var result = "非法输入"
    val dateDiff = nowTimeStamp - this
    if (dateDiff >= 0) {
        val bef = Calendar.getInstance().apply { time = Date(this@time2Now) }
        val aft = Calendar.getInstance().apply { time = Date(nowTimeStamp) }
        val second = dateDiff / 1000.0
        val minute = second / 60.0
        val hour = minute / 60.0
        val day = hour / 24.0
        val month =
            aft[Calendar.MONTH] - bef[Calendar.MONTH] + (aft[Calendar.YEAR] - bef[Calendar.YEAR]) * 12
        val year = month / 12.0
        result = when {
            year.toInt() > 0 -> "${year.toInt()}年前"
            month > 0 -> "${month}个月前"
            day.toInt() > 0 -> "${day.toInt()}天前"
            hour.toInt() > 0 -> "${hour.toInt()}小时前"
            minute.toInt() > 0 -> "${minute.toInt()}分钟前"
            else -> "刚刚"
        }
    }
    return result
}