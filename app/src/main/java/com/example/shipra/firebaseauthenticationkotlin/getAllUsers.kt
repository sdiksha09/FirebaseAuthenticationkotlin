package com.example.shipra.firebaseauthenticationkotlin

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView


import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.FirebaseDatabase
import android.app.ProgressDialog
import android.content.Context
import com.google.firebase.database.DatabaseReference


class getAllUsers : AppCompatActivity() {


    internal lateinit var databaseReference: DatabaseReference

    internal lateinit var progressDialog: ProgressDialog

    internal var list: MutableList<User> = ArrayList()

    internal lateinit var recyclerView: RecyclerView

    internal lateinit var adapter: RecyclerView.Adapter<*>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_get_all_users)

        recyclerView = findViewById(R.id.rv_AllUser) as RecyclerView

        recyclerView.setHasFixedSize(true)

        recyclerView.layoutManager = LinearLayoutManager(this@getAllUsers)

        progressDialog = ProgressDialog(this@getAllUsers)

        progressDialog.setMessage("Loading Data from Firebase Database")

        progressDialog.show()

        databaseReference = FirebaseDatabase.getInstance().getReference("Users")

        databaseReference.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {

                for (dataSnapshot in snapshot.children) {

                    val user = dataSnapshot.getValue<User>(User::class.java!!)

                    list.add(user!!)
                }



              adapter = UserAdapter(list,baseContext)

                recyclerView.adapter = adapter

                progressDialog.dismiss()
            }

            override fun onCancelled(databaseError: DatabaseError) {

                progressDialog.dismiss()

            }
        })

    }
}



