package com.example.evlezzeti.ui.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.example.evlezzeti.R
import com.example.evlezzeti.data.entity.Sepet
import com.example.evlezzeti.databinding.CardTasarimSepetBinding
import com.example.evlezzeti.ui.viewmodel.SharedSepetViewModel


class SepetAdapter (var mContext: Context, var sepetListesi: List<Sepet>,var sharedSepetViewModel: SharedSepetViewModel)
: RecyclerView.Adapter<SepetAdapter.CardTasarimTutucu>() {

    inner class CardTasarimTutucu(var tasarim: CardTasarimSepetBinding) : RecyclerView.ViewHolder(tasarim.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CardTasarimTutucu {
        val binding: CardTasarimSepetBinding = DataBindingUtil.inflate(
            LayoutInflater.from(mContext),
            R.layout.card_tasarim_sepet,
            parent,
            false
        )

        return CardTasarimTutucu(binding)
    }

    override fun onBindViewHolder(holder: CardTasarimTutucu, position: Int) {
        val sepetYemek = sepetListesi.get(position) // Her sepet icin dongu
        val h = holder.tasarim

        h.sepetYemekNesne = sepetYemek

        h.yemekArttirIV.setOnClickListener {//Her bir yemek sayisi azaltilirsa buradan fiyat ve adet guncelleniyor
            var adet = h.yemekAdettextView.text.toString().toInt()
            adet++
            h.yemekAdettextView.text = adet.toString()
            sharedSepetViewModel.sepetListesi[position].yemekAdet = adet.toString()
            val toplamYemekAdetFiyat = sharedSepetViewModel.sepetListesi[position].yemekBirimFiyat!!.toDouble() * adet
            sharedSepetViewModel.yemekFiyatGuncelle(position,toplamYemekAdetFiyat)
            sharedSepetViewModel.hesaplaToplamFiyat()
            sharedSepetViewModel.sepetListesi[position].yemekFiyat ="₺%.2f".format(toplamYemekAdetFiyat)
            h.yemekFiyattextView.text = "₺%.2f".format(toplamYemekAdetFiyat)

        }

        h.yemekAzaltIV.setOnClickListener {
            var adet = h.yemekAdettextView.text.toString().toInt()
            if (adet > 1) {
                adet--
                h.yemekAdettextView.text = adet.toString()
                sharedSepetViewModel.sepetListesi[position].yemekAdet = adet.toString()
                val toplamYemekAdetFiyat = sharedSepetViewModel.sepetListesi[position].yemekBirimFiyat!!.toDouble() * adet
                sharedSepetViewModel.yemekFiyatGuncelle(position,toplamYemekAdetFiyat)
                sharedSepetViewModel.hesaplaToplamFiyat()
                sharedSepetViewModel.sepetListesi[position].yemekFiyat = "₺%.2f".format(toplamYemekAdetFiyat)
                h.yemekFiyattextView.text = "₺%.2f".format(toplamYemekAdetFiyat)
                listeyiGuncelle(sharedSepetViewModel.sepetListesi)
            }
            else{
                sharedSepetViewModel.yemekVeFiyatSil(position)
                sharedSepetViewModel.hesaplaToplamFiyat()
                listeyiGuncelle(sharedSepetViewModel.sepetListesi)
            }
        }


    }

    override fun getItemCount(): Int {
        return sepetListesi.size
    }
    fun listeyiGuncelle(yeniListe: List<Sepet>) {
        sepetListesi = yeniListe
        notifyDataSetChanged()
    }

}