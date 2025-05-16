package com.example.evlezzeti.ui.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class SharedKullaniciViewModel():ViewModel() {
    private val _kullaniciId = MutableLiveData<String>()
    val kullaniciId: MutableLiveData<String> get() = _kullaniciId
    private val _kullaniciEPosta = MutableLiveData<String>()
    val kullaniciEposta: MutableLiveData<String> get() = _kullaniciEPosta

    fun kullaniciIdGuncelle(yeniId: String) {
        _kullaniciId.value = yeniId
    }
    fun kullaniciIdTemizle() {
        _kullaniciId.value = ""

    }fun kullaniciEpostaGuncelle(yeniEPosta: String) {
        _kullaniciEPosta.value = yeniEPosta
    }
    fun kullaniciEPostaTemizle() {
        _kullaniciEPosta.value = ""
    }
}