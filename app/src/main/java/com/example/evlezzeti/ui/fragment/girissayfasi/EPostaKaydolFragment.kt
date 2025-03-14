package com.example.evlezzeti.ui.fragment.girissayfasi

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.navigation.fragment.findNavController
import com.example.evlezzeti.databinding.FragmentEPostaKaydolBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseAuthException
import com.google.firebase.firestore.FirebaseFirestore

class EPostaKaydolFragment : Fragment() {
    private lateinit var binding:FragmentEPostaKaydolBinding
    private lateinit var auth: FirebaseAuth

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,savedInstanceState: Bundle?): View{
        binding = FragmentEPostaKaydolBinding.inflate(inflater, container, false)
        auth = FirebaseAuth.getInstance()

        binding.kaydolButton.setOnClickListener{
            val eposta = binding.ePostaEditText.text.toString()
            val sifre = binding.sifreEditText.text.toString()
            val sifreTekrar = binding.sifreTekrarEditText.text.toString()

            if (eposta.isEmpty() || sifre.isEmpty()){
                Toast.makeText(requireContext(), "Lütfen tüm alanları doldurun!", Toast.LENGTH_LONG).show()
                return@setOnClickListener
            }
            else{
                if(sifre != sifreTekrar){
                    Toast.makeText(requireContext(), "Girdiğiniz şifreler aynı olmalı!", Toast.LENGTH_LONG).show()
                    return@setOnClickListener
                }
                else{
                    // Basarili kayit sonucu burasi
                    auth.createUserWithEmailAndPassword(eposta, sifre)
                        .addOnCompleteListener { task ->
                            if (task.isSuccessful) {
                                val user = auth.currentUser
                                if (user != null) {
                                    val userMap = hashMapOf(
                                        "uid" to user.uid,
                                        "email" to eposta,
                                        "isVerified" to false
                                    )
                                    FirebaseFirestore.getInstance().collection("users")
                                        .document(user.uid)
                                        .set(userMap)
                                        .addOnSuccessListener {
                                            Toast.makeText(requireContext(), "Kayıt başarılı! Lütfen giriş yapın.", Toast.LENGTH_LONG).show()
                                            // GirisSayfasina gecis
                                            findNavController().navigateUp()
                                        }
                                        .addOnFailureListener { e ->
                                            Toast.makeText(requireContext(), "Veritabanı hatası: ${e.message}", Toast.LENGTH_LONG).show()
                                        }
                                }
                            } else {
                                val hataMesaji = when ((task.exception as? FirebaseAuthException)?.errorCode) {
                                    "ERROR_EMAIL_ALREADY_IN_USE" -> "Bu e-posta adresi zaten kullanımda!"
                                    "ERROR_WEAK_PASSWORD" -> "Şifre en az 6 karakter olmalıdır!"
                                    "ERROR_INVALID_EMAIL" -> "Geçersiz e-posta adresi!"
                                    "ERROR_NETWORK_REQUEST_FAILED" -> "İnternet bağlantınızı kontrol edin!"
                                    else -> "Kayıt başarısız: ${task.exception?.localizedMessage}"
                                }
                                Toast.makeText(requireContext(), hataMesaji, Toast.LENGTH_LONG).show()
                            }
                        }
                }
            }
        }

        return binding.root
    }

}