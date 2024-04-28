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
    private lateinit var beakerImageView: ImageView
    private lateinit var testTubeImageView: ImageView
    private lateinit var bunsenBurnerImageView: ImageView
    private lateinit var dropperImageView: ImageView
    private lateinit var forcepsImageView: ImageView
    private lateinit var graduatedCylinderImageView: ImageView
    private lateinit var magnifyingGlassImageView: ImageView
    private lateinit var microscopeImageView: ImageView
    private lateinit var spatulaImageView: ImageView
    private lateinit var funnelImageView: ImageView


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_quiz)

        beakerImageView = findViewById(R.id.beakerImageView)
        testTubeImageView = findViewById(R.id.testTubeImageView)
        funnelImageView = findViewById(R.id.funnelImageView)
        beakerImageView = findViewById(R.id.beakerImageView);
        testTubeImageView = findViewById(R.id.testTubeImageView);
        bunsenBurnerImageView = findViewById(R.id.bunsenBurnerImageView);
        dropperImageView = findViewById(R.id.dropperImageView);
        forcepsImageView = findViewById(R.id.forcepsImageView);
        graduatedCylinderImageView = findViewById(R.id.graduatedCylinderImageView);
        magnifyingGlassImageView = findViewById(R.id.magnifyingGlassImageView);
        microscopeImageView = findViewById(R.id.microscopeImageView);
        spatulaImageView = findViewById(R.id.spatulaImageView);

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



        val forcepsButton: Button = findViewById(R.id.forcepsButton)
        forcepsButton.setOnClickListener {
            selectedItem = QuizItems.FORCEPS.itemName
            val intent = Intent(this, MainActivity::class.java)
            cameraResultLauncher.launch(intent)
        }



        val graduatedCylinderButton: Button = findViewById(R.id.graduatedCylinderButton)
        graduatedCylinderButton.setOnClickListener {
            selectedItem = QuizItems.GRADUATED_CYLINDER.itemName
            val intent = Intent(this, MainActivity::class.java)
            cameraResultLauncher.launch(intent)
        }


        val magnifyingGlassButton: Button = findViewById(R.id.magnifyingGlassButton)
        magnifyingGlassButton.setOnClickListener {
            selectedItem = QuizItems.MAGNIFYING_GLASS.itemName
            val intent = Intent(this, MainActivity::class.java)
            cameraResultLauncher.launch(intent)
        }

        val dropperButton: Button = findViewById(R.id.dropperButton)
        dropperButton.setOnClickListener {
            selectedItem = QuizItems.DROPPER.itemName
            val intent = Intent(this, MainActivity::class.java)
            cameraResultLauncher.launch(intent)
        }





        val microscopeButton: Button = findViewById(R.id.microscopeButton)
        microscopeButton.setOnClickListener {
            selectedItem = QuizItems.MICROSCOPE.itemName
            val intent = Intent(this, MainActivity::class.java)
            cameraResultLauncher.launch(intent)
        }

        val spatulaButton: Button = findViewById(R.id.spatulaButton)
        spatulaButton.setOnClickListener {
            selectedItem = QuizItems.SPATULA.itemName
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
                bunsenBurnerImageView.setImageDrawable(drawable)
            }
            "Dropper" -> {
                dropperImageView.setImageDrawable(drawable)
            }
            "Forceps" -> {
                forcepsImageView.setImageDrawable(drawable)
            }
            "Funnel" -> {
                funnelImageView.setImageDrawable(drawable)
            }
            "Graduated Cylinder" -> {
                graduatedCylinderImageView.setImageDrawable(drawable)
            }
            "Magnifying Glass" -> {
                magnifyingGlassImageView.setImageDrawable(drawable)
            }
            "Microscope" -> {
                microscopeImageView.setImageDrawable(drawable)
            }
            "Spatula" -> {
              spatulaImageView.setImageDrawable(drawable)
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