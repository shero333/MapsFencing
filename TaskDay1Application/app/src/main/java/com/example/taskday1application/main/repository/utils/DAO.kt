package com.example.taskday1application.main.repository.utils

import androidx.lifecycle.LiveData
import androidx.room.*
import com.example.taskday1application.main.models.Todo

@Dao
interface DAO {
    // below method is use to
    // add data to database.
    @Insert
    suspend fun insert(model: Todo?)

    // below method is use to update
    // the data in our database.
    @Update
    suspend fun update(model: Todo?)

    // below line is use to delete a
    // specific item in our database.
    @Delete
    suspend fun delete(model: Todo?)

    // below line is to read all the courses from our database.
    // in this we are ordering our courses in ascending order
    // with our course name.
    @get:Query("SELECT * FROM todo_table ORDER BY name ASC")
    val allTodos: List<Todo?>?
}