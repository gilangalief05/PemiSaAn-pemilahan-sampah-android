package com.assignment6_camera

import android.app.Activity
import android.content.Intent
import android.graphics.Bitmap
import android.os.Build
import android.os.Bundle
import android.provider.MediaStore
import android.view.View
import android.view.View.OnClickListener
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.core.graphics.drawable.toBitmap

class MainActivity : AppCompatActivity(), OnClickListener, MainActivityCallback {
    private val _controller = MainActivityController(this, this)
    lateinit var _cameraActivityResultLauncher : ActivityResultLauncher<Intent>

    lateinit var IV_CameraView : ImageView
    lateinit var Btn_TakePicture : Button
    lateinit var Btn_Upload : Button
    lateinit var textView3 : TextView
    lateinit var textView4 : TextView
    lateinit var textView5 : TextView

    @RequiresApi(Build.VERSION_CODES.TIRAMISU)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        _cameraActivityResultLauncher = _getCameraActivityResultLauncher()

        IV_CameraView = findViewById(R.id.IV_camera_view)
        Btn_TakePicture = findViewById(R.id.Btn_take_pict)
        Btn_Upload = findViewById(R.id.Btn_upload)
        textView3 = findViewById(R.id.textView3)
        textView4 = findViewById(R.id.textView4)
        textView5 = findViewById(R.id.textView5)

        Btn_TakePicture.setOnClickListener(this)
        Btn_Upload.setOnClickListener(this)
    }

    @RequiresApi(Build.VERSION_CODES.TIRAMISU)
    override fun onClick(v: View?) {
        when (v?.id) {
            R.id.Btn_take_pict -> {
                try {
                    // Take and save image
                    val cameraActivityIntent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
                    _cameraActivityResultLauncher.launch(cameraActivityIntent)
                } catch (ex : Exception) {
                    ex.printStackTrace()
                }
            }

            R.id.Btn_upload -> {
                try {
                    val imageByteArray : ByteArray = _controller.bitmapToByteArray(IV_CameraView.drawable.toBitmap())

                    _controller.uploadImage(resources.getString(R.string.BACKEND_URL), imageByteArray)
                } catch (ex : Exception) {
                    ex.printStackTrace()
                }
            }
        }
    }

    override fun updateImageView(imageBitmap : Bitmap) {
        IV_CameraView.visibility = ImageView.VISIBLE
        IV_CameraView.setImageBitmap(imageBitmap)
        textView5.layoutParams.height = 1080
        textView5.visibility = ImageView.INVISIBLE
    }

    override fun showUploadButton() {
        Btn_Upload.visibility = View.VISIBLE

    }

    override fun showDescription(classified : String) {
        val explanation : String
        textView3.text = classified
        when (classified) {
            "Aerosols" -> {
                explanation = "Aerosols is hazardous waste"
            }
            "Aluminum can" -> {
                explanation = "Aluminum can is inorganic waste"
            }
            "Aluminum caps" -> {
                explanation = "Aluminum caps is inorganic waste"
            }
            "Cellulose" -> {
                explanation = "Cellulose is inorganic waste"
            }
            "Ceramic" -> {
                explanation = "Ceramic is inorganic waste"
            }
            "Combined plastic" -> {
                explanation = "Combined plastic is inorganic waste"
            }
            "Disposable tableware" -> {
                explanation = "Questionable"
            }
            "Electronics" -> {
                explanation = "Electronics is hazardous waste"
            }
            "Foil" -> {
                explanation = "Foil is inorganic waste"
            }
            "Furniture" -> {
                explanation = "Questionable"
            }
            "Iron utensils" -> {
                explanation = "Iron utensils is inorganic waste"
            }
            "Liquid" -> {
                explanation = "Questionable"
            }
            "Metal shavings" -> {
                explanation = "Metal shavings is inorganic waste"
            }
            "Milk bottle" -> {
                explanation = "Milk bottle is inorganic waste"
            }
            "Paper bag" -> {
                explanation = "Paper bag is inorganic waste"
            }
            "Paper cups" -> {
                explanation = "Paper cups is inorganic waste"
            }
            "Paper shavings" -> {
                explanation = "Paper shavings is inorganic waste"
            }
            "Papier mache" -> {
                explanation = "Papier mache is inorganic waste"
            }
            "Plastic can" -> {
                explanation = "Plastic can is inorganic waste"
            }
            "Plastic canister" -> {
                explanation = "Plastic canister is inorganic waste"
            }
            "Plastic caps" -> {
                explanation = "Plastic caps is inorganic waste"
            }
            "Plastic cup" -> {
                explanation = "Plastic cup is inorganic waste"
            }
            "Plastic shaker" -> {
                explanation = "Plastic shaker is inorganic waste"
            }
            "Plastic shavings" -> {
                explanation = "Plastic shavings is inorganic waste"
            }
            "Plastic toys" -> {
                explanation = "Plastic toys is inorganic waste"
            }
            "Postal packaging" -> {
                explanation = "Postal packaging is inorganic waste"
            }
            "Scrap metal" -> {
                explanation = "Scrap metal is inorganic waste"
            }
            "Stretch film" -> {
                explanation = "Stretch film is inorganic waste"
            }
            "Tetra pack" -> {
                explanation = "Tetra pack is inorganic waste"
            }
            "Textile" -> {
                explanation = "Textile is inorganic waste"
            }
            "Unknown plastic" -> {
                explanation = "Unknown plastic is inorganic waste"
            }
            "Wood" -> {
                explanation = "Wood is inorganic waste"
            }
            "Zip plastic bag" -> {
                explanation = "Zip plastic bag is inorganic waste"
            }
            "Plastic bottle" -> {
                explanation = "Plastic bottle is inorganic waste"
            }
            "Organic" -> {
                explanation = "This waste is organic, better to place under tree"
            }
            "Paper" -> {
                explanation = "Paper is inorganic waste"
            }
            "Glass bottle" -> {
                explanation = "Glass bottle is inorganic waste"
            }
            "Tin" -> {
                explanation = "Tin is inorganic waste"
            }
            "Cardboard" -> {
                explanation = "Cardboard is inorganic waste"
            }
            "Plastic bag" -> {
                explanation = "Plastic bag is inorganic waste"
            }
            "Printing industry" -> {
                explanation = "Printing industry is inorganic waste"
            }
            "Container for household chemicals" -> {
                explanation = "Container for household chemicals is hazardous waste"
            }
            else -> {
                explanation = "Undefined"
            }
        }

        textView4.text = explanation
    }

    @RequiresApi(Build.VERSION_CODES.TIRAMISU)
    private fun _getCameraActivityResultLauncher() : ActivityResultLauncher<Intent> {
        try {
            val activityResultLauncher = registerForActivityResult(
                ActivityResultContracts.StartActivityForResult()
            ) { result ->
                if (result.resultCode == Activity.RESULT_OK) {
                    val data : Intent? = result.data
                    _controller.saveImageFile(data)
                }
            }

            return activityResultLauncher
        } catch (ex : Exception) {
            throw ex
        }
    }
}