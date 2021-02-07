package ru.gressor.nasa_picture.pres.views

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import ru.gressor.nasa_picture.databinding.FragmentToDoListBinding
import ru.gressor.nasa_picture.domain.entities.ToDoItem

class ToDoListFragment: Fragment(), ToDoListHolder {
    private lateinit var binding: FragmentToDoListBinding
    private lateinit var adapter: ToDoListAdapter
    private lateinit var touchHelper: ItemTouchHelper

    override val toDoList = mutableListOf<ToDoItem> (
        ToDoItem(false, "Task 1"),
        ToDoItem(false, "Task 2"),
        ToDoItem(true, "Task 3"),
        ToDoItem(true, "Task 4"),
        ToDoItem(false, "Task 5")
    )

    override fun onStartDrag(viewHolder: RecyclerView.ViewHolder) {
        touchHelper.startDrag(viewHolder)
    }

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

    override fun moveItem(from: Int, to: Int) {
        val item = toDoList[from]
        if (from > to) {
            toDoList.removeAt(from)
            toDoList.add(to, item)
        } else {
            toDoList.add(to, item)
            toDoList.removeAt(from)
        }
    }

    override fun deleteItem(position: Int) {
        toDoList.removeAt(position)
    }

    override fun addItem(position: Int) {
        toDoList.add(position, ToDoItem(false, "Task " + (toDoList.size+1)))
    }

    override fun shuffledList(): List<ToDoItem> {
        return toDoList.toMutableList().apply { shuffle() }
    }

    override fun updateList(list: List<ToDoItem>) {
        toDoList.clear()
        toDoList.addAll(list)
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
        touchHelper = ItemTouchHelper(ItemTouchHelperCallback(adapter))
        touchHelper.attachToRecyclerView(binding.rvTodoList)
    }
}