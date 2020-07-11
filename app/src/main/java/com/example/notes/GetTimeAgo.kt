package com.example.notes

import android.content.Context

object GetTimeAgo {
    private const val SECOND_MILLIS = 1000
    private const val MINUTE_MILLIS = 60 * SECOND_MILLIS
    private const val HOUR_MILLIS = 60 * MINUTE_MILLIS
    private const val DAY_MILLIS = 24 * HOUR_MILLIS
    fun getTimeAgo(time: String): String? {
        var times = time.toLong()
        if (times < 1000000000000L) {

            // If timestamp given in seconds, convert to millis
            times *= 1000
        }
        val now = System.currentTimeMillis()
        if (times > now || times <= 0) {
            return null
        }

        // TODO: localize
        val diff = now - times
        return if (diff < MINUTE_MILLIS) {
            "just now"
        } else if (diff < 2 * MINUTE_MILLIS) {
            "a minute ago"
        } else if (diff < 50 * MINUTE_MILLIS) {
            (diff / MINUTE_MILLIS).toString() + " minutes ago"
        } else if (diff < 90 * MINUTE_MILLIS) {
            "an hour ago"
        } else if (diff < 24 * HOUR_MILLIS) {
           (diff / HOUR_MILLIS ).toString() + " hours ago"
        } else if (diff < 48 * HOUR_MILLIS) {
            "yesterday"
        } else {
            (diff / DAY_MILLIS).toString() + " days ago"
        }
    }

}