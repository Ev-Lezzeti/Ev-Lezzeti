package com.example.evlezzeti.ui.adapter

import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.ImageView
import androidx.databinding.DataBindingUtil
import androidx.navigation.Navigation
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.evlezzeti.R
import com.example.evlezzeti.data.entity.Yemek
import com.example.evlezzeti.databinding.CardTasarimYemekBinding
import com.example.evlezzeti.ui.fragment.menu.MutfakDetayFragmentDirections
import com.google.firebase.storage.FirebaseStorage

class YemekAdapter(var mContext: Context, var yemekListesi: List<Yemek>)
    : RecyclerView.Adapter<YemekAdapter.CardTasarimTutucu>() {

    var itemClickListener: ((Yemek) -> Unit)? = null

    inner class CardTasarimTutucu(var tasarim: CardTasarimYemekBinding) : RecyclerView.ViewHolder(tasarim.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CardTasarimTutucu {
        val binding: CardTasarimYemekBinding = DataBindingUtil.inflate(
            LayoutInflater.from(mContext),
            R.layout.card_tasarim_yemek,
            parent,
            false
        )

        return CardTasarimTutucu(binding)
    }

    override fun onBindViewHolder(holder: CardTasarimTutucu, position: Int) {
        val yemek = yemekListesi.get(position) // Her yemek icin dongu
        val h = holder.tasarim

        Log.e("yemekListesiNesne","${yemek.yemekAd}")

        h.yemekNesne = yemek

        // Firebase Storage'dan resim yükleme
        loadImageFromFirebase(yemek.yemekResim, h.imageViewYemekResim)

        holder.tasarim.root.setOnClickListener{
            itemClickListener?.invoke(yemek)
        }

        h.yemekCardView.setOnClickListener {
            val gecis = MutfakDetayFragmentDirections.mutfakDetayToYemekDetay(yemek = yemek)
            Navigation.findNavController(it).navigate(gecis)
        }
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

            // "yemekler" klasöründeki resmi referans al
            val imageRef = storageRef.child("yemekler/$imageName.jpg")

            Log.d("FirebaseStorage", "Yemek Resim yükleniyor: yemekler/$imageName.jpg")

            // Resmin URL'sini al
            imageRef.downloadUrl.addOnSuccessListener { uri ->
                // Glide ile resmi yükle
                Glide.with(mContext)
                    .load(uri)
                    .placeholder(R.drawable.placeholder_image)
                    .error(R.drawable.error_image)
                    .into(imageView)

                Log.d("FirebaseStorage", "Yemek Resim başarıyla yüklendi: $uri")
            }.addOnFailureListener { exception ->
                // Hata durumunda
                Log.e("FirebaseStorage", "Yemek Resim yükleme hatası: ${exception.message}", exception)

                // Hata durumunda varsayılan resmi göster
                imageView.setImageResource(R.drawable.default_image)
            }
        } catch (e: Exception) {
            Log.e("FirebaseStorage", "Yemek Genel hata: ${e.message}", e)
            imageView.setImageResource(R.drawable.default_image)
        }
    }

    override fun getItemCount(): Int {
        return yemekListesi.size
    }
}