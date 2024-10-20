package com.example.todoapp.utils

import android.app.Dialog
import android.content.Context
import android.widget.EditText
import android.widget.LinearLayout
import android.widget.Toast
import com.google.android.material.textfield.TextInputEditText
import com.google.android.material.textfield.TextInputLayout

enum class Status{
    SUCCESS,
    ERROR,
    LOADING
}

fun Context.longToasShow(msg: String){
    Toast.makeText(this, msg, Toast.LENGTH_LONG).show()
}

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

fun clearEdittext(editText: EditText, textInputLayout: TextInputLayout) {
    editText.text = null
    textInputLayout.error = null
}