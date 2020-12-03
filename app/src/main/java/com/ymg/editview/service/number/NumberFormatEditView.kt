package com.ymg.editview.service.number

import android.os.Bundle
import com.ymg.editview.R
import com.ymg.editview.databinding.ActivityDecimalFormatEditBinding
import com.ymg.editview.databinding.ActivityMainBinding
import com.ymg.editview.databinding.ActivityNumberFormatEditBinding
import com.ymg.editview.main.BasicActivity



class NumberFormatEditView : BasicActivity() {

    private lateinit var viewBinding: ActivityNumberFormatEditBinding



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewBinding = ActivityNumberFormatEditBinding.inflate(layoutInflater)
        setContentView(viewBinding.root)
    }
}