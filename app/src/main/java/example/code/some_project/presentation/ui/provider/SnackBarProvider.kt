package example.code.some_project.presentation.ui.provider

import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.widget.FrameLayout
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.core.content.ContextCompat
import com.google.android.material.snackbar.Snackbar
import example.code.some_project.R
import example.code.some_project.presentation.ui.extensions.setOnClick

object SnackBarProvider {

    fun showSnackBar(container: View, text: CharSequence, isNegative: Boolean = false) =
        Snackbar.make(container, text, Snackbar.LENGTH_SHORT).apply {
            val snackView = LayoutInflater.from(context).inflate(R.layout.snackbar_layout, null)
            val containerView = snackView.findViewById<LinearLayout>(R.id.layoutSnackBarContainer)
            val textView = snackView.findViewById<TextView>(R.id.tvSnackBarText)
            val closeView = snackView.findViewById<ImageView>(R.id.ivSnackBarClose)
            val newParams =
                (view.layoutParams as? FrameLayout.LayoutParams).apply {
                    this?.gravity = Gravity.TOP
                }

            if (isNegative) {
                containerView.background =
                    ContextCompat.getDrawable(context, R.drawable.bg_snackbar_negative)
                textView.setTextColor(ContextCompat.getColor(context, R.color.red))
                closeView.setColorFilter(ContextCompat.getColor(context, R.color.red))
            } else {
                containerView.background =
                    ContextCompat.getDrawable(context, R.drawable.bg_snackbar_positive)
                textView.setTextColor(ContextCompat.getColor(context, R.color.white))
                closeView.setColorFilter(ContextCompat.getColor(context, R.color.white))
            }

            textView.text = text
            closeView.setOnClick(this::dismiss)

            view.run {
                background = null
                setPadding(0, 0, 0, 0)
                layoutParams = newParams
            }
            (view as? Snackbar.SnackbarLayout)?.addView(snackView)

        }.show()
}