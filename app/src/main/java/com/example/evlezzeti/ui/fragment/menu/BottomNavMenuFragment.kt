package com.example.evlezzeti.ui.fragment.menu

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.example.evlezzeti.R
import com.example.evlezzeti.databinding.FragmentBottomNavMenuBinding
import com.example.evlezzeti.ui.adapter.KategoriAdapter
import com.example.evlezzeti.ui.adapter.MutfakAdapter
import com.example.evlezzeti.ui.adapter.OneriAdapter
import com.example.evlezzeti.ui.viewmodel.BottomNavMenuViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class BottomNavMenuFragment : Fragment() {
    private lateinit var binding: FragmentBottomNavMenuBinding
    private lateinit var viewModel: BottomNavMenuViewModel
    private lateinit var mutfakAdapter: MutfakAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_bottom_nav_menu, container, false)
        // Xml'de kullanmak için fragment ve viewModel'i bağla
        binding.bottomNavMenuFragment = this
        binding.viewModel = viewModel
        binding.lifecycleOwner = viewLifecycleOwner

        // Filtreleme chip'lerinin tıklama olaylarını ayarla
        setupFilterChips()

        // Mutfak ve Kategori listelerini gözlemle
        setupObservers()

        return binding.root
    }


    private fun setupFilterChips() {
        // Varsayılan olarak yıldız chip'ini seçili yap
        binding.chipYildiz.isChecked = true

        // Chip'lere tıklama olaylarını ekle
        binding.chipYildiz.setOnCheckedChangeListener { chip, isChecked ->
            if (isChecked) {
                viewModel.siralamaYap(BottomNavMenuViewModel.SiralamaTipi.YILDIZ_PUANI)
            }
        }

        binding.chipMesafe.setOnCheckedChangeListener { chip, isChecked ->
            if (isChecked) {
                viewModel.siralamaYap(BottomNavMenuViewModel.SiralamaTipi.MESAFE)
            }
        }

        binding.chipFavoriler.setOnCheckedChangeListener { chip, isChecked ->
            if (isChecked) {
                viewModel.siralamaYap(BottomNavMenuViewModel.SiralamaTipi.FAVORILER)
            }
        }
    }

    private fun setupObservers() {
        // Mutfak listesini gözlemle
        viewModel.mutfakListe.observe(viewLifecycleOwner) { mutfaklar ->
            Log.d("Fragment", "Mutfak listesi güncellendi: ${mutfaklar?.size}")

            // Mutfaklar yüklendiğinde sıralamayı uygula
            if (!mutfaklar.isNullOrEmpty()) {
                viewModel.siralamaYap(BottomNavMenuViewModel.SiralamaTipi.YILDIZ_PUANI)
            }
        }

        viewModel.filtrelenmisListe.observe(viewLifecycleOwner) { filtrelenmisListe ->
            Log.d("Fragment", "Filtrelenmiş liste güncellendi: ${filtrelenmisListe?.size}")

            if (::mutfakAdapter.isInitialized) {
                // Listeyi güncelle
                mutfakAdapter.mutfakListesi = filtrelenmisListe
                mutfakAdapter.notifyDataSetChanged()
            } else {
                // Adapter'ı ilk kez oluştur
                mutfakAdapter = MutfakAdapter(requireContext(), filtrelenmisListe)
                mutfakAdapter.favoriteClickListener = { mutfak ->
                    viewModel.favoriyeEkleVeyaCikar(mutfak)
                }
                mutfakAdapter.isFavoriteCheck = { mutfakId ->
                    viewModel.isFavorite(mutfakId)
                }
                mutfakAdapter.itemClickListener = {
                    val bundle = Bundle().apply {
                        putSerializable("mutfak", it)
                    }
                    findNavController().navigate(R.id.action_bottomNavMenuFragment_to_mutfakDetayFragment, bundle)
                }
                binding.mutfakAdapter = mutfakAdapter
            }

            // Ek olarak: adapter boş listeyle de oluşturulmalı (her zaman null değil)
            if (filtrelenmisListe.isNullOrEmpty()) {
                Log.d("Fragment", "Uyarı: filtrelenmiş liste BOŞ.")
            }
        }


        // Kategori listesini gözlemle
        viewModel.kategoriListe.observe(viewLifecycleOwner) { kategoriListesi ->
            if (kategoriListesi.isNotEmpty()) {
                val kategoriAdapter = KategoriAdapter(requireContext(), kategoriListesi)
                binding.kategoriAdapter = kategoriAdapter
                Log.d("Fragment", "Kategori listesi güncellendi: ${kategoriListesi.size}")
            }
        }

        viewModel.oneriListe.observe(viewLifecycleOwner) { oneriListesi ->
            if (oneriListesi.isNotEmpty()) {
                val oneriAdapter = OneriAdapter(requireContext(), oneriListesi)
                binding.oneriAdapter = oneriAdapter
                Log.d("Fragment", "Öneri listesi güncellendi: ${oneriListesi.size}")
            }
        }
    }
    //Direkt viewmodel Kullanamadigimiz icin burası sart
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val tempViewModel: BottomNavMenuViewModel by viewModels()
        viewModel = tempViewModel
    }
}