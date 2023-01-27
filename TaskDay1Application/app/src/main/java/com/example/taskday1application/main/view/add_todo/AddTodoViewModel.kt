package com.example.taskday1application.main.view.add_todo

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.taskday1application.main.models.Todo
import com.example.taskday1application.main.repository.RepositoryTodo
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import java.lang.reflect.Constructor
import javax.inject.Inject

@HiltViewModel
class AddTodoViewModel @Inject constructor(
    application: Application,
    private val repositoryTodo : RepositoryTodo) : ViewModel() {

    var success = MutableLiveData(false)
    var exception : String = ""
    fun addTodo(todo: Todo?) {

        viewModelScope.launch {
            try {
                repositoryTodo.insert(todo)
                success.value = true
            }catch (e:Exception){
                exception = e.message.toString()
                success.value = false
            }
        }
    }
}