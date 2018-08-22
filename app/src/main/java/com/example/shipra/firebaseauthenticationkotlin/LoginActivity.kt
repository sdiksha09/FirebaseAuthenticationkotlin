package com.example.shipra.firebaseauthenticationkotlin

/*
created by Diksha Sharma on 8/8/2018

 */

import android.app.ProgressDialog
import android.content.Intent
import android.nfc.Tag
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.TextUtils
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import kotlinx.android.synthetic.main.activity_login.*

class LoginActivity : AppCompatActivity() {
    //UI elements
    private var tvForgotPassword: TextView? = null
    private var et2Email: EditText? = null
    private var et2Password: EditText? = null
    private var btLogin: Button? = null
    private var btnCreateAccount:Button?=null
    private var mProgressBar: ProgressDialog? = null

    //sign in UI elements

    //private var btTwitterLogin:TwitterLoginButton=null

    private val TAG = "LoginActivity"

    //request code

    val GOOGLE_LOG_IN_RC = 1
    val FACEBOOK_LOG_IN_RC = 2
    val TWITTER_LOG_IN_RC = 3

    //google API client Object


    // firebase references
    private var mDatabaseReference: DatabaseReference? = null
    private var mDatabse: FirebaseDatabase? = null
    private var mAuth: FirebaseAuth? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        //initialise
        et2Email = findViewById<View>(R.id.edt_email) as EditText
        et2Password = findViewById<View>(R.id.edt_password) as EditText
        btLogin = findViewById<View>(R.id.btn_login) as Button
        btnCreateAccount=findViewById<View>(R.id.btn2_createAccount) as Button

        //initialise sign in Buttons


       //btTwitterLogin=findViewById<View>(R.id.bt_loginWithtwitter) as Button


        tvForgotPassword=findViewById<View>(R.id.txt_forgot_password) as TextView
        mProgressBar = ProgressDialog(this)

        //firebase initialization
        mDatabse = FirebaseDatabase.getInstance()
        mDatabaseReference = mDatabse!!.reference!!.child("Users").push()
        mAuth = FirebaseAuth.getInstance()


         btnCreateAccount!!.setOnClickListener {

             val intent = Intent(this@LoginActivity, CreateAccountActivity::class.java)
             startActivity(intent)
         }

         btLogin!!.setOnClickListener {

             logIn(et2Email!!.text.toString(), et2Password!!.text.toString())

         }
      //googleSignIn




         tvForgotPassword!!.setOnClickListener {

             val intent=Intent(this@LoginActivity,ForgotPasswordActivity::class.java)
             startActivity(intent)
         }

         
     }


    //login function
    private fun logIn(email:String, password:String){

        Log.e(TAG,"Login"+email)
        if (!validateForm(email, password)) {
            return
        }

        mAuth!!.signInWithEmailAndPassword(email,password)
                .addOnCompleteListener(this){task ->

                    if (task.isSuccessful){
                        Log.e(TAG,"Sign in Success")
                        Toast.makeText(applicationContext,"sign in successful",Toast.LENGTH_SHORT).show()


                        //update UI with sign in Information
                        val user = mAuth!!.currentUser
                        updateUIuser(user)
                    }else{
                        Log.e(TAG,"Sign in Fail")
                        Toast.makeText(applicationContext,"Authentication fail",Toast.LENGTH_SHORT).show()
                        updateUIuser(null)

                    }
                    if(!task.isSuccessful){


                }
                }
    }

    private fun updateUIuser(user: FirebaseUser?) {

        val intent=Intent(this@LoginActivity,UserProfile::class.java)
        startActivity(intent)



    }

    private fun validateForm(email: String, password: String): Boolean {

        if (TextUtils.isEmpty(email)) {
            Toast.makeText(applicationContext, "Enter email address!", Toast.LENGTH_SHORT).show()
            return false
        }

        if (TextUtils.isEmpty(password)) {
            Toast.makeText(applicationContext, "Enter password!", Toast.LENGTH_SHORT).show()
            return false
        }

        if (password.length < 6) {
            Toast.makeText(applicationContext, "Password too short, enter minimum 6 characters!", Toast.LENGTH_SHORT).show()
            return false
        }

        return true
    }
}
