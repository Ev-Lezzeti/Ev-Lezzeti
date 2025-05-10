package com.example.evlezzeti.ui.fragment

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.activityViewModels
import androidx.navigation.Navigation
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.evlezzeti.R
import com.example.evlezzeti.databinding.FragmentBottomNavSepetBinding
import com.example.evlezzeti.ui.adapter.SepetAdapter
import com.example.evlezzeti.ui.viewmodel.SharedSepetViewModel
import androidx.navigation.findNavController
import com.google.android.material.snackbar.Snackbar

class BottomNavSepetFragment : Fragment() {
    private lateinit var binding: FragmentBottomNavSepetBinding
    private val sharedViewModel: SharedSepetViewModel by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_bottom_nav_sepet, container, false)
        binding.bottomNavSepetFragment = this

        binding.sepetYemekRV.layoutManager = LinearLayoutManager(requireContext())

        //Sepete ekleyip Kaydettigimiz yemekleri burdan alıyoruz
        val sepetListesi = sharedViewModel.sepetListesi
        binding.sepetYemekAdapter = SepetAdapter(requireContext(), sepetListesi,sharedViewModel)
        sharedViewModel.toplamFiyat.observe(viewLifecycleOwner) { toplam ->
            binding.toplamFiyatTextView.text = "Toplam ₺%.2f".format(toplam)
        }


        sepetGorunumGuncelle(sepetListesi.isNotEmpty())

        binding.sepetinBosButton.setOnClickListener {//Sepet bosken MenuFragmenta gecis butonu
            it.findNavController().navigate(R.id.action_bottomNavSepetFragment_to_bottomNavMenuFragment)
        }

        binding.sepetTemizleButton.setOnClickListener {//Yemekleri sepetten silme
            Snackbar.make(it,"Sepeti temizlemek istiyor musunuz ?",Snackbar.LENGTH_LONG)
                .setAction("Evet"){
                    sharedViewModel.sepetListeTemizle()
                    sharedViewModel.yemekFiyatlariTemizle()
                    sepetGorunumGuncelle(false)
                }
            .show()
        }
        sharedViewModel.toplamFiyat.observe(viewLifecycleOwner){//Burada eger sepet bossa fragment eski haline gelmesi icin
            if (it == 0.0){
                sepetGorunumGuncelle(false)
            }
            else{
                sepetGorunumGuncelle(true)

            }
        }

        return binding.root
    }

    private fun sepetGorunumGuncelle (sepetDoluluk:Boolean){
        if (sepetDoluluk){
            binding.sepetYemekRV.visibility = View.VISIBLE
            binding.sepetOnaylaButton.visibility = View.VISIBLE
            binding.ToplamFiyatcardView.visibility = View.VISIBLE
            binding.sepetTemizleButton.visibility = View.VISIBLE

            binding.materialCardView2.visibility = View.GONE
            binding.textView17.visibility = View.GONE
            binding.textView18.visibility = View.GONE
            binding.sepetinBosButton.visibility = View.GONE
        }
        else{
            binding.sepetYemekRV.visibility = View.GONE
            binding.sepetOnaylaButton.visibility = View.GONE
            binding.ToplamFiyatcardView.visibility = View.GONE
            binding.sepetTemizleButton.visibility = View.GONE

            binding.materialCardView2.visibility = View.VISIBLE
            binding.textView17.visibility = View.VISIBLE
            binding.textView18.visibility = View.VISIBLE
            binding.sepetinBosButton.visibility = View.VISIBLE
        }

    }
}