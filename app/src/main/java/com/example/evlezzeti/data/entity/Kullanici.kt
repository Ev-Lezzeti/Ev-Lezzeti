package com.example.evlezzeti.data.entity

import java.io.Serializable

data class Kullanici(
    var kullaniciId:String? ="",
    var ePosta:String? ="",
    var kullaniciTelefon:String? ="",
    var kullaniciAd:String? ="",
    var kullaniciAdress:String? ="",
    var kullaniciEnlem:String? ="",
    var kullaniciBoylam:String? ="",
    var favoriler:String? ="",):Serializable
