package com.example.evlezzeti.data.entity

import java.io.Serializable

data class Sepet(
    var yemekBirimFiyat:String? ="",
    var mutfakAd:String? ="",
    var mutfakId:String? ="",
    var yemekId:String? ="",
    var yemekFiyat:String? ="",
    var yemekAd:String?="",
    var yemekAdet:String?="",
    var yemekNot:String?="",
): Serializable
