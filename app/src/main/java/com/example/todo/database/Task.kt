package com.example.todo.database
//
//import java.util.*
//
//data class Task(val id: UUID = UUID.randomUUID(),
//                var title: String = "",
//                var date: Date = Date(),
//                var description: String = "",
//                var isCompleted: Boolean = false
//
//
//){
//    val photoFileName
//        get() = "IMG_$id.jpg"
//}

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.util.*
@Entity
data class Task(@PrimaryKey val id: UUID = UUID.randomUUID(),
                var title: String = "",
                var taskDate: Date? = null,
                var createDate: Date = Date(),
                var description: String = "",
                var isCompleted: Boolean = false

){
    val photoFileName
        get() = "IMG_$id.jpg"
}