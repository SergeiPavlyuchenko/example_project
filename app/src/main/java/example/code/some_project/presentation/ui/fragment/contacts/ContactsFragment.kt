package example.code.some_project.presentation.ui.fragment.contacts

import android.content.Context
import android.os.Bundle
import android.view.*
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.view.children
import androidx.recyclerview.widget.ConcatAdapter
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import example.code.some_project.R
import example.code.some_project.databinding.FragmentContactsBinding
import example.code.some_project.domain.extensions.orZero
import example.code.some_project.domain.models.Contact
import example.code.some_project.presentation.ui.adapter.contacts.ContactsAdapter
import example.code.some_project.presentation.ui.adapter.contacts.SyncItemAdapter
import example.code.some_project.presentation.ui.base.BaseMvpFragment
import example.code.some_project.presentation.ui.dialog.ContactItemOptionsDialog
import example.code.some_project.presentation.ui.dialog.ContactItemOptionsDialogListener
import example.code.some_project.presentation.ui.extensions.getComplexBarsHeight
import example.code.some_project.presentation.ui.extensions.getScreenHeight
import example.code.some_project.presentation.ui.fragment.contactssearch.ContactsSearchFragment
import example.code.some_project.presentation.ui.fragment.editcontact.EditContactFragment
import example.code.some_project.presentation.ui.provider.DialogProvider
import example.code.some_project.util.SpanTextUtil
import moxy.ktx.moxyPresenter
import org.koin.android.ext.android.get

class ContactsFragment : BaseMvpFragment<FragmentContactsBinding>(), ContactsView,
    ContactItemOptionsDialogListener {

    override val presenter: AbstractContactsPresenter by moxyPresenter { get() }

    override val contentViewId: Int = R.id.contactsContentView

    private var recycler: RecyclerView? = null

    override fun createViewBinding() = FragmentContactsBinding.inflate(layoutInflater)

    private val contactsAdapter = ContactsAdapter(::onMoreClicked, ::onItemClicked)
    private val syncAdapter = SyncItemAdapter(::onSyncButtonClicked)
    private val adapter = ConcatAdapter(syncAdapter, contactsAdapter)

    private var permissionRequestLauncher: ActivityResultLauncher<String>? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        parentFragmentManager.setFragmentResultListener(
            EditContactFragment.REQUEST_KEY,
            this,
            { _, bundle ->
                bundle.getString(EditContactFragment.EXTRA_CONTACT_NAME)?.let { name ->
                    activity?.let {
                        showSnackBar(SpanTextUtil.getSpanText(it, name, isSavedSuccess = true))
                    }
                }
            }
        )
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        permissionRequestLauncher =
            registerForActivityResult(ActivityResultContracts.RequestPermission()) {
                presenter.onRequestPermissionResult(it)
            }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initializeUi()
    }

    private fun initializeUi() {
        with(binding) {
            toolbar.apply {
                inflateMenu(R.menu.fragment_contacts)
                setOnMenuItemClickListener {
                    when (it.itemId) {
                        R.id.menu_add -> {
                            mainActivity?.setupNextContentFragment(
                                EditContactFragment.createInstance(), EditContactFragment.TAG
                            )
                        }
                        R.id.menu_search -> {
                            mainActivity?.setupNextContentFragment(
                                ContactsSearchFragment.createInstance(), ContactsSearchFragment.TAG
                            )
                        }
                    }
                    true
                }
            }
            this@ContactsFragment.recycler = recycler.apply {
                layoutManager = LinearLayoutManager(context)
                adapter = this@ContactsFragment.adapter
            }
        }
    }

    override fun setupUi(model: ContactsScreenModel) {
        contactsAdapter.setData(model.contacts)
        syncAdapter.setData(!model.synchronized, !model.synchronizationAllowed)


        binding.emptyList.apply {
            visibility = if (model.contacts.isEmpty()) View.VISIBLE else View.GONE
            this.post {
                (layoutParams as? ViewGroup.MarginLayoutParams)?.apply {
                    bottomMargin = if (model.synchronized) {
                        0
                    } else {
                        resources.getDimensionPixelSize(R.dimen.margin_large)
                    }
                    topMargin = calculateEmptyListTopMargin(model.synchronized)
                }.let(this::setLayoutParams)
            }
        }
    }

    private fun calculateEmptyListTopMargin(isSynchronized: Boolean): Int {
        with(binding) {
            val displayMetrics = resources.displayMetrics
            val normalScreensHeight = resources.getDimension(R.dimen.normal_screens_height) / displayMetrics.density
            val isScreenSizeSmall =
                displayMetrics.heightPixels / displayMetrics.density < normalScreensHeight.toInt()

            val screenHeight = context?.getScreenHeight().orZero() -
                    context?.getComplexBarsHeight().orZero() -
                    resources.getDimensionPixelSize(R.dimen.main_page_menu_height)

            var recyclerContentHeight = 0
            recycler.children.forEach {
                recyclerContentHeight += it.height
            }

//            ToDo убрать эти костыли, но пока не получается сделать по другому.
            val currentScreenHeight = if (isScreenSizeSmall) svContent.getChildAt(0).height else screenHeight

            return if (isSynchronized) {
                (currentScreenHeight - emptyList.height) / 2
            } else {
                (currentScreenHeight -
                        (emptyList.height + recyclerContentHeight + resources.getDimensionPixelSize(R.dimen.margin_common) * 2)) / 4
            }
        }
    }

    override fun updateSyncronization(model: ContactsScreenModel) =
        syncAdapter.setData(!model.synchronized, !model.synchronizationAllowed)

    override fun updateContacts(model: ContactsScreenModel) =
        contactsAdapter.newItems(model.contacts)

    private fun onMoreClicked(position: Int, item: Contact) {
        ContactItemOptionsDialog.newInstance(position)
            .show(childFragmentManager, ContactItemOptionsDialog.TAG)
    }

    private fun onItemClicked(position: Int, item: Contact) = onEditClicked(position)

    private fun onSyncButtonClicked() = showAllowSyncDialog()

    private fun showAllowSyncDialog() {
        activity?.let {
            DialogProvider.createDialog(
                it,
                getString(R.string.contacts_sync_dialog_title),
                getString(R.string.contacts_sync_dialog_text),
                getString(R.string.contacts_sync_allow),
                { presenter.onSynchronizationAllowed() },
                getString(R.string.contacts_sync_disallow),
                { presenter.onSynchronizationDisallowed() }
            ).show()
        }
    }

    override fun requestPermission(permission: String) {
        permissionRequestLauncher?.launch(permission)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        recycler?.adapter = null
        recycler = null
    }

    override fun onEditClicked(position: Int) {
        contactsAdapter.getItemByPosition(position)?.let {
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

    override fun removeContact(
        position: Int,
        contact: Contact,
        contacts: MutableList<Contact>
    ) {
        contactsAdapter.removeAt(position)
        binding.recycler.postDelayed(
            {
                if (position > 0) {
                    contactsAdapter.notifyItemChanged(position - 1, Unit)
                }
                if (position < contactsAdapter.itemCount - 1) {
                    contactsAdapter.notifyItemChanged(position, Unit)
                }
            },
            100
        )
        activity?.let {
            showErrorSnackBar(SpanTextUtil.getSpanText(it, contact.name, isDeletedSuccess = true))
        }
    }
}