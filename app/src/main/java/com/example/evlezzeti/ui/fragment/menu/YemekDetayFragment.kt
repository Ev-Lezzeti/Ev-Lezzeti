package com.example.evlezzeti.ui.fragment.menu

import android.content.Context
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.navArgs
import com.example.evlezzeti.R
import com.example.evlezzeti.data.entity.Sepet
import com.example.evlezzeti.databinding.FragmentYemekDetayBinding
import com.example.evlezzeti.ui.viewmodel.SharedSepetViewModel
import com.google.android.material.snackbar.Snackbar


class YemekDetayFragment : Fragment() {
    private lateinit var binding: FragmentYemekDetayBinding
    private val sharedViewModel: SharedSepetViewModel by activityViewModels()
    private val args: YemekDetayFragmentArgs by navArgs()
    private var fiyat = 1.0
    private var adet = 1

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_yemek_detay, container, false)


        //RV'den gelen yemek nesnesini burda aldık
        binding.yemekNesne = args.yemek

        binding.fabArttir.setOnClickListener { //Adet arttırma fonk.
            butonYemekSayiDurum("arttir")
        }
        binding.fabAzalt.setOnClickListener {  //Adet azaltma fonk.
            butonYemekSayiDurum("azalt")
        }
        //Yemeğe göre resim kaynagi
        binding.imageViewYemek.setImageResource(resources.getIdentifier(args.yemek.yemekResim, "drawable", requireContext().packageName))

        //Burada sepete ekleme islemleri
        binding.sepeteEkleButton.setOnClickListener {

            var sepet = Sepet()
            sepet.mutfakAd = args.yemek.mutfakAd
            sepet.yemekAd=args.yemek.yemekAd
            sepet.yemekId=args.yemek.yemekId
            sepet.mutfakId=args.yemek.mutfakId
            sepet.yemekFiyat=binding.textViewFiyat.text.toString()
            sepet.yemekNot=binding.siparisNotuEditText.text.toString()
            sepet.yemekAdet=binding.textViewAdet.text.toString()
            sepet.yemekBirimFiyat=fiyatDoubleDonustur(args.yemek.yemekFiyat.toString()).toString()

            sharedViewModel.sepetEkle(sepet)

            val fiyatDouble = binding.textViewFiyat.text
                .toString()
                .replace("₺", "")     // ₺ işaretini kaldır
                .replace(" ", "")     // boşlukları kaldır
                .replace(",", ".")    // virgülü noktaya çevir
                .toDoubleOrNull()     // güvenli çeviri (null olabilir)

            fiyatDouble?.let {
                sharedViewModel.yemekFiyatEkle(it)
                sharedViewModel.hesaplaToplamFiyat()
            }
            Toast.makeText(requireContext(), "${args.yemek.yemekAd} Sepete eklendi!", Toast.LENGTH_SHORT).show()
        }

        return binding.root
    }

    private fun yemekFiyatiAl(): Double {
        val fiyatStr = binding.yemekNesne!!.yemekFiyat.toString()
        val sadeceSayi = fiyatStr.replace(",", ".").filter { it.isDigit() || it == '.' }
        return sadeceSayi.toDoubleOrNull() ?: 0.00
    }


    private fun butonYemekSayiDurum(durum:String){
        if (durum == "arttir"){
            adet++
            val birimFiyat = yemekFiyatiAl()
            fiyat = adet * birimFiyat
            binding.textViewAdet.text = adet.toString()
            binding.textViewFiyat.text = "₺%.2f".format(fiyat).replace('.', ',')
        }

        else if (durum == "azalt"){
            if (adet > 1){
                adet--
                val birimFiyat = yemekFiyatiAl()
                fiyat = adet * birimFiyat
                binding.textViewAdet.text = adet.toString()
                binding.textViewFiyat.text = "₺%.2f".format(fiyat).replace('.', ',')
            }
        }
    }
    private fun fiyatDoubleDonustur(fiyatStr: String): Double {
        var yeniFiyat = fiyatStr
            .replace("₺", "")     // ₺ işaretini kaldır
            .replace(" ", "")     // boşlukları kaldır
            .replace(",", ".")    // virgülü noktaya çevir
            .toDouble()
        return yeniFiyat
    }

}