package example.code.some_project.presentation.ui.fragment.editcontact

import android.os.Bundle
import android.telephony.PhoneNumberFormattingTextWatcher
import android.text.Editable
import android.text.InputFilter
import android.text.TextWatcher
import android.view.View
import androidx.core.os.bundleOf
import androidx.core.view.isVisible
import example.code.some_project.R
import example.code.some_project.databinding.FragmentEditContactBinding
import example.code.some_project.domain.models.Contact
import example.code.some_project.presentation.ui.base.BaseMvpFragment
import example.code.some_project.presentation.ui.provider.DialogProvider
import example.code.some_project.presentation.utils.CharInputFilter
import moxy.ktx.moxyPresenter
import org.koin.android.ext.android.get

class EditContactFragment : BaseMvpFragment<FragmentEditContactBinding>(), EditContactView {

    override val presenter: AbstractEditContactPresenter by moxyPresenter { get() }
    override val contentViewId: Int = R.id.edit_contact_content_view

    override fun createViewBinding() = FragmentEditContactBinding.inflate(layoutInflater)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        initializeData()
    }

    private fun initializeData() {
        presenter.msisdn = arguments?.getString(EXTRA_MSISDN)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initializeUi()
    }

    private fun initializeUi() {
        with(binding) {
            toolbar.setNavigationOnClickListener { goBack() }
            nameInput.addTextChangedListener(HideErrorWatcher(nameError))
            with(phoneInput) {
                addTextChangedListener(HideErrorWatcher(phoneError))
                addTextChangedListener(PhoneNumberFormattingTextWatcher())
                filters = arrayOf(
                    CharInputFilter(CharInputFilter.editContactNumberCharacterSet),
                    InputFilter.LengthFilter(MAX_LENGTH_FOR_CURRENT_MASK)
                )
            }
            saveButton.setOnClickListener {
                val nameString = nameInput.text.toString()
                val phoneString = phoneInput.text.toString()
                presenter.saveContact(nameString, phoneString)
            }
        }
    }

    override fun setupContact(contact: Contact?) = with(binding) {
        if (contact == null) {
            toolbar.setTitle(R.string.contacts_edit_new_title)
            saveButton.setText(R.string.contacts_edit_button_add)
        } else {
            toolbar.setTitle(R.string.contacts_edit_edit_title)
            saveButton.setText(R.string.contacts_edit_button_save)
            nameInput.setText(contact.name)
            phoneInput.setText(contact.number)
        }
    }

    override fun setupMsisdn(msisdn: String) = with(binding) {
        toolbar.setTitle(R.string.contacts_edit_new_title)
        saveButton.setText(R.string.contacts_edit_button_add)
        phoneInput.setText(msisdn)
    }

    override fun onSuccessfullySaved(contact: Contact) {
        parentFragmentManager.setFragmentResult(
            REQUEST_KEY, bundleOf(EXTRA_CONTACT_NAME to contact.name)
        )
        goBack()
    }

    override fun showEmptyName() = with(binding.nameError) {
        isVisible = true
        setText(R.string.contacts_edit_empty_name)
    }

    override fun showEmptyPhone() = with(binding.phoneError) {
        isVisible = true
        setText(R.string.contacts_edit_empty_phone)
    }

    override fun showInvalidPhone() = with(binding.phoneError) {
        isVisible = true
        setText(R.string.contacts_edit_invalid_phone)
    }

    override fun showAlreadyHasContact() {
        activity?.let {
            DialogProvider.createDialog(
                it,
                title = "",
                description = getString(R.string.common_dialog_error_add_repeated_contact_description),
                positiveButtonText = getString(R.string.common_dialog_understand_button_text),
            ).show()
        }
    }

    companion object {

        const val TAG = "EditContactFragment"

        const val REQUEST_KEY = "EDIT_CONTACT_FRAGMENT_RESULT_KEY"

        private const val MAX_LENGTH_FOR_CURRENT_MASK = 20

        private const val EXTRA_MSISDN = "EXTRA_MSISDN"
        const val EXTRA_CONTACT_NAME = "EXTRA_CONTACT_NAME"

        fun createInstance(msisdn: String? = null) = EditContactFragment().apply {
            arguments = Bundle().apply {
                putString(EXTRA_MSISDN, msisdn)
            }
        }
    }
}

private class HideErrorWatcher(private val errorView: View) : TextWatcher {
    override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) = Unit

    override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) = Unit

    override fun afterTextChanged(p0: Editable?) {
        errorView.isVisible = false
    }

}