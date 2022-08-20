package com.example.myshopkute.activities

import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.text.TextUtils
import android.util.Log
import android.view.View
import android.view.WindowInsets
import android.view.WindowManager
import com.example.myshopkute.R
import com.example.myshopkute.databinding.ActivityLoginBinding
import com.example.myshopkute.firestore.FirestoreClass
import com.example.myshopkute.models.User
import com.example.myshopkute.units.Constants
import com.google.firebase.auth.FirebaseAuth


class LoginActivity : BaseActivity(), View.OnClickListener {
    private lateinit var binding: ActivityLoginBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        @Suppress("DEPRECATION")
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            window.insetsController?.hide(WindowInsets.Type.statusBars())
        } else {
            window.setFlags(
                WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN
            )
        }
        binding.tvForgotPassword.setOnClickListener(this)
        binding.btnLogin.setOnClickListener(this)
        binding.tvRigister.setOnClickListener(this)


    }
    override fun onClick(view: View){
        if(view != null){
            when(view.id){
                R.id.tv_forgot_password -> {
                    val intent = Intent(this@LoginActivity, ForgotPasswordActivity::class.java)
                    startActivity((intent))
                }
                R.id.btn_login -> {
                    // Call the validate function
                    logInRegisterUser()
                }
                R.id.tv_rigister ->{
                    // Launch the register screnn when the user clciks on the text
                    val intent = Intent(this@LoginActivity, RegisterActivity::class.java)
                    startActivity((intent))
                }
            }
        }
    }
    fun userLoggedInSuccess(user: User){
        //Hide the progress dialog
        hideProgressDialog()

        // Print the user details in the log as of now
        Log.i("First Name", user.firstName)
        Log.i("Last Name", user.lastName)
        Log.i("Email", user.email)


        if(user.profileCompleted == 0)
        {
            // If the user profile is incomplete then launch the UserProfileActivity.
            val intent = Intent(this@LoginActivity, UserProfileActivity::class.java)
            intent.putExtra(Constants.EXTRA_USER_DETAILS, user)
            startActivity(intent)
        }else{
            // Redirect the user to Main Screen after log in.
            startActivity(Intent(this@LoginActivity, DashboardActivity::class.java))
        }
        finish()
    }
    private fun validateLoginDetails(): Boolean{
        return when {
            TextUtils.isEmpty(binding.etEmail.text.toString().trim(){it <= ' '}) ->{
                showErrorSnackBar(resources.getString(R.string.err_msg_enter_email), errorMessage = true)
                false
            }

            TextUtils.isEmpty(binding.etPassword.text.toString().trim(){it <= ' '}) ->{
                showErrorSnackBar(resources.getString(R.string.err_msg_enter_password), errorMessage = true)
                false
            }
            else -> {
                // showErrorSnackBar("Your details are valid.", errorMessage = false)
                true
            }
        }
    }
    private fun logInRegisterUser(){
        // Check with validate function if the entries are valid or not
        if(validateLoginDetails()){
            // Show the progress dialog.
            showProgressDialog(resources.getString(R.string.please_wait))

            // Get the text from editText and trim the space
            val email: String = binding.etEmail.text.toString().trim{it <= ' '}
            val password: String = binding.etPassword.text.toString().trim{it <= ' '}

            // Login using Firebase auth
            FirebaseAuth.getInstance().signInWithEmailAndPassword(email, password)
                .addOnCompleteListener{
                            task ->
                        // If the registration is successfully done
                        if(task.isSuccessful){
                            FirestoreClass().getUserDetails(this@LoginActivity)
                        }else{
                            hideProgressDialog()
                            // If the login is not succesful then show error
                            showErrorSnackBar(task.exception!!.message.toString(),true)
                        }
                    }
        }
    }
}