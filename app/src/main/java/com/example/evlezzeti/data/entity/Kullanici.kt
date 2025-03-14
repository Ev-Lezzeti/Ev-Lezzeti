package com.example.evlezzeti.data.entity

import java.io.Serializable

data class Kullanici(val kullaniciId:String? ="",
                     val ePosta:String? ="",
                     val sifre:String ?="",
                     val kullaniciTelefon:String? ="",
                     val kullaniciAd:String? ="",
                     val kullaniciAdress:String? ="",
                     val kullaniciKonum:String? ="",
                     val mutfakDurum:String? ="",
                     val favoriler:String? ="",):Serializable
