package com.example.todo.database

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.room.Room
import java.io.File
import java.util.*
import java.util.concurrent.Executor
import java.util.concurrent.Executors

const val DATABASE_KEY = "database"
class TaskRepo private constructor(context: Context){


    val database = Room.databaseBuilder(
        context.applicationContext,
        TaskDatabase::class.java,
        DATABASE_KEY

    ).build()

    private val taskDao = database.taskDao()

    private val executor = Executors.newSingleThreadExecutor()
    private val fileDir = context.applicationContext.filesDir


    fun getAllTasks(): LiveData<List<Task>> = taskDao.getAllTasks()

    fun getTask(id: UUID): LiveData<Task?> = taskDao.getTask(id)


    fun updateTask(task: Task){
        executor.execute {
            taskDao.updateTask(task)
        }
    }

    fun addTask(task: Task){
        executor.execute {
            taskDao.addTask(task)
        }
    }


    fun deleteTask(task: Task){
        executor.execute {
            taskDao.deleteTask(task)
        }
    }

    fun getPhotoFile(task: Task): File = File(fileDir,task.photoFileName)







    companion object{

        private var INSTANCE: TaskRepo? = null

        fun initialize(context: Context){
            if (INSTANCE == null){
                INSTANCE = TaskRepo(context)
            }
        }

        fun get(): TaskRepo{
            return INSTANCE ?: throw IllegalStateException("")
        }

    }
}