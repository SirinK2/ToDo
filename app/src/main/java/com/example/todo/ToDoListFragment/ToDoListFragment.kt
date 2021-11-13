package com.example.todo.ToDoListFragment


import android.annotation.SuppressLint
import android.os.Bundle
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
import android.text.format.DateFormat
import android.widget.ImageView
import com.example.todo.ToDoBottomSheetFragment
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import java.text.SimpleDateFormat
import java.util.*

const val KEY_ID = "task id"
class ToDoListFragment : Fragment() {

    private lateinit var toDoRv : RecyclerView
    private lateinit var toDoBottomSheetFragment: BottomSheetDialogFragment


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
            ): Boolean { return false }


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

        val dateFormat = "MMM dd, yyyy"

        lateinit var task : Task

        private var isCompletedCbItemView : CheckBox = itemView.findViewById(R.id.completed_cb)
        private val titleItemView: TextView = itemView.findViewById(R.id.title_itemview)
        private val dateItemView: TextView = itemView.findViewById(R.id.date_itemview)
        private val taskCountDown: TextView = itemView.findViewById(R.id.task_countdown_itemview)
        private val editImageView: ImageView = itemView.findViewById(R.id.edit_iv)

        init {

            itemView.setOnClickListener(this)
            editImageView.setOnClickListener(this)
        }


        @SuppressLint("SimpleDateFormat")
        fun bind(task: Task) {

            this.task = task

            isCompletedCbItemView.isChecked = task.isCompleted

            titleItemView.text = task.title

            if (task.taskDate != null) {

                dateItemView.text = DateFormat.format(dateFormat, task.taskDate)
            }


            isCompletedCbItemView.setOnCheckedChangeListener { _, isChecked ->


                task.isCompleted = isChecked
                toDoListViewModel.updateTask(task)





            }

            val date1 = Date()

            val sdf = SimpleDateFormat("MMM dd, yyyy")
                sdf.parse(sdf.format(date1))

            val date2 = task.taskDate

            date2?.let {

                sdf.parse(sdf.format(it)) as Date

            }


            val diff = (date2?.time ?: date1.time) - date1.time

            val days = diff / (24)

            val dayFormat = days / (60 * 60 * 1000)

            val day = "$dayFormat day"

            taskCountDown.text = day

            if(days.toInt() <= 0 ){

                isCompletedCbItemView.isEnabled = false

            }

        }


        override fun onClick(v: View?) {

            if ( v == editImageView){

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



            if (v == itemView){

                val args = Bundle()
                args.putSerializable(KEY_ID,task.id)


                toDoBottomSheetFragment = ToDoBottomSheetFragment()
                toDoBottomSheetFragment.arguments = args


                toDoBottomSheetFragment.show(parentFragmentManager,toDoBottomSheetFragment.tag)


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

