package com.example.employeedata.activity

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import com.example.employeedata.R

class SplashScreen : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash_screen)

        startNewActivty()
    }

    private fun startNewActivty(){
        Handler().postDelayed(Runnable {
            kotlin.run {
                startActivity(Intent(this@SplashScreen,MainActivity::class.java))
                finish()
            }
        },2000)
    }
}
