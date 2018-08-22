package com.example.shipra.firebaseauthenticationkotlin

import android.annotation.TargetApi
import android.app.Activity
import android.app.ProgressDialog
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.drawable.BitmapDrawable
import android.media.Image
import android.net.Uri
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.core.content.ContextCompat
import com.bumptech.glide.Glide
import com.example.shipra.firebaseauthenticationkotlin.LoginActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.database.*
import kotlinx.android.synthetic.main.activity_user_profile.*
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import com.squareup.picasso.Picasso
import java.util.*

class UserProfile : AppCompatActivity() {

    lateinit var context:Context

    //Firebase references
    private var mDatabaseReference: DatabaseReference? = null
    private var storageRef:StorageReference?=null
    private var mDatabase: FirebaseDatabase? = null
    private var mAuth: FirebaseAuth? = null
    private var image:ImageView?=null

    //UI elements
    private var tvFirstName: TextView? = null
    private var tvLastName: TextView? = null
    private var tvEmail: TextView? = null
    private var tvEmailVerifiied: TextView? = null
    private var btSignOut: Button? = null
    private var tvuserId:TextView?=null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_user_profile)
        // initialise
        initialise()
    }

    private fun initialise() {

            mDatabase = FirebaseDatabase.getInstance()
            mDatabaseReference = mDatabase!!.reference!!.child("Users")

            mAuth = FirebaseAuth.getInstance()


           // creating an instance of Firebase Storage
           val firebaseStorage = FirebaseStorage.getInstance()
           storageRef = firebaseStorage.reference.child("profileimageurl")


            tvFirstName = findViewById<View>(R.id.tv_firstName) as TextView
            tvLastName = findViewById<View>(R.id.tv_lastName) as TextView
            tvEmail = findViewById<View>(R.id.tv_email) as TextView
            tvEmailVerifiied = findViewById<View>(R.id.tv_emailVerifiied) as TextView
            btSignOut = findViewById<View>(R.id.btn_signout) as Button
           // image=  findViewById<View>(R.id.img_view_dp) as ImageView
            tvuserId=findViewById<View>(R.id.tv_userId) as TextView

          //signOut Button
           btSignOut!!.setOnClickListener {

               mAuth!!.signOut()

               val intent = Intent(this@UserProfile,LoginActivity::class.java)
               startActivity(intent)
           }

         // to choose image from device
           img_view_dp.setOnClickListener {

               val intent =Intent(Intent.ACTION_PICK)
               intent.type="image/*"
               startActivityForResult(intent,0)
           }
    }

    var selectedPhotoUri:Uri?=null

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if(requestCode==0 && resultCode== Activity.RESULT_OK && data!=null){
            //proceed and check what the selected image was...
            Log.d("UserProfile","photo was selected")
            selectedPhotoUri=data.data

            val bitmap = MediaStore.Images.Media.getBitmap(contentResolver,selectedPhotoUri)
            val bitmapDrawable =BitmapDrawable(bitmap)
            img_view_dp.setBackgroundDrawable(bitmapDrawable)
        }
    }

    //store User data in to firebase database
    override fun onStart() {
        super.onStart()

        val mUser = mAuth!!.currentUser
        val mUserReference = mDatabaseReference!!.child(mUser!!.uid)

        tvEmail!!.text = mUser.email
        tvEmailVerifiied!!.text = mUser.isEmailVerified.toString()
       // val storage = FirebaseStorage.getInstance()
       // val storageReference = storage.getReference()

        uploadImageToFirebaseStorage()

        mUserReference.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                tvFirstName!!.text = snapshot.child("firstname").value as String
                tvLastName!!.text = snapshot.child("lastname").value as String
                tvuserId!!.text =snapshot.child("id").value as String
             // Glide.with(this@UserProfile).load(snapshot.child("profileimageurl").toString()).into(image)


            }

            override fun onCancelled(databaseError: DatabaseError) {}
        })


    }
     //upload selected image in to firebase database
    private fun uploadImageToFirebaseStorage() {

        if (selectedPhotoUri == null) return

        val filename = UUID.randomUUID().toString()
        val ref = FirebaseStorage.getInstance().getReference("/image/$filename")

        ref.putFile(selectedPhotoUri!!).addOnSuccessListener {

            Log.d("UserProfile", "successfully uploaded image:${it.metadata?.path}")
            Toast.makeText(this, "image uploaded ", Toast.LENGTH_SHORT).show()
            ref.downloadUrl.addOnSuccessListener {
                Log.d("User_Profile", "FileLocation: $it")

                val ImageUploadId = "$it"

                //  val ImageUploadId = mDatabaseReference!!.push().getKey()
                // saveImageToFireBaseDatabase(it.toString())


                val uid: String = tvuserId.toString()


                mDatabase!!.reference!!.child("Users").child(mAuth!!.currentUser!!.uid).child("profileimageurl").setValue(ImageUploadId)
                Toast.makeText(applicationContext, "image saved successfully", Toast.LENGTH_SHORT).show()


            }

        }

    }

    }









