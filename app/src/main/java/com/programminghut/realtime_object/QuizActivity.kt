package com.programminghut.realtime_object

import android.app.Activity
import android.content.Intent
import android.graphics.drawable.Drawable
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.ImageView
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.content.ContextCompat

class QuizActivity : AppCompatActivity() {

    var selectedItem = ""
    lateinit var beakerImageView: ImageView
    lateinit var testTubeImageView: ImageView
    lateinit var funnelImageView: ImageView
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_quiz)

        beakerImageView = findViewById(R.id.beakerImageView)
        testTubeImageView = findViewById(R.id.testTubeImageView)
        funnelImageView = findViewById(R.id.funnelImageView)

        val beakerButton: Button = findViewById(R.id.beakerButton)
        beakerButton.setOnClickListener {
            selectedItem = QuizItems.BEAKER.itemName
            val intent = Intent(this, MainActivity::class.java)
            cameraResultLauncher.launch(intent)
        }

        val testTubeButton: Button = findViewById(R.id.testTubeButton)
        testTubeButton.setOnClickListener {
            selectedItem = QuizItems.TEST_TUBE.itemName
            val intent = Intent(this, MainActivity::class.java)
            cameraResultLauncher.launch(intent)
        }

        val funnelButton: Button = findViewById(R.id.funnelButton)
        funnelButton.setOnClickListener {
            selectedItem = QuizItems.FUNNEL.itemName
            val intent = Intent(this, MainActivity::class.java)
            cameraResultLauncher.launch(intent)
        }
    }

    fun checkResult(isCorrect: Boolean){
        var drawable: Drawable? = ContextCompat.getDrawable(this, R.drawable.checkmark)

        if(!isCorrect){
            drawable = ContextCompat.getDrawable(this, R.drawable.x_mark)
        }

        when (selectedItem) {
            "Beaker" -> {
                // Set the new Drawable to the ImageView
                beakerImageView.setImageDrawable(drawable)
            }
            "Bunsen Burner" -> {
                beakerImageView.setImageDrawable(drawable)
            }
            "Dropper" -> {
                beakerImageView.setImageDrawable(drawable)
            }
            "Forceps" -> {
                beakerImageView.setImageDrawable(drawable)
            }
            "Funnel" -> {
                funnelImageView.setImageDrawable(drawable)
            }
            "Graduated Cylinder" -> {
                beakerImageView.setImageDrawable(drawable)
            }
            "Magnifying Glass" -> {
                beakerImageView.setImageDrawable(drawable)
            }
            "Microscope" -> {
                beakerImageView.setImageDrawable(drawable)
            }
            "Spatula" -> {
                beakerImageView.setImageDrawable(drawable)
            }
            "Test Tube" -> {
                testTubeImageView.setImageDrawable(drawable)
            }
        }
    }

    private val cameraResultLauncher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
        if (result.resultCode == Activity.RESULT_OK) {
            // Handle the result
            val data: Intent? = result.data
            // Process the data as needed
            val detectedLabel = data?.getStringExtra("detectedLabel").toString()

            Log.d("test123", detectedLabel)
            Log.d("test123", selectedItem)
            if(detectedLabel == selectedItem){
                checkResult(true)
            }else{
                checkResult(false)
            }
        }
    }
}

enum class QuizItems(val itemName: String) {
    BEAKER("Beaker"),
    BUNSEN_BURNER("Bunsen Burner"),
    DROPPER("Dropper"),
    FORCEPS("Forceps"),
    FUNNEL("Funnel"),
    GRADUATED_CYLINDER("Graduated Cylinder"),
    MAGNIFYING_GLASS("Magnifying Glass"),
    MICROSCOPE("Microscope"),
    SPATULA("Spatula"),
    TEST_TUBE("Test Tube");
    // You can add additional properties or methods to the enum if needed
    fun printName() {
        println("Name: $itemName")
    }
}