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


class PasswordEditView : TextInputEditText {

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



    // 클리어 버튼 허용 여부
    private var clearButtonEnabled: Boolean = true

    // 클리어 버튼 Drawable
    private var clearButtonIcon: Drawable? = null

    // 비밀번호 보여주기 버튼 Drawable
    private var passwordShowButtonIcon: Drawable? = null

    // 비밀번호 숨기기 버튼 Drawable
    private var passwordHideButtonIcon: Drawable? = null

    // 비밀번호 보여주기/숨기기 여부
    private var passwordVisible: Boolean = false



    private fun init(context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0) {
        val typedArray =
            context.theme?.obtainStyledAttributes(
                attrs,
                R.styleable.PasswordEditStyle,
                defStyleAttr,
                defStyleAttr
            )


        // 클리어 버튼 허용 여부
        val clearButtonEnabled =
            typedArray?.getBoolean(
                R.styleable.PasswordEditStyle_peClearButtonEnabled,
                true
            )

        // 클리어 버튼 아이콘
        val clearButtonIcon =
            typedArray?.getResourceId(
                R.styleable.PasswordEditStyle_peClearButtonIcon,
                R.drawable.btn_clear
            )

        // 비밀번호 보여주기 아이콘
        val passwordShowButtonIcon =
            typedArray?.getResourceId(
                R.styleable.PasswordEditStyle_pePasswordShowButtonIcon,
                R.drawable.btn_password_show
            )

        // 비밀번호 숨기기 아이콘
        val passwordHideButtonIcon =
            typedArray?.getResourceId(
                R.styleable.PasswordEditStyle_pePasswordHideButtonIcon,
                R.drawable.btn_password_show
            )

        typedArray?.recycle()


        if (clearButtonEnabled != null && clearButtonIcon != null && passwordShowButtonIcon != null && passwordHideButtonIcon != null) {
            setInit(clearButtonEnabled, clearButtonIcon, passwordShowButtonIcon, passwordHideButtonIcon)
        }
    }



    /**
     * 설정
     */
    @SuppressLint("ClickableViewAccessibility")
    private fun setInit(
        clearButtonEnabled: Boolean,
        clearButtonIcon: Int,
        passwordShowButtonIcon: Int,
        passwordHideButtonIcon: Int
    ) {
        this.clearButtonEnabled = clearButtonEnabled

        ContextCompat.getDrawable(context, clearButtonIcon)?.let {
            this.clearButtonIcon = it
        }

        ContextCompat.getDrawable(context, passwordShowButtonIcon)?.let {
            this.passwordShowButtonIcon = it
        }

        ContextCompat.getDrawable(context, passwordHideButtonIcon)?.let {
            this.passwordHideButtonIcon = it
        }


        // 이미지 그리기
        this.clearButtonIcon?.let {
            it.setBounds(
                0,
                0,
                it.intrinsicWidth,
                it.intrinsicHeight
            )
        }

        this.passwordShowButtonIcon?.let {
            it.setBounds(
                0,
                0,
                it.intrinsicWidth,
                it.intrinsicHeight
            )
        }

        this.passwordHideButtonIcon?.let {
            it.setBounds(
                0,
                0,
                it.intrinsicWidth,
                it.intrinsicHeight
            )
        }


        // Edit 기본 설정, Edit Action 설정
        setDefaultPasswordEditView()
        setActionPasswordEditView()
    }





    /**
     * 기본 설정
     */
    private fun setDefaultPasswordEditView() {
        this.apply {
            minHeight = context.resources.getDimension(R.dimen.password_edit_default_min_height).toInt()
            inputType = InputType.TYPE_CLASS_TEXT
            transformationMethod = PasswordTransformationMethod.getInstance()
        }
    }

    /**
     * 소프트 키보드 Action
     */
    private fun setActionPasswordEditView() {
        this.setOnEditorActionListener {  _, actionId, _ ->
            when (actionId) {
                // DONE 버튼
                EditorInfo.IME_ACTION_DONE -> {
                    val inputMethodManager: InputMethodManager =
                        context.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
                    inputMethodManager.hideSoftInputFromWindow(this.windowToken, 0)
                    this.clearFocus()
                    false
                }

                // NEXT 버튼
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
     * 아이콘 그리기
     */
    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)

        if (isFocused && text.toString().isNotEmpty() && clearButtonEnabled) {
            setDrawClearIcon(canvas)
        }

        if (isFocused && text.toString().isNotEmpty()) {
            when (passwordVisible) {
                true -> {
                    setDrawPasswordShowIcon(canvas)
                }

                false -> {
                    setDrawPasswordHideIcon(canvas)
                }
            }
        }
    }

    /**
     * 클리어 아이콘 그리기 설정
     */
    private fun setDrawClearIcon(canvas: Canvas) {
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
     * 비밀번호 보여주기 아이콘 그리기 설정
     */
    private fun setDrawPasswordShowIcon(canvas: Canvas) {
        passwordShowButtonIcon?.let {
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
     * 비밀번호 숨기기 아이콘 그리기 설정
     */
    private fun setDrawPasswordHideIcon(canvas: Canvas) {
        passwordHideButtonIcon?.let {
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
     * 터치 이벤트
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
     * 백 버튼 시 포커스 제거
     */
    override fun onKeyPreIme(keyCode: Int, event: KeyEvent?): Boolean {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            this.clearFocus()
        }

        return super.onKeyPreIme(keyCode, event)
    }



    /**
     * 제안 거부
     */
    override fun isSuggestionsEnabled(): Boolean {
        return false
    }
}