package com.example.todo

import android.app.DatePickerDialog
import android.app.Dialog
import android.os.Bundle
import androidx.fragment.app.DialogFragment
import com.example.todo.ToDoFragment.DATE_KEY
import java.util.*

class DatePickerFragment: DialogFragment() {

    interface DateCallBack{

        fun onDateSelected(date: Date)

    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {


        val date = arguments?.getSerializable(DATE_KEY) as Date

        val calendar = Calendar.getInstance()
        calendar.time = date
        val years = calendar.get(Calendar.YEAR)
        val months = calendar.get(Calendar.MONTH)
        val day = calendar.get(Calendar.DAY_OF_MONTH)

        val dateListener = DatePickerDialog.OnDateSetListener { _, year, month, dayOfMonth ->
            val resultDate = GregorianCalendar(year,month,dayOfMonth).time
            targetFragment?.let {

                (it as DateCallBack).onDateSelected(resultDate)
            }


        }

        return DatePickerDialog(
            requireContext(),
            dateListener,
            years,
            months,
            day
        )


    }




}