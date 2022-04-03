package example.code.some_project.presentation.ui.dialog

import android.app.Dialog
import android.os.Bundle
import android.view.LayoutInflater
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import example.code.some_project.R
import example.code.some_project.databinding.DialogContactItemBinding

class ContactItemOptionsDialog : BottomSheetDialogFragment() {

    private var position: Int = 0

    private val dialogInterfaceListener: ContactItemOptionsDialogListener?
        get() = parentFragment as? ContactItemOptionsDialogListener

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        position = arguments?.getInt(EXTRA_POSITION, 0) ?: 0
        return dialogLaunch()
    }

    private fun dialogLaunch(): BottomSheetDialog {
        val context = requireContext()
        return BottomSheetDialog(context, R.style.CustomBottomSheetDialogTheme).apply {
            val binding = DialogContactItemBinding.inflate(LayoutInflater.from(context))
            setContentView(binding.root)

            with(binding) {

                editContact.setOnClickListener {
                    dismiss()
                    dialogInterfaceListener?.onEditClicked(position)
                }
                removeContact.setOnClickListener {
                    dismiss()
                    dialogInterfaceListener?.onRemoveClicked(position)
                }
            }
        }
    }

    companion object {
        const val TAG = "ContactItemOptionsDialog"

        private const val EXTRA_POSITION = "EXTRA_POSITION"

        fun newInstance(position: Int): ContactItemOptionsDialog {
            return ContactItemOptionsDialog().apply {
                arguments = Bundle().apply {
                    putInt(EXTRA_POSITION, position)
                }
            }
        }
    }
}

interface ContactItemOptionsDialogListener {

    fun onEditClicked(position: Int)

    fun onRemoveClicked(position: Int)
}