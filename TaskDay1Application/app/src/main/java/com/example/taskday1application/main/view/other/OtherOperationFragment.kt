package com.example.taskday1application.main.view.other

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.taskday1application.databinding.FragmentOtherOperationBinding
import com.example.taskday1application.main.models.Todo
import com.example.taskday1application.main.view.add_todo.AddTodoViewModel
import com.example.taskday1application.main.view.other.adapter.OtherOperationsListAdapter
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class OtherOperationFragment : Fragment(), OtherOperationsListAdapter.DeleteTodoOther,
    OtherOperationsListAdapter.UpdateTodoOther {

    private var binding: FragmentOtherOperationBinding? = null
    private lateinit var listAdapter: OtherOperationsListAdapter
    private val otherOperationViewModel by viewModels<OtherOperationViewModel>()
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?, savedInstanceState: Bundle?
    ): View {

        binding = FragmentOtherOperationBinding.inflate(inflater, container, false)

        listAdapter = OtherOperationsListAdapter(requireActivity(), this, this)


        binding!!.todoList.layoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)

        binding!!.todoList.scrollToPosition(listAdapter.itemCount - 1)

        otherOperationViewModel.getTodos()
        otherOperationViewModel.getList().observe(viewLifecycleOwner) {

            if (it!!.isNotEmpty()) {
                listAdapter.todoList = it
                binding!!.todoList.adapter = listAdapter
            } else {
                binding!!.todoList.visibility = View.GONE
                binding!!.noListOther.visibility = View.VISIBLE
            }
        }

        return binding!!.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        binding = null
    }

    override fun deleteItem(todo: Todo) {
        otherOperationViewModel.deleteTodo(todo)

        otherOperationViewModel.getTodos()
        otherOperationViewModel.getList().observe(viewLifecycleOwner) {

            if (it!!.isNotEmpty()) {
                listAdapter.todoList = it
                binding!!.todoList.adapter = listAdapter
            } else {
                binding!!.todoList.visibility = View.GONE
                binding!!.noListOther.visibility = View.VISIBLE
            }
        }

    }

    override fun updatedItem(todo: Todo) {
        otherOperationViewModel.updateTodo(todo)

        otherOperationViewModel.getTodos()
        otherOperationViewModel.getList().observe(viewLifecycleOwner) {

            if (it!!.isNotEmpty()) {
                listAdapter.todoList = it
                binding!!.todoList.adapter = listAdapter
            } else {
                binding!!.todoList.visibility = View.GONE
                binding!!.noListOther.visibility = View.VISIBLE
            }
        }
    }

}