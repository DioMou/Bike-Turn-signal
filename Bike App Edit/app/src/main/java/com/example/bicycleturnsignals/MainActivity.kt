package com.example.bicycleturnsignals

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.content.Intent
import android.content.BroadcastReceiver
import android.content.Context
import androidx.core.app.ComponentActivity.ExtraData
import androidx.core.content.ContextCompat.getSystemService
import android.icu.lang.UCharacter.GraphemeClusterBreak.T
import android.util.Log
import android.content.IntentFilter
import androidx.core.content.ContextCompat.getSystemService
import android.icu.lang.UCharacter.GraphemeClusterBreak.T
import androidx.core.content.ContextCompat.getSystemService
import android.icu.lang.UCharacter.GraphemeClusterBreak.T
import android.view.LayoutInflater
import androidx.core.content.ContextCompat.getSystemService
import android.icu.lang.UCharacter.GraphemeClusterBreak.T
import android.view.View
import android.widget.*


class MainActivity : AppCompatActivity() {

    var broadcastReceiver: BroadcastReceiver = object : BroadcastReceiver() {
        override fun onReceive(context: Context, intent: Intent) {
            val s1 = intent.getStringExtra("DATAPASSED")
            Log.i("NEW IMAGE", s1)
            loadArrow(s1)
        }
    }

    private fun loadArrow(hash: String) {
        val imgSaver = ImageSaver(applicationContext)
        val prefs = getSharedPreferences("arrowInts", 0)

        val parent = findViewById<LinearLayout>(R.id.arrows)
        val inflater = LayoutInflater.from(applicationContext)
        val chunk = inflater.inflate(R.layout.chunk_arrow, parent, false)
        chunk.findViewById<ImageView>(R.id.arrow).setImageBitmap(imgSaver.setFileName(hash).load())
        val spinner = chunk.findViewById<Spinner>(R.id.direction)
        ArrayAdapter.createFromResource(
            applicationContext,
            R.array.directions,
            android.R.layout.simple_spinner_item
        ).also { adapter ->
            // Specify the layout to use when the list of choices appears
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
            // Apply the adapter to the spinner
            spinner.adapter = adapter
        }
        spinner.setSelection(prefs.getInt(hash, 4))
        spinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onNothingSelected(p0: AdapterView<*>?) {
                TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
            }

            override fun onItemSelected(p0: AdapterView<*>?, p1: View?, position: Int, p3: Long) {
                prefs.edit().putInt(hash, position).apply()
            }
        }

        parent.addView(chunk)
    }

    override fun onStart() {
        super.onStart()
        val intentFilter = IntentFilter()
        intentFilter.addAction("com.example.bicycleturnsignals")
        registerReceiver(broadcastReceiver, intentFilter)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
    }

    override fun onStop() {
        super.onStop()
        unregisterReceiver(broadcastReceiver)
    }
}
