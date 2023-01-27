package com.example.taskday1application.main.view.add_todo

import android.graphics.Color
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.annotation.RequiresApi
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.ViewModelProvider
import com.example.taskday1application.R
import com.example.taskday1application.databinding.FragmentAddTodoBinding
import com.example.taskday1application.main.models.Todo
import com.google.android.material.snackbar.Snackbar
import dagger.hilt.android.AndroidEntryPoint
import java.time.LocalDate

@AndroidEntryPoint
class addTodoFragment : Fragment() {

    private var binding: FragmentAddTodoBinding? = null
    private val addTodoViewModel by viewModels<AddTodoViewModel>()

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?, savedInstanceState: Bundle?
    ): View {

        binding = FragmentAddTodoBinding.inflate(inflater, container, false)

        val root: View = binding!!.root
        binding!!.addTodo.setOnClickListener {
            if (binding!!.addTodoForm.addTodoForm.visibility == View.VISIBLE) binding!!.addTodoForm.addTodoForm.visibility =
                View.GONE else binding!!.addTodoForm.addTodoForm.visibility = View.VISIBLE
        }

        binding!!.addTodoForm.saveButton.setOnClickListener {

            var name: String? = null
            var priority: String? = null

            if (!binding!!.addTodoForm.nameEdt.equals(null)){
                name = binding!!.addTodoForm.nameEdt.text.toString()
            }
            else{
                binding!!.addTodoForm.nameEdt.error = "Enter name"
            }

            if (!binding!!.addTodoForm.priorityEdt.equals(null) || !binding!!.addTodoForm.priorityEdt.text!!.contains("_") || !binding!!.addTodoForm.priorityEdt.text!!.contains("-")){
                priority = binding!!.addTodoForm.priorityEdt.text.toString()
            }
            else{
                binding!!.addTodoForm.priorityEdt.error = "Enter priority"
            }

            val dateString = LocalDate.now()

            addTodoViewModel.addTodo(Todo(name.toString(), dateString.toString(),priority!!.toInt()))

            addTodoViewModel.success.observe(viewLifecycleOwner) {

                if (it){
                    val snackbar = Snackbar.make(requireView(), "Todo saved successfully!",
                        Snackbar.LENGTH_LONG).setAction("Ok", null)
                    snackbar.setActionTextColor(Color.BLUE)
                    val snackbarView = snackbar.view
                    snackbarView.setBackgroundColor(Color.LTGRAY)
                    val textView = snackbarView.findViewById(com.google.android.material.R.id.snackbar_text) as TextView
                    textView.setTextColor(resources.getColor(R.color.icon_splash_background))
                    textView.textSize = 28f
                    snackbar.show()

                    binding!!.addTodoForm.addTodoForm.visibility = View.GONE

                }else{

                    Log.d("addTodoException", "onCreateView: "+addTodoViewModel.exception)

                    val snackbar = Snackbar.make(requireView(), addTodoViewModel.exception,
                        Snackbar.LENGTH_LONG).setAction("Ok", null)
                    snackbar.setActionTextColor(Color.BLUE)
                    val snackbarView = snackbar.view
                    snackbarView.setBackgroundColor(Color.LTGRAY)
                    val textView = snackbarView.findViewById(com.google.android.material.R.id.snackbar_text) as TextView
                    textView.setTextColor(Color.BLUE)
                    textView.textSize = 28f
                    snackbar.show()
                }
            }
        }

//        addTodoViewModel.success.observe(this) {
//
//            if (it){
//                Snackbar.make(binding!!.root, "Data inserted successfully!", Snackbar.LENGTH_LONG)
//                    .setAction("CLOSE") { }
//                    .setActionTextColor(resources.getColor(R.color.holo_red_light))
//                    .show()
//            }else{
//
//
//                Snackbar.make(binding!!.root, addTodoViewModel.exception, Snackbar.LENGTH_LONG)
//                    .setAction("CLOSE") { }
//                    .setActionTextColor(resources.getColor(R.color.holo_red_light))
//                    .show()
//            }
//
//        }
        return root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        binding = null
    }
}