package com.ymg.editview.service.password

import android.os.Bundle
import com.ymg.editview.R
import com.ymg.editview.databinding.ActivityMainBinding
import com.ymg.editview.databinding.ActivityNumberFormatEditBinding
import com.ymg.editview.databinding.ActivityPasswordEditBinding
import com.ymg.editview.main.BasicActivity

class PasswordEditActivity : BasicActivity() {

    private lateinit var viewBinding: ActivityPasswordEditBinding



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewBinding = ActivityPasswordEditBinding.inflate(layoutInflater)
        setContentView(viewBinding.root)
    }
}