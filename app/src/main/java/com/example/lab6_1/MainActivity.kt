package com.example.lab6_1

import android.content.SharedPreferences
import android.os.Bundle
import android.util.Log
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity

class MainActivity : AppCompatActivity() {
    private var secondsElapsed: Int = 0
    private lateinit var textSecondsElapsed: TextView
    private val strSecondsElapsed: String = "Seconds elapsed: "
    private lateinit var mPrefs: SharedPreferences
    private lateinit var backgroundThread: Thread

    private fun playTimer() {
        backgroundThread = Thread {
            while (true) {
                try {
                    Thread.sleep(1000)
                } catch (e: InterruptedException) {
                    Log.d("Stopped thread: ", Thread.currentThread().name)
                    return@Thread
                }
                Log.d("Working thread: ", Thread.currentThread().name)
                textSecondsElapsed.post {
                    (strSecondsElapsed + secondsElapsed++).also { textSecondsElapsed.text = it }
                }
            }
        }
        backgroundThread.start()
    }

    override fun onStop() {
        super.onStop()

        val ed = mPrefs.edit()
        ed.putInt(strSecondsElapsed, secondsElapsed)
        ed.apply()

        backgroundThread.interrupt()
    }

    override fun onStart() {
        super.onStart()
        mPrefs = getSharedPreferences(localClassName, MODE_PRIVATE)
        secondsElapsed = mPrefs.getInt(strSecondsElapsed, secondsElapsed)
        playTimer()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        textSecondsElapsed = findViewById(R.id.textSecondsElapsed)
    }
}