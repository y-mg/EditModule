package com.ymg.editmodule.decimal

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Rect
import android.graphics.drawable.Drawable
import android.text.InputFilter
import android.text.InputType
import android.util.AttributeSet
import android.view.*
import android.view.inputmethod.EditorInfo
import android.view.inputmethod.InputMethodManager
import androidx.core.content.ContextCompat
import com.google.android.material.textfield.TextInputEditText
import com.jakewharton.rxbinding4.widget.textChanges
import com.ymg.editmodule.R
import java.text.NumberFormat



/**
 * @author y-mg
 *
 * 이것은 소수를 천 단위일 때마다 "," 로 분리하는 EditText 입니다.<br/>
 * This is a EditText that separates the decimal number into "," every thousand units.
 */
class DecimalFormatEditView : TextInputEditText, View.OnTouchListener {

    private var onTouchListener: OnTouchListener? = null

    private var clearButtonEnabled: Boolean = true
    private var clearButtonIcon: Drawable? = null
    private var addEditStart: String = ""

    // Check Formatting
    private var isFormatting = false



    constructor(context: Context) : super(context) {
        init(context)
    }

    constructor(context: Context, attrs: AttributeSet?) : super(context, attrs) {
        init(context, attrs)
    }

    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int) : super(
        context,
        attrs,
        defStyleAttr
    ) {
        init(context, attrs, defStyleAttr)
    }



    private fun init(context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0) {
        val typedArray =
            context.theme?.obtainStyledAttributes(
                attrs,
                R.styleable.DecimalFormatEditStyle,
                defStyleAttr,
                defStyleAttr
            )

        // 클리어 버튼의 사용 여부를 설정한다.
        // Set whether or not to use the clear button.
        val clearButtonEnabled =
            typedArray?.getBoolean(
                R.styleable.DecimalFormatEditStyle_dfClearButtonEnabled,
                true
            )

        // 클리어 버튼의 아이콘을 설정한다.
        // Set the icon for the clear button.
        val clearButtonIcon =
            typedArray?.getResourceId(
                R.styleable.DecimalFormatEditStyle_dfClearButtonIcon,
                R.drawable.btn_clear
            )

        // 정수의 자릿수이다.
        // It's an integer number.
        val numberCut =
            typedArray?.getInteger(
                R.styleable.DecimalFormatEditStyle_dfNumberCut,
                16
            )

        // 소수점 이하 자릿수이다.
        // It's a decimal place.
        val decimalCut =
            typedArray?.getInteger(
                R.styleable.DecimalFormatEditStyle_dfDecimalCut,
                8
            )

        // 맨 앞에 문자열을 추가한다.
        // Add a string to the beginning.
        val addEditStart =
            typedArray?.getString(
                R.styleable.DecimalFormatEditStyle_dfAddEditStart
            )

        typedArray?.recycle()


        setInit(
            clearButtonEnabled = clearButtonEnabled ?: true,
            clearButtonIcon = clearButtonIcon ?: R.drawable.btn_clear,
            numberCut = numberCut ?: 16,
            decimalCut = decimalCut ?: 8,
            addEditStart = addEditStart ?: ""
        )
    }



    /**
     * Setting Init
     */
    @SuppressLint("ClickableViewAccessibility")
    private fun setInit(
        clearButtonEnabled: Boolean,
        clearButtonIcon: Int,
        numberCut: Int,
        decimalCut: Int,
        addEditStart: String
    ) {
        this.clearButtonEnabled = clearButtonEnabled
        this.clearButtonIcon = ContextCompat.getDrawable(context, clearButtonIcon)


        // Bound Clear Icon
        this.clearButtonIcon?.let {
            it.setBounds(
                0,
                0,
                it.intrinsicWidth,
                it.intrinsicHeight
            )
        }

        // Setting Default, Action, Input Filter
        setDefault()
        setAction()
        setInputFilter(numberCut, decimalCut)

        // Setting AddEditStart
        setAddEditStart(addEditStart)

        // Setting Visible
        setVisible(false)

        // Setting Listener
        super.setOnTouchListener(this)

        // Setting Text Watcher
        this.textChanges()
            .filter {
                !isFormatting
            }
            .filter {
                it != null
            }
            .map {
                val value = it.toString().replace("[^\\d.,]".toRegex(), "")

                when {
                    value.startsWith(".") || value.isEmpty() -> {
                        ""
                    }

                    value.isNotEmpty() && value.last().toString() == "." -> {
                        "${addEditStart}$value"
                    }

                    else -> {
                        "$addEditStart${getCommaFormat(value)}"
                    }
                }
            }
            .subscribe {
                isFormatting = true

                this.setText(it)
                this.setSelection(it.length)

                if (isFocused) {
                    setVisible(it.isNotEmpty())
                }

                isFormatting = false
            }
    }



    /**
     * Setting Default
     */
    private fun setDefault() {
        this.apply {
            minHeight = context.resources.getDimension(R.dimen.decimal_format_edit_default_min_height).toInt()
            inputType = InputType.TYPE_CLASS_NUMBER or InputType.TYPE_NUMBER_FLAG_DECIMAL
        }
    }



    /**
     * Setting Action
     */
    private fun setAction() {
        this.setOnEditorActionListener {  _, actionId, _ ->
            when (actionId) {
                // Action Done
                EditorInfo.IME_ACTION_DONE -> {
                    val inputMethodManager: InputMethodManager =
                        context.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
                    inputMethodManager.hideSoftInputFromWindow(this.windowToken, 0)
                    this.clearFocus()
                    false
                }

                // Action Next
                EditorInfo.IME_ACTION_NEXT -> {
                    this.clearFocus()
                    false
                }

                else -> {
                    true
                }
            }
        }
    }



    /**
     * - 입력 필터를 설정한다.
     * - Set the input filter.
     *
     * @param numberCut -> Integer Digit
     *
     * @param decimalCut -> Decimal Digit
     */
    fun setInputFilter(
        numberCut: Int,
        decimalCut: Int,
    ) {
        this.filters = arrayOf<InputFilter>(
            DecimalFormatFilter(
                numberCut,
                decimalCut
            )
        )
    }



    /**
     * - 맨 앞에 문자열을 추가한다.
     * - Add a string to the beginning.
     *
     * @param addEditStart -> Value to be added first
     */
    fun setAddEditStart(addEditStart: String) {
        this.addEditStart = addEditStart
    }



    /**
     * Setting Visible
     */
    private fun setVisible(visible: Boolean) {
        clearButtonIcon?.setVisible(visible, false)

        when (visible && clearButtonEnabled && isFocused) {
            true -> {
                setCompoundDrawables(null, null, clearButtonIcon, null)
            }

            false -> {
                setCompoundDrawables(null, null, null, null)
            }
        }
    }



    /**
     * Setting Focus
     */
    override fun onFocusChanged(hasFocus: Boolean, direction: Int, previouslyFocusedRect: Rect?) {
        super.onFocusChanged(hasFocus, direction, previouslyFocusedRect)

        when (hasFocus) {
            true -> {
                setVisible(!text.isNullOrEmpty())
            }

            else -> {
                setVisible(false)
            }
        }
    }



    /**
     * Setting Touch
     */
    override fun setOnTouchListener(onTouchListener: OnTouchListener) {
        this.onTouchListener = onTouchListener
    }

    override fun onTouch(view: View?, motionEvent: MotionEvent): Boolean {
        val x = motionEvent.x.toInt()

        clearButtonIcon?.let { drawable ->
            when {
                drawable.isVisible &&
                x > width - paddingRight - drawable.intrinsicWidth &&
                clearButtonEnabled -> {
                    if (motionEvent.action == MotionEvent.ACTION_UP) {
                        error = null
                        text = null
                    }
                    return true
                }


                else -> {
                    onTouchListener?.onTouch(view, motionEvent) ?: let {
                        return false
                    }
                }
            }
        }

        return false
    }



    /**
     * Return separate an integer with "," every thousand units.
     */
    private fun getCommaFormat(str: String): String {
        val value = str.replace("[^\\d.]".toRegex(), "")

        return when {
            value.contains(".") -> {
                val number = value.substring(0, value.indexOf("."))
                val decimal = value.substring(value.indexOf("."))

                val formatter = NumberFormat.getNumberInstance()
                "${formatter.format(number.toBigDecimal())}${decimal}"
            }

            else -> {
                val formatter = NumberFormat.getNumberInstance()
                formatter.format(value.toBigDecimal())
            }
        }
    }



    /**
     * Setting Back Key
     */
    override fun onKeyPreIme(keyCode: Int, event: KeyEvent?): Boolean {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            this.clearFocus()
        }

        return super.onKeyPreIme(keyCode, event)
    }



    /**
     * Setting Suggestion Disable
     */
    override fun isSuggestionsEnabled(): Boolean {
        return false
    }



    /**
     * - 오직 소수 값을 가져온다.
     * - Only take a decimal value.
     */
    fun getFormatText(): String {
        return this.text.toString().replace("[^\\d.]".toRegex(), "")
    }
}