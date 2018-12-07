package com.le.jakelong.facedetection.detector

import android.widget.Toast
import com.google.firebase.ml.vision.common.FirebaseVisionImage
import com.google.firebase.ml.vision.common.FirebaseVisionImageMetadata
import com.google.firebase.ml.vision.face.FirebaseVisionFace
import com.le.jakelong.facedetection.detector.models.FaceBounds
import com.le.jakelong.facedetection.detector.models.Frame

class FaceDetector(private val faceBoundsOverlay: FaceBoundsOverlay) {
    private val faceBoundsOverlayHandler = FaceBoundsOverlayHandler()
    private val fireBaseFaceDetectorWrapper = FireBaseFaceDetectorWrapper()

    fun process(frame: Frame) {
        updateOverlayAttributes(frame)
        detectFacesIn(frame)
    }

    private fun updateOverlayAttributes(frame: Frame) {
        faceBoundsOverlayHandler.updateOverlayAttributes(
            overlayHeight = frame.size.height,
            overlayWidth = frame.size.width,
            rotation = frame.rotation,
            isCameraFacingBack = frame.isCameraFacingBack,
            callback = { newWidth, newHeight, newOrientation, newFacing ->
                faceBoundsOverlay.cameraPreviewWidth = newWidth
                faceBoundsOverlay.cameraPreviewHeight = newHeight
                faceBoundsOverlay.cameraOrientation = newOrientation
                faceBoundsOverlay.cameraFacing = newFacing
            }
        )
    }

    private fun detectFacesIn(frame: Frame) {
        frame.data?.let {
            fireBaseFaceDetectorWrapper.process(
                image = convertFrameToImage(frame),
                onSuccess = {
                    faceBoundsOverlay.updateFaces(convertListToFaceBounds(it))
                },
                onError = {
                    Toast.makeText(faceBoundsOverlay.context, "Error processing images: $it", Toast.LENGTH_LONG).show()
                }
            )
        }
    }

    private fun convertFrameToImage(frame: Frame) =
        FirebaseVisionImage.fromByteArray(frame.data!!,extractFrameMetaData(frame))

    private fun extractFrameMetaData(frame: Frame) =
        FirebaseVisionImageMetadata.Builder()
            .setWidth(frame.size.width)
            .setHeight(frame.size.height)
            .setFormat(frame.format)
            .setRotation(frame.rotation / RIGHT_ANGLE)
            .build()

    private fun convertListToFaceBounds(faces: MutableList<FirebaseVisionFace>) =
        faces.map {
            FaceBounds(it.trackingId, it.boundingBox)
        }

    companion object {
        private const val RIGHT_ANGLE = 90
    }
}