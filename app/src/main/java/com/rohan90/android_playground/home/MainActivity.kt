package com.rohan90.android_playground.home

import android.app.ActivityOptions
import android.os.Bundle
import android.view.ViewGroup
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import androidx.transition.Fade
import androidx.transition.TransitionManager
import com.rohan90.android_playground.R
import com.rohan90.android_playground.home.basic_animations.BasicAnimationsActivity
import com.rohan90.android_playground.home.basic_widgets.BasicWidgetActivity
import com.rohan90.narcissistic.CameraActivity
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.content_main.*

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        setSupportActionBar(toolbar)

        addOptions()
        attachClickListeners()
    }

    private fun addOptions() {
        TransitionManager.beginDelayedTransition(container,Fade())

        //custom camera
        btnStartCustomCamera.setOnClickListener {
            startActivity(CameraActivity.newIntent(this))
        }

        //animations-basic
        val btnGoToAnimations = Button(this)
        btnGoToAnimations.text = "Open Basic Animations"
        btnGoToAnimations.layoutParams = ViewGroup.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT)
        container.addView(btnGoToAnimations)
        btnGoToAnimations.setOnClickListener {
            startActivity(BasicAnimationsActivity.newIntent(this), ActivityOptions.makeSceneTransitionAnimation(this).toBundle())
        }

        //widget-basic
        val btnGoToWidgetBasics = Button(this)
        btnGoToWidgetBasics.text = "Open Basic Widgets"
        btnGoToWidgetBasics.layoutParams = ViewGroup.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT)
        container.addView(btnGoToWidgetBasics)
        btnGoToWidgetBasics.setOnClickListener {
            startActivity(BasicWidgetActivity.newIntent(this), ActivityOptions.makeSceneTransitionAnimation(this).toBundle())
        }
    }

    private fun attachClickListeners() {


    }

}
