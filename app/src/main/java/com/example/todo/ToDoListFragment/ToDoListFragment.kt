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
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.todo.R
import com.example.todo.ToDoFragment.ToDoFragment
import com.example.todo.database.Task
import java.text.DateFormat
import java.util.*

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

                    val fragment = ToDoFragment()

                    activity?.let {
                        it.supportFragmentManager
                            .beginTransaction()
                            .replace(R.id.fragment_container, fragment)
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


        return view
    }


    var tasks = listOf<Task>()
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        toDoListViewModel.liveDataTasks.observe(
            viewLifecycleOwner, Observer {
                updateUI(it)
                tasks = it
            }
        )

        val itemTouchHelperCallback = object : ItemTouchHelper.SimpleCallback(0,ItemTouchHelper.RIGHT){
            override fun onMove(
                recyclerView: RecyclerView,
                viewHolder: RecyclerView.ViewHolder,
                target: RecyclerView.ViewHolder
            ): Boolean {
                return false
            }

            override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {

                val task = tasks[viewHolder.adapterPosition]
                toDoListViewModel.deleteTask(task)




            }

        }

        val itemTouchHelper = ItemTouchHelper(itemTouchHelperCallback)
        itemTouchHelper.attachToRecyclerView(toDoRv)
    }



    private fun updateUI(task: List<Task>){

        val taskAdapter = ToDoAdapter(task)
        toDoRv.adapter = taskAdapter

    }





    private inner class ToDoViewHolder(view:View): RecyclerView.ViewHolder(view), View.OnClickListener{


        lateinit var task : Task
         val isCompletedCbItemView : CheckBox = itemView.findViewById(R.id.completed_cb)
        private val titleItemView: TextView = itemView.findViewById(R.id.title_itemview)
        private val dateItemView: TextView = itemView.findViewById(R.id.date_itemview)
//        private val taskCountDown: TextView = itemView.findViewById(R.id.task_countdown_itemview)

        init {
            itemView.setOnClickListener(this)
        }

        fun bind(task: Task) {
            this.task = task

            titleItemView.text = task.title
            dateItemView.text = task.taskDate.toString()
            isCompletedCbItemView.setOnCheckedChangeListener { _, isChecked ->
                task.isCompleted = isChecked

            }








        }



//        fun dateCompare(){
//
//
//
//            if (task.taskDate.after(task.createDate) && !task.isCompleted){
//
//
//                itemView.setBackgroundColor(resources.getColor(R.color.teal_700))
//
//
//
//                Log.d("dateCompare", "task > creation")
//
//
//            }else if(task.taskDate.after(task.createDate) && task.isCompleted) {
//                itemView.setBackgroundColor(resources.getColor(R.color.purple_700))
//
//            }else if (task.taskDate.before(task.createDate)){
//
//                Log.d("dateCompare", "task < creation")
//
//            }else if (task.taskDate.equals(task.createDate)){
//
//                Log.d("dateCompare", "task == creation")
//
//            }
//            }










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
