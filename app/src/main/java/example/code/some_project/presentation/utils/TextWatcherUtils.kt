package example.code.some_project.presentation.utils

import android.text.Editable
import android.text.TextWatcher
import android.widget.EditText


class CustomTextWatcher(private val editText: EditText) : TextWatcher {
    override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) = Unit
    override fun afterTextChanged(s: Editable) = Unit
    override fun onTextChanged(
        s: CharSequence, cursorPosition: Int, before: Int,
        count: Int
    ) { modifyNumber(s, cursorPosition, before, count) }

    private fun modifyNumber(s: CharSequence, cursorPosition: Int, before: Int, count: Int) {
        var currentCursorPosition = cursorPosition
        if (before == 0 && count == 1) {  //Entering values
            var value = s.toString()
            var a = ""
            var b = ""
            var c = ""
            if (value.isNotEmpty()) {
                value = value.replace("-", "")
                if (value.length >= 3) {
                    a = value.substring(0, 3)
                } else if (value.length < 3) {
                    a = value.substring(0, value.length)
                }
                if (value.length >= 6) {
                    b = value.substring(3, 6)
                    c = value.substring(6, value.length)
                } else if (value.length in 4..5) {
                    b = value.substring(3, value.length)
                }
                val stringBuffer = StringBuffer()
                if (a.isNotEmpty()) {
                    stringBuffer.append(a)
                }
                if (b.isNotEmpty()) {
                    stringBuffer.append("-")
                    stringBuffer.append(b)
                }
                if (c.isNotEmpty()) {
                    stringBuffer.append("-")
                    stringBuffer.append(c)
                }
                editText.removeTextChangedListener(this)
                editText.setText(stringBuffer.toString())
                currentCursorPosition =
                    if (currentCursorPosition == 3 || currentCursorPosition == 7) {
                        currentCursorPosition + 2
                    } else {
                        currentCursorPosition + 1
                    }
                if (currentCursorPosition <= editText.text.toString().length) {
                    editText.setSelection(currentCursorPosition)
                } else {
                    editText.setSelection(editText.text.toString().length)
                }
                editText.addTextChangedListener(this)
            } else {
                editText.removeTextChangedListener(this)
                editText.setText("")
                editText.addTextChangedListener(this)
            }
        }
        if (before == 1 && count == 0) {  //Deleting values
            var value: String? = s.toString()
            var a = ""
            var b = ""
            var c = ""
            if (value != null && value.isNotEmpty()) {
                value = value.replace("-", "")
                if (currentCursorPosition == 3) {
                    value = removeCharAt(value, currentCursorPosition - 1, s.toString().length - 1)
                } else if (currentCursorPosition == 7) {
                    value = removeCharAt(value, currentCursorPosition - 2, s.toString().length - 2)
                }

                if (value != null) {
                    if (value.length >= 3) {
                        a = value.substring(0, 3)
                    } else if (value.length < 3) {
                        a = value.substring(0, value.length)
                    }
                    if (value.length >= 6) {
                        b = value.substring(3, 6)
                        c = value.substring(6, value.length)
                    } else if (value.length in 4..5) {
                        b = value.substring(3, value.length)
                    }
                }

                val stringBuffer = StringBuffer()
                if (a.isNotEmpty()) {
                    stringBuffer.append(a)
                }
                if (b.isNotEmpty()) {
                    stringBuffer.append("-")
                    stringBuffer.append(b)
                }
                if (c.isNotEmpty()) {
                    stringBuffer.append("-")
                    stringBuffer.append(c)
                }
                editText.removeTextChangedListener(this)
                editText.setText(stringBuffer.toString())
                if (currentCursorPosition == 3 || currentCursorPosition == 7) {
                    currentCursorPosition -= 1
                }
                if (currentCursorPosition <= editText.text.toString().length) {
                    editText.setSelection(currentCursorPosition)
                } else {
                    editText.setSelection(editText.text.toString().length)
                }
                editText.addTextChangedListener(this)
            } else {
                editText.removeTextChangedListener(this)
                editText.setText("")
                editText.addTextChangedListener(this)
            }
        }
    }

    private fun removeCharAt(s: String, pos: Int, length: Int): String? {
        var value = ""
        if (length > pos) {
            value = s.substring(pos + 1)
        }
        return s.substring(0, pos) + value
    }
}



