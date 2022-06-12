package com.example.todo.ToDoFragment


import android.Manifest
import android.annotation.SuppressLint
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.text.format.DateFormat
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.content.ContextCompat
import androidx.core.content.FileProvider
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.example.todo.DatePickerFragment
import com.example.todo.R
import com.example.todo.ToDoListFragment.KEY_ID
import com.example.todo.ToDoListFragment.ToDoListFragment
import com.example.todo.database.Task
import com.example.todo.getScaledBitmap
import com.google.android.material.textfield.TextInputLayout
import java.io.File
import java.util.*

const val DATE_KEY = "taskDate"
private const val REQUEST_PHOTO = 2

class ToDoFragment : Fragment(), DatePickerFragment.DateCallBack {
    val dateFormat = "MMM dd, yyyy"

    private lateinit var task: Task

    private lateinit var creationDateTv: TextView

    private lateinit var titleEt : EditText
    private lateinit var titleTil: TextInputLayout
    private lateinit var descriptionEt: EditText

    private lateinit var taskDateBtn:Button
    private lateinit var imgBtn : Button
    private lateinit var addNewTask: Button

    private lateinit var photoFile: File
    private lateinit var photoUri: Uri

    private lateinit var imgView : ImageView


    private fun updatePhotoView(){

        if (photoFile.exists()){

            val bitmap = getScaledBitmap(photoFile.path, requireActivity())

            imgView.setImageBitmap(bitmap)


        }else{

            imgView.setImageDrawable(null)

        }
    }




    private val getResultLauncher =
        registerForActivityResult(ActivityResultContracts.TakePicture()){



        }

    private val requestPermission =
        registerForActivityResult(ActivityResultContracts.RequestPermission()){


        }

    private val toDoViewModel by lazy { ViewModelProvider(this).get(ToDoViewModel::class.java) }



    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        val view =  inflater.inflate(R.layout.fragment_to_do, container, false)

        init(view)

        creationDateTv.text = DateFormat.format(dateFormat,task.createDate)


        return view

    }

    private fun init(view: View) {

        titleEt = view.findViewById(R.id.todo_title_edit_text)
        titleTil = view.findViewById(R.id.title_til)
        descriptionEt = view.findViewById(R.id.todo_description_edit_text)
        taskDateBtn = view.findViewById(R.id.task_date_btn)
        creationDateTv = view.findViewById(R.id.creation_date_tv)
        addNewTask = view.findViewById(R.id.add_task)
        imgView = view.findViewById(R.id.task_image_view)
        imgBtn = view.findViewById(R.id.img_btn)

    }









    @SuppressLint("QueryPermissionsNeeded")
    override fun onStart() {
        super.onStart()

        imgBtn.setOnClickListener {

            when(PackageManager.PERMISSION_GRANTED){

                context?.let {
                    ContextCompat.checkSelfPermission(
                        it,
                        Manifest.permission.CAMERA
                    )
                } -> getResultLauncher.launch(photoUri)

                else -> requestPermission.launch(Manifest.permission.CAMERA)





            }




        }


        taskDateBtn.setOnClickListener {

            val args = Bundle()
            args.putSerializable(DATE_KEY,task.taskDate)

            val datePicker = DatePickerFragment()
            datePicker.arguments = args
            datePicker.setTargetFragment(this,0)
            datePicker.show(this.parentFragmentManager,"date")

        }


        addNewTask.setOnClickListener {

            if (task.title.isNotEmpty()){

                toDoViewModel.updateTask(task)

                val fragment = ToDoListFragment()


                activity?.let {
                    it.supportFragmentManager
                        .beginTransaction()
                        .replace(R.id.fragment_container, fragment)
                        .addToBackStack(null)
                        .commit()
                }


            }
        }



        val textWatcher = object : TextWatcher{
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

        val textWatcher1 = object : TextWatcher{
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

        val taskId = arguments?.getSerializable(KEY_ID) as UUID?

        taskId?.let {

            toDoViewModel.loadTask(it)

        }




    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)



        toDoViewModel.taskLifeDate.observe(
            viewLifecycleOwner, androidx.lifecycle.Observer {
                it?.let {

                    task = it
                    titleEt.setText(it.title)
                    descriptionEt.setText(it.description)

                    if (task.taskDate != null) {

                        taskDateBtn.text = DateFormat.format(dateFormat, it.taskDate)

                    }
                    creationDateTv.text = DateFormat.format(dateFormat,task.createDate)

                    photoFile = toDoViewModel.getPhotoFile(task)
                    photoUri = FileProvider.getUriForFile(requireContext(), "com.example.todo", photoFile)



                    updatePhotoView()



                }
            }
        )


    }

    override fun onDateSelected(date: Date) {

        task.taskDate = date

        if (task.taskDate != null) {

            taskDateBtn.text = DateFormat.format(dateFormat, task.taskDate)
        }
    }

    override fun onStop() {
        super.onStop()

        toDoViewModel.updateTask(task)

    }





}
