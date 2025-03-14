package com.example.evlezzeti.ui.fragment.girissayfasi

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.fragment.app.viewModels
import androidx.navigation.Navigation
import androidx.navigation.fragment.findNavController
import com.example.evlezzeti.BottomNavActivity
import com.example.evlezzeti.R
import com.example.evlezzeti.databinding.FragmentGirisYapBinding
import com.example.evlezzeti.ui.viewmodel.GirisYapViewModel
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.ApiException
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.GoogleAuthProvider
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class GirisYapFragment : Fragment() {
    private lateinit var binding : FragmentGirisYapBinding
    private lateinit var viewModel: GirisYapViewModel
    private lateinit var auth: FirebaseAuth
    private lateinit var googleSignInClient: GoogleSignInClient
    private val googleSignInLauncher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
        val data = result.data
        val task = GoogleSignIn.getSignedInAccountFromIntent(data)
        try {
            val account = task.getResult(ApiException::class.java)
            firebaseAuthWithGoogle(account.idToken!!)
        } catch (e: ApiException) {
            Toast.makeText(requireContext(), "Google Giriş Başarısız: ${e.message}", Toast.LENGTH_LONG).show()
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,savedInstanceState: Bundle?): View{
        binding = FragmentGirisYapBinding.inflate(inflater, container, false)
        auth = FirebaseAuth.getInstance()

        val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestIdToken(getString(R.string.default_web_client_id))
            .requestEmail()
            .build()

        googleSignInClient = GoogleSignIn.getClient(requireActivity(), gso)

        // Google Butonuna Tıklanınca
        binding.googleBaglanButton.setOnClickListener {
            googleSignIn()
        }


        // Animasyon icin
        binding.lottieAnimationView.setAnimation(R.raw.giris_gif_json)
        binding.lottieAnimationView.playAnimation()


        // Kullanici giris yapma kontrolu
        binding.girisYapButton.setOnClickListener{
            val ePosta = binding.ePostaEditText.text.toString()
            val sifre = binding.sifreEditText.text.toString()

            if (ePosta.isEmpty() || sifre.isEmpty()){
                Toast.makeText(requireContext(), "Lütfen tüm alanları doldurun!", Toast.LENGTH_LONG).show()
                return@setOnClickListener
            }
            else {
                val durum = viewModel.kullaniciGirisKontrol(ePosta,sifre)

                // Giris basarili
                if (durum){
                    Navigation.findNavController(it).navigate(R.id.girisYapToBottomNav)
                    val intent = Intent(requireContext(), BottomNavActivity::class.java)
                    intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK

                    startActivity(intent)
                }
                // Giris basarisiz
                else {
                    Toast.makeText(requireContext(), "E-Posta veya şifre Hatalı!!", Toast.LENGTH_LONG).show()
                }
            }
        }

        // EPostaKaydol gecis
        binding.ePostaKaydolButton.setOnClickListener{
            Navigation.findNavController(it).navigate(R.id.girisYapToEPostaKaydol)
        }

        return binding.root
    }

    private fun googleSignIn() {
        val signInIntent = googleSignInClient.signInIntent
        googleSignInLauncher.launch(signInIntent)
    }

    private fun firebaseAuthWithGoogle(idToken: String) {
        val credential = GoogleAuthProvider.getCredential(idToken, null)
        auth.signInWithCredential(credential)
            .addOnCompleteListener(requireActivity()) { task ->
                if (task.isSuccessful) {
                    val user = auth.currentUser
                    Toast.makeText(requireContext(), "Hoşgeldin, ${user?.displayName}!", Toast.LENGTH_LONG).show()

                    // Giriş başarılıysa Ana Sayfa'ya yönlendir
                    findNavController().navigate(R.id.girisYapToBottomNav)
                    val intent = Intent(requireContext(), BottomNavActivity::class.java)
                    intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK

                    startActivity(intent)
                } else {
                    Toast.makeText(requireContext(), "Giriş başarısız: ${task.exception?.message}", Toast.LENGTH_LONG).show()
                }
            }
    }

    //Direkt viewmodel Kullanamadigimiz icin burası sart
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val tempViewModel: GirisYapViewModel by viewModels()
        viewModel = tempViewModel
    }

}