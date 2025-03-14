package com.example.evlezzeti.data.entity

import java.io.Serializable

data class Mutfak(
    var mutfakId:String? ="",
    val mutfakAd:String?="",
    val mutfakTelefon:String? ="",
    val mutfakKonum:String? ="",
    val mutfakPuan:String? ="",
    val mutfakDetay:String? ="",
    val mutfakAdres:String? ="",
    val mutfakResim:String? ="",
    val mutfakIndirimTag:String?=""
):Serializable
