package com.example.evlezzeti.ui.fragment

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.viewModels
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
        binding.oneriAdapter

        // Filtreleme chip'lerinin tıklama olaylarını ayarla
        setupFilterChips()

        // Mutfak ve Kategori listelerini gözlemle
        setupObservers()

        return binding.root
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val tempViewModel: BottomNavMenuViewModel by viewModels()
        viewModel = tempViewModel
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
        // Filtrelenmiş listeyi gözlemle
        viewModel.filtrelenmisListe.observe(viewLifecycleOwner) { filtrelenmisListe ->
            // Filtrelenmiş liste boş değilse adapter'ı güncelle
            if (filtrelenmisListe != null) {
                if (::mutfakAdapter.isInitialized) {
                    mutfakAdapter.mutfakListesi = filtrelenmisListe
                    mutfakAdapter.notifyDataSetChanged()
                } else {
                    // İlk kez oluşturuluyorsa adapter'ı oluştur ve favori callback'i ekle
                    mutfakAdapter = MutfakAdapter(requireContext(), filtrelenmisListe)
                    mutfakAdapter.favoriteClickListener = { mutfak ->
                        viewModel.favoriyeEkleVeyaCikar(mutfak)
                    }
                    mutfakAdapter.isFavoriteCheck = { mutfakId ->
                        viewModel.isFavorite(mutfakId)
                    }
                    binding.mutfakAdapter = mutfakAdapter
                }
            }
        }

        // Kategori listesini gözlemle
        viewModel.kategoriListe.observe(viewLifecycleOwner) { kategoriListesi ->
            val kategoriAdapter = KategoriAdapter(requireContext(), kategoriListesi)
            binding.kategoriAdapter = kategoriAdapter
            Log.e("FragmentBilgi", "${kategoriListesi[0].kategoriAd} Bilgii")
        }

        viewModel.oneriListe.observe(viewLifecycleOwner) {
            val oneriAdapter = OneriAdapter(requireContext(), it)
            binding.oneriAdapter = oneriAdapter
        }
    }
}