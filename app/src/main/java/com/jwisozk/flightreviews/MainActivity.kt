package com.jwisozk.flightreviews

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        if (savedInstanceState == null) {
            supportFragmentManager.beginTransaction()
                .replace(R.id.container, SubmitFragment.newInstance())
                .commitNow()
        }
    }

    fun transitionToSuccessFragment() {
        supportFragmentManager.beginTransaction()
            .replace(R.id.container, SuccessFragment.newInstance())
            .commitNow()
    }
}