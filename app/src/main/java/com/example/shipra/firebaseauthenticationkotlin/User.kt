package com.example.shipra.firebaseauthenticationkotlin

import com.google.firebase.database.Exclude

class User{


    //user model class

    var Id:String?=null
    var firstname:String?=null
    var lastname:String?=null
    var profileimageurl:String?=null

    constructor(){}

    constructor(Id: String?, firstname: String?, lastname: String?, profileimageurl: String?) {
        this.Id = Id
        this.firstname = firstname
        this.lastname = lastname
        this.profileimageurl = profileimageurl
    }


}