package com.programminghut.realtime_object

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.*
import android.hardware.camera2.*
import android.media.ImageReader
import android.os.Bundle
import android.os.Handler
import android.os.HandlerThread
import android.util.Log
import android.view.Surface
import android.view.TextureView
import android.view.View
import android.widget.Button
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import com.programminghut.realtime_object.ml.LabuModel
import com.programminghut.realtime_object.ml.OlabuModel147

import org.tensorflow.lite.DataType
import org.tensorflow.lite.support.common.FileUtil
import org.tensorflow.lite.support.image.ImageProcessor
import org.tensorflow.lite.support.image.TensorImage
import org.tensorflow.lite.support.image.ops.ResizeOp
import org.tensorflow.lite.support.tensorbuffer.TensorBuffer
import java.nio.ByteBuffer
import java.nio.ByteOrder
import kotlin.math.round

class MainActivity : AppCompatActivity() {

    private lateinit var labels: List<String>
    private val colors = listOf(
        Color.BLUE, Color.GREEN, Color.RED, Color.CYAN, Color.GRAY, Color.BLACK,
        Color.DKGRAY, Color.MAGENTA, Color.YELLOW, Color.RED
    )
    private lateinit var imageProcessor: ImageProcessor
    private lateinit var bitmap: Bitmap
    private lateinit var imageView: ImageView
    private lateinit var cameraDevice: CameraDevice
    private lateinit var handler: Handler
    private lateinit var cameraManager: CameraManager
    private lateinit var textureView: TextureView
    private lateinit var textView: TextView
    private lateinit var labuModel: LabuModel
    private lateinit var captureButton: Button
    private lateinit var confirmButton: Button
    private var captureTapped = false
    private val imageSize = 224
    private var fromStart = false
    private var detectedLabel = ""
    var hasResult = false;
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        getPermission()

        fromStart = intent.getBooleanExtra("fromStart", false)

        captureButton = findViewById(R.id.captureButton)
        confirmButton = findViewById(R.id.confirmButton)
        val myImageButton: ImageButton = findViewById(R.id.myImageButton)
        val infoButton: Button = findViewById(R.id.infoButton)
        if(!fromStart){
            confirmButton.setVisibility(View.VISIBLE)
            confirmButton.setOnClickListener {
                val resultIntent = Intent()
                resultIntent.putExtra("detectedLabel", detectedLabel)
                setResult(Activity.RESULT_OK, resultIntent)
                finish()
            }
        }else{
            confirmButton.setVisibility(View.GONE)
        }

        captureButton.setOnClickListener {
            BitmapHolder.bitmap = bitmap

            captureTapped = !captureTapped

            if (captureTapped) {
                captureButton.text = "Scan"
            } else {
                captureButton.text = "Capture"
            }
        }

        myImageButton.setOnClickListener {
            finish()
        }

        infoButton.setOnClickListener {
            if(detectedLabel == "" ){
                Toast.makeText(this, "Not Applicable", Toast.LENGTH_SHORT)
                return@setOnClickListener
            }

            val intent = Intent(this, InfoActivity::class.java)
            intent.putExtra("detectedLabel", detectedLabel)
            startActivity(intent)
        }

        labels = FileUtil.loadLabels(this, "labels.txt")
        labuModel = LabuModel.newInstance(this)
        val handlerThread = HandlerThread("videoThread")
        handlerThread.start()
        handler = Handler(handlerThread.looper)

        imageView = findViewById(R.id.imageView)
        textView = findViewById(R.id.textView)

        textureView = findViewById(R.id.textureView)
        textureView.surfaceTextureListener = object : TextureView.SurfaceTextureListener {
            override fun onSurfaceTextureAvailable(p0: SurfaceTexture, p1: Int, p2: Int) {
                openCamera()
            }

            override fun onSurfaceTextureSizeChanged(p0: SurfaceTexture, p1: Int, p2: Int) {}

            override fun onSurfaceTextureDestroyed(p0: SurfaceTexture): Boolean = false

            override fun onSurfaceTextureUpdated(p0: SurfaceTexture) {
                if (captureTapped) {
                    if(hasResult){
                        return
                    }

                    val imageScaled = Bitmap.createScaledBitmap(bitmap, imageSize, imageSize, false)

                    val inputFeature0 =
                        TensorBuffer.createFixedSize(intArrayOf(1, 224, 224, 3), DataType.FLOAT32)
                    val byteBuffer = ByteBuffer.allocateDirect(4 * imageSize * imageSize * 3)
                    byteBuffer.order(ByteOrder.nativeOrder())

                    val intValues = IntArray(imageSize * imageSize)
                    imageScaled.getPixels(
                        intValues,
                        0,
                        imageScaled.width,
                        0,
                        0,
                        imageScaled.width,
                        imageScaled.height
                    )

                    var pixel = 0
                    for (i in 0 until imageSize) {
                        for (j in 0 until imageSize) {
                            val value = intValues[pixel++]
                            byteBuffer.putFloat((value shr 16 and 0xFF) * (1f / 255f))
                            byteBuffer.putFloat((value shr 8 and 0xFF) * (1f / 255f))
                            byteBuffer.putFloat((value and 0xFF) * (1f / 255f))
                        }
                    }
                    inputFeature0.loadBuffer(byteBuffer)

                    val output = labuModel.process(inputFeature0)
                    val outputFeature0 = output.outputFeature0AsTensorBuffer

                    val confidences = outputFeature0.floatArray
                    var maxPos = 0
                    var maxConfidence: Float = 0F
                    for (i in confidences.indices) {
                        if (confidences[i] > maxConfidence) {
                            maxConfidence = confidences[i]
                            maxPos = i
                        }
                    }
                    if (maxPos < labels.size) {
                        val label = labels[maxPos]
                        Log.d("", label)
                        Log.d("conf", maxConfidence.toString())
                        val confidence = maxConfidence.toDouble().round(2)
                        val text = "$label $confidence"
                        if (maxConfidence > 0.95) {
                            detectedLabel = label
                            Log.d("detectedLabel", label)
                            textView.text = text
                        }else{
                            detectedLabel = ""
                            textView.text = "Not Applicable"
                            hasResult = true
                        }
                    }
                    return
                }

                bitmap = textureView.bitmap ?: return
                hasResult = false
                textView.text = ""
                val mutable = bitmap.copy(Bitmap.Config.ARGB_8888, true)
                imageView.setImageBitmap(mutable)
            }
        }

        cameraManager = getSystemService(Context.CAMERA_SERVICE) as CameraManager
    }

    override fun onDestroy() {
        super.onDestroy()
        labuModel.close()
    }

    fun Double.round(decimals: Int): Double {
        var multiplier = 1.0
        repeat(decimals) { multiplier *= 10 }
        return round(this * multiplier) / multiplier
    }

    @SuppressLint("MissingPermission")
    fun openCamera() {
        cameraManager.openCamera(cameraManager.cameraIdList[0], object : CameraDevice.StateCallback() {
            override fun onOpened(p0: CameraDevice) {
                cameraDevice = p0

                val surfaceTexture = textureView.surfaceTexture
                val surface = Surface(surfaceTexture)

                val captureRequest =
                    cameraDevice.createCaptureRequest(CameraDevice.TEMPLATE_PREVIEW)
                captureRequest.addTarget(surface)

                cameraDevice.createCaptureSession(listOf(surface), object : CameraCaptureSession.StateCallback() {
                    override fun onConfigured(p0: CameraCaptureSession) {
                        p0.setRepeatingRequest(captureRequest.build(), null, null)
                    }

                    override fun onConfigureFailed(p0: CameraCaptureSession) {}
                }, handler)
            }

            override fun onDisconnected(p0: CameraDevice) {}

            override fun onError(p0: CameraDevice, p1: Int) {}
        }, handler)
    }

    private fun getPermission() {
        if (ContextCompat.checkSelfPermission(this, android.Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
            requestPermissions(arrayOf(android.Manifest.permission.CAMERA), 101)
        }
    }
    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if(grantResults[0] != PackageManager.PERMISSION_GRANTED){
            getPermission()
        }
    }
}

object BitmapHolder {
    var bitmap: Bitmap? = null
}