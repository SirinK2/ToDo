package com.example.todo.database

import androidx.room.TypeConverter
import java.util.*

class TaskTypeConverters {

    @TypeConverter
    fun fromDate(date: Date?): Long?{
        return date?.time
    }

    @TypeConverter
    fun toDate(date: Long?): Date?{
        return date?.let { Date(it) }
    }

    @TypeConverter
    fun fromUUID(id: UUID?):String?{

        return id?.toString()

    }

    @TypeConverter
    fun toUUID(id: String?): UUID?{

        return UUID.fromString(id)

    }
}