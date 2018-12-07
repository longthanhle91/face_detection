package com.le.jakelong.facedetection.detector

import com.le.jakelong.facedetection.detector.models.Facing
import com.le.jakelong.facedetection.detector.models.Orientation
import com.le.jakelong.facedetection.detector.models.convertToFacing
import com.le.jakelong.facedetection.detector.models.convertToOrientation

class FaceBoundsOverlayHandler {
    private var previousMin: Float = -1f
    private var previousMax: Float = -1f
    private var previousOrientation: Orientation = Orientation.ANGLE_0
    private var previousFacing: Facing = Facing.BACK

    fun updateOverlayAttributes(overlayWidth: Int,
                                overlayHeight: Int,
                                rotation: Int,
                                isCameraFacingBack: Boolean,
                                callback: (Float, Float, Orientation, Facing) -> Unit) {
        val min = Math.min(overlayWidth, overlayHeight).toFloat()
        val max = Math.max(overlayWidth, overlayHeight).toFloat()
        val orientation = rotation.convertToOrientation()
        val facing = isCameraFacingBack.convertToFacing()

        if (previousMin == min
            && previousMax == max
            && previousOrientation == orientation
            && facing == previousFacing) {
            return
        }

        previousMin = min
        previousMax = max
        previousOrientation = orientation
        previousFacing = facing

        when (orientation) {
            Orientation.ANGLE_0, Orientation.ANGLE_180 -> {
                callback(max, min, orientation, facing)
            }
            Orientation.ANGLE_90, Orientation.ANGLE_270 -> {
                callback(min, max, orientation, facing)
            }
        }
    }
}