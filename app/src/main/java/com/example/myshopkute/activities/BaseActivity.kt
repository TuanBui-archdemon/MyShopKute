package com.example.myshopkute.activities

import android.app.Dialog
import android.app.ProgressDialog
import android.content.Context
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.os.Message
import android.widget.Toast
import androidx.core.content.ContextCompat
import com.example.myshopkute.R
import com.example.myshopkute.databinding.ActivityBaseBinding
import com.example.myshopkute.databinding.ActivityLoginBinding
import com.example.myshopkute.databinding.ActivityMainBinding
import com.example.myshopkute.databinding.ActivityRigisterBinding
import com.google.android.material.snackbar.Snackbar

open class BaseActivity : AppCompatActivity() {
    // A global variable for double back press feature.
    private var doubleBackToExitPressedOnce = false
//    private lateinit var binding: ActivityBaseBinding
    private lateinit var mProgressDialog: Dialog
//    override fun onCreate(savedInstanceState: Bundle?) {
//        super.onCreate(savedInstanceState)
//        binding = ActivityBaseBinding.inflate(layoutInflater)
//        setContentView(binding.root)
    fun showErrorSnackBar(message: String, errorMessage: Boolean){
        val snackBar = Snackbar.make(findViewById(android.R.id.content), message, Snackbar.LENGTH_LONG)
        val snackBarView = snackBar.view
//        binding = ActivityBaseBinding.inflate(layoutInflater)
//        setContentView(binding.root)
        if(errorMessage){
            snackBarView.setBackgroundColor(
                ContextCompat.getColor(this@BaseActivity, R.color.colorError)
            )
        }else{
            snackBarView.setBackgroundColor(
                ContextCompat.getColor(this@BaseActivity, R.color.colorSuccess)
            )
        }
        snackBar.show()
    }

    fun showProgressDialog(text: String){
        mProgressDialog = Dialog(this)
        // Set the screen content from the layout source
        // The resource will be inflated, adđing all top level view to screen
        mProgressDialog.setContentView(R.layout.dialog_progress)
        // mProgressDialog.tv_progress_text.text = text
        mProgressDialog.setCancelable(false)
        mProgressDialog.setCanceledOnTouchOutside(false)

        mProgressDialog.show()
    }

    fun hideProgressDialog(){
        mProgressDialog.dismiss()
    }
    fun doubleBackToExit() {

        if (doubleBackToExitPressedOnce) {
            super.onBackPressed()
            return
        }

        this.doubleBackToExitPressedOnce = true

        Toast.makeText(
            this,
            resources.getString(R.string.please_clickback_again_to_exit),
            Toast.LENGTH_SHORT
        ).show()

        @Suppress("DEPRECATION")
        Handler().postDelayed({ doubleBackToExitPressedOnce = false }, 2000)
    }
}