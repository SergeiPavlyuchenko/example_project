package example.code.some_project.presentation.ui.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import example.code.some_project.R
import example.code.some_project.databinding.ItemContactsBinding
import example.code.some_project.domain.models.Contact
import example.code.some_project.util.toBlacklistNumber

class ContactsSearchAdapter(
    private val moreClickListener: (Int, Contact) -> Unit,
    private val itemClickListener: (Int, Contact) -> Unit
) : BaseItemsAdapter<Contact>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BaseViewHolder<Contact> {
        val binding = ItemContactsBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ContactsViewHolder(binding)
    }

    inner class ContactsViewHolder(
        private val binding: ItemContactsBinding
    ) : BaseViewHolder<Contact>(binding.root) {

        init {
            binding.header.visibility = View.GONE
        }

        override fun bind(item: Contact) {
            with(binding) {
                name.text = item.name
                phone.text = item.number
                moreButton.setOnClickListener {
                    moreClickListener(bindingAdapterPosition, item)
                }
                root.setOnClickListener { itemClickListener(bindingAdapterPosition, item) }

                val first = bindingAdapterPosition == 0
                val last = bindingAdapterPosition == itemCount - 1

                divider.visibility = if (last) View.GONE else View.VISIBLE
                content.setBackgroundResource(if (first && last) {
                    R.drawable.bg_progressbar_base
                } else if (first) {
                    R.drawable.bg_item_first
                } else if (last) {
                    R.drawable.bg_item_last
                } else {
                    R.color.white
                })
            }
        }
    }

}