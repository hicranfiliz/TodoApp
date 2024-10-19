package com.example.todoapp.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.example.todoapp.models.Task
import kotlinx.coroutines.flow.Flow

@Dao
interface TaskDao {

    @Query("SELECT * FROM Task ORDER BY date DESC")
    fun getTaskList(): Flow<List<Task>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertTask(task: Task): Long

    // first way
    @Delete
    suspend fun deleteTask(task: Task): Int

    //second way (recommended)
    @Query("DELETE FROM Task WHERE taskId == :taskId")
    suspend fun deleteTaskUsingId(taskId: String): Int

    @Update
    suspend fun updateTask(task: Task): Int

    @Query("UPDATE Task SET taskTitle = :title, description = :description WHERE taskId = :taskId")
    suspend fun updateTaskPaticularField(taskId: String, title: String, description: String): Int
}