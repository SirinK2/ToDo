package com.example.todo.ToDoListFragment

import android.annotation.SuppressLint
import android.os.Bundle
import android.os.CountDownTimer
import android.util.Log
import android.view.*
import androidx.fragment.app.Fragment
import android.widget.CheckBox
import android.widget.TextView
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.todo.R
import com.example.todo.ToDoFragment.ToDoFragment
import com.example.todo.database.Task
import java.text.DateFormat

const val KEY_ID = "task id"
class ToDoListFragment : Fragment() {

    private lateinit var toDoRv : RecyclerView



    private val toDoListViewModel by lazy { ViewModelProvider(this).get(ToDoListViewModel::class.java)}

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        super.onCreateOptionsMenu(menu, inflater)

        inflater.inflate(R.menu.task_menu,menu)

    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)

    }


    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when(item.itemId) {
            R.id.newtask -> {
                val task = Task()

                toDoListViewModel.addTask(task)

                val args = Bundle()
                args.putSerializable(KEY_ID,task.id)

                val fragment = ToDoFragment()
                fragment.arguments = args

                activity?.let {
                    it.supportFragmentManager
                        .beginTransaction()
                        .replace(R.id.fragment_container,fragment)
                        .addToBackStack(null)
                        .commit()

                }






                true
            }
            else -> super.onOptionsItemSelected(item)
        }

    }





    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        val view = inflater.inflate(R.layout.fragment_to_do_list, container, false)

        toDoRv = view.findViewById(R.id.todo_rv)

        val linearLayoutManager = LinearLayoutManager(context)

        toDoRv.layoutManager = linearLayoutManager

        //adaptor




        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        toDoListViewModel.liveDataTasks.observe(
            viewLifecycleOwner, Observer {
                updateUI(it)
            }
        )
    }



    private fun updateUI(task: List<Task>){

        val taskAdapter = ToDoAdapter(task)
        toDoRv.adapter = taskAdapter

    }





    private inner class ToDoViewHolder(view:View): RecyclerView.ViewHolder(view), View.OnClickListener{


        private lateinit var task : Task
        private val isCompletedCbItemView : CheckBox = itemView.findViewById(R.id.completed_cb)
        private val titleItemView: TextView = itemView.findViewById(R.id.title_itemview)
        private val dateItemView: TextView = itemView.findViewById(R.id.date_itemview)
        private val taskCountDown: TextView = itemView.findViewById(R.id.task_countdown_itemview)

        init {
            itemView.setOnClickListener(this)
        }

       // @SuppressLint("ResourceAsColor")
        fun bind(task: Task) {
            this.task = task

            titleItemView.text = task.title
            dateItemView.text = DateFormat.getDateInstance().format(task.taskDate)
            isCompletedCbItemView.setOnCheckedChangeListener { _, isChecked ->
                task.isCompleted = isChecked
//                if (task.isCompleted){
//                    itemView.setBackgroundColor(resources.getColor(R.color.teal_700))
//                }
            }








        }

//        private fun updateDay(){
//            val diff = task.taskDate.time - task.createDate.time
//            diff/24
//            Log.d("","")
//        }

        fun dateCompare(){



            if (task.taskDate.after(task.createDate) && !task.isCompleted){


                itemView.setBackgroundColor(resources.getColor(R.color.teal_700))



                Log.d("dateCompare", "task > creation")


            }else if(task.taskDate.after(task.createDate) && task.isCompleted) {
                itemView.setBackgroundColor(resources.getColor(R.color.purple_700))

            }else if (task.taskDate.before(task.createDate)){

                Log.d("dateCompare", "task < creation")

            }else if (task.taskDate.equals(task.createDate)){

                Log.d("dateCompare", "task == creation")

            }
            }










        override fun onClick(v: View?) {
            if ( v == itemView){

                val args = Bundle()
                args.putSerializable(KEY_ID,task.id)

                val fragment = ToDoFragment()
                fragment.arguments = args

                activity?.let { frag ->
                    frag.supportFragmentManager
                        .beginTransaction()
                        .replace(R.id.fragment_container,fragment)
                        .addToBackStack(null)
                        .commit()


                }








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




//        val format = "MMM d, yyyy"

//        val s = SimpleDateFormat("MMM d, yyyy")
//
//        val c = DateFormat.getDateInstance().format("MMM d, yyyy")

//
//            val dateCompare = when{
//                task.taskDate.after(task.createDate) -> ""
//                task.taskDate.before(task.createDate) ->""
//                else -> ""
//
//
//
//
//            }
