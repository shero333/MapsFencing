package com.example.taskday1application.main.view.other.adapter

import android.app.Activity
import android.app.Dialog
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.Window
import androidx.appcompat.widget.AppCompatButton
import androidx.appcompat.widget.AppCompatEditText
import androidx.appcompat.widget.AppCompatImageView
import androidx.appcompat.widget.AppCompatTextView
import androidx.recyclerview.widget.RecyclerView
import com.example.taskday1application.R
import com.example.taskday1application.main.models.Todo
import com.example.taskday1application.main.view.other.adapter.OtherOperationsListAdapter.OtherOperationsListViewHolder

class OtherOperationsListAdapter(
    private var activity: Activity,
    private var deleteTodo: DeleteTodoOther,
    private var updateTodo: UpdateTodoOther) : RecyclerView.Adapter<OtherOperationsListViewHolder>() {

    var todoList: List<Todo?>? = null
        set(value) {
            field = value
        }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): OtherOperationsListViewHolder {

        val view: View = LayoutInflater.from(parent.context).inflate(R.layout.other_ops_list_item_vew, parent, false)

        return OtherOperationsListViewHolder(view)
    }

    override fun onBindViewHolder(holder: OtherOperationsListViewHolder, position: Int) {

        holder.name.text = todoList!![position]!!.name
        holder.date.text = todoList!![position]!!.date
        holder.priority.text = todoList!![position]!!.priority.toString()

        holder.edit.setOnClickListener {
            showDialog(todoList!![position]!!.name,todoList!![position]!!.date,todoList!![position]!!.priority)
        }

        holder.delete.setOnClickListener {
            deleteTodo.deleteItem(Todo(todoList!![position]!!.name,todoList!![position]!!.date, todoList!![position]!!.priority))
        }
    }
    override fun getItemCount(): Int {
        if (todoList != null)
            return todoList!!.size
        else
            return 0;
    }

    private fun showDialog(nameItem: String,dateItem: String,priorityItem: Int) {
        val dialog = Dialog(activity)
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        dialog.setCancelable(false)
        dialog.setContentView(R.layout.update_data_todo)

        val name = dialog.findViewById(R.id.name_edt_update) as AppCompatEditText
        name.setText(nameItem)

        val date = dialog.findViewById(R.id.date_edt_update) as AppCompatEditText
        date.setText(dateItem)
        date.isEnabled = false

        val priority = dialog.findViewById(R.id.priority_edt_update) as AppCompatEditText
        priority.setText(priorityItem.toString())

        val save = dialog.findViewById(R.id.update_button_update) as AppCompatButton

        val priorityVal : String = priority.text.toString()

        save.setOnClickListener {
            updateTodo.updatedItem(Todo(name.text.toString(),date.text.toString(), priorityVal.toInt()))
            dialog.dismiss()
        }
        dialog.show()
    }

    class OtherOperationsListViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView){

        val name : AppCompatTextView = itemView.findViewById(R.id.name_other)
        val date : AppCompatTextView = itemView.findViewById(R.id.date_other)
        val priority : AppCompatTextView = itemView.findViewById(R.id.priority_other)

        val edit : AppCompatImageView = itemView.findViewById(R.id.edit_button_other)
        val delete : AppCompatImageView = itemView.findViewById(R.id.delete_button_other)
    }

    interface UpdateTodoOther{

        fun updatedItem(todo:Todo)
    }

    interface DeleteTodoOther{

        fun deleteItem(todo:Todo)
    }
}