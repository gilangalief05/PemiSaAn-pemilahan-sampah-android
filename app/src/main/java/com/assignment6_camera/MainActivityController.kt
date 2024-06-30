package com.assignment6_camera

import android.annotation.SuppressLint
import android.content.ContentValues
import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.net.Uri
import android.os.Build
import android.os.Handler
import android.os.Looper
import android.provider.MediaStore
import android.text.format.DateFormat
import android.widget.Toast
import androidx.annotation.RequiresApi
import java.io.ByteArrayOutputStream
import org.json.JSONArray
import org.json.JSONObject
import java.util.Date
import java.util.concurrent.Executor
import java.util.concurrent.Executors

@Suppress("DEPRECATION")
class MainActivityController(private val context: Context, private val callback: MainActivityCallback) {
    @SuppressLint("Recycle")
    fun saveImageFile(intent : Intent?) {
        try {
            if (intent == null) {
                throw Exception("Error passing image file!")
            }

            val imageBitmap : Bitmap = intent.getParcelableExtra("data")
                ?: throw Exception("Error passing image file!")

            val contentResolver = context.contentResolver

            val dirUri : Uri = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                MediaStore.Images.Media.getContentUri(MediaStore.VOLUME_EXTERNAL_PRIMARY)
            } else {
                MediaStore.Images.Media.EXTERNAL_CONTENT_URI
            }

            val contentValues = ContentValues()
            val date = Date()
            val dateNow = DateFormat.format("MM-dd-yyyy hh-mm-ss", date.time)
            contentValues.put(MediaStore.Images.Media.DISPLAY_NAME, "${dateNow}.png")
            contentValues.put(MediaStore.Images.Media.MIME_TYPE, "images/")

            val imageUri : Uri? = contentResolver.insert(dirUri, contentValues)

            try {
                val outputStream = contentResolver.openOutputStream(imageUri!!)
                imageBitmap.compress(Bitmap.CompressFormat.PNG, 100, outputStream!!)

                Toast.makeText(context, "Image saved to the gallery", Toast.LENGTH_SHORT).show()
            } catch (ex : Exception) {
                Toast.makeText(context, "Failed to save image", Toast.LENGTH_SHORT).show()
                throw ex
            }

            // update the image view
            callback.updateImageView(imageBitmap)

            // show upload button
            callback.showUploadButton()

        } catch (ex : Exception) {
            throw ex
        }
    }

    fun bitmapToByteArray(imageBitmap: Bitmap) : ByteArray {
        val byteStream = ByteArrayOutputStream()
        imageBitmap.compress(Bitmap.CompressFormat.PNG, 100, byteStream)
        return byteStream.toByteArray()
    }

    @RequiresApi(Build.VERSION_CODES.O)
    fun uploadImage(requestUrl : String, imageByteArray: ByteArray) {
        try {
            val executor : Executor = Executors.newSingleThreadExecutor()
            val uiHandler = Handler(Looper.getMainLooper())
            val uploadImageApi = "${requestUrl}/api/upload-photo"

            Toast.makeText(context, "Uploading image...", Toast.LENGTH_SHORT).show()

            executor.execute {
                val httpHandler = HttpHandler()
                val result : String = httpHandler.uploadImage(uploadImageApi, imageByteArray)

                uiHandler.post {
                    if (result != "no") {
                        Toast.makeText(context, result, Toast.LENGTH_SHORT).show()
                    } else {
                        Toast.makeText(context, "Failed to upload image", Toast.LENGTH_SHORT).show()
                    }
                }
            }

        } catch (ex : Exception) {
            throw ex
        }
    }
}