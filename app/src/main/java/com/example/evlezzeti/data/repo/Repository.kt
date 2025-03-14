package com.example.evlezzeti.data.repo

import androidx.lifecycle.MutableLiveData
import com.example.evlezzeti.data.datasource.FirestoreDataSource
import com.example.evlezzeti.data.entity.Kategori
import com.example.evlezzeti.data.entity.Mutfak

class Repository (var fds:FirestoreDataSource)  {


    fun kullaniciGirisKontrol(ePosta:String, sifre:String) : Boolean = fds.kullaniciGirisKontrol(ePosta,sifre)

    fun mutfakYukle() : MutableLiveData<List<Mutfak>> = fds.mutfakYukle()

    fun kategoriYukle() : MutableLiveData<List<Kategori>> = fds.kategoriYukle()
}