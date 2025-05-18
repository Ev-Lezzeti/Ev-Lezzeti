package com.example.evlezzeti.data.repo

import androidx.lifecycle.MutableLiveData
import com.example.evlezzeti.data.datasource.FirestoreDataSource
import com.example.evlezzeti.data.entity.Kategori
import com.example.evlezzeti.data.entity.Kullanici
import com.example.evlezzeti.data.entity.Mutfak
import com.example.evlezzeti.data.entity.Oneri
import com.example.evlezzeti.data.entity.Siparis
import com.example.evlezzeti.data.entity.Yemek

class Repository (var fds:FirestoreDataSource)  {

    fun mutfakYukle() : MutableLiveData<List<Mutfak>> = fds.mutfakYukle()

    fun kategoriYukle() : MutableLiveData<List<Kategori>> = fds.kategoriYukle()

    fun oneriYukle() : MutableLiveData<List<Oneri>> = fds.oneriYukle()

    fun yemekYukle() : MutableLiveData<List<Yemek>> = fds.yemekYukle()

    fun otpKontrol(ePosta :String) : Boolean = fds.otpKontrol(ePosta)

    fun otpGuncelle(ePosta :String) : Boolean = fds.otpGuncelle(ePosta)

    fun kullaniciKaydet(kullanici: Kullanici) = fds.kullaniciKaydet(kullanici)

    fun kullaniciAdresKontrol(kullaniciId: String, callback: (Boolean) -> Unit) {
        fds.kullaniciAdresKontrol(kullaniciId, callback)
    }
    fun kullaniciAl(kullaniciId: String, callback: (Kullanici?) -> Unit) {
        fds.kullaniciVerisiAl(kullaniciId, callback)
    }
    fun siparisEkle(siparis: Siparis){
        fds.siparisEkle(siparis)
    }

}