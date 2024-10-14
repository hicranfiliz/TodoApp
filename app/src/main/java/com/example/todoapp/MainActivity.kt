package com.example.todoapp

import android.app.Dialog
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.widget.Button
import android.widget.ImageView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.widget.addTextChangedListener
import com.example.todoapp.databinding.ActivityMainBinding
import com.example.todoapp.utils.setupDialog
import com.example.todoapp.utils.validateEdittext
import com.google.android.material.textfield.TextInputEditText
import com.google.android.material.textfield.TextInputLayout

class MainActivity : AppCompatActivity() {

    private val mainBinding: ActivityMainBinding by lazy{
        ActivityMainBinding.inflate(layoutInflater)
    }

    private val addTaskDialog : Dialog by lazy {
        Dialog(this, R.style.DialogCustomTheme).apply {
            setupDialog(R.layout.add_task_dialog)
        }
    }

    private val updateTaskDialog : Dialog by lazy {
        Dialog(this, R.style.DialogCustomTheme).apply {
            setupDialog(R.layout.update_task_layout)
        }
    }

    private val loadingDialog : Dialog by lazy {
        Dialog(this, R.style.DialogCustomTheme).apply {
            setupDialog(R.layout.loading_dialog)
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(mainBinding.root)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        // add task start
        val addCloseBtn = addTaskDialog.findViewById<ImageView>(R.id.ImgClose)
        addCloseBtn.setOnClickListener { addTaskDialog.dismiss() }


        val addEditTitle = addTaskDialog.findViewById<TextInputEditText>(R.id.edTaskTitle)
        val addEditTitleL = addTaskDialog.findViewById<TextInputLayout>(R.id.edTaskTitleL)

        addEditTitle.addTextChangedListener { object : TextWatcher{
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}
            override fun afterTextChanged(s: Editable?) {
                validateEdittext(addEditTitle, addEditTitleL)
            }
        } }

        val addEditDesc = addTaskDialog.findViewById<TextInputEditText>(R.id.edTaskDescription)
        val addEditDescL = addTaskDialog.findViewById<TextInputLayout>(R.id.edTaskDescriptionL)

        addEditDesc.addTextChangedListener { object : TextWatcher{
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}
            override fun afterTextChanged(s: Editable?) {
                validateEdittext(addEditDesc, addEditDescL)
            }
        } }

        mainBinding.addTaskFABtn.setOnClickListener { addTaskDialog.show() }
        val saveTaskBtn = addTaskDialog.findViewById<Button>(R.id.btnSaveText)
        saveTaskBtn.setOnClickListener {
            if (validateEdittext(addEditTitle, addEditTitleL) && validateEdittext(addEditDesc, addEditDescL)){
                addTaskDialog.dismiss()
                Toast.makeText(this, "validated!!", Toast.LENGTH_SHORT).show()
                loadingDialog.show()
            }
        }
        // add task end

        // update task start
        val updateEditTitle = updateTaskDialog.findViewById<TextInputEditText>(R.id.edTaskTitle)
        val updateEditTitleL = addTaskDialog.findViewById<TextInputLayout>(R.id.edTaskTitleL)

        updateEditTitle.addTextChangedListener { object : TextWatcher{
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}
            override fun afterTextChanged(s: Editable?) {
                validateEdittext(updateEditTitle, updateEditTitleL)
            }
        } }

        val updateEditDesc = addTaskDialog.findViewById<TextInputEditText>(R.id.edTaskDescription)
        val updateEditDescL = addTaskDialog.findViewById<TextInputLayout>(R.id.edTaskDescriptionL)

        updateEditDesc.addTextChangedListener { object : TextWatcher{
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}
            override fun afterTextChanged(s: Editable?) {
                validateEdittext(updateEditTitle, updateEditTitleL)
            }
        } }

        val updateCloseBtn = updateTaskDialog.findViewById<ImageView>(R.id.ImgClose)
        updateCloseBtn.setOnClickListener { updateTaskDialog.dismiss() }

        val updateTaskBtn = updateTaskDialog.findViewById<Button>(R.id.btnUpdateTask)
        updateTaskBtn.setOnClickListener {
            if (validateEdittext(updateEditTitle, updateEditTitleL) && validateEdittext(updateEditTitle, updateEditTitleL)){
                addTaskDialog.dismiss()
                Toast.makeText(this, "validated!!", Toast.LENGTH_SHORT).show()
                loadingDialog.show()
            }
        }

        // update task end
    }
}