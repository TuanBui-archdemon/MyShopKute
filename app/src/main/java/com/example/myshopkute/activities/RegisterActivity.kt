package com.example.myshopkute.activities

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.TextUtils
import android.view.WindowInsets
import android.view.WindowManager
import android.widget.Toast
import com.example.myshopkute.R
import com.example.myshopkute.databinding.ActivityLoginBinding
import com.example.myshopkute.databinding.ActivityRigisterBinding
import com.example.myshopkute.firestore.FirestoreClass
import com.example.myshopkute.models.User
import com.google.android.gms.tasks.OnCompleteListener
import com.google.firebase.auth.AuthResult
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser

class RegisterActivity : BaseActivity() {
    private lateinit var binding: ActivityRigisterBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityRigisterBinding.inflate(layoutInflater)
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
        setupActionBar()
        binding.tvLogin.setOnClickListener{
            onBackPressed()
        }
        binding.btnRegister.setOnClickListener{
            registerUser()
        }
    }

    private fun setupActionBar(){
        setSupportActionBar(binding.toolbarRegisterActivity)

        val actionBar = supportActionBar
        if (actionBar != null){
            actionBar.setDisplayHomeAsUpEnabled(true)
            actionBar.setHomeAsUpIndicator(R.drawable.ic_black_back_ios_24)

        }
        binding.toolbarRegisterActivity.setNavigationOnClickListener { onBackPressed() }
    }

    private fun validateRegisterDetail(): Boolean{
        return when {
            TextUtils.isEmpty(binding.etFirstname.text.toString().trim(){it <= ' '}) ->{
                 showErrorSnackBar(resources.getString(R.string.err_msg_enter_firstname), errorMessage = true)
                 false
             }

            TextUtils.isEmpty(binding.etLastname.text.toString().trim(){it <= ' '}) ->{
                showErrorSnackBar(resources.getString(R.string.err_msg_enter_lastname), errorMessage = true)
                false
            }

            TextUtils.isEmpty(binding.etEmail.text.toString().trim(){it <= ' '}) ->{
                showErrorSnackBar(resources.getString(R.string.err_msg_enter_email), errorMessage = true)
                false
            }

            TextUtils.isEmpty(binding.etPassword.text.toString().trim(){it <= ' '}) ->{
                showErrorSnackBar(resources.getString(R.string.err_msg_enter_password), errorMessage = true)
                false
            }

            TextUtils.isEmpty(binding.etConfirmPassword.text.toString().trim(){it <= ' '}) ->{
                showErrorSnackBar(resources.getString(R.string.err_msg_enter_confirmpassword), errorMessage = true)
                false
            }

            binding.etPassword.text.toString().trim { it <= ' ' }!= binding.etConfirmPassword.text.toString().
            trim { it <= ' ' } -> {
               showErrorSnackBar(resources.getString(R.string.err_msg_pass_and_cfpass), errorMessage = true)
               false
           }

            !binding.cbTermAndCondition.isChecked -> {
                showErrorSnackBar(resources.getString(R.string.err_msg_agree_terms_and_condition), errorMessage = true)
                false
            }


            else -> {
               // showErrorSnackBar(resources.getString(R.string.registery_successful), errorMessage = false)
                true
            }
        }
    }

    private fun registerUser(){
        // Check with validate function if the entries are valid or not
        if(validateRegisterDetail()){
            val email: String = binding.etEmail.text.toString().trim{it <= ' '}
            val password: String = binding.etPassword.text.toString().trim{it <= ' '}
            showProgressDialog(resources.getString(R.string.please_wait))
            // Create an instance and create a register a user with email and password
            FirebaseAuth.getInstance().createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(
                    OnCompleteListener<AuthResult> {
                        task ->

                        // If the registration is successfully done
                        if(task.isSuccessful){
                            // Firebase registered user
                            val firebaseUser: FirebaseUser = task.result!!.user!!
                            val user = User(
                                firebaseUser.uid,
                                binding.etFirstname.text.toString().trim{it <= ' '},
                                binding.etLastname.text.toString().trim{it <= ' '},
                                binding.etEmail.text.toString().trim{it <= ' '}
                            )

                            FirestoreClass().registerUser(this@RegisterActivity, user)
                            //FirebaseAuth.getInstance().signOut()
                            //finish()
                            showErrorSnackBar("you are registerly successful", false)
                        }else{
                            hideProgressDialog()
                            // If the registering is not succesful then show error
                            showErrorSnackBar(task.exception!!.message.toString(), errorMessage = true)
                        }
                    }
                )
        }
    }
    fun  userRegistrationSuccess(){
        // Hide the progress dialog
        hideProgressDialog()
        Toast.makeText(
            this@RegisterActivity,
            resources.getString(R.string.register_success),
            Toast.LENGTH_SHORT
        )
    }
}