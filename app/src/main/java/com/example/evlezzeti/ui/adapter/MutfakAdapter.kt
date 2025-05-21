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
import com.example.evlezzeti.data.entity.Mutfak
import com.example.evlezzeti.databinding.CardTasarimMutfakBinding
import com.example.evlezzeti.ui.fragment.menu.BottomNavMenuFragmentDirections
import com.google.firebase.storage.FirebaseStorage

class MutfakAdapter(var mContext: Context, var mutfakListesi: List<Mutfak>)
    : RecyclerView.Adapter<MutfakAdapter.CardTasarimTutucu>() {

    // Favori butonuna tıklama için callback
    var favoriteClickListener: ((Mutfak) -> Unit)? = null

    var itemClickListener: ((Mutfak) -> Unit)? = null


    // Favori durumunu kontrol etmek için callback
    var isFavoriteCheck: ((String?) -> Boolean)? = null

    inner class CardTasarimTutucu(var tasarim: CardTasarimMutfakBinding) : RecyclerView.ViewHolder(tasarim.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CardTasarimTutucu {
        val binding: CardTasarimMutfakBinding = DataBindingUtil.inflate(
            LayoutInflater.from(mContext),
            R.layout.card_tasarim_mutfak,
            parent,
            false
        )

        return CardTasarimTutucu(binding)
    }

    override fun onBindViewHolder(holder: CardTasarimTutucu, position: Int) {
        val mutfak = mutfakListesi.get(position) // Her mutfak icin dongu
        val h = holder.tasarim

        Log.e("mutfakBilgiNesne","${mutfak.mutfakAd}")

        h.mutfakNesne = mutfak

        // Firebase Storage'dan resim yükleme
        loadImageFromFirebase(mutfak.mutfakResim, h.imageViewMutfakResim)

        holder.tasarim.root.setOnClickListener{
            itemClickListener?.invoke(mutfak)
        }
        //Her bir mutfak icin yemeklere gecis
        h.tasarimCardView.setOnClickListener {
            val gecis = BottomNavMenuFragmentDirections.actionBottomNavMenuFragmentToMutfakDetayFragment(mutfak = mutfak)
            Navigation.findNavController(it).navigate(gecis)
        }

        // Favori butonunun durumunu ayarla
        val isFavorite = isFavoriteCheck?.invoke(mutfak.mutfakId) ?: false
        h.favoriteButton.setImageResource(
            if (isFavorite) R.drawable.like else R.drawable.unlike
        )

        // Favori butonuna tıklama olayı
        h.favoriteButton.setOnClickListener {
            favoriteClickListener?.invoke(mutfak)

            // UI'yi anında güncelleme
            val newFavoriteState = isFavoriteCheck?.invoke(mutfak.mutfakId) ?: false
            h.favoriteButton.setImageResource(
                if (newFavoriteState) R.drawable.like else R.drawable.unlike
            )
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

            // "mutfak" klasöründeki resmi referans al
            val imageRef = storageRef.child("mutfak/$imageName.webp")

            Log.d("FirebaseStorage", "Resim yükleniyor: mutfak/$imageName.webp")

            // Resmin URL'sini al
            imageRef.downloadUrl.addOnSuccessListener { uri ->
                // Glide ile resmi yükle
                Glide.with(mContext)
                    .load(uri)
                    .placeholder(R.drawable.placeholder_image)
                    .error(R.drawable.error_image)
                    .into(imageView)

                Log.d("FirebaseStorage", "Resim başarıyla yüklendi: $uri")
            }.addOnFailureListener { exception ->
                // Hata durumunda
                Log.e("FirebaseStorage", "Resim yükleme hatası: ${exception.message}", exception)

                // Hata durumunda varsayılan resmi göster
                imageView.setImageResource(R.drawable.default_image)
            }
        } catch (e: Exception) {
            Log.e("FirebaseStorage", "Genel hata: ${e.message}", e)
            imageView.setImageResource(R.drawable.default_image)
        }
    }

    override fun getItemCount(): Int {
        return mutfakListesi.size
    }
}