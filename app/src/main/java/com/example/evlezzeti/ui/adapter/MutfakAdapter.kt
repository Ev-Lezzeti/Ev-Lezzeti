package com.example.evlezzeti.ui.adapter

import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.example.evlezzeti.R
import com.example.evlezzeti.data.entity.Mutfak
import com.example.evlezzeti.databinding.CardTasarimMutfakBinding

class MutfakAdapter(var mContext: Context, var mutfakListesi: List<Mutfak>)
    : RecyclerView.Adapter<MutfakAdapter.CardTasarimTutucu>() {

    // Favori butonuna tıklama için callback
    var favoriteClickListener: ((Mutfak) -> Unit)? = null

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

        h.imageViewMutfakResim.setImageResource(
            mContext.resources.getIdentifier(mutfak.mutfakResim,"drawable",mContext.packageName))

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

    override fun getItemCount(): Int {
        return mutfakListesi.size
    }
}