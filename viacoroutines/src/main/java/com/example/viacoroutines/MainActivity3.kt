package com.example.viacoroutines

import android.content.SharedPreferences
import android.os.Bundle
import android.util.Log
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import kotlinx.coroutines.*

class MainActivity3 : AppCompatActivity() {
    private var secondsElapsed: Int = 0
    private lateinit var textSecondsElapsed: TextView
    private val strSecondsElapsed: String = "Seconds elapsed: "
    private lateinit var mPrefs: SharedPreferences

    override fun onStop() {
        super.onStop()

        val ed = mPrefs.edit()
        ed.putInt(strSecondsElapsed, secondsElapsed)
        ed.apply()
    }

    override fun onStart() {
        super.onStart()
        mPrefs = getSharedPreferences(localClassName, MODE_PRIVATE)
        secondsElapsed = mPrefs.getInt(strSecondsElapsed, secondsElapsed)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main3)
        textSecondsElapsed = findViewById(R.id.textSecondsElapsed3)
        lifecycleScope.launchWhenResumed {
            while (true) {
                delay(1000)
                Log.d("Working thread1: ", Thread.currentThread().name)
                withContext(Dispatchers.Main) {
                    (strSecondsElapsed + secondsElapsed++).also { textSecondsElapsed.text = it }
                }
            }
        }
    }
}