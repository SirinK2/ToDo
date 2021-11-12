package com.example.todo

import android.app.Activity
import android.app.TimePickerDialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import android.widget.TimePicker
import androidx.lifecycle.ViewModelProvider
import com.example.todo.ToDoFragment.ToDoViewModel
import com.example.todo.ToDoListFragment.KEY_ID
import com.example.todo.database.Task
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import java.util.*

class ToDoBottomSheetFragment: BottomSheetDialogFragment() {

    var task = Task()

    private lateinit var titleTv : TextView
    private lateinit var descriptionTv : TextView
    private lateinit var showTimeTv : TextView
    private lateinit var selectTime : Button

    val toDoViewModel by lazy { ViewModelProvider(this).get(ToDoViewModel::class.java) }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        toDoViewModel.taskLifeDate.observe(
            viewLifecycleOwner, androidx.lifecycle.Observer {
                it?.let {
                    task = it
                    titleTv.text = it.title
                }

            }
        )

//
//
     }



    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        val view = inflater.inflate(R.layout.fragment_bottom_sheet,container,false)

        titleTv = view.findViewById(R.id.title_tv_botton_sheet)
        descriptionTv = view.findViewById(R.id.description_tv_bottom_sheet)
        showTimeTv = view.findViewById(R.id.timer1_tv_bottom_sheet)
        selectTime = view.findViewById(R.id.timer_btn_bottom_sheet)

        return view
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setStyle(STYLE_NORMAL,R.style.AppBottomSheetDialogTheme)

        val taskId = arguments?.getSerializable(KEY_ID) as UUID?

        taskId?.let {

            toDoViewModel.loadTask(it)

        }







//        val timePicker: TimePickerDialog
//        val currentTime = Calendar.getInstance()
//        val hour = currentTime.get(Calendar.HOUR_OF_DAY)
//        val min = currentTime.get(Calendar.MINUTE)
//
//        timePicker = TimePickerDialog( this ,
//            TimePickerDialog.OnTimeSetListener { view, hourOfDay, minute ->
//            selectTime.text = String.format("%d : %d", hourOfDay,minute)
//        }
//            , hour,min, false)
//
//        selectTime.setOnClickListener {
//            timePicker.show()
//        }
    }






    override fun onStart() {
        super.onStart()

        val sheetContainer = requireView().parent as? ViewGroup ?: return
        sheetContainer.layoutParams.height = ViewGroup.LayoutParams.MATCH_PARENT

        titleTv.text = task.title

        selectTime.setOnClickListener {


        }





    }



//    fun setTimeBtn(view: View){
//        val timePikerFragment = TimePikerFragment()
//        timePikerFragment.show(parentFragmentManager,"Select time")
//
//    }
//
//    fun setTime(hours: Int, minutes: Int): String {
//        return "$hours : $minutes"
//    }

}