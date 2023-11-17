package com.manishdubey.filetransfer

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import android.os.Handler
import android.os.Looper
import android.widget.ProgressBar

class LodingPage : AppCompatActivity() {

    private val progressBar: ProgressBar by lazy { findViewById(R.id.progressBar) }
    private var progressBarStatus = 0
    private val handler = Handler(Looper.getMainLooper())

    override fun onCreate(savedInstanceState: Bundle?) {
        supportActionBar?.hide()
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_loding_page)

        // Simulate progress updates
        val maxProgress = 100
        val updateInterval = 60

        // Start a background thread to update the progress
        Thread {
            while (progressBarStatus < maxProgress) {
                progressBarStatus++
                handler.post {
                    // Update the progress bar on the main thread
                    progressBar.progress = progressBarStatus
                }

                try {
                    Thread.sleep(updateInterval.toLong())
                } catch (e: InterruptedException) {
                    e.printStackTrace()
                }
            }


        val intent=Intent(this,SplashScreen::class.java)
            startActivity(intent)
            finish()
        }.start()
    }
}
