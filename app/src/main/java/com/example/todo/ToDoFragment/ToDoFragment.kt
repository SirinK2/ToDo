package com.example.todo.ToDoFragment

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.TextView
import androidx.lifecycle.ViewModelProvider
import com.example.todo.R
import com.example.todo.database.Task


class ToDoFragment : Fragment() {

    private lateinit var titleTv: TextView
    private lateinit var titleEt : EditText
    private lateinit var descriptionTv : TextView
    private lateinit var descriptionEt: EditText

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

        return view

    }

    override fun onStart() {
        super.onStart()




        var textWatcher = object : TextWatcher{
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {

                task.title = s.toString()
                task.description = s.toString()
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {

                task.title = s.toString()
                task.description = s.toString()


            }

            override fun afterTextChanged(s: Editable?) {

                task.title = s.toString()
                task.description = s.toString()



            }

        }

        titleEt.addTextChangedListener(textWatcher)
        descriptionEt.addTextChangedListener(textWatcher)




    }


}