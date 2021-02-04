package ru.gressor.nasa_picture.pres.views

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import androidx.viewbinding.ViewBinding
import ru.gressor.nasa_picture.databinding.FooterTodoListBinding
import ru.gressor.nasa_picture.databinding.HeaderTodoListBinding
import ru.gressor.nasa_picture.databinding.ItemToDoListBinding
import ru.gressor.nasa_picture.domain.entities.ToDoItem

interface ToDoListHolder {
    val toDoList: List<ToDoItem>
    fun itemChanged(position: Int, newToDoItem: ToDoItem)
    fun itemChanged(position: Int)
    fun swapItems(first: Int, second: Int)
    fun deleteItem(position: Int)
    fun addItem()
    fun shuffledList(): List<ToDoItem>
    fun updateList(list: List<ToDoItem>)
}

class ToDoListAdapter(
    private val toDoListHolder: ToDoListHolder
) : RecyclerView.Adapter<ToDoListAdapter.AbstractViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AbstractViewHolder =
        LayoutInflater.from(parent.context).let {
            when (viewType) {
                RECYCLER_HEADER -> HeaderViewHolder(
                    HeaderTodoListBinding.inflate(
                        it,
                        parent,
                        false
                    )
                )
                RECYCLER_FOOTER -> FooterViewHolder(
                    FooterTodoListBinding.inflate(
                        it,
                        parent,
                        false
                    )
                )
                else -> ToDoViewHolder(ItemToDoListBinding.inflate(it, parent, false))
            }
        }

    override fun getItemViewType(position: Int): Int {
        return when (position) {
            0 -> RECYCLER_HEADER
            toDoListHolder.toDoList.size + 1 -> RECYCLER_FOOTER
            else -> 0
        }
    }

    override fun onBindViewHolder(holder: AbstractViewHolder, position: Int) {
        holder.bind()
    }

    override fun getItemCount(): Int = toDoListHolder.toDoList.size + 2

    fun setItems(newToDoList: List<ToDoItem>, oldToDoList: List<ToDoItem>) {
        val result = DiffUtil.calculateDiff(DiffUtilCallback(newToDoList, oldToDoList))
        result.dispatchUpdatesTo(this)
        toDoListHolder.updateList(newToDoList)
    }

    abstract class AbstractViewHolder(
        binding: ViewBinding
    ) : RecyclerView.ViewHolder(binding.root) {
        abstract fun bind()
    }

    inner class HeaderViewHolder(
        headerBinding: HeaderTodoListBinding
    ) : AbstractViewHolder(headerBinding) {

        override fun bind() {
            itemView.setOnClickListener {
                toDoListHolder.addItem()
                notifyItemInserted(1)
            }
        }
    }

    inner class FooterViewHolder(
        footerBinding: FooterTodoListBinding
    ) : AbstractViewHolder(footerBinding) {

        override fun bind() {
            itemView.setOnClickListener {
                val newList = toDoListHolder.shuffledList()
                setItems(newList, toDoListHolder.toDoList)
            }
        }
    }

    inner class ToDoViewHolder(
        private val toDoItemBinding: ItemToDoListBinding
    ) : AbstractViewHolder(toDoItemBinding) {

        override fun bind() {
            toDoItemBinding.run {

                cbIsCompleted.isChecked = toDoListHolder.toDoList[adapterPosition - 1].isDone
                cbIsCompleted.setOnClickListener { _ ->
                    toDoListHolder.toDoList[adapterPosition - 1].isDone = cbIsCompleted.isChecked
                }

                etToDoText.text.clear()
                etToDoText.text.append(toDoListHolder.toDoList[adapterPosition - 1].taskText)
                etToDoText.onFocusChangeListener = View.OnFocusChangeListener { _, hasFocus ->
                    if (!hasFocus &&
                        toDoListHolder.toDoList[adapterPosition - 1]
                            .taskText != etToDoText.text.toString()
                    ) {
                        toDoListHolder.toDoList[adapterPosition - 1]
                            .taskText = etToDoText.text.toString()
                        notifyItemChanged(adapterPosition)
                    }
                }

                btnUp.setOnClickListener {
                    if (adapterPosition > 1) {
                        toDoListHolder.swapItems(adapterPosition - 1, adapterPosition - 2)
                        notifyItemMoved(adapterPosition, adapterPosition - 1)
                    }
                }

                btnDown.setOnClickListener {
                    if (adapterPosition < itemCount - 2) {
                        toDoListHolder.swapItems(adapterPosition - 1, adapterPosition)
                        notifyItemMoved(adapterPosition, adapterPosition + 1)
                    }
                }

                btnDelete.setOnClickListener {
                    toDoListHolder.deleteItem(adapterPosition - 1)
                    notifyItemRemoved(adapterPosition)
                }
            }
        }
    }

    inner class DiffUtilCallback(
        private val newToDoList: List<ToDoItem>,
        private val oldToDoList: List<ToDoItem>
    ): DiffUtil.Callback() {
        override fun getOldListSize(): Int = oldToDoList.size + 2

        override fun getNewListSize(): Int = newToDoList.size + 2

        override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
            // header does not change
            if (oldItemPosition == 0 && newItemPosition == 0) return true
            if (oldItemPosition == 0 || newItemPosition == 0) return false

            // footer does not change
            if (oldItemPosition == oldToDoList.size + 1
                && newItemPosition == newToDoList.size + 1) return true
            if (oldItemPosition == oldToDoList.size + 1
                || newItemPosition == newToDoList.size + 1) return false

            // same texts >> same items
            if (newToDoList[newItemPosition - 1].taskText
                == oldToDoList[oldItemPosition - 1].taskText) return true
            return false
        }

        override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
            return areItemsTheSame(oldItemPosition, newItemPosition)
        }
    }

    companion object {
        private const val RECYCLER_HEADER = -1
        private const val RECYCLER_FOOTER = -2
    }
}