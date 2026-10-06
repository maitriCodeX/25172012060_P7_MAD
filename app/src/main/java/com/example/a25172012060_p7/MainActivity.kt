package com.example.a25172012060_p7

import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.floatingactionbutton.FloatingActionButton
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import org.json.JSONArray
import org.json.JSONObject

class MainActivity : AppCompatActivity() {

    private val TAG = "MainActivity"
    val personList = ArrayList<Person>()
    private lateinit var db: DatabaseHelper
    private lateinit var personAdapter: PersonAdapter
    private lateinit var recyclerView: RecyclerView
    private lateinit var fabRefresh: FloatingActionButton

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        db = DatabaseHelper(this)

        recyclerView = findViewById(R.id.recyclerView)
        fabRefresh = findViewById(R.id.fabRefresh)

        recyclerView.layoutManager = LinearLayoutManager(this)
        personAdapter = PersonAdapter(this, personList) { person, position ->
            deletePerson(person, position)
        }
        recyclerView.adapter = personAdapter

        fabRefresh.setOnClickListener {
            Toast.makeText(this, "Fetching data...", Toast.LENGTH_SHORT).show()
            networkDb()
        }

        loadDataFromDb()

        if (personList.isEmpty()) {
            networkDb()
        }
    }

    private fun loadDataFromDb() {
        personList.clear()
        personList.addAll(db.getAllPersons())
        personAdapter.notifyDataSetChanged()
    }

    private fun deletePerson(person: Person, position: Int) {
        db.deletePerson(person.id)
        personList.removeAt(position)
        personAdapter.notifyItemRemoved(position)
        personAdapter.notifyItemRangeChanged(position, personList.size)
        Toast.makeText(this, "Deleted ${person.name}", Toast.LENGTH_SHORT).show()
    }

    private fun getPersonDetailFromJson(sJson: String?) {
        if (sJson.isNullOrEmpty()) return
        try {
            val jsonArray = if (sJson.trim().startsWith("{")) {
                val jsonObject = JSONObject(sJson)
                jsonObject.optJSONArray("value") ?: JSONArray()
            } else {
                JSONArray(sJson)
            }

            for (i in 0 until jsonArray.length()) {
                val jsonObject = jsonArray.getJSONObject(i)
                val person = Person(jsonObject)
                try {
                    if (db.getPerson(person.id) != null) {
                        db.updatePerson(person)
                    } else {
                        db.insertPerson(person)
                    }
                } catch (e: Exception) {
                    e.printStackTrace()
                }
            }

            runOnUiThread {
                loadDataFromDb()
                Toast.makeText(this, "Data loaded (${personList.size} contacts)", Toast.LENGTH_SHORT).show()
            }
        } catch (e: Exception) {
            Log.e(TAG, "Error parsing JSON: ${e.message}")
            e.printStackTrace()
        }
    }

    fun getPersonData(data: String) {
        Log.i(TAG, "getPersonData: $data")
        getPersonDetailFromJson(data)
    }

    fun networkDb() {
        CoroutineScope(Dispatchers.IO).launch {
            try {
                val data = HttpRequest().makeServiceCall(
                    "https://api.json-generator.com/templates/5rDXHcbgpo93/data",
                    "d7wrtfqywyhu7y2bcbsz3cgjpbfisuhnmbibvgvf"
                )
                withContext(Dispatchers.Main) {
                    try {
                        if (data != null) {
                            getPersonData(data)
                        } else {
                            Toast.makeText(this@MainActivity, "Failed to fetch data from network", Toast.LENGTH_SHORT).show()
                        }
                    } catch (e: Exception) {
                        e.printStackTrace()
                    }
                }
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }
}
