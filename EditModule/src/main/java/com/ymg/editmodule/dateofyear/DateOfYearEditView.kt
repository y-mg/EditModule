package com.ymg.editmodule.dateofyear

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Rect
import android.graphics.drawable.Drawable
import android.text.InputFilter
import android.text.InputFilter.LengthFilter
import android.text.InputType
import android.util.AttributeSet
import android.view.KeyEvent
import android.view.MotionEvent
import android.view.View
import android.view.inputmethod.EditorInfo
import android.view.inputmethod.InputMethodManager
import androidx.core.content.ContextCompat
import com.google.android.material.textfield.TextInputEditText
import com.jakewharton.rxbinding4.widget.textChanges
import com.ymg.editmodule.R



/**
 * @author y-mg
 *
 * 이것은 생년월일을 설정할 수 있는 EditText 입니다.
 * This is EditText, where you can set the date of birth.
 */
class DateOfYearEditView : TextInputEditText, View.OnTouchListener {

    private var onTouchListener: OnTouchListener? = null
    private var clearButtonEnabled: Boolean = true
    private var clearButtonIcon: Drawable? = null

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
                R.styleable.DateOfYearEditStyle,
                defStyleAttr,
                defStyleAttr
            )

        // 클리어 버튼 사용 여부를 설정한다.
        // Set whether or not to use the clear button.
        val clearButtonEnabled =
            typedArray?.getBoolean(
                R.styleable.DateOfYearEditStyle_doyClearButtonEnabled,
                true
            )

        // 클리어 버튼 아이콘을 설정한다.
        // Set the clear button icon.
        val clearButtonIcon =
            typedArray?.getResourceId(
                R.styleable.DateOfYearEditStyle_doyClearButtonIcon,
                R.drawable.btn_clear
            )

        typedArray?.recycle()


        setInit(
            clearButtonEnabled = clearButtonEnabled ?: true,
            clearButtonIcon = clearButtonIcon ?: R.drawable.btn_clear
        )
    }



    /**
     * Setting Init
     */
    @SuppressLint("ClickableViewAccessibility")
    private fun setInit(
        clearButtonEnabled: Boolean,
        clearButtonIcon: Int
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

        // Setting Default, Action
        setDefault()
        setAction()

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
                getDateOfYear(it.toString())
            }
            .subscribe {
                isFormatting = true

                if (it.isNotEmpty()) {
                    this.setText(it)
                    this.setSelection(it.length)
                }

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
            minHeight = context.resources.getDimension(R.dimen.date_of_year_edit_default_min_height).toInt()
            filters = arrayOf<InputFilter>(LengthFilter(10))
            inputType = InputType.TYPE_CLASS_NUMBER
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
     * Setting Visible
     */
    private fun setVisible(visible: Boolean) {
        this.clearButtonIcon?.setVisible(visible, false)

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

            false -> {
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
     * Return Date Of Year Format
     */
    private fun getDateOfYear(str: String): String {
        val value = str.replace(Regex("[^0-9]"), "")
        val sb = StringBuilder()

        when {
            // Action Delete: Delete "-"
            value.length == 4 && str.length == 5 -> {
                sb.append(value)
            }

            // Action Input: Add "-"
            value.length == 5 -> {
                sb.append(value.substring(0, 4))
                sb.append("-")
                sb.append(value.last())
            }

            // Action Delete: Delete "-"
            value.length == 6 && str.length == 8 -> {
                sb.append(value.substring(0, 4))
                sb.append("-")
                sb.append(value.substring(4, 6))
            }

            // Action Input: Add "-"
            value.length == 7 -> {
                sb.append(value.substring(0, 4))
                sb.append("-")
                sb.append(value.substring(4, 6))
                sb.append("-")
                sb.append(value.last())
            }

            else -> {
                sb.append(str)
            }
        }

        return sb.toString()
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
}