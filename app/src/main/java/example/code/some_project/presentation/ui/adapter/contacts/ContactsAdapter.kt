package example.code.some_project.presentation.ui.adapter.contacts

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import example.code.some_project.R
import example.code.some_project.databinding.ItemContactsBinding
import example.code.some_project.domain.models.Contact
import example.code.some_project.presentation.ui.adapter.BaseItemsAdapter
import example.code.some_project.presentation.ui.adapter.BaseViewHolder
import example.code.some_project.util.toBlacklistNumber

class ContactsAdapter(
    private val moreClickListener: (Int, Contact) -> Unit,
    private val itemClickListener: (Int, Contact) -> Unit
) : BaseItemsAdapter<Contact>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BaseViewHolder<Contact> {
        val binding = ItemContactsBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ContactsViewHolder(binding)
    }

    fun setData(items: List<Contact>) {
        newItems(items)
    }

    inner class ContactsViewHolder(
        private val binding: ItemContactsBinding
    ) : BaseViewHolder<Contact>(binding.root) {

        override fun bind(item: Contact) {
            with(binding) {
                name.text = item.name
                phone.text = item.number
                moreButton.setOnClickListener {
                    moreClickListener(bindingAdapterPosition, item)
                }
                root.setOnClickListener { itemClickListener(bindingAdapterPosition, item) }

                val firstLetter = item.nameFirstLetter
                val previousFirstLetter =
                    getItemByPosition(bindingAdapterPosition - 1)?.nameFirstLetter
                val nextFirstLetter =
                    getItemByPosition(bindingAdapterPosition + 1)?.nameFirstLetter

                val first = firstLetter != previousFirstLetter
                val last = firstLetter != nextFirstLetter

                header.text = firstLetter
                header.visibility = if (first) View.VISIBLE else View.GONE
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

private val Contact.nameFirstLetter: String? get() =
    if (name.isNotBlank()) name[0].uppercase() else null