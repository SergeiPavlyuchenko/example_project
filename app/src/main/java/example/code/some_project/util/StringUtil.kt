package example.code.some_project.util

import example.code.some_project.presentation.ui.util.Constansts.MIN_NUMBER_OF_BLACKLIST
import example.code.some_project.presentation.utils.CharInputFilter
import java.lang.StringBuilder

fun String.toBlacklistNumber() = this.filter { it.isDigit() }

fun String.toLocalNumber() = this.filter { it.isDigit() || it == '+' }

fun String.isAllowedNumber() = this.all { it in CharInputFilter.numberCharacterSet }
        && this.length >= MIN_NUMBER_OF_BLACKLIST

fun String.beforeDot() = this.takeWhile { it != '.' }

fun String.maskFiltered() = this.filter { it !in setOf(' ', '-', '(', ')') }

fun String?.toFormattedNumber(): String {
    if (isNullOrBlank()) {
        return ""
    }

    if (length < 11) {
        return this
    }

    val countryCodeSize = length - 10
    val hasPlus = this[0] == '+'

    return StringBuilder(this)
        .apply {
            insert(countryCodeSize, ' ')
            insert(countryCodeSize + 4, ' ')
            insert(countryCodeSize + 8, '-')
            insert(countryCodeSize + 11, '-')
            if (!hasPlus) {
                insert(0, '+')
            }
        }
        .toString()
}