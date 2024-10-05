package com.example.veganlens.cameraPages

import android.Manifest
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.ImageFormat
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AlertDialog
import androidx.camera.core.CameraSelector
import androidx.camera.core.ImageCapture
import androidx.camera.core.ImageCaptureException
import androidx.camera.core.ImageProxy
import androidx.camera.core.Preview
import androidx.camera.lifecycle.ProcessCameraProvider
import androidx.camera.view.PreviewView
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.LifecycleOwner
import com.example.veganlens.R
import com.example.veganlens.network.IngredientsResponse
import com.example.veganlens.network.NetworkService
import com.google.mlkit.vision.common.InputImage
import com.google.mlkit.vision.text.TextRecognition
import com.google.mlkit.vision.text.korean.KoreanTextRecognizerOptions
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.io.ByteArrayOutputStream
import java.util.concurrent.ExecutorService
import java.util.concurrent.Executors

class CameraFragment : Fragment() {

    private lateinit var cameraExecutor: ExecutorService
    private lateinit var previewView: PreviewView
    private var imageCapture: ImageCapture? = null
    private var processingImage = false // 이미지 처리 중 여부를 나타내는 변수

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_camera, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        cameraExecutor = Executors.newSingleThreadExecutor()
        previewView = view.findViewById(R.id.camera_frame)

        if (ContextCompat.checkSelfPermission(requireContext(), Manifest.permission.CAMERA)
            != PackageManager.PERMISSION_GRANTED
        ) {
            requestPermissions(
                arrayOf(Manifest.permission.CAMERA),
                CAMERA_REQUEST_CODE
            )
        } else {
            startCamera()
        }
    }

    fun onCameraIconClicked() {
        if (!processingImage) {
            takePhoto()
        }
    }

    private fun startCamera() {
        val cameraProviderFuture = ProcessCameraProvider.getInstance(requireContext())

        cameraProviderFuture.addListener({
            val cameraProvider = cameraProviderFuture.get()
            val preview = Preview.Builder().build().also {
                it.setSurfaceProvider(previewView.surfaceProvider)
            }

            imageCapture = ImageCapture.Builder().build()

            val cameraSelector = CameraSelector.DEFAULT_BACK_CAMERA

            try {
                cameraProvider.unbindAll()
                cameraProvider.bindToLifecycle(
                    this as LifecycleOwner,
                    cameraSelector,
                    preview,
                    imageCapture
                )
            } catch (exc: Exception) {
                Log.e("CameraFragment", "Use case binding failed", exc)
            }
        }, ContextCompat.getMainExecutor(requireContext()))
    }

    private fun takePhoto() {
        val imageCapture = imageCapture ?: return

        processingImage = true // 이미지 처리 중으로 설정

        imageCapture.takePicture(ContextCompat.getMainExecutor(requireContext()), object : ImageCapture.OnImageCapturedCallback() {
            override fun onCaptureSuccess(image: ImageProxy) {
                val bitmap = image.toBitmap()
                image.close()

                // 이미지 처리 메서드 호출
                processImage(bitmap)
            }

            override fun onError(exception: ImageCaptureException) {
                Log.e("CameraFragment", "Photo capture failed: ${exception.message}", exception)
                processingImage = false // 이미지 처리 실패 시 초기화
            }
        })
    }

    private fun ImageProxy.toBitmap(): Bitmap {
        val nv21Buffer = yuv420ThreePlanesToNV21(planes, width, height)
        val yuvImage = android.graphics.YuvImage(nv21Buffer, ImageFormat.NV21, width, height, null)
        val out = ByteArrayOutputStream()
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

    private fun unpackPlane(
        plane: ImageProxy.PlaneProxy, width: Int, height: Int,
        out: ByteArray, offset: Int, pixelsStride: Int
    ) {
        val buffer = plane.buffer
        buffer.rewind() // 버퍼의 위치를 초기화합니다.

        val numRow = (buffer.remaining() + plane.rowStride - 1) / plane.rowStride
        if (numRow == 0) return

        val scaleFactor = width / plane.rowStride.toFloat()
        if (scaleFactor != scaleFactor.toInt().toFloat()) {
            // scaleFactor가 정수가 아닐 때
            for (i in 0 until numRow) {
                val rowStart = i * plane.rowStride
                val length = plane.rowStride.coerceAtMost(buffer.remaining())
                buffer.limit(rowStart + length)
                buffer.position(rowStart)
                buffer.get(out, offset + i * pixelsStride * width, length)
            }
        } else {
            // scaleFactor가 정수일 때
            val rowLength = (width / scaleFactor).toInt()
            for (i in 0 until numRow) {
                val rowStart = i * plane.rowStride
                buffer.limit(rowStart + plane.rowStride)
                buffer.position(rowStart)
                buffer.get(out, offset + i * rowLength * pixelsStride, rowLength)
            }
        }
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

    private fun processImage(bitmap: Bitmap) {
        val image = InputImage.fromBitmap(bitmap, 0)
        val recognizer = TextRecognition.getClient(KoreanTextRecognizerOptions.Builder().build())

        recognizer.process(image)
            .addOnSuccessListener { visionText ->
                var resultText = visionText.text
                // 줄바꿈과 공백을 제거하여 한 줄로 만듦
                resultText = resultText.replace(Regex("\\s+"), "")
                Log.d("OCR Result", resultText)

                //TODO: OCR 정확도가 낮아 임시로 텍스트 적음
                resultText = "야자유,팜유,혼합식용유,전분,버터,정제소금,기타과당,산도조절제,";
                // 서버와 통신
                checkIngredientsWithServer(resultText);
            }
            .addOnFailureListener { e ->
                Log.e("OCR Error", e.message.toString())
                showTextPopup("OCR Error. " + e.message.toString())
                processingImage = false
            }
    }

    private fun showTextPopup(text: String) {
        val alertDialogBuilder = AlertDialog.Builder(requireContext())
        alertDialogBuilder.setTitle("인식된 텍스트")
        alertDialogBuilder.setMessage(text)
        alertDialogBuilder.setPositiveButton("확인") { dialog, which ->
            // 사용자가 확인을 눌렀을 때 추가적인 동작을 수행할 수 있음
        }
        val alertDialog = alertDialogBuilder.create()
        alertDialog.show()
    }

    override fun onDestroyView() {
        super.onDestroyView()
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

    private fun checkIngredientsWithServer(ingredients: String) {
        NetworkService.service.checkIngredients(ingredients)
            .enqueue(object : Callback<IngredientsResponse> {
                override fun onResponse(call: Call<IngredientsResponse>, response: Response<IngredientsResponse>) {
                    if (response.isSuccessful) {
                        val ingredientsResponse = response.body()
                        if (ingredientsResponse != null) {
                            handleServerResponse(ingredientsResponse)
                        } else {
                            showTextPopup("서버 응답이 비어있습니다.")
                        }
                    } else {
                        showTextPopup("서버 오류 발생: ${response.code()}")
                    }
                    processingImage = false
                }

                override fun onFailure(call: Call<IngredientsResponse>, t: Throwable) {
                    showTextPopup("네트워크 오류 발생: ${t.message}")
                    Log.e("NetworkError", "Error during API call", t)
                    processingImage = false
                }
            })
    }

    private fun handleServerResponse(response: IngredientsResponse) {
        val ingredientsText = if (response.ingredients.isEmpty()) {
            "없음"
        } else {
            response.ingredients.entries.joinToString(", ") { "${it.key}(${it.value})" }
        }

        val veganTypesText = response.suitableVeganTypes.joinToString("\n") { mapVeganTypeToString(it)}

        // Create and display the custom dialog fragment
        val foodIngredientResultFragment = FoodIngredientResultFragment()
        foodIngredientResultFragment.setContent(ingredientsText, veganTypesText)

        foodIngredientResultFragment.show(parentFragmentManager, "FoodIngredientResultFragment")
    }

    private fun mapVeganTypeToString(type: Int): String {
        return when (type) {
            1 -> "비건"
            2 -> "락토"
            3 -> "오보"
            4 -> "락토 오보"
            5 -> "페스코"
            6 -> "폴로"
            7 -> "플렉시테리안"
            else -> "알 수 없음"
        }
    }





}
