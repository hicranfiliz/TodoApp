package com.example.todoapp.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.todoapp.R
import com.example.todoapp.databinding.ViewTaskLayoutBinding
import com.example.todoapp.models.Task
import java.text.SimpleDateFormat
import java.util.Locale

class TaskRViewBindingAdapter(
    private val deleteUpdateCallback : (type: String, position : Int, task: Task) -> Unit
):
    RecyclerView.Adapter<TaskRViewBindingAdapter.ViewHolder>() {

    private val taskList = arrayListOf<Task>()

    class ViewHolder(val viewTaskLayoutBinding: ViewTaskLayoutBinding)
        : RecyclerView.ViewHolder(viewTaskLayoutBinding.root){

        }


    fun addAllTask(newTaskList : List<Task>){
        taskList.clear()
        taskList.addAll(newTaskList)
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(
            ViewTaskLayoutBinding.inflate(LayoutInflater.from(parent.context), parent, false))

    }

    override fun getItemCount(): Int {
        return taskList.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val task = taskList[position]

        holder.viewTaskLayoutBinding.txtTitle.text = task.title
        holder.viewTaskLayoutBinding.txtDesc.text = task.description

        val dateFormat = SimpleDateFormat("dd-MMM-yyyy HH:mm:ss a", Locale.getDefault())
        holder.viewTaskLayoutBinding.txtDate.text = dateFormat.format(task.date)

        holder.viewTaskLayoutBinding.ImgDelete.setOnClickListener {
            if (holder.adapterPosition != -1){
                deleteUpdateCallback("delete", position, task)
            }
        }

        holder.viewTaskLayoutBinding.ImgEdit.setOnClickListener {
            if (holder.adapterPosition != -1){
                deleteUpdateCallback("update", position, task)
            }
        }
    }


}