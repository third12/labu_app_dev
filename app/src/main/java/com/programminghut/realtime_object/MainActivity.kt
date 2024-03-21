package com.programminghut.realtime_object

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.*
import android.hardware.camera2.CameraCaptureSession
import android.hardware.camera2.CameraDevice
import android.hardware.camera2.CameraManager
import android.media.ThumbnailUtils
import android.os.Bundle
import android.os.Handler
import android.os.HandlerThread
import android.util.Log
import android.view.Surface
import android.view.TextureView
import android.widget.Button
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import com.programminghut.realtime_object.ml.LabuModel
import com.programminghut.realtime_object.ml.SsdMobilenetV11Metadata1
import org.tensorflow.lite.DataType
import org.tensorflow.lite.support.common.FileUtil
import org.tensorflow.lite.support.image.ImageProcessor
import org.tensorflow.lite.support.image.TensorImage
import org.tensorflow.lite.support.image.ops.ResizeOp
import org.tensorflow.lite.support.tensorbuffer.TensorBuffer
import java.nio.ByteBuffer
import java.nio.ByteOrder
import kotlin.math.round
import kotlin.math.roundToInt


class MainActivity : AppCompatActivity() {

    lateinit var labels:List<String>
    var colors = listOf<Int>(
        Color.BLUE, Color.GREEN, Color.RED, Color.CYAN, Color.GRAY, Color.BLACK,
        Color.DKGRAY, Color.MAGENTA, Color.YELLOW, Color.RED)
    val paint = Paint()
    lateinit var imageProcessor: ImageProcessor
    lateinit var bitmap:Bitmap
    lateinit var imageView: ImageView
    lateinit var cameraDevice: CameraDevice
    lateinit var handler: Handler
    lateinit var cameraManager: CameraManager
    lateinit var textureView: TextureView
    lateinit var textView: TextView
    lateinit var model:SsdMobilenetV11Metadata1
    lateinit var labuModel:LabuModel
    lateinit var captureButton: Button
    var captureTapped: Boolean = false
    val imageSize = 224
    var fromStart = false
    var detectedLabel = ""
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        get_permission()

        fromStart = intent.getBooleanExtra("fromStart", false)

        captureButton = findViewById(R.id.captureButton)
        val myImageButton: ImageButton = findViewById(R.id.myImageButton)
        val infoButton: Button = findViewById(R.id.infoButton)
        // Set a click listener for the Button
        captureButton.setOnClickListener {
            BitmapHolder.bitmap = bitmap
            if(!fromStart){
                // Send data back to MainActivity
                val resultIntent = Intent()
                resultIntent.putExtra("detectedLabel", detectedLabel)
                setResult(Activity.RESULT_OK, resultIntent)
                finish()
            }

            captureTapped = !captureTapped

            if(captureTapped){
                captureButton.text = "Scan"
            }else{
                captureButton.text = "Capture"
            }
        }

        myImageButton.setOnClickListener {
            finish()
        }

        infoButton.setOnClickListener {
            val intent = Intent(this, InfoActivity::class.java)
            intent.putExtra("detectedLabel", detectedLabel)
            startActivity(intent)
        }

        labels = FileUtil.loadLabels(this, "labels.txt")
        imageProcessor = ImageProcessor.Builder().add(ResizeOp(224, 224, ResizeOp.ResizeMethod.BILINEAR)).build()
        model = SsdMobilenetV11Metadata1.newInstance(this)
        labuModel = LabuModel.newInstance(this)
        val handlerThread = HandlerThread("videoThread")
        handlerThread.start()
        handler = Handler(handlerThread.looper)

        imageView = findViewById(R.id.imageView)
        textView = findViewById(R.id.textView)

        textureView = findViewById(R.id.textureView)
        textureView.surfaceTextureListener = object:TextureView.SurfaceTextureListener{
            override fun onSurfaceTextureAvailable(p0: SurfaceTexture, p1: Int, p2: Int) {
                open_camera()
            }
            override fun onSurfaceTextureSizeChanged(p0: SurfaceTexture, p1: Int, p2: Int) {
            }

            override fun onSurfaceTextureDestroyed(p0: SurfaceTexture): Boolean {
                return false
            }

            override fun onSurfaceTextureUpdated(p0: SurfaceTexture) {
                if(captureTapped){
                    return
                }

                bitmap = textureView.bitmap!!
                var image = TensorImage.fromBitmap(bitmap)
                image = imageProcessor.process(image)

//                val outputs = model.process(image)
//                val locations = outputs.locationsAsTensorBuffer.floatArray
//                val classes = outputs.classesAsTensorBuffer.floatArray
//                val scores = outputs.scoresAsTensorBuffer.floatArray
//                val numberOfDetections = outputs.numberOfDetectionsAsTensorBuffer.floatArray
//
                var mutable = bitmap.copy(Bitmap.Config.ARGB_8888, true)
//                val canvas = Canvas(mutable)
//
//                val h = mutable.height
//                val w = mutable.width
//                paint.textSize = h/15f
//                paint.strokeWidth = h/85f
//                var x = 0
//                scores.forEachIndexed { index, fl ->
//                    x = index
//                    x *= 4
//                    if(fl > 0.7){
//                        val label = labels.get(classes.get(index).toInt())
//                        textView.text = label
//                        Log.d("",label)
//                        paint.setColor(colors.get(index))
//                        paint.style = Paint.Style.STROKE
//                        canvas.drawRect(RectF(locations.get(x+1)*w, locations.get(x)*h, locations.get(x+3)*w, locations.get(x+2)*h), paint)
//                        paint.style = Paint.Style.FILL
//                        canvas.drawText(labels.get(classes.get(index).toInt())+" "+fl.toString(), locations.get(x+1)*w, locations.get(x)*h, paint)
//                    }
//                }


                val imageScaled = Bitmap.createScaledBitmap(bitmap, imageSize, imageSize, false)

                // Creates inputs for reference.
                val inputFeature0 = TensorBuffer.createFixedSize(intArrayOf(1, 224, 224, 3), DataType.FLOAT32)
                val byteBuffer = ByteBuffer.allocateDirect(4 * imageSize * imageSize * 3)
                byteBuffer.order(ByteOrder.nativeOrder())

                // get 1D array of 224 * 224 pixels in image

                // get 1D array of 224 * 224 pixels in image
                val intValues = IntArray(imageSize * imageSize)
                imageScaled.getPixels(intValues, 0, imageScaled.width, 0, 0, imageScaled.width, imageScaled.height)

                // iterate over pixels and extract R, G, and B values. Add to bytebuffer.

                // iterate over pixels and extract R, G, and B values. Add to bytebuffer.
                var pixel = 0
                for (i in 0 until imageSize) {
                    for (j in 0 until imageSize) {
                        val `val` = intValues[pixel++] // RGB
                        byteBuffer.putFloat((`val` shr 16 and 0xFF) * (1f / 255f))
                        byteBuffer.putFloat((`val` shr 8 and 0xFF) * (1f / 255f))
                        byteBuffer.putFloat((`val` and 0xFF) * (1f / 255f))
                    }
                }
                inputFeature0.loadBuffer(byteBuffer)

                // Runs model inference and gets result.
                val outputs = labuModel.process(inputFeature0)
                val outputFeature0 = outputs.outputFeature0AsTensorBuffer

                // Releases model resources if no longer used.
                val confidences = outputFeature0.floatArray
                var maxPos = 0
                var maxConfidence:Float = 0F
                for (i in confidences.indices) {
                    if(confidences[i] > maxConfidence){
                        maxConfidence = confidences[i]
                        maxPos = i
                    }
                }
                val label = labels.get(maxPos)
                Log.d("",label)
                Log.d("",maxConfidence.toString())
                val confidence = maxConfidence.toDouble().round(2)
                val text = "$label $confidence"
                if(maxConfidence > 0.7){
                    detectedLabel = label
                    textView.text = text
                }
                imageView.setImageBitmap(mutable)

            }
        }

        cameraManager = getSystemService(Context.CAMERA_SERVICE) as CameraManager

    }

    override fun onDestroy() {
        super.onDestroy()
        model.close()
        labuModel.close()
    }

    fun Double.round(decimals: Int): Double {
        var multiplier = 1.0
        repeat(decimals) { multiplier *= 10 }
        return round(this * multiplier) / multiplier
    }

    @SuppressLint("MissingPermission")
    fun open_camera(){
        cameraManager.openCamera(cameraManager.cameraIdList[0], object:CameraDevice.StateCallback(){
            override fun onOpened(p0: CameraDevice) {
                cameraDevice = p0

                var surfaceTexture = textureView.surfaceTexture
                var surface = Surface(surfaceTexture)

                var captureRequest = cameraDevice.createCaptureRequest(CameraDevice.TEMPLATE_PREVIEW)
                captureRequest.addTarget(surface)

                cameraDevice.createCaptureSession(listOf(surface), object: CameraCaptureSession.StateCallback(){
                    override fun onConfigured(p0: CameraCaptureSession) {
                        p0.setRepeatingRequest(captureRequest.build(), null, null)
                    }
                    override fun onConfigureFailed(p0: CameraCaptureSession) {
                    }
                }, handler)
            }

            override fun onDisconnected(p0: CameraDevice) {

            }

            override fun onError(p0: CameraDevice, p1: Int) {

            }
        }, handler)
    }

    fun get_permission(){
        if(ContextCompat.checkSelfPermission(this, android.Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED){
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
            get_permission()
        }
    }
}

object BitmapHolder {
    var bitmap: Bitmap? = null
}