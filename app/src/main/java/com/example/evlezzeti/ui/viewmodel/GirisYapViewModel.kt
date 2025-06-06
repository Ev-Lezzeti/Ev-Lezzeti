package com.example.evlezzeti.ui.viewmodel

import androidx.lifecycle.ViewModel
import com.example.evlezzeti.data.repo.Repository
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class GirisYapViewModel @Inject constructor (var rep : Repository): ViewModel() {

    fun otpKontrol(ePosta:String) : Boolean {
        var otpDurum = rep.otpKontrol(ePosta)
        return otpDurum
    }
}