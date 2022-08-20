package com.example.myshopkute.activities

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Toast
import com.example.myshopkute.R
import com.example.myshopkute.databinding.ActivityForgotPasswordBinding
import com.example.myshopkute.databinding.ActivityRigisterBinding
import com.google.firebase.auth.FirebaseAuth

class ForgotPasswordActivity : BaseActivity() {
    private lateinit var binding: ActivityForgotPasswordBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityForgotPasswordBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setupActionBar()
    }
    private fun setupActionBar(){
        setSupportActionBar(binding.toolbarForgotPassworkActivity)

        val actionBar = supportActionBar
        if (actionBar != null){
            actionBar.setDisplayHomeAsUpEnabled(true)
            actionBar.setHomeAsUpIndicator(R.drawable.ic_black_back_ios_24)

        }
        binding.toolbarForgotPassworkActivity.setNavigationOnClickListener { onBackPressed() }
        binding.btnSubmit.setOnClickListener{
            val email: String = binding.etEmail.text.toString().trim { it <= ' ' }
            if(email.isEmpty()){
                showErrorSnackBar(resources.getString(R.string.err_msg_enter_email), errorMessage = true)
            }else{
                showProgressDialog(resources.getString(R.string.please_wait))
                FirebaseAuth.getInstance().sendPasswordResetEmail(email).addOnCompleteListener{
                    task -> hideProgressDialog()
                    if(task.isSuccessful){
                        Toast.makeText(
                            this@ForgotPasswordActivity,
                            resources.getString(R.string.email_sent_success),
                            Toast.LENGTH_LONG
                        ).show()
                        finish()
                    }else{
                        showErrorSnackBar(task.exception!!.message.toString(), errorMessage = true)
                    }
                }
            }
        }
    }
}