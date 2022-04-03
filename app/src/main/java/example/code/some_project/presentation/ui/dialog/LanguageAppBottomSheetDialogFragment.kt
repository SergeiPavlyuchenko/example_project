package example.code.some_project.presentation.ui.dialog

import android.app.Dialog
import android.os.Bundle
import android.view.LayoutInflater
import androidx.core.view.isInvisible
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import example.code.some_project.R
import example.code.some_project.databinding.FragmentLanguageAppBinding
import example.code.some_project.presentation.ui.extensions.withArguments
import example.code.some_project.presentation.ui.listener.ProfileFragmentListener
import example.code.some_project.presentation.ui.util.Constansts.KEY_CURRENT_LANGUAGE
import example.code.some_project.presentation.ui.util.Constansts.KEY_KZ_LANGUAGE
import example.code.some_project.presentation.ui.util.Constansts.KEY_RU_LANGUAGE

class LanguageAppBottomSheetDialogFragment : BottomSheetDialogFragment() {

    private val profileFragmentListener: ProfileFragmentListener?
        get() = parentFragment as? ProfileFragmentListener

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val currentLanguage = arguments?.getString(KEY_CURRENT_LANGUAGE, "") ?: ""
        return dialogLaunch(currentLanguage)
    }


    private fun dialogLaunch(currentLanguage: String): BottomSheetDialog {
        val context = requireContext()
        val binding = FragmentLanguageAppBinding.inflate(LayoutInflater.from(context))
        return BottomSheetDialog(requireContext(), R.style.CustomBottomSheetDialogTheme).apply {
            setContentView(binding.root)

            with(binding) {
                itemLanguageAppKz.tvItemLanguageText.text = getString(R.string.kz_language_text)

                if (currentLanguage == KEY_RU_LANGUAGE) {
                    itemLanguageAppRu.ivItemLanguageImage.isInvisible = false
                    itemLanguageAppKz.ivItemLanguageImage.isInvisible = true
                } else {
                    itemLanguageAppRu.ivItemLanguageImage.isInvisible = true
                    itemLanguageAppKz.ivItemLanguageImage.isInvisible = false
                }

                itemLanguageAppRu.tvItemLanguageText.text = getString(R.string.ru_language_text)

                itemLanguageAppRu.llItemLanguageContainer.setOnClickListener {
                    profileFragmentListener?.setNewAppLanguage(KEY_RU_LANGUAGE)
                    itemLanguageAppRu.ivItemLanguageImage.isInvisible = false
                    itemLanguageAppKz.ivItemLanguageImage.isInvisible = true
                    dismiss()
                }

                itemLanguageAppKz.llItemLanguageContainer.setOnClickListener {
                    profileFragmentListener?.setNewAppLanguage(KEY_KZ_LANGUAGE)
                    itemLanguageAppRu.ivItemLanguageImage.isInvisible = true
                    itemLanguageAppKz.ivItemLanguageImage.isInvisible = false
                    dismiss()
                }


            }
        }
    }

    companion object {

        fun newInstance(currentLanguage: String) =
            LanguageAppBottomSheetDialogFragment().withArguments {
                putString(KEY_CURRENT_LANGUAGE, currentLanguage)
            }
    }
}