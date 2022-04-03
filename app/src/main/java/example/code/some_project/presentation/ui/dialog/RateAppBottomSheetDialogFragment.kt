package example.code.some_project.presentation.ui.dialog

import android.app.Dialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import androidx.core.view.isVisible
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import example.code.some_project.R
import example.code.some_project.databinding.FragmentRateAppBinding

class RateAppBottomSheetDialogFragment : BottomSheetDialogFragment() {

    private var rating: Int = 0

    private val listener: RateAppBottomSheetDialogListener?
        get() = parentFragment as? RateAppBottomSheetDialogListener

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val auto = arguments?.getBoolean(EXTRA_AUTO) ?: false
        return dialogLaunch(auto)
    }

    private fun dialogLaunch(auto: Boolean): BottomSheetDialog {
        return BottomSheetDialog(requireContext(), R.style.CustomBottomSheetDialogTheme).apply {
            val binding = FragmentRateAppBinding.inflate(LayoutInflater.from(context))
            setContentView(binding.root)

            with (binding) {
                RatingHelper(listOf(star1, star2, star3, star4, star5)) {
                    rating = it
                    when (it) {
                        0 -> {
                            title.setText(R.string.rate_app_empty_title)
                            text.setText(R.string.rate_app_empty_text)
                            btn.setText(R.string.rate_app_empty_button)
                            btn.isVisible = auto
                        }
                        1 -> {
                            title.setText(R.string.rate_app_1_title)
                            text.setText(R.string.rate_app_1_text)
                            btn.setText(R.string.rate_app_bad_button)
                            btn.isVisible = true
                        }
                        2 -> {
                            title.setText(R.string.rate_app_2_title)
                            text.setText(R.string.rate_app_2_text)
                            btn.setText(R.string.rate_app_bad_button)
                            btn.isVisible = true
                        }
                        3 -> {
                            title.setText(R.string.rate_app_3_title)
                            text.setText(R.string.rate_app_3_text)
                            btn.setText(R.string.rate_app_bad_button)
                            btn.isVisible = true
                        }
                        4 -> {
                            title.setText(R.string.rate_app_4_title)
                            text.setText(R.string.rate_app_4_text)
                            btn.setText(R.string.rate_app_good_button)
                            btn.isVisible = true
                        }
                        5 -> {
                            title.setText(R.string.rate_app_5_title)
                            text.setText(R.string.rate_app_5_text)
                            btn.setText(R.string.rate_app_good_button)
                            btn.isVisible = true
                        }
                    }
                }

                btn.isVisible = auto
                btn.setOnClickListener {
                    dismiss()
                    if (rating == 0) {
                        listener?.hideAlways()
                    } else {
                        listener?.ratingSelected(rating)
                    }
                }
            }
        }
    }

    companion object {

        const val TAG = "RateAppBottomSheetDialogFragment"

        private const val EXTRA_AUTO = "EXTRA_AUTO"

        fun createInstance(auto: Boolean = false) = RateAppBottomSheetDialogFragment().apply {
            arguments = Bundle().apply {
                putBoolean(EXTRA_AUTO, auto)
            }
        }
    }

    private class RatingHelper(
        private val ratingViews: List<out View>,
        ratingChangedListener: (Int) -> Unit
    ) {

        init {
            ratingViews.withIndex().forEach { (index, view) ->
                view.setOnClickListener {
                    val rating = index + 1
                    setRating(rating)
                    ratingChangedListener(rating)
                }
            }
        }

        private fun setRating(rating: Int) {
            ratingViews.withIndex().forEach { (index, view) ->
                view.isSelected = rating > index
            }
        }
    }
}

interface RateAppBottomSheetDialogListener {

    fun hideAlways()

    fun ratingSelected(rating: Int)
}