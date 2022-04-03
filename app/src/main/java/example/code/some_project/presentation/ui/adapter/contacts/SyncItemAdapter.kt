package example.code.some_project.presentation.ui.adapter.contacts

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import example.code.some_project.R
import example.code.some_project.databinding.ItemListHeaderBinding

class SyncItemAdapter(
    private val onButtonClick: () -> Unit
) : RecyclerView.Adapter<SyncItemAdapter.SyncViewHolder>() {

    private var notSynchronized: Boolean = false
    private var permissionNotAllowed: Boolean = false

    @SuppressLint("NotifyDataSetChanged")
    fun setData(notSynchronized: Boolean, permissionNotAllowed: Boolean) {
        this.notSynchronized = notSynchronized
        this.permissionNotAllowed = permissionNotAllowed

        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SyncViewHolder {
        val binding = ItemListHeaderBinding.inflate(
            LayoutInflater.from(parent.context), parent, false
        )
        return SyncViewHolder(binding)
    }

    override fun onBindViewHolder(holder: SyncViewHolder, position: Int) {
        with(holder.binding) {
            (itemListHeaderContainer.layoutParams as? ViewGroup.MarginLayoutParams)?.apply {
                bottomMargin = holder.itemView.resources.getDimensionPixelSize(R.dimen.margin_common)
            }.let(itemListHeaderContainer::setLayoutParams)

            syncButton.apply {
            setText(R.string.contacts_sync_button)

            setOnClickListener { onButtonClick() }
        }
        }
    }

    override fun getItemCount(): Int = if (notSynchronized) 1 else 0

    inner class SyncViewHolder(
        val binding: ItemListHeaderBinding
    ) : RecyclerView.ViewHolder(binding.root)
}