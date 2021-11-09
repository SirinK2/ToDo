package com.example.todo.ToDoFragment

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import androidx.lifecycle.ViewModelProvider
import com.example.todo.DatePickerFragment
import com.example.todo.R
import com.example.todo.ToDoListFragment.ToDoListFragment
import com.example.todo.database.Task
import java.text.DateFormat
import java.util.*

const val DATE_KEY = "taskDate"
class ToDoFragment : Fragment(), DatePickerFragment.DateCallBack {

    private lateinit var titleTv: TextView
    private lateinit var titleEt : EditText
    private lateinit var descriptionTv : TextView
    private lateinit var descriptionEt: EditText
    private lateinit var taskDateBtn:Button
    private lateinit var creationDateTv: TextView
    private lateinit var isCompletedTv : TextView
    private lateinit var addNewTask: Button

    private lateinit var task: Task

    private val toDoViewModel by lazy { ViewModelProvider(this).get(ToDoViewModel::class.java) }



    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
       val view =  inflater.inflate(R.layout.fragment_to_do, container, false)

        titleTv = view.findViewById(R.id.todo_title_tv)
        titleEt = view.findViewById(R.id.todo_title_edit_text)
        descriptionTv = view.findViewById(R.id.todo_description_tv)
        descriptionEt = view.findViewById(R.id.todo_description_edit_text)
        taskDateBtn = view.findViewById(R.id.task_date_btn)
        creationDateTv = view.findViewById(R.id.creation_date_tv)
        isCompletedTv = view.findViewById(R.id.is_completed_tv)
        addNewTask = view.findViewById(R.id.add_task)





        creationDateTv.text = DateFormat.getDateInstance().format(task.createDate)

        creationDateTv.setOnClickListener {

        }




        return view

    }

//    fun dateCompare(){
//
////        val format = "MMM d, yyyy"
//
////        val s = SimpleDateFormat("MMM d, yyyy")
////
////        val c = DateFormat.getDateInstance().format("MMM d, yyyy")
//
//
//        val dateCompare = when{
//            task.taskDate.after(task.createDate) -> ""
//            task.taskDate.before(task.createDate) ->""
//            else -> ""
//
//
//
//
//        }
//
//
//       if (task.taskDate.after(task.createDate) && !task.isCompleted){
//
//
//
//            Log.d("dateCompare", "task > creation")
//
//
//        }else if (task.taskDate.before(task.createDate)){
//
//            Log.d("dateCompare", "task < creation")
//
//        }else if (task.taskDate.equals(task.createDate)){
//
//            Log.d("dateCompare", "task == creation")
//
//        }
//
//
//
//
//
//
//
//    }
//






    override fun onStart() {
        super.onStart()

        taskDateBtn.setOnClickListener {

            val args = Bundle()
            args.putSerializable(DATE_KEY,task.taskDate)

            val datePicker = DatePickerFragment()
            datePicker.arguments = args
            datePicker.setTargetFragment(this,0)
            datePicker.show(this.parentFragmentManager,"date")

        }


        addNewTask.setOnClickListener {
            val task = Task()

            toDoViewModel.addTask(task)



            val fragment = ToDoListFragment()


            activity?.let {
                it.supportFragmentManager
                    .beginTransaction()
                    .replace(R.id.fragment_container,fragment)
                    .addToBackStack(null)
                    .commit()
            }

        }


        var textWatcher = object : TextWatcher{
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {

                task.title = s.toString()

            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {

                task.title = s.toString()

            }

            override fun afterTextChanged(s: Editable?) {

                task.title = s.toString()


            }

        }

        var textWatcher1 = object : TextWatcher{
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
                task.description = s.toString()
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                task.description = s.toString()
            }

            override fun afterTextChanged(s: Editable?) {
                task.description = s.toString()
            }

        }


        titleEt.addTextChangedListener(textWatcher)
        descriptionEt.addTextChangedListener(textWatcher1)




    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        task = Task()
//
//        val taskId = arguments?.getSerializable(KEY_ID) as UUID
//
//        toDoViewModel.loadTask(taskId)

    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        toDoViewModel.taskLifeDate.observe(
            viewLifecycleOwner, androidx.lifecycle.Observer {
                it?.let {
                    task = it
                    titleEt.setText(it.title)
                    descriptionEt.setText(it.description)
                    taskDateBtn.text = DateFormat.getDateInstance().format(it.taskDate)
                    creationDateTv.text = DateFormat.getDateInstance().format(it.createDate)
                    //add completed task
                    isCompletedTv.text = it.isCompleted.toString()



                }
            }
        )


    }

    override fun onDateSelected(date: Date) {
        task.taskDate = date
        taskDateBtn.text = task.taskDate.toString()

    }


    override fun onStop() {
        super.onStop()
        if (task.title.isEmpty()){

            toDoViewModel.deleteTask(task)
        }else {
            toDoViewModel.updateTask(task)
        }
    }


}

