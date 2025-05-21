package com.example.evlezzeti.ui.adapter

import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.ImageView
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.evlezzeti.R
import com.example.evlezzeti.data.entity.Kategori
import com.example.evlezzeti.databinding.CardTasarimKategoriBinding
import com.google.firebase.storage.FirebaseStorage

class KategoriAdapter(var mContext: Context, var kategoriListesi : List<Kategori>)
    : RecyclerView.Adapter<KategoriAdapter.CardTasarimTutucu>() {

    inner class CardTasarimTutucu(var tasarim : CardTasarimKategoriBinding) : RecyclerView.ViewHolder(tasarim.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CardTasarimTutucu {
        val binding:CardTasarimKategoriBinding = DataBindingUtil.inflate(LayoutInflater.from(mContext),
            R.layout.card_tasarim_kategori,parent,false)
        return CardTasarimTutucu(binding)
    }

    override fun onBindViewHolder(holder: CardTasarimTutucu, position: Int) {
        val kategori = kategoriListesi.get(position) // Her kategori icin dongu
        val h = holder.tasarim

        Log.e("kategoriBilgiNesnesi","${kategori.kategoriAd}")

        h.kategoriNesnesi = kategori

        // Firebase Storage'dan resim yükleme
        loadImageFromFirebase(kategori.kategoriResim, h.imageView7)
    }

    /**
     * Firebase Storage'dan resim yükler
     */
    private fun loadImageFromFirebase(imageName: String?, imageView: ImageView) {
        if (imageName.isNullOrEmpty()) {
            // Eğer resim adı yoksa varsayılan resmi göster
            imageView.setImageResource(R.drawable.default_image)
            return
        }

        try {
            // Firebase Storage referansı
            val storageRef = FirebaseStorage.getInstance().reference

            // "kategoriler" klasöründeki resmi referans al
            val imageRef = storageRef.child("kategoriler/$imageName.webp")

            Log.d("FirebaseStorage", "Kategori Resim yükleniyor: kategoriler/$imageName.webp")

            // Resmin URL'sini al
            imageRef.downloadUrl.addOnSuccessListener { uri ->
                // Glide ile resmi yükle
                Glide.with(mContext)
                    .load(uri)
                    .placeholder(R.drawable.placeholder_image)
                    .error(R.drawable.error_image)
                    .into(imageView)

                Log.d("FirebaseStorage", "Kategori Resim başarıyla yüklendi: $uri")
            }.addOnFailureListener { exception ->
                // Hata durumunda
                Log.e("FirebaseStorage", "Kategori Resim yükleme hatası: ${exception.message}", exception)

                // Hata durumunda varsayılan resmi göster
                imageView.setImageResource(R.drawable.default_image)
            }
        } catch (e: Exception) {
            Log.e("FirebaseStorage", "Kategori Genel hata: ${e.message}", e)
            imageView.setImageResource(R.drawable.default_image)
        }
    }

    override fun getItemCount(): Int {
        return kategoriListesi.size
    }
}