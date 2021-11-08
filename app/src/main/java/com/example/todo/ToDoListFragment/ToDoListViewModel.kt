package com.example.todo.ToDoListFragment

import androidx.lifecycle.ViewModel
import com.example.todo.database.Task

class ToDoListViewModel:ViewModel() {

    var tasks = mutableListOf<Task>()

    init {

        for (i in 0..10){
            val task = Task()
            task.title = "title $i"

            tasks += task

        }

    }


}