package com.ymg.editmodule.password

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Canvas
import android.graphics.drawable.Drawable
import android.text.InputType
import android.text.method.HideReturnsTransformationMethod
import android.text.method.PasswordTransformationMethod
import android.util.AttributeSet
import android.view.KeyEvent
import android.view.MotionEvent
import android.view.inputmethod.EditorInfo
import android.view.inputmethod.InputMethodManager
import androidx.core.content.ContextCompat
import com.google.android.material.textfield.TextInputEditText
import com.ymg.editmodule.R



/**
 * @author y-mg
 *
 * 이것은 비밀번호 보안성을 설정할 수 있는 EditText 입니다.
 * This is EditText, which allows you to set password security.
 */
class PasswordEditView : TextInputEditText {

    private var clearButtonEnabled: Boolean = true
    private var clearButtonIcon: Drawable? = null
    private var showPasswordButtonIcon: Drawable? = null
    private var hidePasswordButtonIcon: Drawable? = null
    private var passwordVisible: Boolean = false



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
                R.styleable.PasswordEditStyle,
                defStyleAttr,
                defStyleAttr
            )

        // 클리어 버튼 사용 여부를 설정한다.
        // Set whether or not to use the clear button.
        val clearButtonEnabled =
            typedArray?.getBoolean(
                R.styleable.PasswordEditStyle_peClearButtonEnabled,
                true
            )

        // 클리어 버튼 아이콘을 설정한다.
        // Set the clear button icon.
        val clearButtonIcon =
            typedArray?.getResourceId(
                R.styleable.PasswordEditStyle_peClearButtonIcon,
                R.drawable.btn_clear
            )

        // 비밀번호 보이기 버튼 아이콘을 설정한다.
        // Set the Show Password button icon.
        val showPasswordButtonIcon =
            typedArray?.getResourceId(
                R.styleable.PasswordEditStyle_peShowPasswordButtonIcon,
                R.drawable.btn_password_show
            )

        // 비밀번호 숨기기 버튼 아이콘을 설정한다.
        // Set the Hide Password button icon.
        val hidePasswordButtonIcon =
            typedArray?.getResourceId(
                R.styleable.PasswordEditStyle_peHidePasswordButtonIcon,
                R.drawable.btn_password_hide
            )

        typedArray?.recycle()


        setInit(
            clearButtonEnabled = clearButtonEnabled ?: true,
            clearButtonIcon = clearButtonIcon ?: R.drawable.btn_clear,
            showPasswordButtonIcon = showPasswordButtonIcon ?: R.drawable.btn_password_show,
            hidePasswordButtonIcon = hidePasswordButtonIcon ?: R.drawable.btn_password_hide,
        )
    }



    /**
     * Setting Init
     */
    @SuppressLint("ClickableViewAccessibility")
    private fun setInit(
        clearButtonEnabled: Boolean,
        clearButtonIcon: Int,
        showPasswordButtonIcon: Int,
        hidePasswordButtonIcon: Int
    ) {
        this.clearButtonEnabled = clearButtonEnabled
        this.clearButtonIcon = ContextCompat.getDrawable(context, clearButtonIcon)
        this.showPasswordButtonIcon = ContextCompat.getDrawable(context, showPasswordButtonIcon)
        this.hidePasswordButtonIcon = ContextCompat.getDrawable(context, hidePasswordButtonIcon)


        // Bound Clear Icon
        this.clearButtonIcon?.let {
            it.setBounds(
                0,
                0,
                it.intrinsicWidth,
                it.intrinsicHeight
            )
        }

        // Bound Show Password Icon
        this.showPasswordButtonIcon?.let {
            it.setBounds(
                0,
                0,
                it.intrinsicWidth,
                it.intrinsicHeight
            )
        }

        // Bound Hide Password Icon
        this.hidePasswordButtonIcon?.let {
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
    }



    /**
     * Setting Default
     */
    private fun setDefault() {
        this.apply {
            minHeight = context.resources.getDimension(R.dimen.password_edit_default_min_height).toInt()
            inputType = InputType.TYPE_CLASS_TEXT
            transformationMethod = PasswordTransformationMethod.getInstance()
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
     * Setting Draw Icon
     */
    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)

        if (isFocused && text.toString().isNotEmpty() && clearButtonEnabled) {
            onDrawClearIcon(canvas)
        }

        if (isFocused && text.toString().isNotEmpty()) {
            when (passwordVisible) {
                true -> {
                    onDrawPasswordShowIcon(canvas)
                }

                false -> {
                    onDrawPasswordHideIcon(canvas)
                }
            }
        }
    }

    /**
     * Draw Clear Icon
     */
    private fun onDrawClearIcon(canvas: Canvas) {
        clearButtonIcon?.let {
            canvas.save()
            canvas.translate(
                (width - it.intrinsicWidth).toFloat(),
                (height / 2 - it.intrinsicHeight / 2).toFloat()
            )

            it.draw(canvas)
            canvas.restore()
        }
    }

    /**
     * Draw Show Password Icon
     */
    private fun onDrawPasswordShowIcon(canvas: Canvas) {
        showPasswordButtonIcon?.let {
            canvas.save()

            when (clearButtonEnabled) {
                true -> {
                    canvas.translate(
                        (width - it.intrinsicWidth * 2 - 15).toFloat(),
                        (height / 2 - it.intrinsicHeight / 2).toFloat()
                    )
                }

                false -> {
                    canvas.translate(
                        (width - it.intrinsicWidth).toFloat(),
                        (height / 2 - it.intrinsicHeight / 2).toFloat()
                    )
                }
            }

            it.draw(canvas)
            canvas.restore()
        }
    }

    /**
     * Draw Hide Password Icon
     */
    private fun onDrawPasswordHideIcon(canvas: Canvas) {
        hidePasswordButtonIcon?.let {
            canvas.save()

            when (clearButtonEnabled) {
                true -> {
                    canvas.translate(
                        (width - it.intrinsicWidth * 2 - 15).toFloat(),
                        (height / 2 - it.intrinsicHeight / 2).toFloat()
                    )
                }

                false -> {
                    canvas.translate(
                        (width - it.intrinsicWidth).toFloat(),
                        (height / 2 - it.intrinsicHeight / 2).toFloat()
                    )
                }
            }

            it.draw(canvas)
            canvas.restore()
        }
    }



    /**
     * Setting Touch
     */
    @SuppressLint("ClickableViewAccessibility")
    override fun onTouchEvent(event: MotionEvent): Boolean {
        when (event.action) {
            MotionEvent.ACTION_UP -> {
                clearButtonIcon?.let { clearDrawable ->
                    if (isFocused && clearButtonEnabled && event.x > width - clearDrawable.intrinsicWidth) {
                        setText("")
                        return true
                    }

                    if (isFocused && clearButtonEnabled && event.x > width - clearDrawable.intrinsicWidth * 2 - 15 && event.y > 0 && event.y < height) {
                        when (passwordVisible) {
                            true -> {
                                transformationMethod = PasswordTransformationMethod.getInstance()
                                passwordVisible = false

                                text?.length?.let {
                                    setSelection(it)
                                }
                            }

                            false -> {
                                transformationMethod = HideReturnsTransformationMethod.getInstance()
                                passwordVisible = true

                                text?.length?.let {
                                    setSelection(it)
                                }
                            }
                        }
                        return true
                    }

                    if (isFocused && !clearButtonEnabled && event.x > width - clearDrawable.intrinsicWidth) {
                        when (passwordVisible) {
                            true -> {
                                transformationMethod = PasswordTransformationMethod.getInstance()
                                passwordVisible = false

                                text?.length?.let {
                                    setSelection(it)
                                }
                            }

                            false -> {
                                transformationMethod = HideReturnsTransformationMethod.getInstance()
                                passwordVisible = true

                                text?.length?.let {
                                    setSelection(it)
                                }
                            }
                        }
                        return true
                    }
                }
            }
        }
        return super.onTouchEvent(event)
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