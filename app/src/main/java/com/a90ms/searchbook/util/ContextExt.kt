package com.a90ms.searchbook.util

import android.app.Activity
import android.content.Context
import android.widget.Toast

fun Context.toast(msg: String) = Toast.makeText(this, msg, Toast.LENGTH_SHORT).show()

fun Context?.isValidContext() = when (this) {
    null -> false
    is Activity -> (isDestroyed || isFinishing).not()
    else -> true
}