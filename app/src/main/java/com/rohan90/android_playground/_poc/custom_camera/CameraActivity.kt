package com.rohan90.narcissistic

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import com.rohan90.android_playground.R
import kotlinx.android.synthetic.main.activity_camera.*


/**
 * Created by rohan on 17/4/19.
 *
 *
 * tests:
 * 1. should be able to preview camera feed.
 * 2. should be able to see options like flash, take picture, orientation, change camera.
 * 3. should be able to focus before taking picture (good to have)
 * 4. should be able to take picture.
 * 5. should be able to save a taken picture.
 * 6. should be able to cancel a taken picture.
 * 7. should be able to add a silhouette to to preview camera feed
 * 8. (good to have) should be able to do operations on a taken picture like (crop,edit(line, text,) and undo these operations).
 *
 */
class CameraActivity : AppCompatActivity() {
    companion object {
        @JvmStatic
        fun newIntent(context: Context): Intent {
            return Intent(context, CameraActivity::class.java)
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_camera)
        start()
    }

    private fun start() {
        this.lifecycle.addObserver(cameraPreview)
    }

}