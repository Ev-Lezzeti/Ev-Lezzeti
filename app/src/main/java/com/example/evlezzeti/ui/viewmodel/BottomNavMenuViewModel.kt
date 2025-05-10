package com.example.evlezzeti.ui.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.evlezzeti.data.entity.Kategori
import com.example.evlezzeti.data.entity.Mutfak
import com.example.evlezzeti.data.entity.Oneri
import com.example.evlezzeti.data.repo.Repository
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class BottomNavMenuViewModel @Inject constructor (var rep : Repository): ViewModel(){
    var mutfakListe = MutableLiveData<List<Mutfak>>()
    var kategoriListe = MutableLiveData<List<Kategori>>()
    var oneriListe = MutableLiveData<List<Oneri>>()

    // Arama için kullanılacak değişkenler
    private val _aramaMetni = MutableLiveData<String>("")
    val aramaMetni: LiveData<String> = _aramaMetni

    private val _filtrelenmisListe = MutableLiveData<List<Mutfak>>()
    val filtrelenmisListe: LiveData<List<Mutfak>> = _filtrelenmisListe

    // Favori mutfakların ID'lerini tutan LiveData
    private val _favoriMutfaklar = MutableLiveData<Set<String>>(setOf())
    val favoriMutfaklar: LiveData<Set<String>> = _favoriMutfaklar

    // Yükleniyor durumu için
    private val _yukleniyor = MutableLiveData<Boolean>(false)
    val yukleniyor: LiveData<Boolean> = _yukleniyor

    // Sıralama tipi enum
    enum class SiralamaTipi {
        YILDIZ_PUANI, MESAFE, FAVORILER
    }

    // Aktif sıralama tipini tutan değişken
    private var aktifSiralama = SiralamaTipi.YILDIZ_PUANI

    init {
        if (mutfakListe.value.isNullOrEmpty()) {
            mutfakYukle()
            kategoriYukle()
            oneriYukle()
        }
    }


    private fun mutfakYukle() {
        _yukleniyor.value = true

        val liveData = rep.mutfakYukle()

        mutfakListe = liveData

        liveData.observeForever { mutfaklar ->
            if (mutfaklar != null && _filtrelenmisListe.value == null) {
                _filtrelenmisListe.value = mutfaklar
                _yukleniyor.value = false
            }
        }
    }

    private fun kategoriYukle(){
        kategoriListe = rep.kategoriYukle()
    }

    private fun oneriYukle(){
        oneriListe = rep.oneriYukle()
    }

    // Arama fonksiyonu
    fun mutfakAra(arama: String) {
        _aramaMetni.value = arama

        // Boş arama durumunda tüm listeyi göster ve aktif sıralamayı uygula
        if (arama.isEmpty()) {
            siralamaYap(aktifSiralama)
            return
        }

        // Arama kriterine göre filtreleme yap
        mutfakListe.value?.let { tumMutfaklar ->
            val filtrelenmis = tumMutfaklar.filter { mutfak ->
                mutfak.mutfakAd!!.contains(arama, ignoreCase = true)
                // Eğer mutfak sınıfınızda açıklama gibi diğer alanlar varsa onlara da bakabilirsiniz
                // || mutfak.aciklama?.contains(arama, ignoreCase = true) == true
            }

            // Filtrelenmiş listeyi aktif sıralamaya göre sırala
            val siraliListe = siralananListeGetir(filtrelenmis, aktifSiralama)
            _filtrelenmisListe.value = siraliListe
        }
    }

    // Sıralama fonksiyonu
    fun siralamaYap(tip: SiralamaTipi) {
        aktifSiralama = tip // Aktif sıralamayı güncelle

        val listeKaynagi = if (_aramaMetni.value.isNullOrEmpty()) {
            // Arama yoksa tüm listeyi kullan
            mutfakListe.value ?: emptyList()
        } else {
            // Arama varsa mevcut filtrelenmiş listeyi kullan
            _filtrelenmisListe.value ?: emptyList()
        }

        val siraliListe = siralananListeGetir(listeKaynagi, tip)
        _filtrelenmisListe.value = siraliListe
    }

    // Verilen listeyi belirtilen kritere göre sıralayan yardımcı fonksiyon
    private fun siralananListeGetir(liste: List<Mutfak>, tip: SiralamaTipi): List<Mutfak> {
        return when (tip) {
            SiralamaTipi.YILDIZ_PUANI -> {
                // Yıldız puanına göre sırala (büyükten küçüğe)
                liste.sortedByDescending { it.mutfakPuan?.toDoubleOrNull() ?: 0.0 }
            }
            SiralamaTipi.MESAFE -> {
                // Mesafeye göre sırala - basit sıralama olarak adres alfabetik sıra
                liste.sortedBy { it.mutfakAdres }
            }
            SiralamaTipi.FAVORILER -> {
                // Favorileri önce göster
                val favoriIdleri = _favoriMutfaklar.value ?: setOf()
                liste.sortedByDescending { mutfak -> favoriIdleri.contains(mutfak.mutfakId) }
            }
        }
    }

    // Favoriye ekleme/çıkarma fonksiyonu
    fun favoriyeEkleVeyaCikar(mutfak: Mutfak) {
        // Mevcut favori listesini al
        val mevcutFavoriler = _favoriMutfaklar.value?.toMutableSet() ?: mutableSetOf()

        // Favorilerde var mı kontrol et
        if (mevcutFavoriler.contains(mutfak.mutfakId)) {
            // Favorilerden çıkar
            mevcutFavoriler.remove(mutfak.mutfakId)
        } else {
            // Favorilere ekle
            mutfak.mutfakId?.let { mevcutFavoriler.add(it) }
        }

        // Güncellenmiş favori listesini kaydet
        _favoriMutfaklar.value = mevcutFavoriler

        // Eğer aktif sıralama favoriler ise listeyi güncelle
        if (aktifSiralama == SiralamaTipi.FAVORILER) {
            siralamaYap(SiralamaTipi.FAVORILER)
        }
    }

    // Favoride olup olmadığını kontrol et
    fun isFavorite(mutfakId: String?): Boolean {
        return mutfakId != null && _favoriMutfaklar.value?.contains(mutfakId) == true
    }
}