package com.example.taskday1application.main.models

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "todo_table")
class Todo(
    var name: String, var date: String, @field:PrimaryKey(autoGenerate = true) var priority: Int
) {

    override fun toString(): String {
        return "Todo{" +
                "name='" + name + '\'' +
                ", date='" + date + '\'' +
                ", priority=" + priority +
                '}'
    }
}