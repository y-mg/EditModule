package com.ymg.editview.service.clear

import android.os.Bundle
import com.ymg.editview.databinding.ActivityClearEditBinding
import com.ymg.editview.main.BasicActivity


class ClearEditActivity : BasicActivity() {

    private lateinit var viewBinding: ActivityClearEditBinding



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewBinding = ActivityClearEditBinding.inflate(layoutInflater)
        setContentView(viewBinding.root)
    }
}