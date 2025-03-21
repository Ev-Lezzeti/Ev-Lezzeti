package com.example.evlezzeti.ui.adapter

import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.example.evlezzeti.R
import com.example.evlezzeti.data.entity.Kategori
import com.example.evlezzeti.data.entity.Oneri
import com.example.evlezzeti.databinding.CardTasarimKategoriBinding
import com.example.evlezzeti.databinding.CardTasarimOneriBinding

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
        val oneri = oneriListesi.get(position) // Her mutfak icin dongu
        val h = holder.tasarim

        Log.e("oneriBilgiNesne","${oneri.onerilerAd}")

        h.oneriNesnesi = oneri

        h.imageView77.setImageResource(
            mContext.resources.getIdentifier(oneri.onerilerResim,"drawable",mContext.packageName))

    }

    override fun getItemCount(): Int {
        return oneriListesi.size
    }
}