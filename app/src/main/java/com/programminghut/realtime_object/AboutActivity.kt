package com.programminghut.realtime_object
import android.graphics.*
import android.os.Bundle
import android.text.method.ScrollingMovementMethod
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity


class AboutActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_about)
        val aboutImage = findViewById<ImageView>(R.id.aboutImage)
        val aboutText = findViewById<TextView>(R.id.aboutText)
        val beakerText = findViewById<TextView>(R.id.beakerText)
        val testTubeText = findViewById<TextView>(R.id.testTubeText)
        val bunsenBurnerText = findViewById<TextView>(R.id.bunsenBurnerText)
        val dropperText = findViewById<TextView>(R.id.dropperText)
        val forcepsText = findViewById<TextView>(R.id.forcepsText)

        val graduatedCylinderText = findViewById<TextView>(R.id.graduatedCylinderText)
        val magnifyingGlassText = findViewById<TextView>(R.id.magnifyingGlassText)

        val microscopeText = findViewById<TextView>(R.id.microscopeText)
        val spatulaText = findViewById<TextView>(R.id.spatulaText)
        val funnelText = findViewById<TextView>(R.id.funnelText)

        aboutText.setMovementMethod(ScrollingMovementMethod())

        beakerText.setOnClickListener {
            aboutImage.setImageResource(R.drawable.beaker)
            aboutText.text = getString(R.string.about_beaker)
        }
        testTubeText.setOnClickListener {
            aboutImage.setImageResource(R.drawable.test_tube)
            aboutText.text = getString(R.string.about_test_tube)
        }
        bunsenBurnerText.setOnClickListener {
            aboutImage.setImageResource(R.drawable.bunsen_burner)
            aboutText.text = getString(R.string.about_bunsen_burner)
        }

        dropperText.setOnClickListener {
            aboutImage.setImageResource(R.drawable.bunsen_burner)
            aboutText.text = getString(R.string.about_bunsen_burner)
        }


        forcepsText.setOnClickListener {
            aboutImage.setImageResource(R.drawable.bunsen_burner)
            aboutText.text = getString(R.string.about_forceps)
        }

        graduatedCylinderText.setOnClickListener {
            aboutImage.setImageResource(R.drawable.bunsen_burner)
            aboutText.text = getString(R.string.about_graduatedcylinder)
        }
        magnifyingGlassText.setOnClickListener {
            aboutImage.setImageResource(R.drawable.bunsen_burner)
            aboutText.text = getString(R.string.about_magnifyingglass)
        }

        microscopeText.setOnClickListener {
            aboutImage.setImageResource(R.drawable.bunsen_burner)
            aboutText.text = getString(R.string.about_microscope)
        }
        spatulaText.setOnClickListener {
            aboutImage.setImageResource(R.drawable.bunsen_burner)
            aboutText.text = getString(R.string.about_spatula)
        }
        funnelText.setOnClickListener {
            aboutImage.setImageResource(R.drawable.bunsen_burner)
            aboutText.text = getString(R.string.about_funnel)
        }
    }
}
