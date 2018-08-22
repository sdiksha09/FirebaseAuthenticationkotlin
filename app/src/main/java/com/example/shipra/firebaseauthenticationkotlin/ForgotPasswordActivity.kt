package com.example.shipra.firebaseauthenticationkotlin
/*
created by diksha sharma 8/8/2018
 */

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.TextUtils
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth

class ForgotPasswordActivity : AppCompatActivity() {

    // declare variables for UI elements
    private val TAG="ForgotPasswordActivity"

    private var etEmail:EditText?=null
    private var btSendPassword:Button?=null

    //firebase references
    private var mAuth:FirebaseAuth?=null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_forgot_password)

        etEmail=findViewById(R.id.edt_email_reset_pass) as EditText
        btSendPassword=findViewById(R.id.btn_resetPassword)


        mAuth =FirebaseAuth.getInstance()

        btSendPassword!!.setOnClickListener {
            sendPasswordResetEmail()
        }




     }

    private fun sendPasswordResetEmail() {
        val email =etEmail?.text.toString()

        if(!TextUtils.isEmpty(email)){

            mAuth!!.sendPasswordResetEmail(email)
                    .addOnCompleteListener { task ->
                        if(task.isSuccessful){

                            Log.e(TAG,"send password: successful")
                            Toast.makeText(this,"send email ",Toast.LENGTH_SHORT).show()
                            updateUI()
                        }else{
                       Log.w(TAG,task.exception!!.message)
                            Toast.makeText(this@ForgotPasswordActivity,"email not Found",Toast.LENGTH_SHORT).show()
                        }
                    }
        }else{
            Toast.makeText(this@ForgotPasswordActivity,"Enter Email",Toast.LENGTH_SHORT).show()
        }
    }

    private fun updateUI() {
        val intent= Intent(this@ForgotPasswordActivity,LoginActivity::class.java)
        startActivity(intent)
    }
}
