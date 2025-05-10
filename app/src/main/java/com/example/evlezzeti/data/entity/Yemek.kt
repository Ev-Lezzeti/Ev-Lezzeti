package com.example.evlezzeti.data.entity

import java.io.Serializable

data class Yemek(
    var mutfakAd:String? ="",
    var mutfakId:String? ="",
    var yemekId:String? ="",
    val yemekDetay:String? ="",
    val yemekFiyat:String? ="",
    val yemekAd:String?="",
    val yemekResim:String?=""
): Serializable