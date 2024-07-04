package com.example.veganlens.cameraManager

import android.annotation.SuppressLint
import androidx.camera.core.CameraXConfig

class MyCameraXConfigProvider : CameraXConfig.Provider {
    @SuppressLint("RestrictedApi")
    override fun getCameraXConfig(): CameraXConfig {
        return CameraXConfig.Builder().build()
    }
}