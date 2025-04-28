package com.example.evlezzeti.ui.fragment

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.navigation.fragment.navArgs
import com.example.evlezzeti.R
import com.example.evlezzeti.databinding.FragmentMutfakDetayBinding
import com.example.evlezzeti.ui.viewmodel.MutfakDetayViewModel

class MutfakDetayFragment : Fragment() {
    private val args: MutfakDetayFragmentArgs by navArgs()
    private lateinit var binding: FragmentMutfakDetayBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_mutfak_detay, container, false)

        // Mutfak nesnesini binding'e ata
        binding.mutfakNesne = args.mutfak

        // Binding'i hemen uygula
        binding.executePendingBindings()

        return binding.root
    }
}