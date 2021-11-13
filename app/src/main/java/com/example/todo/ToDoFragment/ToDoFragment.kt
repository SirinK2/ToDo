package com.example.todo.ToDoFragment

import android.annotation.SuppressLint
import android.app.Activity
import android.app.Activity.RESULT_OK
import android.content.Intent
import android.content.pm.PackageManager
import android.content.pm.ResolveInfo
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.text.Editable
import android.text.TextWatcher
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import com.example.todo.DatePickerFragment
import com.example.todo.R
import com.example.todo.ToDoListFragment.KEY_ID
import com.example.todo.ToDoListFragment.ToDoListFragment
import com.example.todo.database.Task
import android.text.format.DateFormat
import android.widget.*
import androidx.core.content.FileProvider
import com.example.todo.getScaledBitmap
import java.io.File
import java.util.*
const val DATE_KEY = "taskDate"
private const val REQUEST_PHOTO = 2

class ToDoFragment : Fragment(), DatePickerFragment.DateCallBack, View.OnClickListener {
    val dateFormat = "MMM dd, yyyy"

    private lateinit var task: Task

    private lateinit var creationDateTv: TextView

    private lateinit var titleEt : EditText
    private lateinit var descriptionEt: EditText

    private lateinit var taskDateBtn:Button
    private lateinit var imgBtn : Button
    private lateinit var addNewTask: Button

    private lateinit var viewFlipper: ViewFlipper

    private lateinit var nextBtn: ImageView
    private lateinit var prevBtn: ImageView

    private lateinit var photoFile: File
    private lateinit var photoUri: Uri

    private var imgView : ImageView? = null


    private fun updatePhotoView(){

        if (photoFile.exists()){

            val bitmap = getScaledBitmap(photoFile.path, requireActivity())

            imgView?.setImageBitmap(bitmap)

        }else{

            imgView?.setImageDrawable(null)

        }
    }



    override fun onDetach() {
        super.onDetach()

        requireActivity().revokeUriPermission(photoUri,Intent.FLAG_GRANT_WRITE_URI_PERMISSION)

    }


    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == REQUEST_PHOTO) {

            requireActivity().revokeUriPermission(photoUri,Intent.FLAG_GRANT_WRITE_URI_PERMISSION)

            updatePhotoView()

        }



    }

    private val toDoViewModel by lazy { ViewModelProvider(this).get(ToDoViewModel::class.java) }



    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

       val view =  inflater.inflate(R.layout.fragment_to_do, container, false)

        init(view)

        creationDateTv.text = DateFormat.format(dateFormat,task.createDate)

        nextBtn.setOnClickListener(this)
        prevBtn.setOnClickListener(this)

        return view

    }

    private fun init(view: View) {
        viewFlipper = view.findViewById(R.id.view_flipper)
        titleEt = view.findViewById(R.id.todo_title_edit_text)
        descriptionEt = view.findViewById(R.id.todo_description_edit_text)
        taskDateBtn = view.findViewById(R.id.task_date_btn)
        creationDateTv = view.findViewById(R.id.creation_date_tv)
        addNewTask = view.findViewById(R.id.add_task)
        imgView = view.findViewById(R.id.task_image_view)
        imgBtn = view.findViewById(R.id.img_btn)
        nextBtn = view.findViewById(R.id.next_btn)
        prevBtn = view.findViewById(R.id.previous_btn)
    }

    override fun onClick(v: View?) {



        if (v == nextBtn){

                viewFlipper.showNext()

        }
        else if (v == prevBtn){

                viewFlipper.showPrevious()

        }

    }







    @SuppressLint("QueryPermissionsNeeded")
    override fun onStart() {
        super.onStart()


        imgBtn.apply {

            val packageManager: PackageManager = requireActivity().packageManager

            val captureImage = Intent(MediaStore.ACTION_IMAGE_CAPTURE)

            val resolvedActivity: ResolveInfo? =
                packageManager.resolveActivity(captureImage,
                    PackageManager.MATCH_DEFAULT_ONLY)

            if (resolvedActivity == null){

                isEnabled = false

            }

            setOnClickListener {
                captureImage.putExtra(MediaStore.EXTRA_OUTPUT, photoUri)

                val cameraActivities: List<ResolveInfo> =
                    packageManager.queryIntentActivities(captureImage,
                        PackageManager.MATCH_DEFAULT_ONLY)


                for (cameraActivity in cameraActivities){
                    requireActivity().grantUriPermission(cameraActivity.activityInfo.packageName,
                        photoUri,
                        Intent.FLAG_GRANT_READ_URI_PERMISSION)

                }
                startActivityForResult(captureImage, REQUEST_PHOTO)
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

        photoFile = toDoViewModel.getPhotoFile(task)
        photoUri = FileProvider.getUriForFile(requireContext(), "com.example.todo", photoFile)

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

        if (task.title.isEmpty()){

            toDoViewModel.deleteTask(task)

        }else {

            toDoViewModel.updateTask(task)

        }
    }




}

