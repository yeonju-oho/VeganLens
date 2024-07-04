package com.example.veganlens

import android.Manifest
import android.content.pm.PackageManager
import android.graphics.ImageFormat
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.os.Bundle
import android.util.Log
import android.widget.ImageView
import androidx.appcompat.app.AppCompatActivity
import androidx.camera.core.CameraSelector
import androidx.camera.core.ImageAnalysis
import androidx.camera.core.ImageProxy
import androidx.camera.core.Preview
import androidx.camera.lifecycle.ProcessCameraProvider
import androidx.camera.view.PreviewView
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.lifecycle.LifecycleOwner
import com.google.mlkit.vision.common.InputImage
import com.google.mlkit.vision.text.TextRecognition
import com.google.mlkit.vision.text.korean.KoreanTextRecognizerOptions
import java.util.concurrent.ExecutorService
import java.util.concurrent.Executors

class MainMenuActivity : AppCompatActivity() {

    private lateinit var cameraExecutor: ExecutorService
    private lateinit var previewView: PreviewView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main_menu)

        supportActionBar?.hide()

        cameraExecutor = Executors.newSingleThreadExecutor()
        previewView = findViewById(R.id.camera_frame)

        if (ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA)
            != PackageManager.PERMISSION_GRANTED
        ) {
            ActivityCompat.requestPermissions(
                this,
                arrayOf(Manifest.permission.CAMERA),
                CAMERA_REQUEST_CODE
            )
        } else {
            startCamera()
        }

        findViewById<ImageView>(R.id.icon_camera).setOnClickListener {
            startCamera()
        }
    }

    private fun startCamera() {
        val cameraProviderFuture = ProcessCameraProvider.getInstance(this)

        cameraProviderFuture.addListener({
            val cameraProvider = cameraProviderFuture.get()
            val preview = Preview.Builder().build().also {
                it.setSurfaceProvider(previewView.surfaceProvider)
            }

            val imageAnalyzer = ImageAnalysis.Builder()
                .build()
                .also {
                    it.setAnalyzer(cameraExecutor, { imageProxy ->
                        val bitmap = imageProxy.toBitmap()
                        processImage(bitmap)
                        imageProxy.close()
                    })
                }

            val cameraSelector = CameraSelector.DEFAULT_BACK_CAMERA

            try {
                cameraProvider.unbindAll()
                cameraProvider.bindToLifecycle(
                    this as LifecycleOwner,
                    cameraSelector,
                    preview,
                    imageAnalyzer
                )
            } catch (exc: Exception) {
                Log.e("MainMenuActivity", "Use case binding failed", exc)
            }
        }, ContextCompat.getMainExecutor(this))
    }

    private fun ImageProxy.toBitmap(): Bitmap {
        val nv21Buffer = yuv420ThreePlanesToNV21(planes, width, height)
        val yuvImage = android.graphics.YuvImage(nv21Buffer, ImageFormat.NV21, width, height, null)
        val out = java.io.ByteArrayOutputStream()
        yuvImage.compressToJpeg(android.graphics.Rect(0, 0, width, height), 100, out)
        val imageBytes = out.toByteArray()
        return BitmapFactory.decodeByteArray(imageBytes, 0, imageBytes.size)
    }

    private fun yuv420ThreePlanesToNV21(
        yuv420888planes: Array<ImageProxy.PlaneProxy>,
        width: Int,
        height: Int
    ): ByteArray {
        val imageSize = width * height
        val out = ByteArray(imageSize + 2 * (imageSize / 4))

        if (areUVPlanesNV21(yuv420888planes, width, height)) {
            yuv420888planes[0].buffer.get(out, 0, imageSize)

            val uBuffer = yuv420888planes[1].buffer
            val vBuffer = yuv420888planes[2].buffer
            val rowStride = yuv420888planes[1].rowStride
            val pixelStride = yuv420888planes[1].pixelStride

            var pos = imageSize
            for (row in 0 until height / 2) {
                for (col in 0 until width / 2) {
                    out[pos++] = vBuffer[row * rowStride + col * pixelStride]
                    out[pos++] = uBuffer[row * rowStride + col * pixelStride]
                }
            }
        } else {
            unpackPlane(yuv420888planes[0], width, height, out, 0, 1)
            unpackPlane(yuv420888planes[1], width, height, out, imageSize + 1, 2)
            unpackPlane(yuv420888planes[2], width, height, out, imageSize, 2)
        }
        return out
    }

    private fun areUVPlanesNV21(
        planes: Array<ImageProxy.PlaneProxy>,
        width: Int,
        height: Int
    ): Boolean {
        val imageSize = width * height

        val uBuffer = planes[1].buffer
        val vBuffer = planes[2].buffer

        val vBufferPosition = vBuffer.position()
        val uBufferLimit = uBuffer.limit()

        uBuffer.position(vBufferPosition)

        val uBufferLimitOriginal = uBuffer.limit()
        uBuffer.limit(vBuffer.limit())

        return uBuffer.remaining() == imageSize / 4 && vBuffer.remaining() == imageSize / 4
    }

    private fun unpackPlane(
        plane: ImageProxy.PlaneProxy, width: Int, height: Int,
        out: ByteArray, offset: Int, pixelsStride: Int
    ) {
        val buffer = plane.buffer
        buffer.rewind()

        val numRow = (buffer.remaining() + plane.rowStride - 1) / plane.rowStride
        if (numRow == 0) return

        val scaleFactor = width / plane.rowStride.toFloat()
        if (scaleFactor != scaleFactor.toInt().toFloat()) {
            for (i in 0 until numRow) {
                buffer.limit((i + 1) * plane.rowStride)
                buffer.position(i * plane.rowStride)
                val length = plane.rowStride.coerceAtMost(buffer.remaining())
                buffer.get(out, offset + i * pixelsStride * width, length)
            }
        } else {
            val rowLength = (width / scaleFactor).toInt()
            for (i in 0 until numRow) {
                buffer.limit((i + 1) * plane.rowStride)
                buffer.position(i * plane.rowStride)
                buffer.get(out, offset + i * rowLength * pixelsStride, rowLength)
            }
        }
    }

    private fun processImage(bitmap: Bitmap) {
        val image = InputImage.fromBitmap(bitmap, 0)
        val recognizer = TextRecognition.getClient(KoreanTextRecognizerOptions.Builder().build())

        recognizer.process(image)
            .addOnSuccessListener { visionText ->
                val resultText = visionText.text
                Log.d("OCR Result", resultText)
                if (resultText.contains("계란") || resultText.contains("우유")) {
                    Log.d("Keyword Found", "특정 키워드가 발견되었습니다!")
                } else {
                    Log.d("Keyword Not Found", "특정 키워드가 없습니다.")
                }
            }
            .addOnFailureListener { e ->
                Log.e("OCR Error", e.message.toString())
            }
    }

    override fun onDestroy() {
        super.onDestroy()
        cameraExecutor.shutdown()
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == CAMERA_REQUEST_CODE && grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            startCamera()
        } else {
            // 권한 거부 시 추가적인 처리가 필요하다면 여기에 구현
        }
    }

    companion object {
        private const val CAMERA_REQUEST_CODE = 100
    }
}