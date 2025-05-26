package com.example.evlezzeti.ui.viewmodel

import androidx.lifecycle.ViewModel
import com.example.evlezzeti.data.entity.Kullanici
import com.example.evlezzeti.data.repo.Repository
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class HaritaIslemleriViewModel @Inject constructor (var rep : Repository): ViewModel() {

    fun kullaniciKaydet(kullanici: Kullanici){
        rep.kullaniciKaydet(kullanici)
    }

    fun kullaniciGuncelle(kullanici: Kullanici){
        rep.kullaniciGuncelle(kullanici)
    }
}