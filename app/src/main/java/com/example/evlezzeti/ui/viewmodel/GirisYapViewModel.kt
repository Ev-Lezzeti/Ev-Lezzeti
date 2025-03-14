package com.example.evlezzeti.ui.viewmodel

import android.app.Application
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.ViewModel
import com.example.evlezzeti.data.repo.Repository
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class GirisYapViewModel @Inject constructor (var rep : Repository): ViewModel() {


    fun kullaniciGirisKontrol(ePosta:String, sifre:String): Boolean{

        val durum = rep.kullaniciGirisKontrol(ePosta,sifre)

        return durum
    }
}