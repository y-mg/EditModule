package com.ymg.editmodule.number

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Rect
import android.graphics.drawable.Drawable
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
import java.text.NumberFormat



class NumberFormatEditView : TextInputEditText, View.OnTouchListener {

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

    // 클리어 버튼 허용 여부
    private var clearButtonEnabled: Boolean = true

    // 클리어 버튼 Drawable
    private var clearButtonIcon: Drawable? = null

    // Edit 앞에 붙일 텍스트
    private var addEditStart: String = ""

    // 포맷팅 여부
    private var isFormatting = false



    private fun init(context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0) {
        val typedArray =
            context.theme?.obtainStyledAttributes(
                attrs,
                R.styleable.NumberFormatEditStyle,
                defStyleAttr,
                defStyleAttr
            )


        // 클리어 버튼 허용 여부
        val clearButtonEnabled =
            typedArray?.getBoolean(
                R.styleable.NumberFormatEditStyle_nfClearButtonEnabled,
                true
            )

        // 클리어 버튼 아이콘
        val clearButtonIcon =
            typedArray?.getResourceId(
                R.styleable.NumberFormatEditStyle_nfClearButtonIcon,
                R.drawable.btn_clear
            )

        // Edit 앞에 붙일 값
        val addEditStart =
            typedArray?.getString(
                R.styleable.NumberFormatEditStyle_nfAddEditStart
            )

        typedArray?.recycle()


        if (clearButtonEnabled != null && clearButtonIcon != null) {
            when {
                !addEditStart.isNullOrEmpty() -> {
                    setInit(clearButtonEnabled, clearButtonIcon, addEditStart)
                }

                else -> {
                    setInit(clearButtonEnabled, clearButtonIcon)
                }
            }
        }
    }



    /**
     * 설정
     */
    @SuppressLint("ClickableViewAccessibility")
    private fun setInit(
        clearButtonEnabled: Boolean,
        clearButtonIcon: Int,
        addEditStart: String = ""
    ) {
        this.clearButtonEnabled = clearButtonEnabled

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
        setDefaultNumberFormatEditView()
        setActionNumberFormatEditView()

        // Edit 앞에 붙일 값 설정
        setAddEditStart(addEditStart)

        // 클리어 버튼 아이콘 가시성 설정
        setClearButtonIconVisible(false)

        // 리스너 설정
        super.setOnTouchListener(this)

        // TextChanges RxBind
        this.textChanges()
            .filter {
                !isFormatting
            }
            .filter {
                it != null
            }
            .map {
                val value = it.toString().replace("[^\\d]".toRegex(), "")

                if (value.isNotEmpty()) {
                    val formatter = NumberFormat.getNumberInstance()
                    "$addEditStart${formatter.format(value.toBigDecimal())}"
                } else {
                    ""
                }
            }
            .subscribe {
                isFormatting = true

                this.setText(it)
                this.setSelection(it.length)

                if (isFocused) {
                    setClearButtonIconVisible(it.isNotEmpty())
                }

                isFormatting = false
            }
    }





    /**
     * 기본 설정
     */
    private fun setDefaultNumberFormatEditView() {
        this.apply {
            minHeight = context.resources.getDimension(R.dimen.number_format_edit_default_min_height).toInt()
            inputType = InputType.TYPE_CLASS_NUMBER
        }
    }

    /**
     * 소프트 키보드 Action
     */
    private fun setActionNumberFormatEditView() {
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
     * Edit 앞에 붙일 값 설정
     */
    fun setAddEditStart(addEditStart: String) {
        this.addEditStart = addEditStart
    }



    /**
     * 클리어 버튼 가시성 설정
     */
    private fun setClearButtonIconVisible(visible: Boolean) {
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



    /**
     * 입력 값 가져오기
     */
    fun getFormatText(): String {
        return this.text.toString().replace("[^\\d]".toRegex(), "")
    }
}