package com.example.myshopkute.activities

import android.app.Activity
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Bundle
import android.text.TextUtils
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.example.myshopkute.R
import com.example.myshopkute.databinding.ActivityUserProfileBinding
import com.example.myshopkute.firestore.FirestoreClass
import com.example.myshopkute.models.User
import com.example.myshopkute.units.Constants
import com.example.myshopkute.units.GlideLoader
import java.io.IOException

class UserProfileActivity : BaseActivity(), View.OnClickListener {
    private lateinit var mUserDetails: User

    // Add a global variable for URI of a selected image from phone storage.
    private var mSelectedImageFileUri: Uri? = null

    private var mUserProfileImageURL: String = ""
    private lateinit var binding: ActivityUserProfileBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityUserProfileBinding.inflate(layoutInflater)
        setContentView(binding.root)


        if(intent.hasExtra(Constants.EXTRA_USER_DETAILS)){
            // Get user details from intent as a ParcelableExtra
            mUserDetails = intent.getParcelableExtra(Constants.EXTRA_USER_DETAILS)!!
        }
        if (mUserDetails.profileCompleted == 0) {
            // Update the title of the screen to complete profile.
            binding.tvTitle.text = resources.getString(R.string.title_complete_profile)

            // Here, the some of the edittext components are disabled because it is added at a time of Registration.
            binding.etFirstname.isEnabled = false
            binding.etFirstname.setText(mUserDetails.firstName)

            binding.etLastname.isEnabled = false
            binding.etLastname.setText(mUserDetails.lastName)

            binding.etEmail.isEnabled = false
            binding.etEmail.setText(mUserDetails.email)
        } else {

            // Call the setup action bar function.
            setupActionBar()

            // Update the title of the screen to edit profile.
            binding.tvTitle.text = resources.getString(R.string.title_edit_profile)

            // Load the image using the GlideLoader class with the use of Glide Library.
            GlideLoader(this@UserProfileActivity).loadUserPicture(mUserDetails.image, binding.ivUserPhoto)

            // Set the existing values to the UI and allow user to edit except the Email ID.
            binding.etFirstname.setText(mUserDetails.firstName)
            binding.etFirstname.setText(mUserDetails.lastName)

            binding.etEmail.isEnabled = false
            binding.etEmail.setText(mUserDetails.email)

            if (mUserDetails.mobile != 0L) {
                binding.etMobileNumber.setText(mUserDetails.mobile.toString())
            }
            if (mUserDetails.gender == Constants.MALE) {
                binding.rbMale.isChecked = true
            } else {
                binding.rbFemale.isChecked = true
            }
        }
        // END

        // Assign the on click event to the user profile photo.
        binding.ivUserPhoto.setOnClickListener(this@UserProfileActivity)
        // Assign the on click event to the SAVE button.
        binding.btnSave.setOnClickListener(this@UserProfileActivity)
    }

    override fun onClick(v: View?) {
        if(v != null){
            when(v.id){
                binding.ivUserPhoto.id -> {
                    // Here we will check if the permission is already allowed or we need to request for it
                    // First off all we will check the READ_EXTERNAL_STRORAGE permission and ifit not allowed we
                    if (ContextCompat.checkSelfPermission(
                            this, android.Manifest.permission.READ_EXTERNAL_STORAGE
                        ) == PackageManager.PERMISSION_GRANTED
                    ) {
                        //showErrorSnackBar("You already have the storage permissio", false)
                        Constants.showImageChooser(this@UserProfileActivity)
                    }else {
                        //Request permission to be granted to this application. There permissions must be requeted in
                        //your manifest, they should not be granted to your app, and they should have protection level
                        ActivityCompat.requestPermissions(
                            this,
                            arrayOf(android.Manifest.permission.READ_EXTERNAL_STORAGE),
                            Constants.READ_STORAGE_PERMISSION_CODE
                        )
                    }
                }
                binding.btnSave.id -> {

                    if (validateUserProfileDetails()) {
                       // showErrorSnackBar("Your detail are valid, you can update them.", false)

                         // Show the progress dialog.
                        showProgressDialog(resources.getString(R.string.please_wait))

                        if (mSelectedImageFileUri != null) {

                            FirestoreClass().uploadImageToCloudStorage(
                                this@UserProfileActivity,
                                mSelectedImageFileUri, Constants.USER_PROFILE_IMAGE
                            )
                        } else {


                        }
                        updateUserProfileDetails()
                   }
                }
            }

        }
    }
    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out  String>,
        grantResults: IntArray
    ){
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        // If permission is granted
        if (grantResults.isNotEmpty()&& grantResults[0]== PackageManager.PERMISSION_GRANTED){
            //showErrorSnackBar("The storage permission is granted.", false)
            Constants.showImageChooser(this@UserProfileActivity)
        }else{
            //Displaying another toast if permission is not granted
            Toast.makeText(
                this,
                resources.getString(R.string.read_storage_permission_denied),
                Toast.LENGTH_LONG
            ).show()
        }

    }
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?){
        super.onActivityResult(requestCode, resultCode, data)
        if(resultCode == Activity.RESULT_OK){
            if(requestCode == Constants.PICK_IMAGE_REQUEST_CODE){
                if (data != null){
                    try {
                        //the uri selected image from phone storage
                         mSelectedImageFileUri = data.data!!
                        // binding.ivUserPhoto.setImageURI(selectedImageFileUri)
                        GlideLoader(this@UserProfileActivity).loadUserPicture(mSelectedImageFileUri!!, binding.ivUserPhoto)
                    }catch (e: IOException){
                        e.printStackTrace()
                        Toast.makeText(
                            this@UserProfileActivity,
                            resources.getString(R.string.image_selection_failed),
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                }
            }
        }else if(resultCode == Activity.RESULT_CANCELED){
            // A log is printed when user close or cancel the image selection
            Log.e("Request Cancelled", "Image selection cancelled")
        }

    }
    private fun validateUserProfileDetails(): Boolean {
        return when {

            // We have kept the user profile picture is optional.
            // The FirstName, LastName, and Email Id are not editable when they come from the login screen.
            // The Radio button for Gender always has the default selected value.

            // Check if the mobile number is not empty as it is mandatory to enter.
            TextUtils.isEmpty(binding.etMobileNumber.text.toString().trim { it <= ' ' }) -> {
                showErrorSnackBar(resources.getString(R.string.err_msg_enter_mobile_number), true)
                false
            }
            else -> {
                true
            }
        }
    }
    private fun updateUserProfileDetails() {

        val userHashMap = HashMap<String, Any>()
        // TODO Step 5: Update the code if user is about to Edit Profile details instead of Complete Profile.
        // Get the FirstName from editText and trim the space
        val firstName = binding.etFirstname.text.toString().trim { it <= ' ' }
        if (firstName != mUserDetails.firstName) {
            userHashMap[Constants.FIRST_NAME] = firstName
        }

        // Get the LastName from editText and trim the space
        val lastName = binding.etLastname.text.toString().trim { it <= ' ' }
        if (lastName != mUserDetails.lastName) {
            userHashMap[Constants.LAST_NAME] = lastName
        }
        // Here the field which are not editable needs no update. So, we will update user Mobile Number and Gender for now.

        // Here we get the text from editText and trim the space
        val mobileNumber = binding.etMobileNumber.text.toString().trim { it <= ' ' }

        val gender = if (binding.rbMale.isChecked) {
            Constants.MALE
        } else {
            Constants.FEMALE
        }

        if (mUserProfileImageURL.isNotEmpty()) {
            userHashMap[Constants.IMAGE] = mUserProfileImageURL
        }

        if (mobileNumber.isNotEmpty() && mobileNumber != mUserDetails.mobile.toString()) {
            userHashMap[Constants.MOBILE] = mobileNumber.toLong()
        }

        if (gender.isNotEmpty() && gender != mUserDetails.gender) {
            userHashMap[Constants.GENDER] = gender
        }

        // 0: User profile is incomplete.
        // 1: User profile is completed.
        userHashMap[Constants.COMPLETE_PROFILE] = 1

        // call the registerUser function of FireStore class to make an entry in the database.
        FirestoreClass().updateUserProfileData(
            this@UserProfileActivity,
            userHashMap
        )
    }
    fun userProfileUpdateSuccess() {

        // Hide the progress dialog
        hideProgressDialog()

        Toast.makeText(
            this@UserProfileActivity,
            resources.getString(R.string.msg_profile_update_success),
            Toast.LENGTH_SHORT
        ).show()


        // Redirect to the Main Screen after profile completion.
        startActivity(Intent(this@UserProfileActivity, DashboardActivity::class.java))
        finish()
    }
    fun imageUploadSuccess(imageURL: String) {

        mUserProfileImageURL = imageURL

        updateUserProfileDetails()
    }
    private fun setupActionBar() {

        setSupportActionBar(binding.toolbarUserProfileActivity)

        val actionBar = supportActionBar
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true)
            actionBar.setHomeAsUpIndicator(R.drawable.ic_white_color_back_24dp)
        }

        binding.toolbarUserProfileActivity.setNavigationOnClickListener { onBackPressed() }
    }
}