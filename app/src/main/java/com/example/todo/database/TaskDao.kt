package com.example.todo.database

import androidx.lifecycle.LiveData
import androidx.room.*
import java.util.*

@Dao
interface TaskDao {

    @Query("SELECT * FROM task")
    fun getAllTasks(): LiveData<List<Task>>

    @Query("SELECT * FROM task WHERE id= (:id)")
    fun getTask(id: UUID): LiveData<Task?>

    @Update
    fun updateTask(task: Task)

    @Insert
    fun addTask(task: Task)

    @Delete

    fun deleteTask(task: Task)



}