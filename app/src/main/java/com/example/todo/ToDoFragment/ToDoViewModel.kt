package com.example.todo.ToDoFragment

import android.app.TimePickerDialog
import androidx.core.content.ContentProviderCompat.requireContext
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import androidx.lifecycle.ViewModel
import com.example.todo.database.Task
import com.example.todo.database.TaskRepo
import java.io.File
import java.util.*

class ToDoViewModel:ViewModel() {

    private val taskRepo = TaskRepo.get()

    private val taskIdLiveData = MutableLiveData<UUID>()

    var taskLifeDate: LiveData<Task?> =

        Transformations.switchMap(taskIdLiveData){
            taskRepo.getTask(it)
        }

    fun loadTask(taskId: UUID){
        taskIdLiveData.value = taskId
    }

    fun updateTask(task: Task){
        taskRepo.updateTask(task)
    }


    fun deleteTask(task: Task){

        taskRepo.deleteTask(task)

    }

    fun getPhotoFile(task: Task): File{
        return taskRepo.getPhotoFile(task)
    }
}