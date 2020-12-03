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



class ClearEditView : TextInputEditText, TextWatcher, View.OnTouchListener {

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



    // 터치 리스너
    private var onTouchListener: OnTouchListener? = null

    // 클리어 버튼 Drawable
    private var clearButtonIcon: Drawable? = null



    private fun init(context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0) {
        val typedArray =
            context.theme?.obtainStyledAttributes(
                attrs,
                R.styleable.ClearEditStyle,
                defStyleAttr,
                defStyleAttr
            )

        // 클리어 버튼 아이콘
        val clearButtonIcon =
            typedArray?.getResourceId(
                R.styleable.ClearEditStyle_ceClearButtonIcon,
                R.drawable.btn_clear
            )

        typedArray?.recycle()

        clearButtonIcon?.let {
            setInit(it)
        }
    }



    /**
     * 설정
     */
    @SuppressLint("ClickableViewAccessibility")
    private fun setInit(
        clearButtonIcon: Int
    ) {
        ContextCompat.getDrawable(context, clearButtonIcon)?.let {
            this.clearButtonIcon = it
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


        // Edit 기본 설정, Edit Action 설정
        setDefaultClearEditView()
        setActionClearEditView()

        // 클리어 버튼 아이콘 가시성 설정
        setClearButtonIconVisible(false)

        // 리스너 설정
        super.setOnTouchListener(this)
        addTextChangedListener(this)
    }





    /**
     * 기본 설정
     */
    private fun setDefaultClearEditView() {
        this.apply {
            minHeight = context.resources.getDimension(R.dimen.clear_edit_default_min_height).toInt()
        }
    }



    /**
     * 소프트 키보드 Action
     */
    private fun setActionClearEditView() {
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
     * 클리어 버튼 가시성 설정
     */
    private fun setClearButtonIconVisible(visible: Boolean) {
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
     * 입력창 포커스
     */
    override fun onFocusChanged(hasFocus: Boolean, direction: Int, previouslyFocusedRect: Rect?) {
        super.onFocusChanged(hasFocus, direction, previouslyFocusedRect)

        when (hasFocus) {
            true -> {
                setClearButtonIconVisible(!text.isNullOrEmpty())
            }

            else -> {
                setClearButtonIconVisible(false)
            }
        }
    }



    /**
     * 터치
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
     * 입력값 변경
     */
    override fun onTextChanged(
        s: CharSequence,
        start: Int,
        before: Int,
        count: Int
    ) {
        if (isFocused) {
            setClearButtonIconVisible(s.isNotEmpty())
        }
    }

    override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) { }

    override fun afterTextChanged(s: Editable) { }



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