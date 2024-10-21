package com.example.todoapp.utils

import android.app.Dialog
import android.content.Context
import android.inputmethodservice.InputMethodService
import android.view.View
import android.view.inputmethod.InputMethodManager
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

enum class StatusResult{
    Added,
    Updated,
    Deleted
}

fun Context.hideKeyBoard(view: View){
    try {
        val imm = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        imm.hideSoftInputFromWindow(view.windowToken, 0)
    }catch (e: Exception){
        e.printStackTrace()
    }
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