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
import com.example.evlezzeti.ui.viewmodel.BottomNavMenuViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class BottomNavMenuFragment : Fragment() {
    private lateinit var binding: FragmentBottomNavMenuBinding
    private lateinit var viewModel: BottomNavMenuViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DataBindingUtil.inflate(inflater,R.layout.fragment_bottom_nav_menu,container,false)

        // Xml'den buraya eriserek burdaki fonksiyonları oraya tasiyabiliriz
        binding.bottomNavMenuFragment = this

        // Burda surekli viewModeli dinleyerek LiveData ile calisiyoruz
        viewModel.mutfakListe.observe(viewLifecycleOwner){
            val mutfakAdapter = MutfakAdapter(requireContext(),it)
            binding.mutfakAdapter = mutfakAdapter
            Log.e("FragmentBilgi", "${it[0].mutfakAd} Bilgii")
        }

        viewModel.kategoriListe.observe(viewLifecycleOwner){
            val kategoriAdapter = KategoriAdapter(requireContext(),it)
            binding.kategoriAdapter = kategoriAdapter
            Log.e("FragmentBilgi", "${it[0].kategoriAd} Bilgii")
        }


        return binding.root
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val tempViewModel:BottomNavMenuViewModel by viewModels()
        viewModel = tempViewModel
    }
}