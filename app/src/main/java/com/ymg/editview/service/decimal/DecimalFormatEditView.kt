package com.ymg.editview.service.decimal

import android.os.Bundle
import com.ymg.editview.R
import com.ymg.editview.databinding.ActivityDateOfYearEditBinding
import com.ymg.editview.databinding.ActivityDecimalFormatEditBinding
import com.ymg.editview.databinding.ActivityMainBinding
import com.ymg.editview.main.BasicActivity



class DecimalFormatEditView : BasicActivity() {

    private lateinit var viewBinding: ActivityDecimalFormatEditBinding



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewBinding = ActivityDecimalFormatEditBinding.inflate(layoutInflater)
        setContentView(viewBinding.root)
    }
}