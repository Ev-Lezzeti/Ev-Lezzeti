package com.example.evlezzeti.data.entity

import java.io.Serializable

data class Users(var email : String? = "",
                 var isVerified: Boolean? = null,
                 var uid : String? = "") :Serializable
