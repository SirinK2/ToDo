package com.example.todo.ToDoListFragment
import androidx.lifecycle.ViewModel
import com.example.todo.database.Task
import com.example.todo.database.TaskRepo
import java.util.*

class ToDoListViewModel:ViewModel() {


    private val taskRepo = TaskRepo.get()

    val liveDataTasks = taskRepo.getAllTasks()

    fun addTask(task: Task){

        taskRepo.addTask(task)

    }

    fun deleteTask(task: Task){

        taskRepo.deleteTask(task)

    }

    fun updateTask(task: Task){
        taskRepo.updateTask(task)
    }


}