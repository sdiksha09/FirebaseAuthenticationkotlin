package com.example.shipra.firebaseauthenticationkotlin
/*
created by diksha sharma 7/8/2018
 */

import android.app.ProgressDialog
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.TextUtils
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser

import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import kotlinx.android.synthetic.main.activity_create_account.*

class CreateAccountActivity : AppCompatActivity() {

    // define variable for UI elemnt
    private var etFirstName: EditText? = null
    private var etLastName: EditText? = null
    private var etEmail: EditText? = null
    private var etPassword: EditText? = null
    private var btCreateAccount: Button? = null
    private var mProgressBar: ProgressDialog? = null


    // firebase references
    private var mDatabaseReference: DatabaseReference? = null
    private var mDatabse: FirebaseDatabase? = null
    private var mAuth: FirebaseAuth? = null

    private val TAG = "CreateAccountActivity"

    //globle variable

    private var firstName: String? = null
    private var lastName: String? = null
    private var email: String? = null
    private var password: String? = null
    private var profileImage:String?=null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_create_account)

        initialise()

        //initialise all references

    }

    private fun initialise() {

        etFirstName = findViewById<View>(R.id.edt_firstName) as EditText
        etLastName = findViewById<View>(R.id.edt_lastName) as EditText
        etEmail = findViewById<View>(R.id.edt_email) as EditText
        etPassword = findViewById<View>(R.id.edt_password) as EditText
        btCreateAccount = findViewById<View>(R.id.btn_createAccount) as Button
        mProgressBar = ProgressDialog(this)

        mDatabse = FirebaseDatabase.getInstance()
        mDatabaseReference = mDatabse!!.getReference().child("Users")

        mAuth = FirebaseAuth.getInstance()


        btn_createAccount!!.setOnClickListener {

            createAccount()
        }
    }

    private fun createAccount() {

        firstName = etFirstName?.text.toString()
        lastName = etLastName?.text.toString()
        email = etEmail?.text.toString()
        password = etPassword?.text.toString()
        profileImage="image"


        if (!TextUtils.isEmpty(firstName) && !TextUtils.isEmpty(lastName)
                && !TextUtils.isEmpty(email) && !TextUtils.isEmpty(password)) {


            mProgressBar!!.setMessage("Registering User...")
            mProgressBar!!.show()



            mAuth!!
                    .createUserWithEmailAndPassword(email!!, password!!)
                    .addOnCompleteListener(this) { task ->
                        mProgressBar!!.hide()
                        if (task.isSuccessful) {
                            // Sign in success, update UI with the signed-in user's information
                            Log.d(TAG, "createUserWithEmail:success")
                            val userId = mAuth!!.currentUser!!.uid
                            //Verify Email
                            verifyEmail();
                            //update user profile information
                            val currentUserDb = mDatabaseReference!!.child(userId)

                           /* val ref = FirebaseDatabase.getInstance().getReference("userData")
                            val userId =ref.push().key

                            val user = User(userId!!,firstName ,lastName ,Address,Department)*/

                            val user = User(userId, firstName!!, lastName!!,profileImage!!)

                            mDatabaseReference!!.child(userId).setValue(user)
                                Toast.makeText(applicationContext,"data saved successfully",Toast.LENGTH_SHORT).show()



                           // currentUserDb.child("firstName").setValue(firstName)
                           // currentUserDb.child("lastName").setValue(lastName)
                            updateUserInfoAndUI()
                        } else {
                            // If sign in fails, display a message to the user.
                            Log.w(TAG, "createUserWithEmail:failure", task.exception)
                            Toast.makeText(this@CreateAccountActivity, "Authentication failed.",
                                    Toast.LENGTH_SHORT).show()
                        }
                    }



        } else {
            Toast.makeText(this, "Enter all details", Toast.LENGTH_SHORT).show()
        }

        }

    private fun verifyEmail() {


        val mUser = mAuth!!.currentUser;
        mUser!!.sendEmailVerification()
                .addOnCompleteListener(this) { task ->
                    if (task.isSuccessful) {
                        Toast.makeText(this@CreateAccountActivity,
                                "Verification email sent to " + mUser.getEmail(),
                                Toast.LENGTH_SHORT).show()
                    } else {
                        Log.e(TAG, "sendEmailVerification", task.exception)
                        Toast.makeText(this@CreateAccountActivity,
                                "Failed to send verification email.",
                                Toast.LENGTH_SHORT).show()
                    }
                }
    }

    private fun updateUserInfoAndUI() {

        //start next activity
        val intent = Intent(this@CreateAccountActivity, LoginActivity::class.java)
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
        startActivity(intent)

    }


}







