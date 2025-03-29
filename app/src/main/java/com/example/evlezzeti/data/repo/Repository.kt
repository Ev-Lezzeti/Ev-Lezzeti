package com.example.evlezzeti.data.repo

import androidx.lifecycle.MutableLiveData
import com.example.evlezzeti.data.datasource.FirestoreDataSource
import com.example.evlezzeti.data.entity.Kategori
import com.example.evlezzeti.data.entity.Mutfak
import com.example.evlezzeti.data.entity.Oneri

class Repository (var fds:FirestoreDataSource)  {

    fun mutfakYukle() : MutableLiveData<List<Mutfak>> = fds.mutfakYukle()

    fun kategoriYukle() : MutableLiveData<List<Kategori>> = fds.kategoriYukle()

    fun oneriYukle() : MutableLiveData<List<Oneri>> = fds.oneriYukle()

    fun otpKontrol(ePosta :String) : Boolean = fds.otpKontrol(ePosta)

    fun otpGuncelle(ePosta :String) : Boolean = fds.otpGuncelle(ePosta)

}