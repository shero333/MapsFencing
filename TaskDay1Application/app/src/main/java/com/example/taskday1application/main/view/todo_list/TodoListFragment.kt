package com.example.taskday1application.main.view.todo_list

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.taskday1application.databinding.FragmentTodoListBinding
import com.example.taskday1application.main.models.Todo
import com.example.taskday1application.main.view.other.OtherOperationViewModel
import com.example.taskday1application.main.view.todo_list.adapter.todoListViewAdapter
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class TodoListFragment : Fragment(), todoListViewAdapter.UpdateTodo {
    private var binding: FragmentTodoListBinding? = null
    private lateinit var listAdapter: todoListViewAdapter
    private val todoListViewModel by viewModels<TodoListViewModel>()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        binding = FragmentTodoListBinding.inflate(inflater, container, false)

        listAdapter = todoListViewAdapter(requireActivity(), this)

        binding!!.todoList.layoutManager =
            LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
        binding!!.todoList.scrollToPosition(listAdapter.itemCount - 1)

        todoListViewModel.getTodos()

        todoListViewModel.getList().observe(viewLifecycleOwner) {

            if (it!!.isNotEmpty()) {
                listAdapter.todoList = it
                binding!!.todoList.adapter = listAdapter
            } else {
                binding!!.todoList.visibility = View.GONE
                binding!!.noList.visibility = View.VISIBLE
            }
        }
        return binding!!.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        binding = null
    }

    override fun updatedItem(todo: Todo) {
        todoListViewModel.updateTodo(todo)

        todoListViewModel.getTodos()

        todoListViewModel.getList().observe(viewLifecycleOwner) {

            if (it!!.isNotEmpty()) {
                listAdapter.todoList = it
                binding!!.todoList.adapter = listAdapter
            } else {
                binding!!.todoList.visibility = View.GONE
                binding!!.noList.visibility = View.VISIBLE
            }
        }
    }
}