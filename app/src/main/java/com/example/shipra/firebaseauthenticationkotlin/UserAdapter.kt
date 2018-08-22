package com.example.shipra.firebaseauthenticationkotlin

import android.content.Context
import androidx.recyclerview.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import com.bumptech.glide.Glide
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.list_layout.view.*


class UserAdapter(item: MutableList<User>, ctx:Context):RecyclerView.Adapter<UserAdapter.ViewHolder>()//   creating a constructor inside the Adapter class as parameter
{

    var List = item
    var context = ctx

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        //set the view holder
        return ViewHolder(LayoutInflater.from(context).inflate(R.layout.list_layout, parent, false))


    }

    override fun getItemCount(): Int {

        // this has to return ItemCount

        return List.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        // to bind this view need to set the text

       //puting a ?mark after holder and name to check it is null or not
        var userDto = List[position]
        holder?.id?.text = userDto.Id
        holder?.fname?.text = userDto.firstname
        holder?.lname.text = userDto.lastname
        Glide.with(context).load(userDto.profileimageurl.toString()).into(holder.img)  //for retrive image from firebase database



    }
    //Creating a view Holder inside this class

    //step: 4 creating a constructon with view as a parameter and extand the parent class RecyclerView
    class ViewHolder(v: View) : RecyclerView.ViewHolder(v) {

        val id = v.user_id
        val fname=v.user_name
        val lname=v.user_Lname
     var img =v.user_img//inside this creating a variable


    }



}