package example.code.some_project.presentation.ui.adapter

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.LayoutRes
import androidx.recyclerview.widget.RecyclerView

abstract class BaseItemsAdapter<T>() : RecyclerView.Adapter<BaseViewHolder<T>>() {

    val items: MutableList<T> = mutableListOf()
    val isEmpty: Boolean get() = itemCount < 1
    val maxPosition: Int get() = itemCount - 1

    override fun onBindViewHolder(holder: BaseViewHolder<T>, position: Int) =
        holder.bind(getItemByPosition(position)!!)

    override fun onBindViewHolder(
        holder: BaseViewHolder<T>,
        position: Int,
        payloads: MutableList<Any>
    ) = holder.bindWithPayloads(getItemByPosition(position)!!, payloads)

    override fun getItemCount(): Int = items.size

    fun replaceItem(position: Int, newItem: T) {
        val count = items.size
        if (count > position) {
            items[position] = newItem
            notifyItemChanged(position)
        }
    }

    fun updateItem(item: T, payload: Any = Unit) {
        val index = items.indexOf(item)
        if (index >= 0) {
            notifyItemChanged(index, payload)
        }
    }

    fun removeItem(item: T) {
        val index = items.indexOf(item)
        if (index >= 0) {
            items.removeAt(index)
            notifyItemRemoved(index)
        }
    }

    fun removeAt(position: Int) {
        items.removeAt(position)
        notifyItemRemoved(position)
    }

    fun addItems(newItems: List<T>) {
        val start = items.size
        val count = newItems.size

        items.addAll(newItems)
        notifyItemRangeInserted(start, count)
    }

    fun addItem(newItem: T) {
        val start = items.size

        items.add(newItem)
        notifyItemInserted(start)
    }

    @SuppressLint("NotifyDataSetChanged")
    fun newItems(newItems: List<T>) {
        items.clear()
        items.addAll(newItems)
        notifyDataSetChanged()
    }

    @SuppressLint("NotifyDataSetChanged")
    fun clear() {
        items.clear()
        notifyDataSetChanged()
    }

    open fun getItemByPosition(position: Int): T? =
        if (position >= items.size || position < 0) null else items[position]

    open fun getPositionByItem(item: T): Int = items.indexOf(item)
}

fun inflateView(parent: ViewGroup, @LayoutRes viewResId: Int): View =
    LayoutInflater.from(parent.context).inflate(viewResId, parent, false)

abstract class BaseViewHolder<T>(view: View) : RecyclerView.ViewHolder(view) {

    abstract fun bind(item: T)

    open fun bindWithPayloads(item: T, payloads: List<Any>) = bind(item)
}