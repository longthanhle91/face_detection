package com.le.jakelong.facedetection.detector

import com.google.firebase.ml.vision.FirebaseVision
import com.google.firebase.ml.vision.common.FirebaseVisionImage
import com.google.firebase.ml.vision.face.FirebaseVisionFace
import com.google.firebase.ml.vision.face.FirebaseVisionFaceDetector
import com.google.firebase.ml.vision.face.FirebaseVisionFaceDetectorOptions
import java.lang.Exception

class FireBaseFaceDetectorWrapper {
    private val faceDetectorOptions: FirebaseVisionFaceDetectorOptions by lazy {
        FirebaseVisionFaceDetectorOptions.Builder()
            .setPerformanceMode(FirebaseVisionFaceDetectorOptions.FAST)
            .setLandmarkMode(FirebaseVisionFaceDetectorOptions.ALL_LANDMARKS)
            .setClassificationMode(FirebaseVisionFaceDetectorOptions.NO_CLASSIFICATIONS)
            .setMinFaceSize(MIN_FACE_SIZE)
            //.enableTracking()
            .build()
    }

    private val faceDetector: FirebaseVisionFaceDetector by lazy {
        FirebaseVision.getInstance().getVisionFaceDetector(faceDetectorOptions)
    }

    fun process(image: FirebaseVisionImage,
                 onSuccess: (MutableList<FirebaseVisionFace>) -> Unit,
                 onError: (Exception) -> Unit) {
        faceDetector.detectInImage(image)
            .addOnSuccessListener{
                onSuccess(it)
            }
            .addOnFailureListener {
                onError(it)
            }
    }

    companion object {
        private val TAG = FireBaseFaceDetectorWrapper::class.java.simpleName
        private const val MIN_FACE_SIZE = 0.15f
    }
}

