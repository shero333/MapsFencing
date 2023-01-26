package com.example.taskday1application.main.repository

import android.app.Application
import androidx.lifecycle.LiveData
import com.example.taskday1application.main.models.Todo
import com.example.taskday1application.main.repository.utils.DAO
import com.example.taskday1application.main.repository.utils.DatabaseInjection
import javax.inject.Inject

class RepositoryTodo @Inject constructor(
    private val dao: DAO?
) {
    suspend fun insert(model: Todo?) {
        dao!!.insert(model)
    }

    // creating a method to update data in database.
    suspend fun update(model: Todo?) {
        dao!!.update(model)
    }

    // creating a method to delete the data in our database.
    suspend fun delete(model: Todo?) {
        dao!!.delete(model)
    }

    fun getAll():List<Todo?>?{
        return dao?.allTodos
    }
}