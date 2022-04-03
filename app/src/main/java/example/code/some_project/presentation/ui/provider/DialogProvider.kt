package example.code.some_project.presentation.ui.provider

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.widget.TextView
import androidx.appcompat.app.AlertDialog
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import example.code.some_project.R

object DialogProvider {

    fun createDialog(
        context: Context,
        title: String,
        description: String = "",
        positiveButtonText: String = "",
        positiveButtonListener: (() -> Unit)? = null,
        negativeButtonText: String? = null,
        negativeButtonListener: (() -> Unit)? = null,
        spannedDescription: CharSequence? = null
    ): AlertDialog {
        val view = LayoutInflater.from(context).inflate(R.layout.dialog_common_alert, null)

        val dialog = MaterialAlertDialogBuilder(context, R.style.CustomThemeAlertDialog)
            .setView(view)
            .create()

        view.apply {
            findViewById<TextView>(R.id.tvCommonAlertDialogTitle)?.apply {
                text = title
            }

            findViewById<TextView>(R.id.tvCommonAlertDialogDescription)?.apply {
                text = spannedDescription ?: description
            }

            findViewById<TextView>(R.id.tvCommonAlertDialogPositiveButton)?.apply {
                text = positiveButtonText
                visibility = if (text.isNotEmpty()) View.VISIBLE else View.GONE
                setOnClickListener {
                    if (positiveButtonListener != null) {
                        positiveButtonListener()
                    }
                    dialog.dismiss()
                }
            }

            findViewById<TextView>(R.id.tvCommonAlertDialogNegativeButton)?.apply {
                text = negativeButtonText
                visibility = if (text.isNotEmpty()) View.VISIBLE else View.GONE
                setOnClickListener {
                    if (negativeButtonListener != null) {
                        negativeButtonListener()
                    }
                    dialog.dismiss()
                }
            }
        }

        return dialog
    }
}