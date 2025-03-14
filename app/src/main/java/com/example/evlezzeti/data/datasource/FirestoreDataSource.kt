package com.example.evlezzeti.data.datasource

import androidx.lifecycle.MutableLiveData
import com.example.evlezzeti.data.entity.Kategori
import com.example.evlezzeti.data.entity.Kullanici
import com.example.evlezzeti.data.entity.Mutfak
import com.google.firebase.firestore.CollectionReference


class FirestoreDataSource (var mutfakCollection: CollectionReference, var kategoriCollection: CollectionReference) {

    var kullanici = MutableLiveData<Kullanici>()
    var mutfakListe = MutableLiveData<List<Mutfak>>()
    var kategoriListe = MutableLiveData<List<Kategori>>()

    fun kullaniciGirisKontrol(ePosta:String, sifre:String): Boolean {

        var dogruEPosta = "merhaba@evlezzeti.com"
        var dogruSifre = "1"

        return ePosta == dogruEPosta && sifre==dogruSifre
    }

     fun mutfakYukle() : MutableLiveData<List<Mutfak>>{ // Mutfak listesinin dondugu yer
         mutfakCollection.addSnapshotListener { value, error ->
             if (value !=null ){
                 val liste = ArrayList<Mutfak>()

                 for (m in value.documents){
                     val mutfak = m.toObject(Mutfak::class.java)
                     if(mutfak != null) {
                         mutfak.mutfakId = m.id
                         liste.add(mutfak)
                     }
                 }
                 mutfakListe.value = liste
             }
         }
            return mutfakListe
     }
    fun kategoriYukle() : MutableLiveData<List<Kategori>>{ // Kategori listesinin dondugu yer
        kategoriCollection.addSnapshotListener { value, error ->
            if (value !=null ){
                val liste = ArrayList<Kategori>()

                for (k in value.documents){
                    val kategori = k.toObject(Kategori::class.java)
                    if(kategori != null) {
                        kategori.kategoriId = k.id
                        liste.add(kategori)
                    }
                }
                kategoriListe.value = liste
            }
        }
        return kategoriListe
    }
}