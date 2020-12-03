package com.ymg.editview.service.dateofyear

import android.os.Bundle
import com.ymg.editview.R
import com.ymg.editview.databinding.ActivityClearEditBinding
import com.ymg.editview.databinding.ActivityDateOfYearEditBinding
import com.ymg.editview.databinding.ActivityMainBinding
import com.ymg.editview.main.BasicActivity

class DateOfYearActivity : BasicActivity() {

    private lateinit var viewBinding: ActivityDateOfYearEditBinding



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewBinding = ActivityDateOfYearEditBinding.inflate(layoutInflater)
        setContentView(viewBinding.root)
    }
}