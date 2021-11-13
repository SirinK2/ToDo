package com.example.todo

import android.annotation.SuppressLint
import android.app.TimePickerDialog
import android.net.Uri
import android.os.Bundle
import android.os.CountDownTimer
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.core.content.FileProvider
import androidx.lifecycle.ViewModelProvider
import com.example.todo.ToDoFragment.ToDoViewModel
import com.example.todo.ToDoListFragment.KEY_ID
import com.example.todo.database.Task
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import java.io.File
import android.text.format.DateFormat
import java.util.*

class ToDoBottomSheetFragment: BottomSheetDialogFragment() {

    var task = Task()

    private lateinit var titleTv : TextView
    private lateinit var descriptionTv : TextView
    private lateinit var showTimeTv : TextView
    private lateinit var showImg: ImageView
    private lateinit var selectTime : Button
    private lateinit var photoFile : File
    private lateinit var photoUri: Uri

//    private lateinit var countDownTimer: CountDownTimer

    private val toDoViewModel by lazy { ViewModelProvider(this).get(ToDoViewModel::class.java) }

    private val timePickerDialogListener: TimePickerDialog.OnTimeSetListener =
        TimePickerDialog.OnTimeSetListener { _, hourOfDay, minute ->

            val formattedTime = when {
                hourOfDay == 0 -> {
                    if (minute < 10) {
                        "${hourOfDay + 12}:0${minute} am"
                    } else {
                        "${hourOfDay + 12}:${minute} am"
                    }
                }
                hourOfDay > 12 -> {
                    if (minute < 10) {
                        "${hourOfDay - 12}:0${minute} pm"
                    } else {
                        "${hourOfDay - 12}:${minute} pm"
                    }
                }
                hourOfDay == 12 -> {
                    if (minute < 10) {
                        "${hourOfDay}:0${minute} pm"
                    } else {
                        "${hourOfDay}:${minute} pm"
                    }
                }
                else -> {
                    if (minute < 10) {
                        "${hourOfDay}:${minute} am"
                    } else {
                        "${hourOfDay}:${minute} am"
                    }
                }
            }

            showTimeTv.text = formattedTime
        }



    private fun updatePhotoView(){

        if (photoFile.exists()){


            val bitmap = getScaledBitmap(photoFile.path, requireActivity())

            showImg.setImageBitmap(bitmap)

        }else{

            showImg.setImageDrawable(null)

        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val form = "hh:mm a"
        photoFile = toDoViewModel.getPhotoFile(task)

        toDoViewModel.taskLifeDate.observe(
            viewLifecycleOwner, androidx.lifecycle.Observer {
                it?.let {
                    task = it
                    titleTv.text = it.title.uppercase()
                    descriptionTv.text = it.description.replace("\n", " ")
                    photoFile = toDoViewModel.getPhotoFile(task)
                    photoUri = FileProvider.getUriForFile(requireContext(), "com.example.todo", photoFile)

                    updatePhotoView()


                }

            }
        )

    }



    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        val view = inflater.inflate(R.layout.fragment_bottom_sheet,container,false)

        titleTv = view.findViewById(R.id.title_tv_botton_sheet)
        descriptionTv = view.findViewById(R.id.description_tv_bottom_sheet)
        showImg = view.findViewById(R.id.image_view_bottom_sheet)
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


//    @SuppressLint("SimpleDateFormat")
//    fun timer(){
////        val sdf =
//
//        val currentTime = Calendar.getInstance().time
//        val sdf = SimpleDateFormat("hh:mm")
//        sdf.parse(sdf.format(currentTime))
//        val endDateDay = task.createDate
//        val format1 = SimpleDateFormat("hh:mm",Locale.getDefault())
//        val endDate = format1.parse(sdf.format(endDateDay))
//
////        val endTime =
////       val endTime = task.taskDate?.let {
////            sdf.parse(sdf.format(it))
////        }
//
//
////        var diff =   - currentTime.time
//        countDownTimer = object : CountDownTimer(,1000){
//            override fun onTick(millisUntilFinished: Long) {
//                var diff = millisUntilFinished
//                val sec : Long = 1000
//                val min = sec * 60
//                val hour = min * 60
//
//                val elapsedHours = diff / hour
//                diff %= hour
//
//                val elapsedMin = diff / min
//                diff %= min
//
//                val timer = "${millisUntilFinished / 1000} sec"
//                showTimeTv.text = timer
//
//
//
//
//
//            }
//
//            override fun onFinish() {
//                showTimeTv.text = "done"
//            }
//
//
//        }.start()
//
//
//
//
//
//    }
//


    override fun onStart() {
        super.onStart()

        val sheetContainer = requireView().parent as? ViewGroup ?: return
        sheetContainer.layoutParams.height = ViewGroup.LayoutParams.MATCH_PARENT



        showTimeTv.setOnClickListener {


            val timePicker =
                TimePickerDialog(requireContext(), timePickerDialogListener, 12, 0, false)

            timePicker.show()


        }
    }
}

