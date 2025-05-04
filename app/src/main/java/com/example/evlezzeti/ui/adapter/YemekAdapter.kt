package com.example.evlezzeti.ui.adapter

import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.example.evlezzeti.R
import com.example.evlezzeti.data.entity.Mutfak
import com.example.evlezzeti.data.entity.Yemek
import com.example.evlezzeti.databinding.CardTasarimMutfakBinding
import com.example.evlezzeti.databinding.CardTasarimYemekBinding

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

        //Yemeklerin görsellerinin, yemekResim verisinde göre degismesini sağlıyor
        h.imageViewYemekResim.setImageResource(
           mContext.resources.getIdentifier(yemek.yemekResim,"drawable",mContext.packageName))

        holder.tasarim.root.setOnClickListener{
            itemClickListener?.invoke(yemek)
        }


    }

    override fun getItemCount(): Int {
        return yemekListesi.size
    }
}