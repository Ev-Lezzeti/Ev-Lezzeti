package com.example.evlezzeti.ui.adapter

import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.example.evlezzeti.R
import com.example.evlezzeti.data.entity.Kategori
import com.example.evlezzeti.databinding.CardTasarimKategoriBinding

class KategoriAdapter(var mContext: Context, var kategoriListesi : List<Kategori>)
    : RecyclerView.Adapter<KategoriAdapter.CardTasarimTutucu>() {

    inner class CardTasarimTutucu(var tasarim : CardTasarimKategoriBinding) : RecyclerView.ViewHolder(tasarim.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CardTasarimTutucu {
        val binding:CardTasarimKategoriBinding = DataBindingUtil.inflate(LayoutInflater.from(mContext),
            R.layout.card_tasarim_kategori,parent,false)
        return CardTasarimTutucu(binding)
    }

    override fun onBindViewHolder(holder: CardTasarimTutucu, position: Int) {
        val kategori = kategoriListesi.get(position) // Her mutfak icin dongu
        val h = holder.tasarim

        Log.e("mutfakBilgiNesne","${kategori.kategoriAd}")

        h.kategoriNesnesi = kategori

        h.imageView7.setImageResource(
            mContext.resources.getIdentifier(kategori.kategoriResim,"drawable",mContext.packageName))

    }

    override fun getItemCount(): Int {
        return kategoriListesi.size
    }
}