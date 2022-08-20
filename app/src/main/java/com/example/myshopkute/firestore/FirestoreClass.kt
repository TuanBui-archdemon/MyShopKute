package com.example.myshopkute.firestore

import android.app.Activity
import android.content.Context
import android.content.SharedPreferences
import android.net.Uri
import android.util.Log
import com.example.myshopkute.activities.*
import com.example.myshopkute.models.Produces
import com.example.myshopkute.models.User
import com.example.myshopkute.units.Constants
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.SetOptions
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference

class FirestoreClass {
    private  val mFirestore = FirebaseFirestore.getInstance()

    fun registerUser(activity: RegisterActivity, userInfo: User){
        // The "users" is the collection name. If the colection is already created then it will not crete the same
        mFirestore.collection(Constants.USERS)
            // Doccument for user fields. Here the doccument it is the User Id
            .document(userInfo.id)
            // Here the userInfo are field and the setOption is set to merge. It is for if we want to merge later ofn instead of replacing the fileds.
            .set(userInfo, SetOptions.merge())
            .addOnSuccessListener {
                // Here call a function of base activity for transferring the result to it.
                activity.userRegistrationSuccess()
                 }
            .addOnFailureListener{
                e -> activity.hideProgressDialog()
                Log.e(
                    activity.javaClass.simpleName,
                    "Error while registering the user.",
                    e
                )
            }
    }

    fun getCurrentUserID(): String {
        // An Instance of currentUser using FirebaseAuth
        val currentUser = FirebaseAuth.getInstance().currentUser
        // A variable to assign the currentId if it is not null or else it will be blank
        var currentUserID = ""
        if (currentUser != null){
            currentUserID = currentUser.uid
        }
        return currentUserID
    }

    fun getUserDetails(activity: Activity){
        // Here we pass the collection name from which we wants the data
        mFirestore.collection(Constants.USERS)
            // The document id to get the Fileds of user
            .document(getCurrentUserID())
            .get()
            .addOnSuccessListener { document ->
                Log.i(activity.javaClass.simpleName, document.toString())
                // Here we have recived the document snashot which is converted into the User Dât model object
                val user = document.toObject(User::class.java)!!

                val sharedPreferences = activity.getSharedPreferences(
                    Constants.MYSHOPKUTE_PREFERRENCES,
                    Context.MODE_PRIVATE
                )

                val editor: SharedPreferences.Editor = sharedPreferences.edit()
                //key: logged_in_username:Tuan Van
                //value:
                editor.putString(
                    Constants.LOGGED_IN_USERNAME,
                    "${user.firstName} ${user.lastName}"
                )
                // TODO STEP 6: PASS THE RESULT TO THE LOGIN ACTIVITY
                // START
                when(activity){
                    is LoginActivity -> {
                        // Call a function of base activity for transferring the result to it
                        activity.userLoggedInSuccess(user)
                    }
                    is SettingsActivity -> {
                        // Call a function of base activity for transferring the result to it.
                        activity.userDetailsSuccess(user)
                    }
                }
                // END
            }
            .addOnFailureListener {
                e ->
                // Hide the progress dialog iff there is any error. And print the error in log
                when(activity){
                    is LoginActivity -> {
                        activity.hideProgressDialog()
                    }
                    is SettingsActivity -> {
                        // Call a function of base activity for transferring the result to it.
                        activity.hideProgressDialog()
                    }
                }

                Log.e(
                    activity.javaClass.simpleName,
                    "error while getting user details",
                    e
                )
            }

    }

    fun updateUserProfileData(activity: Activity, userHashMap: HashMap<String, Any>) {
        // Collection Name
        mFirestore.collection(Constants.USERS)
            // Document ID against which the data to be updated. Here the document id is the current logged in user id.
            .document(getCurrentUserID())
            // A HashMap of fields which are to be updated.
            .update(userHashMap)
            .addOnSuccessListener {

                // Notify the success result.
                when (activity) {
                    is UserProfileActivity -> {
                        // Call a function of base activity for transferring the result to it.
                        activity.userProfileUpdateSuccess()
                    }

                }
            }
            .addOnFailureListener { e ->

                when (activity) {
                    is UserProfileActivity -> {
                        // Hide the progress dialog if there is any error. And print the error in log.
                        activity.hideProgressDialog()
                    }
                }

                Log.e(
                    activity.javaClass.simpleName,
                    "Error while updating the user details.",
                    e
                )
            }
    }
    // A function to upload the image to the cloud storage.
    fun uploadImageToCloudStorage(activity: Activity, imageFileURI: Uri?,  imageType: String) {

        //getting the storage reference
//        val sRef: StorageReference = FirebaseStorage.getInstance().reference.child(
//            imageType + System.currentTimeMillis() + "."
//                    + Constants.getFileExtension(
//                activity,
//                imageFileURI
//            )
//        )
        val sRef: StorageReference = FirebaseStorage.getInstance().reference.child(
            "${getCurrentUserID()}.jpg"
        )
        //adding the file to reference
        sRef.putFile(imageFileURI!!)
            .addOnSuccessListener { taskSnapshot ->
                // The image upload is success
                Log.e(
                    "Firebase Image URL",
                    taskSnapshot.metadata!!.reference!!.downloadUrl.toString()
                )

                // Get the downloadable url from the task snapshot
                taskSnapshot.metadata!!.reference!!.downloadUrl
                    .addOnSuccessListener { uri ->
                        Log.e("Downloadable Image URL", uri.toString())

                        // Here call a function of base activity for transferring the result to it.
                        when (activity) {
                            is UserProfileActivity -> {
                                activity.imageUploadSuccess(uri.toString())
                            }
                        }
                    }.addOnFailureListener { it.printStackTrace() }
            }
            .addOnFailureListener { exception ->
                exception.printStackTrace()
                // Hide the progress dialog if there is any error. And print the error in log.
                when (activity) {
                    is UserProfileActivity -> {
                        activity.hideProgressDialog()
                    }
                }

                Log.e(
                    activity.javaClass.simpleName,
                    exception.message,
                    exception
                )
            }
    }
    fun uploadProductDetails(activity: AddProducesActivity, productInfo: Produces) {

        mFirestore.collection(Constants.PRODUCTS)
            .document()
            // Here the userInfo are Field and the SetOption is set to merge. It is for if we wants to merge
            .set(productInfo, SetOptions.merge())
            .addOnSuccessListener {

                // Here call a function of base activity for transferring the result to it.
                activity.productUploadSuccess()
            }
            .addOnFailureListener { e ->

                activity.hideProgressDialog()

                Log.e(
                    activity.javaClass.simpleName,
                    "Error while uploading the product details.",
                    e
                )
            }
    }


}