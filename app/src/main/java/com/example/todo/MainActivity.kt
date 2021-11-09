package com.example.todo

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.todo.ToDoListFragment.ToDoListFragment

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val currFragment = supportFragmentManager.findFragmentById(R.id.fragment_container)

        if (currFragment == null){
            val fragment = ToDoListFragment()

            supportFragmentManager
                .beginTransaction()
                .add(R.id.fragment_container,fragment)
                .commit()
        }

    }


}