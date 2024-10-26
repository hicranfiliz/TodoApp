package com.example.todoapp

import android.app.Dialog
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.View
import android.view.inputmethod.EditorInfo
import android.widget.Button
import android.widget.ImageView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.widget.addTextChangedListener
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.RecyclerView
import com.example.todoapp.adapters.TaskRVVBlistAdapter
import com.example.todoapp.databinding.ActivityMainBinding
import com.example.todoapp.models.Task
import com.example.todoapp.utils.Status
import com.example.todoapp.utils.StatusResult
import com.example.todoapp.utils.StatusResult.*
import com.example.todoapp.utils.clearEdittext
import com.example.todoapp.utils.hideKeyBoard
import com.example.todoapp.utils.longToasShow
import com.example.todoapp.utils.setupDialog
import com.example.todoapp.utils.validateEdittext
import com.example.todoapp.viewmodels.TaskViewModel
import com.google.android.material.snackbar.Snackbar
import com.google.android.material.textfield.TextInputEditText
import com.google.android.material.textfield.TextInputLayout
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.collectLatest
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

        mainBinding.addTaskFABtn.setOnClickListener {
            clearEdittext(addEditTitle, addEditTitleL)
            clearEdittext(addEditDesc, addEditDescL)
            addTaskDialog.show() }
        val saveTaskBtn = addTaskDialog.findViewById<Button>(R.id.btnSaveText)
        saveTaskBtn.setOnClickListener {
            if (validateEdittext(addEditTitle, addEditTitleL) && validateEdittext(addEditDesc, addEditDescL)){

                val newTask = Task(
                    UUID.randomUUID().toString(),
                    addEditTitle.text.toString().trim(),
                    addEditDesc.text.toString().trim(),
                    Date()
                )
                hideKeyBoard(it)
                addTaskDialog.dismiss()
                taskViewModel.insertTask(newTask)
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

        val updateEditDesc = updateTaskDialog.findViewById<TextInputEditText>(R.id.edTaskDescription)
        val updateEditDescL = updateTaskDialog.findViewById<TextInputLayout>(R.id.edTaskDescriptionL)

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


        // update task end

        val taskRVVBlistAdapter = TaskRVVBlistAdapter{type, position, task ->
            if (type == "delete"){
            taskViewModel
                //.deleteTask(task)
                .deleteTaskUsingId(task.id)
                restoreDeletedTask(task)

            }else if(type == "update"){
                updateEditTitle.setText(task.title)
                updateEditDesc.setText(task.description)
                updateTaskBtn.setOnClickListener {
                    if (validateEdittext(updateEditTitle, updateEditTitleL) && validateEdittext(updateEditTitle, updateEditTitleL)){
                        val updateTask = Task(
                            task.id,
                            updateEditTitle.text.toString().trim(),
                            updateEditDesc.text.toString().trim(),
                            Date()
                        )
                        hideKeyBoard(it)
                        updateTaskDialog.dismiss()
                        taskViewModel
                            .updateTask(updateTask)
                            // updateTask paticular field date alanini guncellemez. sadece belirledigimiz alanlari gunceller.
//                            .updateTaskPaticularField(
//                                task.id,
//                                updateEditTitle.text.toString().trim(),
//                                updateEditDesc.text.toString().trim(),
//                            )

                    }
                }
                updateTaskDialog.show()
            }
        }

        // eger recycler adapter kullanirsam tum data refreshlenir.
        // listadapter kullanirsam belli datalar guncellenir.
        mainBinding.taskrv.adapter = taskRVVBlistAdapter
        taskRVVBlistAdapter.registerAdapterDataObserver(object : RecyclerView.AdapterDataObserver(){
            override fun onItemRangeInserted(positionStart: Int, itemCount: Int) {
                super.onItemRangeInserted(positionStart, itemCount)
                mainBinding.taskrv.smoothScrollToPosition(positionStart)
            }
        })
        callGetTaskList(taskRVVBlistAdapter)
        taskViewModel.getTaskList()
        statusCallback()

        callSearch()
    }

    private fun restoreDeletedTask(deletedTask: Task){
        val snackBar = Snackbar.make(
            mainBinding.root, "Deleted '${deletedTask.title}'",
            Snackbar.LENGTH_LONG
        )
        snackBar.setAction("Undo"){
            taskViewModel.insertTask(deletedTask)
        }
        snackBar.show()
    }

    private fun callSearch() {
        mainBinding.edSearch.addTextChangedListener(object : TextWatcher{
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {

            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
            }

            override fun afterTextChanged(query: Editable?) {
                if (query.toString().isNotEmpty()){
                    taskViewModel.searchTaskList(query.toString())
                }else{
                    taskViewModel.getTaskList()
                }
            }
        })

        mainBinding.edSearch.setOnEditorActionListener { v, actionId, event ->
            if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                hideKeyBoard(v)
                return@setOnEditorActionListener true
            }
            false
        }
    }

    private fun statusCallback(){
        taskViewModel
            .statusLiveData
            .observe(this){
                when(it.status){
                    Status.LOADING -> {
                        loadingDialog.show()
                    }
                    Status.SUCCESS -> {
                        loadingDialog.dismiss()
                        when(it.data as StatusResult){
                            Added ->{
                                Log.d("StatusResult", "Added")
                            }
                            Deleted ->{
                                Log.d("StatusResult", "Deleted")
                            }
                            Updated ->{
                                Log.d("StatusResult", "Updated")
                                Log.d("StatusResult", "Updated")
                            }
                        }
                        it.message?.let { it1 -> longToasShow(it1) }
                    }
                    Status.ERROR -> {
                        loadingDialog.dismiss()
                        it.message?.let { it1 -> longToasShow(it1) }
                    }
                }
            }
    }

    private fun callGetTaskList(taskRecyclerViewAdapter : TaskRVVBlistAdapter){
        CoroutineScope(Dispatchers.Main).launch {
            taskViewModel.taskStateFlow.collectLatest{
                when(it.status){
                    Status.LOADING -> {
                        loadingDialog.show()
                    }
                    Status.SUCCESS -> {
                        loadingDialog.dismiss()
                        it.data?.collect{taskList ->
                            taskRecyclerViewAdapter.submitList(taskList)
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