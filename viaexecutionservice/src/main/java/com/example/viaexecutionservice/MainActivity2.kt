package com.example.viaexecutionservice

import android.content.SharedPreferences
import android.os.Bundle
import android.util.Log
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import java.util.concurrent.ExecutorService
import java.util.concurrent.Executors

class MainActivity : AppCompatActivity() {
    private var secondsElapsed: Int = 0
    private lateinit var textSecondsElapsed: TextView
    private val strSecondsElapsed: String = "Seconds elapsed: "
    private lateinit var mPrefs: SharedPreferences
    private lateinit var executor: ExecutorService

    private fun playTimer() {
        executor = Executors.newSingleThreadExecutor()
        executor.submit {
            while (!executor.isShutdown) {
                Thread.sleep(1000)
                Log.d("Working thread: ", Thread.currentThread().name)
                textSecondsElapsed.post {
                    (strSecondsElapsed + secondsElapsed++).also { textSecondsElapsed.text = it }
                }
            }
        }
    }

    override fun onStop() {
        super.onStop()
        val ed = mPrefs.edit()
        ed.putInt(strSecondsElapsed, secondsElapsed)
        ed.apply()
        executor.shutdown()
    }

    override fun onStart() {
        super.onStart()
        mPrefs = getSharedPreferences(localClassName, MODE_PRIVATE)
        secondsElapsed = mPrefs.getInt(strSecondsElapsed, secondsElapsed)
        playTimer()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main2)
        textSecondsElapsed = findViewById(R.id.textSecondsElapsed2)
    }
}