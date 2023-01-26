package com.example.taskday1application.main.view.todo_list

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.taskday1application.main.models.Todo
import com.example.taskday1application.main.repository.RepositoryTodo
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class TodoListViewModel @Inject constructor(application: Application, private val repositoryTodo : RepositoryTodo) : AndroidViewModel(application) {

    private var listTodoLivedata : MutableLiveData<List<Todo?>?> = MutableLiveData()
    fun getList() : MutableLiveData<List<Todo?>?> {

        return listTodoLivedata
    }

    fun getTodos(){
        viewModelScope.launch {
            listTodoLivedata.postValue(repositoryTodo.getAll())
        }
    }

    fun updateTodo(todo: Todo){
        viewModelScope.launch {
            repositoryTodo.update(todo)
        }
    }
}