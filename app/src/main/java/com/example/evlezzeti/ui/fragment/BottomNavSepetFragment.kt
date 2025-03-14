package com.example.evlezzeti.ui.fragment

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import com.example.evlezzeti.R
import com.example.evlezzeti.databinding.FragmentBottomNavSepetBinding

class BottomNavSepetFragment : Fragment() {
    private lateinit var binding: FragmentBottomNavSepetBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_bottom_nav_sepet, container, false)
        binding.bottomNavSepetFragment = this

        return binding.root
    }
}