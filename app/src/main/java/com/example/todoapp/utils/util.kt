package com.example.todoapp.utils

import android.app.Dialog
import android.widget.EditText
import android.widget.LinearLayout
import com.google.android.material.textfield.TextInputEditText
import com.google.android.material.textfield.TextInputLayout

fun Dialog.setupDialog(layoutResId : Int){
    setContentView(layoutResId)
    window!!.setLayout(
        LinearLayout.LayoutParams.MATCH_PARENT,
        LinearLayout.LayoutParams.WRAP_CONTENT,
    )
    setCancelable(false)
}

fun validateEdittext(editText: EditText, textInputLayout: TextInputLayout): Boolean {
    return when{
        editText.text.toString().trim().isEmpty() -> {
            textInputLayout.error = "Required"
            false
        }else -> {
            textInputLayout.error = null
            true
        }
    }
}