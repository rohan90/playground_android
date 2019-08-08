package com.rohan90.android_playground.home.basic_animations

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.animation.AnimationUtils
import com.rohan90.android_playground.R
import kotlinx.android.synthetic.main.activity_basic_animations.*

class BasicAnimationsActivity : AppCompatActivity() {

    companion object {
        @JvmStatic
        fun newIntent(context: Context): Intent {
            return Intent(context, BasicAnimationsActivity::class.java)
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_basic_animations)

        start()
    }
    private fun start(){
        btnScale.setOnClickListener {
            val scaleAnimation = AnimationUtils.loadAnimation(this,R.anim.scale)
            tvTarget.startAnimation(scaleAnimation)
        }

        btnScaleNKeep.setOnClickListener {
            val scaleAnimation = AnimationUtils.loadAnimation(this,R.anim.scale)
            scaleAnimation.fillAfter = true
            tvTarget.startAnimation(scaleAnimation)
        }

        btnFadeIn.setOnClickListener {
            val fadeInAnimation = AnimationUtils.loadAnimation(this,R.anim.fade_in)
            tvTarget.startAnimation(fadeInAnimation)
        }

        btnFadeOut.setOnClickListener {
            val fadeOutAnimation = AnimationUtils.loadAnimation(this,R.anim.fade_out)
            tvTarget.startAnimation(fadeOutAnimation)
        }

        btnBlink.setOnClickListener {
            val blinkAnim = AnimationUtils.loadAnimation(this,R.anim.blink)
            tvTarget.startAnimation(blinkAnim)
        }

        btnZoomIn.setOnClickListener {
            val zoomAnim = AnimationUtils.loadAnimation(this,R.anim.zoom_in)
            tvTarget.startAnimation(zoomAnim)
        }

        btnZoomOut.setOnClickListener {
            val zoomAnim = AnimationUtils.loadAnimation(this,R.anim.zoom_out)
            tvTarget.startAnimation(zoomAnim)
        }

        btnRotate.setOnClickListener {
            val rotate = AnimationUtils.loadAnimation(this,R.anim.rotate)
            tvTarget.startAnimation(rotate)
        }

        btnMove.setOnClickListener {
            val move = AnimationUtils.loadAnimation(this,R.anim.move)
            tvTarget.startAnimation(move)
        }

        btnBounce.setOnClickListener {
            val bounce = AnimationUtils.loadAnimation(this,R.anim.bounce)
            tvTarget.startAnimation(bounce)
        }

        btnSequential.setOnClickListener {
            val sequ = AnimationUtils.loadAnimation(this,R.anim.sequential)
            tvTarget.startAnimation(sequ)
        }

    }
}
