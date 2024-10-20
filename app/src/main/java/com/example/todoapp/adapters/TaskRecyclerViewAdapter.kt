package com.example.todoapp.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.todoapp.R
import com.example.todoapp.models.Task
import java.text.SimpleDateFormat
import java.util.Locale

class TaskRecyclerViewAdapter(
    private val deleteUpdateCallback : (type: String, position : Int, task: Task) -> Unit
):
    RecyclerView.Adapter<TaskRecyclerViewAdapter.ViewHolder>() {

    private val taskList = arrayListOf<Task>()

    class ViewHolder(itemView : View): RecyclerView.ViewHolder(itemView){
        val titleTxt : TextView = itemView.findViewById(R.id.txtTitle)
        val descTxt : TextView = itemView.findViewById(R.id.txtDesc)
        val dateTxt : TextView = itemView.findViewById(R.id.txtDate)

        val deleteImg : ImageView = itemView.findViewById(R.id.ImgDelete)
        val editImg : ImageView = itemView.findViewById(R.id.ImgEdit)

    }

    fun addAllTask(newTaskList : List<Task>){
        taskList.clear()
        taskList.addAll(newTaskList)
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(LayoutInflater.from(parent.context)
            .inflate(R.layout.view_task_layout, parent , false))

    }

    override fun getItemCount(): Int {
        return taskList.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val task = taskList[position]

        holder.titleTxt.text = task.title
        holder.descTxt.text = task.description

        val dateFormat = SimpleDateFormat("dd-MMM-yyyy HH:mm:ss a", Locale.getDefault())
        holder.dateTxt.text = dateFormat.format(task.date)

        holder.deleteImg.setOnClickListener {
            if (holder.adapterPosition != -1){
                deleteUpdateCallback("delete", position, task)
            }
        }

        holder.editImg.setOnClickListener {
            if (holder.adapterPosition != -1){
                deleteUpdateCallback("update", position, task)
            }
        }
    }


}