package com.example.evlezzeti.ui.fragment

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.evlezzeti.R
import com.example.evlezzeti.databinding.FragmentBottomNavSepetBinding
import com.example.evlezzeti.ui.adapter.SepetAdapter
import com.example.evlezzeti.ui.viewmodel.SharedSepetViewModel
import androidx.navigation.findNavController
import com.example.evlezzeti.ui.viewmodel.BottomNavSepetViewModel
import com.example.evlezzeti.ui.viewmodel.SharedKullaniciViewModel
import com.google.android.material.snackbar.Snackbar
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class BottomNavSepetFragment : Fragment() {
    private lateinit var binding: FragmentBottomNavSepetBinding
    private lateinit var viewModel: BottomNavSepetViewModel
    private val sharedSepetViewModel: SharedSepetViewModel by activityViewModels()
    private val sharedKullaniciViewModel: SharedKullaniciViewModel by activityViewModels()
    private lateinit var kullaniciId :String

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_bottom_nav_sepet, container, false)
        binding.bottomNavSepetFragment = this

        binding.sepetYemekRV.layoutManager = LinearLayoutManager(requireContext())

        //Sepete ekleyip Kaydettigimiz yemekleri burdan alıyoruz
        val sepetListesi = sharedSepetViewModel.sepetListesi
        binding.sepetYemekAdapter = SepetAdapter(requireContext(), sepetListesi,sharedSepetViewModel)
        sharedSepetViewModel.toplamFiyat.observe(viewLifecycleOwner) { toplam ->
            binding.toplamFiyatTextView.text = "Toplam ₺%.2f".format(toplam)
        }
        sharedKullaniciViewModel.kullaniciId.observe(viewLifecycleOwner){//KullaniciId ile adres var mi
            kullaniciId = it
        }


        sepetGorunumGuncelle(sepetListesi.isNotEmpty())

        binding.sepetinBosButton.setOnClickListener {//Sepet bosken MenuFragmenta gecis butonu
            it.findNavController().navigate(R.id.action_bottomNavSepetFragment_to_bottomNavMenuFragment)
        }

        binding.sepetTemizleButton.setOnClickListener {//Yemekleri sepetten silme
            Snackbar.make(it,"Sepeti temizlemek istiyor musunuz ?",Snackbar.LENGTH_LONG)
                .setAction("Evet"){
                    sharedSepetViewModel.sepetListeTemizle()
                    sharedSepetViewModel.yemekFiyatlariTemizle()
                    sharedSepetViewModel.toplamFiyatGuncelle(0.0)
                    sepetGorunumGuncelle(false)
                }
            .show()
        }
        sharedSepetViewModel.toplamFiyat.observe(viewLifecycleOwner){//Burada eger sepet bossa fragment eski haline gelmesi icin
            if (it == 0.0){
                sepetGorunumGuncelle(false)
            }
            else{
                sepetGorunumGuncelle(true)

            }
        }

        //Sepeti onayladiktan sonra Konum kontrolu burada
        binding.sepetOnaylaButton.setOnClickListener { view ->
            sharedKullaniciViewModel.kullaniciId.observe(viewLifecycleOwner) { kullaniciId ->
                viewModel.kullaniciAdresKontrolWithCallback(kullaniciId) { durum ->

                    if (durum) {// Adres varsa, işleme devam et
                        Toast.makeText(requireContext(), "Adres Kayıtlı", Toast.LENGTH_SHORT).show()
                    } else { //Adres yoksa haritaya gecis
                        Toast.makeText(requireContext(), "Adres Yok", Toast.LENGTH_SHORT).show()
                        view.findNavController().navigate(R.id.sepetFragmentToHaritaIslemleriFragment)
                    }
                }
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
    //Direkt viewmodel Kullanamadigimiz icin burasi sart
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val tempViewModel: BottomNavSepetViewModel by viewModels()
        viewModel = tempViewModel
    }
}