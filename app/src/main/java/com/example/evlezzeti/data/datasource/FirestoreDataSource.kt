package com.example.evlezzeti.data.datasource

import android.util.Log
import androidx.lifecycle.MutableLiveData
import com.example.evlezzeti.data.entity.Kategori
import com.example.evlezzeti.data.entity.Kullanici
import com.example.evlezzeti.data.entity.Mutfak
import com.example.evlezzeti.data.entity.Oneri
import com.example.evlezzeti.data.entity.Siparis
import com.example.evlezzeti.data.entity.Users
import com.example.evlezzeti.data.entity.Yemek
import com.google.firebase.firestore.CollectionReference
import kotlinx.coroutines.tasks.await


class FirestoreDataSource(
    var mutfakCollection: CollectionReference,
    var kategoriCollection: CollectionReference,
    var oneriCollection: CollectionReference,
    var usersCollection: CollectionReference,
    var yemekCollection: CollectionReference,
    var kullanicilarCollection: CollectionReference,
    var siparislerCollection: CollectionReference
) {

    var kullanici = MutableLiveData<Kullanici>()
    var mutfakListe = MutableLiveData<List<Mutfak>>()
    var kategoriListe = MutableLiveData<List<Kategori>>()
    var oneriListe = MutableLiveData<List<Oneri>>()
    var yemekListe = MutableLiveData<List<Yemek>>()
    var otp = false
    var guncelleme = false


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

    fun oneriYukle() : MutableLiveData<List<Oneri>>{ // Oneri listesinin dondugu yer
        oneriCollection.addSnapshotListener { value, error ->
            if (value !=null ){
                val liste = ArrayList<Oneri>()
                value.documents
                for (o in value.documents){
                    val oneri = o.toObject(Oneri::class.java)
                    if(oneri != null) {
                        oneri.onerlerilerId = o.id
                        liste.add(oneri)
                    }
                }
                oneriListe.value = liste
            }
        }
        return oneriListe
    }

    fun yemekYukle() : MutableLiveData<List<Yemek>>{ // Yemek listesinin dondugu yer
        yemekCollection.addSnapshotListener { value, error ->
            if (value !=null ){
                val liste = ArrayList<Yemek>()
                value.documents
                for (o in value.documents){
                    val yemek = o.toObject(Yemek::class.java)
                    if(yemek != null) {
                        yemek.yemekId = o.id
                        liste.add(yemek)
                    }
                }
                yemekListe.value = liste
            }
        }
        return yemekListe
    }



    fun otpKontrol(ePosta: String) : Boolean { //users listesi alinir
        usersCollection.addSnapshotListener { value, error ->
            if (value !=null ){
                val liste = ArrayList<Users>()
                for (u in value.documents){
                    //var user = u.toObject(Users::class.java)

                    val email =u.getString("email")?: ""
                    val isVerified = u.getBoolean("isVerified")?: false
                    val uid = u.getString("uid")?: ""
                    val user = Users(email,isVerified,uid)
                    if (user.email== ePosta && user.isVerified == true) {
                        otp = true
                    }
                }
            }
        }
        return otp
    }

    fun otpGuncelle(ePosta: String) : Boolean {
        usersCollection.addSnapshotListener { value, error ->
            if (value !=null ){
                val liste = ArrayList<Users>()

                for (u in value.documents){
                    val user = u.toObject(Users::class.java)
                    if(user != null) {
                        liste.add(user)
                        if (user.email == ePosta){
                            val guncelUser = HashMap<String,Any>()
                            guncelUser["isVerified"] = true
                            guncelUser["email"] = user.email.toString()
                            guncelUser["uid"] = user.uid.toString()
                            usersCollection.document(user.uid!!).update(guncelUser)
                            guncelleme = true
                        }
                    }
                }
            }
        }
        return guncelleme
    }
    fun kullaniciKaydet (kullanici: Kullanici) {
        val yeniKullanici = Kullanici("${kullanici.kullaniciId}",
            "${kullanici.ePosta}",
            "${kullanici.kullaniciTelefon}",
            "${kullanici.kullaniciAd}",
            "${kullanici.kullaniciAdress}",
            "${kullanici.kullaniciEnlem}",
            "${kullanici.kullaniciBoylam}",
            "${kullanici.favoriler}")
        kullanicilarCollection.document().set(yeniKullanici)
    }

    fun kullaniciGuncelle(kullanici: Kullanici) {
        val kullaniciRef = kullanicilarCollection.document(kullanici.kullaniciId ?: return)

        val yeniKullanici = Kullanici(
            kullanici.kullaniciId ?: "",
            kullanici.ePosta ?: "",
            kullanici.kullaniciTelefon ?: "",
            kullanici.kullaniciAd ?: "",
            kullanici.kullaniciAdress ?: "",
            kullanici.kullaniciEnlem ?: "",
            kullanici.kullaniciBoylam ?: "",
            kullanici.favoriler ?: ""
        )

        kullaniciRef.set(yeniKullanici)
            .addOnSuccessListener {
                Log.d("Firestore", "Kullanıcı başarıyla eklendi/güncellendi.")
            }
            .addOnFailureListener { e ->
                Log.e("Firestore", "Kullanıcı eklenirken/güncellenirken hata oluştu.", e)
            }
    }


    fun kullaniciAdresKontrol(kullaniciId: String, callback: (Boolean) -> Unit) {
        if (kullaniciId != "Id Yok") {
            kullanicilarCollection
                .whereEqualTo("kullaniciId", kullaniciId)
                .get()
                .addOnSuccessListener { documents ->
                    val bulundu = documents.any {
                        val user = it.toObject(Kullanici::class.java)
                        !user.kullaniciAdress.isNullOrEmpty()
                    }
                    Log.e("KullaniciAdresKontrol", "Adres bulundu mu: $bulundu")
                    callback(bulundu)
                }
                .addOnFailureListener { error ->
                    Log.e("KullaniciAdresKontrol", "Hata: ${error.message}")
                    callback(false)
                }
        } else {
            Log.e("KullaniciAdresKontrol", "Geçersiz ID")
            callback(false)
        }
    }

    fun kullaniciVerisiAl(kullaniciId: String, callback: (Kullanici?) -> Unit) {
        kullanicilarCollection
            .whereEqualTo("kullaniciId", kullaniciId)
            .get()
            .addOnSuccessListener { documents ->
                if (!documents.isEmpty) {
                    val user = documents.first().toObject(Kullanici::class.java)
                    callback(user)
                } else {
                    callback(null)
                    }
            }
            .addOnFailureListener {
                callback(null)
            }

    }
    fun siparisEkle(siparis: Siparis){
        siparislerCollection.document().set(siparis)
    }

    fun aktifSiparisByKullaniciIdAl(
        kullaniciId: String,
        onSuccess: (Siparis?) -> Unit,
        onFailure: (Exception) -> Unit) {
        siparislerCollection
            .whereEqualTo("kullaniciId", kullaniciId)
            .whereIn("siparisDurum", listOf("Sipariş Alındı",
                "Siparişiniz Hazırlanıyor",
                "Siparişiniz Yola Çıktı",
                "Sipariş Teslim Edildi"))
            .limit(1)
            .get()
            .addOnSuccessListener { result ->
                val siparis = result.firstOrNull()?.toObject(Siparis::class.java)
                onSuccess(siparis)
                Log.e("aktifSiparisByKullaniciIdAl", "Sipariş: $siparis $kullaniciId")
            }
            .addOnFailureListener { exception ->
                onFailure(exception)
            }
    }
    suspend fun guncelleSiparisDurum(kullaniciId: String, yeniDurum: String) {
        try {
            val snapshot = siparislerCollection
                .whereEqualTo("kullaniciId", kullaniciId)
                .whereIn("siparisDurum", listOf("Sipariş Alındı",
                    "Sipariş Onaylandı",
                    "Siparişiniz Hazırlanıyor",
                    "Siparişiniz Yola Çıktı",
                    "Sipariş Teslim Edildi"))
                .limit(1)
                .get()
                .await()

            val doc = snapshot.documents.firstOrNull()
            doc?.reference?.update("siparisDurum", yeniDurum)
        } catch (e: Exception) {
            Log.e("FirestoreDataSource", "Durum güncellenirken hata oluştu", e)
        }
    }
    suspend fun tumSiparislerByKullaniciIdAl(kullaniciId: String): List<Siparis> {
        return try {
            val snapshot = siparislerCollection
                .whereEqualTo("kullaniciId", kullaniciId)
                .get()
                .await()

            snapshot.documents.mapNotNull { it.toObject(Siparis::class.java) }
        } catch (e: Exception) {
            Log.e("FirestoreDataSource", "Hata: ${e.localizedMessage}")
            emptyList()
        }
    }

}