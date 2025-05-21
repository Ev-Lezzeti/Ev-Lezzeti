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
import com.example.evlezzeti.data.entity.Oneri
import com.example.evlezzeti.databinding.CardTasarimOneriBinding
import com.google.firebase.storage.FirebaseStorage

class OneriAdapter(var mContext: Context, var oneriListesi : List<Oneri>)
    : RecyclerView.Adapter<OneriAdapter.CardTasarimTutucu>() {

    inner class CardTasarimTutucu(var tasarim : CardTasarimOneriBinding) : RecyclerView.ViewHolder(tasarim.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CardTasarimTutucu {
        val binding: CardTasarimOneriBinding = DataBindingUtil.inflate(
            LayoutInflater.from(mContext),
            R.layout.card_tasarim_oneri,parent,false)
        return CardTasarimTutucu(binding)
    }

    override fun onBindViewHolder(holder: CardTasarimTutucu, position: Int) {
        val oneri = oneriListesi.get(position) // Her öneri için döngü
        val h = holder.tasarim

        Log.e("oneriBilgiNesne","${oneri.onerilerAd}")

        h.oneriNesnesi = oneri

        // Firebase Storage'dan resim yükleme
        loadImageFromFirebase(oneri.onerilerResim, h.imageView77)
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

            // "oneriler" klasöründeki resmi referans al (PNG uzantısı)
            val imageRef = storageRef.child("oneriler/$imageName.png")

            Log.d("FirebaseStorage", "Öneri Resim yükleniyor: oneriler/$imageName.png")

            // Resmin URL'sini al
            imageRef.downloadUrl.addOnSuccessListener { uri ->
                // Glide ile resmi yükle
                Glide.with(mContext)
                    .load(uri)
                    .placeholder(R.drawable.placeholder_image)
                    .error(R.drawable.error_image)
                    .into(imageView)

                Log.d("FirebaseStorage", "Öneri Resim başarıyla yüklendi: $uri")
            }.addOnFailureListener { exception ->
                // Hata durumunda
                Log.e("FirebaseStorage", "Öneri Resim yükleme hatası: ${exception.message}", exception)

                // Hata durumunda varsayılan resmi göster
                imageView.setImageResource(R.drawable.default_image)
            }
        } catch (e: Exception) {
            Log.e("FirebaseStorage", "Öneri Genel hata: ${e.message}", e)
            imageView.setImageResource(R.drawable.default_image)
        }
    }

    override fun getItemCount(): Int {
        return oneriListesi.size
    }
}