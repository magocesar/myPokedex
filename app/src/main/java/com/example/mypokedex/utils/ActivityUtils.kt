package com.example.mypokedex.utils

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity

object ActivityUtils {
    fun navigateToActivity(currentActivity: AppCompatActivity, targetActivityClass: Class<*>, extras: Bundle? = null, shouldFinish: Boolean = true) {
        val intent = Intent(currentActivity, targetActivityClass)
        extras?.let {
            intent.putExtras(it)
        }
        currentActivity.startActivity(intent)
        if (shouldFinish) {
            currentActivity.finish()
        }
    }
}