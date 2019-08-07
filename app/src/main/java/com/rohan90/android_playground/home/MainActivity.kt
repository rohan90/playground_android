package com.rohan90.android_playground.home

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import com.rohan90.android_playground.R
import com.rohan90.narcissistic.CameraActivity
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.content_main.*

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        setSupportActionBar(toolbar as Toolbar?)

        start()
    }

    private fun start() {

        btnStartCustomCamera.setOnClickListener {
            startActivity(CameraActivity.newIntent(this))
        }
    }

}
