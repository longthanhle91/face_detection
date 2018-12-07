package com.le.jakelong.facedetection

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import com.le.jakelong.facedetection.detector.FaceDetector
import com.le.jakelong.facedetection.detector.models.Frame
import com.le.jakelong.facedetection.detector.models.Size
import com.otaliastudios.cameraview.Facing
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    private val faceDetector: FaceDetector by lazy {
        FaceDetector(facesBoundOverlay)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        setupCamera()
    }

    private fun setupCamera() {
        cameraView.toggleFacing()
        cameraView.addFrameProcessor {
            faceDetector.process(
                Frame(
                data = it.data,
                rotation = it.rotation,
                size = Size(it.size?.width ?: 0, it.size?.height ?: 0),
                format = it.format,
                isCameraFacingBack = cameraView.facing == Facing.BACK)
            )
        }

        revertCameraButton.setOnClickListener {
            cameraView.toggleFacing()
        }
    }

    override fun onResume() {
        super.onResume()
        cameraView.start()
    }

    override fun onPause() {
        super.onPause()
        cameraView.stop()
    }

    override fun onDestroy() {
        super.onDestroy()
        cameraView.destroy()
    }
}
