package com.ymg.editmodule.decimal

import android.text.InputFilter
import android.text.Spanned
import android.text.SpannedString
import java.util.regex.Matcher
import java.util.regex.Pattern



/**
 * @author y-mg
 *
 * 입력 필터를 설정한다.
 * Set the input filter.
 */
class DecimalFormatFilter(
    numberCut: Int,
    decimalCut: Int
) : InputFilter {
    private var pattern: Pattern = Pattern.compile("[0-9]{0," + (numberCut - 1) + "}+((\\.[0-9]{0," + (decimalCut - 1) + "})?)||(\\.)?")

    override fun filter(
        source: CharSequence,
        start: Int,
        end: Int,
        dest: Spanned,
        dstart: Int,
        dend: Int
    ): CharSequence? {
        val check = dest.toString().replace("[^\\d.]".toRegex(), "")

        SpannedString.valueOf(check)

        val matcher: Matcher = pattern.matcher(check)

        return when {
            !matcher.matches() -> {
                ""
            }

            else -> {
                null
            }
        }
    }
}