package com.example.a25172012060_p7

import android.content.Intent
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat

class MapActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_map)

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.mapLayout)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        val person = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            intent.getSerializableExtra("person", Person::class.java)
        } else {
            @Suppress("DEPRECATION")
            intent.getSerializableExtra("person") as? Person
        }

        val tvName = findViewById<TextView>(R.id.tvMapName)
        val tvPhone = findViewById<TextView>(R.id.tvMapPhone)
        val tvEmail = findViewById<TextView>(R.id.tvMapEmail)
        val tvAddress = findViewById<TextView>(R.id.tvMapAddress)
        val tvCoordinates = findViewById<TextView>(R.id.tvMapCoordinates)
        val btnOpenMap = findViewById<Button>(R.id.btnOpenMap)

        if (person != null) {
            tvName.text = person.name
            tvPhone.text = "Phone: ${person.phoneNo}"
            tvEmail.text = "Email: ${person.emailId}"
            tvAddress.text = "Address: ${person.address}"
            tvCoordinates.text = "Latitude: ${person.latitude}, Longitude: ${person.longitute}"

            btnOpenMap.setOnClickListener {
                val geoUri = "geo:${person.latitude},${person.longitute}?q=${person.latitude},${person.longitute}(${Uri.encode(person.name)})"
                val mapIntent = Intent(Intent.ACTION_VIEW, Uri.parse(geoUri))
                mapIntent.setPackage("com.google.android.apps.maps")
                try {
                    startActivity(mapIntent)
                } catch (e: Exception) {
                    val browserIntent = Intent(
                        Intent.ACTION_VIEW,
                        Uri.parse("https://www.google.com/maps/search/?api=1&query=${person.latitude},${person.longitute}")
                    )
                    startActivity(browserIntent)
                }
            }
        } else {
            Toast.makeText(this, "Person data not found", Toast.LENGTH_SHORT).show()
        }
    }
}
