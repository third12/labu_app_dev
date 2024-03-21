package com.programminghut.realtime_object

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView

class InfoActivity : AppCompatActivity() {

    var detectedLabel = ""
    lateinit var textView:TextView
    lateinit var infoTextView:TextView
    lateinit var imageView: ImageView
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_info)
        textView = findViewById(R.id.textView)
        infoTextView = findViewById(R.id.infoTextView)
        imageView = findViewById(R.id.imageView)
        val scanButton: Button = findViewById(R.id.scanButton)

        imageView.setImageBitmap(BitmapHolder.bitmap)
        detectedLabel = intent.getStringExtra("detectedLabel").toString()
        textView.text = detectedLabel

        scanButton.setOnClickListener {
            finish()
        }

        when (detectedLabel) {
            "Beaker" -> {
                infoTextView.text = "This is the text for beaker"
            }
            "Bunsen Burner" -> {
                infoTextView.text = "This is the text for $detectedLabel"
            }
            "Dropper" -> {
                infoTextView.text = "This is the text for $detectedLabel"
            }
            "Forceps" -> {
                infoTextView.text = "This is the text for $detectedLabel"
            }
            "Funnel" -> {
                infoTextView.text = "This is the text for $detectedLabel"
            }
            "Graduated Cylinder" -> {
                infoTextView.text = "This is the text for $detectedLabel"
            }
            "Magnifying Glass" -> {
                infoTextView.text = "This is the text for $detectedLabel"
            }
            "Microscope" -> {
                infoTextView.text = "This is the text for $detectedLabel"
            }
            "Spatula" -> {
                infoTextView.text = "This is the text for $detectedLabel"
            }
            "Test Tube" -> {
                infoTextView.text = "This is the text for $detectedLabel"
            }
            else -> {
                infoTextView.text = detectedLabel
            }
        }
    }
}