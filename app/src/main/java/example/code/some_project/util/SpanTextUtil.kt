package example.code.some_project.util

import android.content.Context
import android.graphics.Paint
import android.graphics.Typeface
import android.text.Spannable
import android.text.SpannableString
import android.text.SpannableStringBuilder
import android.text.TextPaint
import android.text.style.TypefaceSpan
import androidx.core.content.res.ResourcesCompat
import example.code.some_project.R

object SpanTextUtil {
    fun getSpanText(
        context: Context,
        name: String,
        isSavedSuccess: Boolean = false,
        isDeletedSuccess: Boolean = false
    ): SpannableStringBuilder {
        val resources = context.resources
        val contact = resources.getString(R.string.snackbar_text_contact)
        val spannableStringBuilder = SpannableStringBuilder(contact)
        val boldFont = ResourcesCompat.getFont(context, R.font.open_sans_bold)
        val boldFontSpan = CustomTypefaceSpan("", boldFont)
        val text = SpannableString(name).apply {
            setSpan(
                boldFontSpan, 0, this.length,
                Spannable.SPAN_EXCLUSIVE_EXCLUSIVE
            )
        }
        with(spannableStringBuilder) {
            append(" ")
            append(text)
            append(" ")
            append(
                resources.getString(
                    when {
                        isSavedSuccess -> R.string.snackbar_text_saved_is_success
                        isDeletedSuccess -> R.string.snackbar_text_deleted_is_success
                        else -> error("Unknown case for SnackBar showing")
                    }
                )
            )
        }
        return spannableStringBuilder
    }
}

private class CustomTypefaceSpan(fontFamily: String?, private val newType: Typeface?) :
    TypefaceSpan(fontFamily) {
    override fun updateDrawState(ds: TextPaint) {
        newType?.let { applyCustomTypeFace(ds, it) }
    }

    override fun updateMeasureState(paint: TextPaint) {
        newType?.let { applyCustomTypeFace(paint, it) }
    }

    companion object {
        private fun applyCustomTypeFace(paint: Paint, tf: Typeface) {
            paint.typeface = tf
        }
    }
}