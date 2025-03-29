package com.example.evlezzeti.ui.viewmodel

import androidx.lifecycle.ViewModel
import com.example.evlezzeti.data.repo.Repository
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class KullaniciVerifiedViewModel @Inject constructor (var rep : Repository): ViewModel() {

    fun otpGuncelle(ePosta:String) : Boolean{
        val durum = rep.otpGuncelle(ePosta)
        return durum
    }
}