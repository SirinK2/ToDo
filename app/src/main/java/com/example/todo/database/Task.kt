package com.example.todo.database

import java.util.*

data class Task(val id: UUID = UUID.randomUUID(),
                var title: String = "",
                var date: Date = Date(),
                var description: String = "",
                var isCompleted: Boolean = false
)