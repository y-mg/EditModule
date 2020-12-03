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



class DateOfYearEditView : TextInputEditText, View.OnTouchListener {

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

    // 포맷팅 여부
    private var isFormatting = false



    private fun init(context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0) {
        val typedArray =
            context.theme?.obtainStyledAttributes(
                attrs,
                R.styleable.DateOfYearEditStyle,
                defStyleAttr,
                defStyleAttr
            )


        // 클리어 버튼 허용 여부
        val clearButtonEnabled =
            typedArray?.getBoolean(
                R.styleable.DateOfYearEditStyle_doyClearButtonEnabled,
                true
            )

        // 클리어 버튼 아이콘
        val clearButtonIcon =
            typedArray?.getResourceId(
                R.styleable.DateOfYearEditStyle_doyClearButtonIcon,
                R.drawable.btn_clear
            )

        typedArray?.recycle()


        if (clearButtonEnabled != null && clearButtonIcon != null) {
            setInit(clearButtonEnabled, clearButtonIcon)
        }
    }



    /**
     * 설정
     */
    @SuppressLint("ClickableViewAccessibility")
    private fun setInit(
        clearButtonEnabled: Boolean,
        clearButtonIcon: Int
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
        setDefaultDateOfYearEditView()
        setActionDateOfYearEditView()

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
                getDateOfYear(it.toString())
            }
            .subscribe {
                isFormatting = true

                if (it.isNotEmpty()) {
                    this.setText(it)
                    this.setSelection(it.length)
                }

                if (isFocused) {
                    setClearButtonIconVisible(it.isNotEmpty())
                }

                isFormatting = false
            }
    }





    /**
     * 기본 설정
     */
    private fun setDefaultDateOfYearEditView() {
        this.apply {
            minHeight = context.resources.getDimension(R.dimen.date_of_year_edit_default_min_height).toInt()
            filters = arrayOf<InputFilter>(LengthFilter(10))
            inputType = InputType.TYPE_CLASS_NUMBER
        }
    }

    /**
     * 소프트 키보드 Action
     */
    private fun setActionDateOfYearEditView() {
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
     * 입력창 포커스
     */
    override fun onFocusChanged(hasFocus: Boolean, direction: Int, previouslyFocusedRect: Rect?) {
        super.onFocusChanged(hasFocus, direction, previouslyFocusedRect)

        when (hasFocus) {
            true -> {
                setClearButtonIconVisible(!text.isNullOrEmpty())
            }

            false -> {
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
     * 생년월일 변경
     */
    private fun getDateOfYear(str: String): String {
        val value = str.replace(Regex("[^0-9]"), "")
        val sb = StringBuilder()

        when {
            // Delete 시 - 삭제
            value.length == 4 && str.length == 5 -> {
                sb.append(value)
            }

            // 입력 시 - 추가
            value.length == 5 -> {
                sb.append(value.substring(0, 4))
                sb.append("-")
                sb.append(value.last())
            }

            // Delete 시 - 삭제
            value.length == 6 && str.length == 8 -> {
                sb.append(value.substring(0, 4))
                sb.append("-")
                sb.append(value.substring(4, 6))
            }

            // 입력 시 - 추가
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