package ru.gressor.nasa_picture.pres.views

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import ru.gressor.nasa_picture.databinding.FragmentToDoListBinding
import ru.gressor.nasa_picture.domain.entities.ToDoItem

class ToDoListFragment: Fragment(), ToDoListHolder {
    private lateinit var binding: FragmentToDoListBinding
    private lateinit var adapter: ToDoListAdapter

    override val toDoList = mutableListOf<ToDoItem> (
        ToDoItem(false, "Task 1"),
        ToDoItem(false, "Task 2"),
        ToDoItem(true, "Task 3"),
        ToDoItem(true, "Task 4"),
        ToDoItem(false, "Task 5")
    )

    override fun itemChanged(position: Int, newToDoItem: ToDoItem) {
        toDoList[position] = newToDoItem
        itemChanged(position)
    }

    override fun itemChanged(position: Int) {
        adapter.notifyItemChanged(position)
    }

    override fun swapItems(first: Int, second: Int) {
        toDoList[first] = toDoList[second].also { toDoList[second] = toDoList[first] }
    }

    override fun deleteItem(position: Int) {
        toDoList.removeAt(position)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View = FragmentToDoListBinding.inflate(inflater, container, false)
        .also {
            binding = it
        }.root

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        adapter = ToDoListAdapter(this)
        binding.rvTodoList.layoutManager = LinearLayoutManager(context)
        binding.rvTodoList.adapter = adapter
    }
}