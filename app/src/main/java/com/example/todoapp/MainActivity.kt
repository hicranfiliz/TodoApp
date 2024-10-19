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
import androidx.lifecycle.ViewModelProvider
import com.example.todoapp.adapters.TaskRecyclerViewAdapter
import com.example.todoapp.databinding.ActivityMainBinding
import com.example.todoapp.models.Task
import com.example.todoapp.utils.Status
import com.example.todoapp.utils.clearEdittext
import com.example.todoapp.utils.longToasShow
import com.example.todoapp.utils.setupDialog
import com.example.todoapp.utils.validateEdittext
import com.example.todoapp.viewmodels.TaskViewModel
import com.google.android.material.textfield.TextInputEditText
import com.google.android.material.textfield.TextInputLayout
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.launch
import java.util.Date
import java.util.UUID

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

    private val taskViewModel : TaskViewModel by lazy {
        ViewModelProvider(this)[TaskViewModel::class.java]
    }

    private val taskRecyclerViewAdapter :  TaskRecyclerViewAdapter by lazy {
        TaskRecyclerViewAdapter(){ position, task ->
            taskViewModel
                .deleteTask(task)
                .observe(this){
                    when(it.status){
                        Status.LOADING -> {
                            loadingDialog.show()
                        }
                        Status.SUCCESS -> {
                            loadingDialog.dismiss()
                            if (it.date != -1){
                                longToasShow("Task Deleted Successfully!")
                            }
                        }
                        Status.ERROR -> {
                            loadingDialog.dismiss()
                            it.message?.let { it1 -> longToasShow(it1) }
                        }
                    }
                }
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

        mainBinding.taskrv.adapter = taskRecyclerViewAdapter

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

        mainBinding.addTaskFABtn.setOnClickListener {
            clearEdittext(addEditTitle, addEditTitleL)
            clearEdittext(addEditDesc, addEditDescL)
            addTaskDialog.show() }
        val saveTaskBtn = addTaskDialog.findViewById<Button>(R.id.btnSaveText)
        saveTaskBtn.setOnClickListener {
            if (validateEdittext(addEditTitle, addEditTitleL) && validateEdittext(addEditDesc, addEditDescL)){
                addTaskDialog.dismiss()
                val newTask = Task(
                    UUID.randomUUID().toString(),
                    addEditTitle.text.toString().trim(),
                    addEditDesc.text.toString().trim(),
                    Date()
                )
                taskViewModel.insertTask(newTask).observe(this){
                    when(it.status){
                        Status.LOADING -> {
                            loadingDialog.show()
                        }
                        Status.SUCCESS -> {
                            loadingDialog.dismiss()
                            if (it.date?.toInt() != -1){
                                longToasShow("Task Added Successfully!")
                            }
                        }
                        Status.ERROR -> {
                            loadingDialog.dismiss()
                            it.message?.let { it1 -> longToasShow(it1) }
                        }
                    }
                }
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
        callGetTaskList()
    }

    private fun callGetTaskList(){
        loadingDialog.show()
        CoroutineScope(Dispatchers.Main).launch {
            taskViewModel.getTaskList().collect{
                when(it.status){
                    Status.LOADING -> {
                        loadingDialog.show()
                    }
                    Status.SUCCESS -> {
                        it.date?.collect{taskList ->
                            loadingDialog.dismiss()
                            taskRecyclerViewAdapter.addAllTask(taskList)
                        }
                    }
                    Status.ERROR -> {
                        loadingDialog.dismiss()
                        it.message?.let { it1 -> longToasShow(it1) }
                    }
                }
        }

        }
    }
}