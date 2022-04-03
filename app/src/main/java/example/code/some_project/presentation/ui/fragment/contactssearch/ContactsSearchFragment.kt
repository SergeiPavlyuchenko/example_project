package example.code.some_project.presentation.ui.fragment.contactssearch

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.jakewharton.rxbinding2.widget.RxTextView
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import kotlinx.android.synthetic.main.item_list_header.view.*
import example.code.some_project.R
import example.code.some_project.databinding.FragmentContactsSearchBinding
import example.code.some_project.domain.models.Contact
import example.code.some_project.presentation.ui.adapter.ContactsSearchAdapter
import example.code.some_project.presentation.ui.base.BaseMvpFragment
import example.code.some_project.presentation.ui.dialog.ContactItemOptionsDialog
import example.code.some_project.presentation.ui.dialog.ContactItemOptionsDialogListener
import example.code.some_project.presentation.ui.fragment.editcontact.EditContactFragment
import example.code.some_project.presentation.ui.provider.DialogProvider
import example.code.some_project.util.SpanTextUtil
import moxy.ktx.moxyPresenter
import org.koin.android.ext.android.get
import java.util.concurrent.TimeUnit

class ContactsSearchFragment : BaseMvpFragment<FragmentContactsSearchBinding>(), ContactsSearchView,
    ContactItemOptionsDialogListener {

    override val presenter: AbstractContactsSearchPresenter by moxyPresenter { get() }
    override val contentViewId: Int = R.id.contactsContentView

    private var recycler: RecyclerView? = null

    private val adapter = ContactsSearchAdapter(::onMoreClicked, ::onItemClicked)

    private var textChangedDisposable: Disposable? = null

    override fun createViewBinding() = FragmentContactsSearchBinding.inflate(layoutInflater)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initializeUi()
    }

    private fun initializeUi() {
        with(binding) {
            toolbar.apply {
                setNavigationOnClickListener { goBack() }
            }
            clearSearch.setOnClickListener { search.setText("") }
            this@ContactsSearchFragment.recycler = recycler.apply {
                layoutManager = LinearLayoutManager(context)
                adapter = this@ContactsSearchFragment.adapter
            }
        }
    }

    override fun onResume() {
        super.onResume()
        setupTextChanged()
    }

    private fun setupTextChanged() {
        textChangedDisposable = RxTextView.textChanges(binding.search)
            .debounce(500, TimeUnit.MILLISECONDS)
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe {
                val string = it.toString()
                binding.clearSearch.visibility = if (string.isEmpty()) View.GONE else View.VISIBLE
                presenter.searchTextChanged(string)
            }
    }

    override fun onPause() {
        super.onPause()
        disposeTextChanged()
    }

    private fun disposeTextChanged() {
        textChangedDisposable?.dispose()
    }

    override fun updateContacts(contacts: MutableList<Contact>, isBlankSearch: Boolean) {
        adapter.newItems(contacts)

        binding.emptyList.apply {
            visibility = if (contacts.isEmpty() || isBlankSearch) View.VISIBLE else View.GONE
        }
        binding.emptyListText.text = if (contacts.isEmpty() && isBlankSearch) {
            getString(R.string.contacts_search_empty_text)
        } else {
            getString(R.string.contacts_empty_search_stub)
        }
    }

    private fun onMoreClicked(position: Int, item: Contact) {
        ContactItemOptionsDialog.newInstance(position)
            .show(childFragmentManager, ContactItemOptionsDialog.TAG)
    }

    private fun onItemClicked(position: Int, item: Contact) = onEditClicked(position)

    override fun onDestroyView() {
        super.onDestroyView()
        recycler?.adapter = null
        recycler = null
    }

    override fun onEditClicked(position: Int) {
        adapter.getItemByPosition(position)?.let {
            mainActivity?.setupNextContentFragment(
                EditContactFragment.createInstance(it.number), EditContactFragment.TAG
            )
        }
    }

    override fun onRemoveClicked(position: Int) {
        activity?.let {
            DialogProvider.createDialog(
                it,
                title = getString(R.string.contacts_remove_dialog_title),
                description = getString(R.string.contacts_remove_dialog_text),
                positiveButtonText = getString(R.string.remove),
                positiveButtonListener = { presenter.removeContact(position) },
                negativeButtonText = getString(R.string.cancel)
            ).show()
        }
    }

    override fun removeContact(contact: Contact) {
        adapter.removeItem(contact)
        activity?.let {
            showErrorSnackBar(SpanTextUtil.getSpanText(it, contact.name, isDeletedSuccess = true))
        }
    }

    companion object {

        const val TAG = "ContactsSearchFragment"

        fun createInstance() = ContactsSearchFragment()
    }

}