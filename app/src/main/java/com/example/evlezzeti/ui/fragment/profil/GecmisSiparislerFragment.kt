package com.example.evlezzeti.ui.fragment.profil

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.evlezzeti.databinding.FragmentGecmisSiparislerBinding
import com.example.evlezzeti.ui.adapter.GecmisSiparislerAdapter
import com.example.evlezzeti.ui.viewmodel.GecmisSiparislerViewModel
import com.example.evlezzeti.ui.viewmodel.SharedKullaniciViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class GecmisSiparislerFragment : Fragment() {
    private lateinit var binding: FragmentGecmisSiparislerBinding
    private lateinit var viewModel: GecmisSiparislerViewModel
    private val sharedKullaniciViewModel : SharedKullaniciViewModel by activityViewModels()
    val bundle: GecmisSiparislerFragmentArgs by navArgs()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
       binding = FragmentGecmisSiparislerBinding.inflate(inflater, container, false)

        val kullaniciId = sharedKullaniciViewModel.kullaniciId.value.toString()
        Log.e("GecmisSiparislerFragment", "kullaniciId: $kullaniciId")

        viewModel.siparisleriGetir(kullaniciId)

        binding.gecmisSiparislerRV.layoutManager = LinearLayoutManager(requireContext())



        viewModel.siparisListesi.observe(viewLifecycleOwner) { siparisler ->
            binding.gecmisSiparislerRV.adapter = GecmisSiparislerAdapter(requireContext(), siparisler)
        }

        return binding.root
    }

    //Direkt viewmodel Kullanamadigimiz icin burası sart
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val tempViewModel: GecmisSiparislerViewModel by viewModels()
        viewModel = tempViewModel
    }

}