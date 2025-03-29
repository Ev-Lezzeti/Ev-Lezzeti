package com.example.evlezzeti.ui.fragment.girissayfasi

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.example.evlezzeti.databinding.FragmentKullaniciVerifiedBinding
import com.example.evlezzeti.ui.viewmodel.KullaniciVerifiedViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class KullaniciVerified : Fragment() {
    private lateinit var binding: FragmentKullaniciVerifiedBinding
    private lateinit var viewModel: KullaniciVerifiedViewModel
    val bundle:KullaniciVerifiedArgs by navArgs()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentKullaniciVerifiedBinding.inflate(inflater, container, false)

        val otp1 = binding.otp1
        val otp2 = binding.otp2
        val otp3 = binding.otp3
        val otp4 = binding.otp4

        val editText = listOf(otp1, otp2, otp3, otp4)

        for(i in editText.indices){//OTP textInput alani icin dinleyici
            editText[i].addTextChangedListener(object : TextWatcher{
                override fun afterTextChanged(s: Editable?) {
                    if (s?.length == 1 && i < editText.size - 1) {
                        editText[i + 1].requestFocus()
                    } else if (s!!.isEmpty() && i > 0) {
                        editText[i - 1].requestFocus()
                    }
                }
                override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
                override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}
            })
        }
        binding.btnVerify.setOnClickListener {//ePostayi Verified ettigimiz kisim
            if (otp1.text.isNullOrEmpty() || otp2.text.isNullOrEmpty() || otp3.text.isNullOrEmpty() || otp4.text.isNullOrEmpty()){
                Toast.makeText(requireContext(), "Lütfen tüm alanları doldurun!", Toast.LENGTH_LONG).show()
                return@setOnClickListener
            }
            else {
                viewModel.otpGuncelle(bundle.ePosta)
                findNavController().navigateUp()
            }
        }

        binding.textViewePosta.text = bundle.ePosta
        return binding.root
    }

    //Direkt viewmodel Kullanamadigimiz icin burasi sart
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val tempViewModel: KullaniciVerifiedViewModel by viewModels()
        viewModel = tempViewModel
    }

}