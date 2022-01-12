package com.example.viaexecutionservice

import android.app.Application
import android.content.SharedPreferences
import android.os.Bundle
import android.util.Log
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import java.util.concurrent.ExecutorService
import java.util.concurrent.Executors
import java.util.concurrent.Future

class MainActivity2 : AppCompatActivity() {
    private var secondsElapsed: Int = 0
    private lateinit var textSecondsElapsed: TextView
    private val strSecondsElapsed: String = "Seconds elapsed: "
    private lateinit var mPrefs: SharedPreferences
    private lateinit var task: Future<*>
    private val executor by lazy { (application as MyApplication).executor }

    private fun playTimer(): Future<*> {
        return executor.submit {
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
        task.cancel(true)
        val ed = mPrefs.edit()
        ed.putInt(strSecondsElapsed, secondsElapsed)
        ed.apply()
    }

    override fun onStart() {
        super.onStart()
        mPrefs = getSharedPreferences(localClassName, MODE_PRIVATE)
        secondsElapsed = mPrefs.getInt(strSecondsElapsed, secondsElapsed)
        task = playTimer()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main2)
        textSecondsElapsed = findViewById(R.id.textSecondsElapsed2)
    }
}

class MyApplication : Application() {
    val executor: ExecutorService = Executors.newSingleThreadExecutor()
}