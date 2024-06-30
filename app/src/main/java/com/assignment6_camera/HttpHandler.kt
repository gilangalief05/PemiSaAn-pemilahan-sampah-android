package com.assignment6_camera

import android.os.Build
import android.util.Log
import androidx.annotation.RequiresApi
import org.json.JSONArray
import org.json.JSONObject
import java.io.BufferedInputStream
import java.io.BufferedReader
import java.io.IOException
import java.io.InputStream
import java.io.InputStreamReader
import java.io.OutputStreamWriter
import java.net.HttpURLConnection
import java.net.MalformedURLException
import java.net.ProtocolException
import java.net.URL
import java.util.Base64

class HttpHandler {


    @RequiresApi(Build.VERSION_CODES.O)
    fun uploadImage(requestUrl : String, imageByteArray : ByteArray): String {
        val imageBase64 : String = Base64.getEncoder().encodeToString(imageByteArray)
        val jsonObject = JSONObject()
        jsonObject.put("image", imageBase64)
        val response : String

        try {
            var flag : Boolean = false
            val url = URL(requestUrl)
            val conn : HttpURLConnection = url.openConnection() as HttpURLConnection
            conn.requestMethod = "POST"
            conn.doInput = true
            conn.doOutput = true
            conn.setRequestProperty("Content-Type", "application/json")
            conn.setRequestProperty("Accept", "application/json")

            val outputStream = OutputStreamWriter(conn.outputStream)
            outputStream.write(jsonObject.toString())
            outputStream.flush()

            flag = conn.responseCode == HttpURLConnection.HTTP_OK

            val inputStream : InputStream = BufferedInputStream(conn.inputStream)
            response = _convertStreamToString(inputStream)
            val detectedObj = JSONObject(response)
            val classified : String = detectedObj.getString("class")
            return classified

        } catch (ex : MalformedURLException) {
            Log.e("HttpHandler", "MalformedURLException: " + ex.message)
            return "App error"
        } catch (ex : ProtocolException) {
            Log.e("HttpHandler", "ProtocolException: " + ex.message)
            return "App error"
        } catch (ex : IOException) {
            Log.e("HttpHandler", "IOException: " + ex.message)
            return "App error"
        } catch (ex : Exception) {
            Log.e("HttpHandler", "Exception: " + ex.message)
            return "App error"
        }
    }

    private fun _convertStreamToString(inputStream: InputStream) : String {
        val bufferReader = BufferedReader(InputStreamReader(inputStream))
        val strBuilder = StringBuilder()

        bufferReader.use { reader ->
            var line: String?
            try {
                while (reader.readLine().also { line = it } != null) {
                    strBuilder.append("$line\n")
                }
            } catch (ex: IOException) {
                ex.printStackTrace()
            }
        }

        return strBuilder.toString()
    }
}
