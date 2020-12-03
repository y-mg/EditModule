package com.ymg.editview.main

import android.content.Intent
import android.os.Bundle
import com.ymg.editview.databinding.ActivityMainBinding
import com.ymg.editview.service.clear.ClearEditActivity
import com.ymg.editview.service.dateofyear.DateOfYearActivity
import com.ymg.editview.service.decimal.DecimalFormatEditView
import com.ymg.editview.service.number.NumberFormatEditView
import com.ymg.editview.service.password.PasswordEditActivity


class MainActivity : BasicActivity() {

    private lateinit var viewBinding: ActivityMainBinding



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewBinding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(viewBinding.root)


        viewBinding.apply {
            // Clear Edit View
            btnClearEditView.setOnClickListener {
                startActivity(Intent(this@MainActivity, ClearEditActivity::class.java))
            }

            // Password Edit View
            btnPasswordEditView.setOnClickListener {
                startActivity(Intent(this@MainActivity, PasswordEditActivity::class.java))
            }

            // Date Of Year Edit View
            btnDateOfYearEditView.setOnClickListener {
                startActivity(Intent(this@MainActivity, DateOfYearActivity::class.java))
            }

            // Number Format Edit View
            btnNumberFormatEditView.setOnClickListener {
                startActivity(Intent(this@MainActivity, NumberFormatEditView::class.java))
            }

            // Decimal Format Edit View
            btnDecimalFormatEditView.setOnClickListener {
                startActivity(Intent(this@MainActivity, DecimalFormatEditView::class.java))
            }
        }
    }
}