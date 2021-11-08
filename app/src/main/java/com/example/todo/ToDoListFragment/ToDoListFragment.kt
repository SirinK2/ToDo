package com.example.todo.ToDoListFragment

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CheckBox
import android.widget.TextView
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.todo.R
import com.example.todo.database.Task
import java.text.DateFormat


class ToDoListFragment : Fragment() {

    private lateinit var toDoRv : RecyclerView



    private val toDoListViewModel by lazy { ViewModelProvider(this).get(ToDoListViewModel::class.java)}


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        val view = inflater.inflate(R.layout.fragment_to_do_list, container, false)

        toDoRv = view.findViewById(R.id.todo_rv)

        val linearLayoutManager = LinearLayoutManager(context)

        toDoRv.layoutManager = linearLayoutManager

        //adaptor
        updateUI()



        return view
    }
    private fun updateUI(){

        val taskAdapter = ToDoAdapter(toDoListViewModel.tasks)
        toDoRv.adapter = taskAdapter
    }



    private inner class ToDoViewHolder(view:View): RecyclerView.ViewHolder(view){


        private lateinit var task : Task
        private val isCompletedCbItemView : CheckBox = itemView.findViewById(R.id.completed_cb)
        private val titleItemView: TextView = itemView.findViewById(R.id.title_itemview)
        private val dateItemView: TextView = itemView.findViewById(R.id.date_itemview)
        private val taskCountDown: TextView = itemView.findViewById(R.id.task_countdown_itemview)


        fun bind(task: Task) {

            titleItemView.text = task.title
            dateItemView.text = DateFormat.getDateInstance().format(task.date)
            isCompletedCbItemView.setOnCheckedChangeListener { _, isChecked ->
                task.isCompleted = isChecked
            }



        }


    }


    private inner class ToDoAdapter(var tasks: List<Task>): RecyclerView.Adapter<ToDoViewHolder>(){

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ToDoViewHolder {

            val view = layoutInflater.inflate(R.layout.todo_itemview,parent,false)

            return ToDoViewHolder(view)
        }

        override fun onBindViewHolder(holder: ToDoViewHolder, position: Int) {

            val task = tasks[position]

            holder.bind(task)

        }

        override fun getItemCount(): Int = tasks.size

    }








}