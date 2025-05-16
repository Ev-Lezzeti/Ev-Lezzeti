package com.example.evlezzeti.ui.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.evlezzeti.data.repo.Repository
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
@HiltViewModel
class BottomNavSepetViewModel @Inject constructor(private val rep: Repository) : ViewModel() {

    private val _durum = MutableLiveData<Boolean>()
    val durum: LiveData<Boolean> get() = _durum

    fun kullaniciAdresKontrolWithCallback(kullaniciId: String, callback: (Boolean) -> Unit) {
        rep.kullaniciAdresKontrol(kullaniciId) { adresDurumu ->
            _durum.value = adresDurumu //MutableLiveData'ya atıyoruz
            callback(adresDurumu)
        }
    }

}