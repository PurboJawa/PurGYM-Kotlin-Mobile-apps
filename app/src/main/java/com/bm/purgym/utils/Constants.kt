package com.bm.purgym.utils

import android.content.Context
import androidx.appcompat.app.AlertDialog
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Date
import java.util.Locale

object Constants {
    fun convertTimestamp(millis: Long) =
        SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(Date(millis))

    fun alertDialogMessage(context: Context, message: String, title: String? = null) {
        val builder = AlertDialog.Builder(context)
        builder.setCancelable(false)

        with(builder)
        {
            if (title != null) {
                setTitle(title)
            }
            setMessage(message)
            setPositiveButton("OK") { dialog, _ -> dialog.dismiss() }
            show()
        }
    }

    fun expirationChecker(expirationTimestamp: Long, currentTimestamp: Long): Boolean {
        return expirationTimestamp > currentTimestamp
    }

    enum class Gender {
        Male,
        Female
    }
}