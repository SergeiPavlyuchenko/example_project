package example.code.some_project.presentation.utils

import android.text.InputFilter
import android.text.Spanned

class CharInputFilter(private val characterSet: Set<Char>) : InputFilter {
    override fun filter(
        source: CharSequence,
        p1: Int,
        p2: Int,
        p3: Spanned?,
        p4: Int,
        p5: Int
    ): CharSequence? =
        if (source.all { it in characterSet }) {
            null
        } else {
            source.filter { it in characterSet }
        }

    companion object {
        val numberCharacterSet = setOf(
            '+', '0', '1', '2', '3', '4', '5', '6', '7', '8', '9', '(', ')', '-', ' '
        )

        val editContactNumberCharacterSet = setOf(
            '+', '0', '1', '2', '3', '4', '5', '6', '7', '8', '9', '(', ')', '-', ' ', '#', '*'
        )

        val codeCharacterSet = setOf('0', '1', '2', '3', '4', '5', '6', '7', '8', '9')
    }
}