package com.le.jakelong.facedetection.detector.models

fun Boolean.convertToFacing() = when(this) {
    true -> Facing.BACK
    false -> Facing.FRONT
}

fun Int.convertToOrientation() = when(this) {
    0 -> Orientation.ANGLE_0
    90 -> Orientation.ANGLE_90
    180 -> Orientation.ANGLE_180
    270 -> Orientation.ANGLE_270
    else -> Orientation.ANGLE_270
}