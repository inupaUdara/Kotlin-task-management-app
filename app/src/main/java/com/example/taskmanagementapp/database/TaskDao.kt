package com.example.taskmanagementapp.database

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.example.taskmanagementapp.model.Task

@Dao
interface TaskDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertTask(task: Task)

    @Update
    suspend fun updateTask(task: Task)

    @Delete
    suspend fun deleteTask(task: Task)

    @Query("SELECT * FROM TASKS ORDER BY id DESC")
    fun getAllTasks(): LiveData<List<Task>>

    @Query("SELECT * FROM TASKS WHERE status = 'To-Do' ORDER BY id DESC")
    fun getAllTodoTasks(): LiveData<List<Task>>

    @Query("SELECT * FROM TASKS WHERE status = 'Completed' ORDER BY id DESC")
    fun getAllCompleteTasks(): LiveData<List<Task>>

    @Query("SELECT * FROM TASKS WHERE status = 'In-Progress' ORDER BY id DESC")
    fun getAllProgressTasks(): LiveData<List<Task>>

    @Query("SELECT * FROM TASKS WHERE taskTitle LIKE :query OR taskDesc LIKE :query")
    fun searchTask(query: String?): LiveData<List<Task>>


}