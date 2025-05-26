package com.example.evlezzeti.ui.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.example.evlezzeti.R
import com.example.evlezzeti.data.entity.Siparis
import com.example.evlezzeti.databinding.CardTasarimGecmisSiparislerBinding

class GecmisSiparislerAdapter(var mContext: Context, var gecmisSiparislerListesi: List<Siparis>)
    : RecyclerView.Adapter<GecmisSiparislerAdapter.CardTasarimTutucu>() {

    inner class CardTasarimTutucu(var tasarim: CardTasarimGecmisSiparislerBinding) : RecyclerView.ViewHolder(tasarim.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CardTasarimTutucu {
        val binding: CardTasarimGecmisSiparislerBinding = DataBindingUtil.inflate(
            LayoutInflater.from(mContext),
            R.layout.card_tasarim_gecmis_siparisler,
            parent,
            false
        )

        return CardTasarimTutucu(binding)
    }

    override fun onBindViewHolder(holder: CardTasarimTutucu, position: Int) {
        val siparis = gecmisSiparislerListesi.get(position)
        val h = holder.tasarim

        h.siparisNesne = siparis

        // siparisOzeti'ni formatla
        val temizOzet = siparis.siparisOzeti
            ?.removePrefix("[")
            ?.removeSuffix("]")
            ?.split(", ")
            ?.joinToString("\n")

        // TextView'a manuel olarak ata
        h.textViewSiparisOzet.text = temizOzet

    }

    override fun getItemCount(): Int {
        return gecmisSiparislerListesi.size
    }
}