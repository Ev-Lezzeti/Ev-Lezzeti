package com.example.evlezzeti.ui.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.evlezzeti.data.entity.Kullanici
import com.example.evlezzeti.data.entity.Siparis
import com.example.evlezzeti.data.repo.Repository
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class OdemeViewModel @Inject constructor (var rep : Repository): ViewModel() {

    private val _kullanici = MutableLiveData<Kullanici?>()
    val kullanici: LiveData<Kullanici?> get() = _kullanici

    fun kullaniciAl(kullaniciId: String) {
        rep.kullaniciAl(kullaniciId) { result ->
            _kullanici.postValue(result)
        }
    }

    fun siparisEkle(siparis: Siparis){
        rep.siparisEkle(siparis)
    }
}