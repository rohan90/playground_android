package com.rohan90.android_playground.home.basic_widgets

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import androidx.appcompat.app.AppCompatActivity
import com.rohan90.android_playground.R
import kotlinx.android.synthetic.main.activity_basic_widgets.*

class BasicWidgetActivity : AppCompatActivity() {
    companion object {
        @JvmStatic
        fun newIntent(context: Context): Intent {
            return Intent(context, BasicWidgetActivity::class.java)
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_basic_widgets)

        start()
    }

    private fun start() {
        tvSample.addTextChangedListener(object: TextWatcher{
            override fun afterTextChanged(s: Editable?) {
                TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
                TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
            }
        })

        etSample.addTextChangedListener(object: TextWatcher{
            override fun afterTextChanged(s: Editable?) {
                TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
                TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
            }
        })
    }
}