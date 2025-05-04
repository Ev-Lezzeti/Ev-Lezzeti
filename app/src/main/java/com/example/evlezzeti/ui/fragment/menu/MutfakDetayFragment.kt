package com.example.evlezzeti.ui.fragment.menu

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.navArgs
import com.example.evlezzeti.R
import com.example.evlezzeti.databinding.FragmentMutfakDetayBinding
import com.example.evlezzeti.ui.adapter.YemekAdapter
import com.example.evlezzeti.ui.viewmodel.MutfakDetayViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MutfakDetayFragment : Fragment() {
    private val args: MutfakDetayFragmentArgs by navArgs()
    private lateinit var binding: FragmentMutfakDetayBinding
    private lateinit var viewModel: MutfakDetayViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_mutfak_detay, container, false)

        // Mutfak nesnesini binding'e ata
        binding.mutfakNesne = args.mutfak

        // Binding'i hemen uygula
        binding.executePendingBindings()

        //Yemekleri yükleme
        viewModel.yemekListe.observe(viewLifecycleOwner){ yemekler ->
            val yemekAdapter = YemekAdapter(requireContext(), yemekler)
            binding.yemekAdapter = yemekAdapter
        }

        return binding.root
    }

    //Direkt viewmodel Kullanamadigimiz icin burası sart
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val tempViewModel: MutfakDetayViewModel by viewModels()
        viewModel = tempViewModel
    }
}