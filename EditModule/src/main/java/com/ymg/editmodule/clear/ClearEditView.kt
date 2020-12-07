package com.ymg.editmodule.clear

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Rect
import android.graphics.drawable.Drawable
import android.text.Editable
import android.text.TextWatcher
import android.util.AttributeSet
import android.view.KeyEvent
import android.view.MotionEvent
import android.view.View
import android.view.inputmethod.EditorInfo
import android.view.inputmethod.InputMethodManager
import androidx.core.content.ContextCompat
import com.google.android.material.textfield.TextInputEditText
import com.ymg.editmodule.R



/**
 * @author y-mg
 *
 * 이것은 클리어 버튼을 설정할 수 있는 EditText 입니다.
 * This is EditText where you can set the Clear button.
 */
class ClearEditView : TextInputEditText, TextWatcher, View.OnTouchListener {

    private var onTouchListener: OnTouchListener? = null
    private var clearButtonIcon: Drawable? = null



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
                R.styleable.ClearEditStyle,
                defStyleAttr,
                defStyleAttr
            )

        // 클리어 버튼 아이콘을 설정한다.
        // Set the clear button icon.
        val clearButtonIcon =
            typedArray?.getResourceId(
                R.styleable.ClearEditStyle_ceClearButtonIcon,
                R.drawable.btn_clear
            )

        typedArray?.recycle()


        setInit(clearButtonIcon ?: R.drawable.btn_clear)
    }



    /**
     * Setting Init
     */
    @SuppressLint("ClickableViewAccessibility")
    private fun setInit(
        clearButtonIcon: Int
    ) {
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
        addTextChangedListener(this)
    }



    /**
     * Setting Default
     */
    private fun setDefault() {
        this.apply {
            minHeight = context.resources.getDimension(R.dimen.clear_edit_default_min_height).toInt()
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
        clearButtonIcon?.setVisible(visible, false)

        when (visible && isFocused) {
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
                x > width - paddingRight - drawable.intrinsicWidth -> {
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
     * Setting Text Watcher
     */
    override fun onTextChanged(
        s: CharSequence,
        start: Int,
        before: Int,
        count: Int
    ) {
        if (isFocused) {
            setVisible(s.isNotEmpty())
        }
    }

    override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) { }

    override fun afterTextChanged(s: Editable) { }



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