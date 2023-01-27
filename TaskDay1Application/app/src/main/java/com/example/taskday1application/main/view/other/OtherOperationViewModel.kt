package com.example.taskday1application.main.view.other

import android.app.Application
import androidx.lifecycle.*
import com.example.taskday1application.main.models.Todo
import com.example.taskday1application.main.repository.RepositoryTodo
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class OtherOperationViewModel @Inject constructor(application: Application, private val repositoryTodo : RepositoryTodo) : ViewModel() {

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

    fun deleteTodo(todo: Todo){
        viewModelScope.launch {
            repositoryTodo.delete(todo)
        }
    }
}