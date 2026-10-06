package com.example.a25172012060_p7

import android.util.Log
import java.io.BufferedInputStream
import java.io.BufferedReader
import java.io.InputStreamReader
import java.net.HttpURLConnection
import java.net.URL

class HttpRequest {
    val TAG = "HttpRequest"

    fun makeServiceCall(reqUrl: String, token: String): String? {
        var response: String? = null
        try {
            val url = URL(reqUrl)
            val conn = url.openConnection() as HttpURLConnection
            conn.setRequestProperty("Authorization", "Bearer $token")
            conn.setRequestProperty("Content-Type", "application/json")
            response = convertStreamToString(BufferedInputStream(conn.getInputStream()))
        } catch (e: Exception) {
            Log.e(TAG, "makeServiceCall: ${e.message}")
        }
        return response
    }

    private fun convertStreamToString(iss: BufferedInputStream): String {
        val reader = BufferedReader(InputStreamReader(iss))
        val sb = StringBuilder()
        var line: String?
        try {
            while (reader.readLine().also { line = it } != null) {
                sb.append(line).append("\n")
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
        return sb.toString()
    }
}
