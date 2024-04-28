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
                infoTextView.text = "$detectedLabel - \n\" +A beaker is a cylindrical container with a flat bottom, typically used in laboratories for holding, mixing, and heating liquids. It usually has volume markings to measure the amount of liquid being held. Beakers come in various sizes and are made from materials such as glass or plastic. They are commonly used for simple reactions, titrations, and general liquid handling in scientific experiments."
            }
            "Bunsen Burner" -> {
                infoTextView.text = "$detectedLabel - \n" + "A Bunsen burner is a common laboratory device used for heating, sterilizing, and combustion. It consists of a metal tube connected to a gas source (such as natural gas or propane) with adjustable airflow. The gas is mixed with air in the tube and ignited at the burner's tip, producing a hot, blue flame. The flame's temperature and intensity can be controlled by adjusting the gas flow and airflow. Bunsen burners are widely used in chemistry, biology, and other scientific disciplines for tasks like heating solutions, sterilizing equipment, and conducting experiments requiring high temperatures."
            }
            "Dropper" -> {
                infoTextView.text = "$detectedLabel - \n" + "A dropper is a laboratory tool used to transfer small amounts of liquid with precision. It typically consists of a small glass or plastic tube with a tapered tip at one end. By squeezing the bulb or pressing a plunger, the dropper can draw liquid into the tube. When the pressure is released, the liquid is dispensed drop by drop from the tapered tip. Droppers are commonly used in chemistry, biology, and medical settings for tasks such as adding reagents to solutions, dispensing medication, or transferring small volumes of liquids for analysis."
            }
            "Forceps" -> {
                infoTextView.text = "$detectedLabel -\n" + " Forceps are a type of handheld surgical instrument with two blades and a handle used for grasping, holding, or manipulating objects during medical procedures or laboratory work. They come in various shapes and sizes, with specialized tips designed for different tasks. Forceps are commonly made of stainless steel and can be either straight or curved. They are widely used in surgeries, medical examinations, and laboratory experiments for tasks such as grasping tissue, handling small objects, or performing delicate procedures with precision."
            }
            "Funnel" -> {
                infoTextView.text ="$detectedLabel -\n" +  "A funnel is a cone-shaped or cylindrical device with a narrow tube at the bottom and a wider opening at the top. It is commonly used in laboratories and various other settings for transferring liquids or fine-grained substances from one container to another without spillage. Funnels are available in different materials such as glass, plastic, or metal, and they come in various sizes to suit different needs. They are essential tools for filtering liquids, decanting, and directing the flow of substances into narrow openings or containers. Funnels play a crucial role in processes like chemical filtration, separation, and sample preparation in scientific experiments and industrial applications."

            }
            "Graduated Cylinder" -> {
                infoTextView.text = "$detectedLabel"+ "\n" +
                        "A graduated cylinder is a cylindrical glass or plastic container used to measure the volume of liquids accurately. It has a narrow, uniform diameter and is marked with graduated lines along its length, indicating different volume measurements. The volume markings typically appear in milliliters (mL) or cubic centimeters (cm³), allowing precise measurement of liquid volumes. Graduated cylinders come in various sizes, ranging from small laboratory sizes to larger industrial ones. They are commonly used in chemistry, biology, and other scientific disciplines for tasks such as measuring reagents, preparing solutions, and conducting experiments that require precise volume measurements."
            }
            "Magnifying Glass" -> {
                infoTextView.text = "$detectedLabel"+ "\n" +
              "A magnifying glass is a simple optical instrument consisting of a convex lens mounted in a frame with a handle. When held close to an object, it magnifies and enhances its appearance by refracting light, making details appear larger and clearer. Magnifying glasses are commonly used for tasks such as reading fine print, examining small objects or details, and conducting close-up inspections. They are essential tools in various fields, including science, engineering, medicine, and hobbies."
            }
            "Microscope" -> {
                infoTextView.text = " $detectedLabel"+"\n" +
                        "A microscope is a scientific instrument used to magnify and visualize objects that are too small to be seen by the naked eye. It consists of an optical system that magnifies the image of the object, typically using lenses or mirrors, and an eyepiece or camera to view the magnified image. Microscopes come in various types, including optical microscopes, electron microscopes, and scanning probe microscopes, each with different magnification levels and capabilities. They are widely used in fields such as biology, medicine, materials science, forensics, and nanotechnology for research, diagnosis, quality control, and education purposes."
            }
            "Spatula" -> {
                infoTextView.text = "$detectedLabel"+"\n" +
                        "A spatula is a versatile laboratory tool used for various tasks such as mixing, spreading, scraping, and transferring materials. It typically consists of a flat, flexible blade attached to a handle, which can be made of metal, plastic, or rubber. Spatulas come in different shapes and sizes, including flat, rounded, or spoon-shaped blades, to suit different applications. They are commonly used in chemistry, biology, and other scientific disciplines for tasks such as mixing chemicals, transferring powders or liquids, scraping samples from containers, and manipulating delicate materials. Spatulas are essential tools in laboratory settings for precise and controlled handling of substances."
            }
            "Test Tube" -> {
                infoTextView.text = "$detectedLabel"+ "\n"+ "\n" +
                        "A test tube is a cylindrical, hollow glass or plastic container with one open end and one closed end. It is used for holding, mixing, and heating small amounts of liquid or solid substances in laboratory experiments. Test tubes come in various sizes, ranging from small tubes for individual samples to larger tubes for bulk storage. They are commonly used in chemistry, biology, and other scientific disciplines for tasks such as mixing reagents, conducting reactions, culturing microorganisms, and storing samples. Test tubes are typically held in a test tube rack for stability and organization during experiments."
            }
            else -> {
                infoTextView.text = detectedLabel
            }
        }
    }
}