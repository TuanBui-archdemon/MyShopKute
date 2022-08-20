package com.example.myshopkute.activities

import android.content.Context
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.myshopkute.R
import com.example.myshopkute.databinding.ActivityLoginBinding
import com.example.myshopkute.databinding.ActivityMainBinding
import com.example.myshopkute.databinding.ActivitySplashBinding
import com.example.myshopkute.units.Constants

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val sharedPreferences = getSharedPreferences(Constants.MYSHOPKUTE_PREFERRENCES, Context.MODE_PRIVATE)
        val username = sharedPreferences.getString(Constants.LOGGED_IN_USERNAME, " ")!!
        binding.tvMain.text = "Hello $username."
    }
}