package com.example.evlezzeti.ui.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.evlezzeti.data.entity.Siparis
import com.example.evlezzeti.data.repo.Repository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class GecmisSiparislerViewModel  @Inject constructor (var rep : Repository): ViewModel() {

    private val _siparisListesi = MutableLiveData<List<Siparis>>()
    val siparisListesi: LiveData<List<Siparis>> get() = _siparisListesi

    fun siparisleriGetir(kullaniciId: String) {
        viewModelScope.launch {
            val liste = rep.tumSiparislerByKullaniciAl(kullaniciId)
            _siparisListesi.value = liste
        }
    }
}