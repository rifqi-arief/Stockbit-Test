package com.stockbit.common.utils

import android.app.ActionBar
import android.app.Dialog
import android.content.Context
import android.view.LayoutInflater
import android.view.Window
import com.stockbit.common.R
import com.stockbit.common.databinding.LoadingBinding

object Dialog {

    fun loading(context: Context) : Dialog {
        var dialog = Dialog(context, R.style.CustomDialog)
        val binding = LoadingBinding.inflate(LayoutInflater.from(context))

        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        dialog.setContentView(binding.root)
        dialog.setCancelable(false)

        val window = dialog.window
        window?.setLayout(ActionBar.LayoutParams.MATCH_PARENT, ActionBar.LayoutParams.WRAP_CONTENT)
        return dialog
    }

}