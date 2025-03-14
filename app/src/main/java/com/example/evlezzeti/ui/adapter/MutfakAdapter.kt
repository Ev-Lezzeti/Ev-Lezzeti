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
import com.example.evlezzeti.ui.viewmodel.BottomNavMenuViewModel

class MutfakAdapter(var mContext: Context,var mutfakListesi : List<Mutfak>)
    : RecyclerView.Adapter<MutfakAdapter.CardTasarimTutucu>() {

        inner class CardTasarimTutucu(var tasarim : CardTasarimMutfakBinding) : RecyclerView.ViewHolder(tasarim.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CardTasarimTutucu {
        val binding:CardTasarimMutfakBinding =DataBindingUtil.inflate(LayoutInflater.from(mContext),R.layout.card_tasarim_mutfak,parent,false)
        return CardTasarimTutucu(binding)
    }

    override fun onBindViewHolder(holder: CardTasarimTutucu, position: Int) {
        val mutfak = mutfakListesi.get(position) // Her mutfak icin dongu
        val h = holder.tasarim

        Log.e("mutfakBilgiNesne","${mutfak.mutfakAd}")

        h.mutfakNesne = mutfak

        h.imageViewMutfakResim.setImageResource(
            mContext.resources.getIdentifier(mutfak.mutfakResim,"drawable",mContext.packageName))

    }

    override fun getItemCount(): Int {
        return mutfakListesi.size
    }
}