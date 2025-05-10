package com.example.evlezzeti.ui.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.evlezzeti.data.entity.Sepet

class SharedSepetViewModel :ViewModel(){
    private val _sepetListesi = mutableListOf<Sepet>() //Sepete YemekDetaydan yemek eklemek icin
    val sepetListesi: List<Sepet> get() = _sepetListesi
    private val _toplamFiyat = MutableLiveData<Double>()
    val toplamFiyat: LiveData<Double> get() = _toplamFiyat

    private val _fiyat = mutableListOf<Double>() //Sepete YemekDetaydan fiyat eklemek icin
    val fiyat: List<Double> get() = _fiyat

    fun yemekFiyatEkle(fiyat: Double) {
        _fiyat.add(fiyat)
    }

    fun yemekFiyatlariTemizle() {
        _fiyat.clear()
    }
    fun yemekFiyatGuncelle(index: Int, yeniFiyat: Double) {
        if (index in _fiyat.indices) {
            _fiyat[index] = yeniFiyat
        }
    }
    fun hesaplaToplamFiyat() {
        _toplamFiyat.value = _fiyat.sum()
    }


    fun sepetEkle(sepet:Sepet) {
        _sepetListesi.add(sepet)
    }

    fun sepetListeTemizle() {
        _sepetListesi.clear()
    }
    fun yemekVeFiyatSil(index: Int) {
        if (index in _sepetListesi.indices) {
            _sepetListesi.removeAt(index)
            _fiyat.removeAt(index)
        }
    }


}