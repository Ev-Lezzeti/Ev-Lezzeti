package com.example.evlezzeti.ui.fragment.menu

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.navArgs
import com.bumptech.glide.Glide
import com.example.evlezzeti.R
import com.example.evlezzeti.databinding.FragmentMutfakDetayBinding
import com.example.evlezzeti.ui.adapter.YemekAdapter
import com.example.evlezzeti.ui.viewmodel.MutfakDetayViewModel
import com.google.firebase.storage.FirebaseStorage
import dagger.hilt.android.AndroidEntryPoint
import android.util.Log

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

        // Firebase Storage'dan mutfak resmini yükle
        val mutfakResim = args.mutfak.mutfakResim
        if (!mutfakResim.isNullOrEmpty()) {
            loadImageFromFirebase(mutfakResim, binding.imageViewMutfak)
        }

        // Binding'i hemen uygula
        binding.executePendingBindings()

        //Yemekleri yükleme
        viewModel.yemekListe.observe(viewLifecycleOwner){ yemekler ->
            val yemekAdapter = YemekAdapter(requireContext(), yemekler)
            binding.yemekAdapter = yemekAdapter
        }

        return binding.root
    }

    /**
     * Firebase Storage'dan resim yükler
     */
    private fun loadImageFromFirebase(imageName: String, imageView: ImageView) {
        try {
            // Firebase Storage referansı
            val storageRef = FirebaseStorage.getInstance().reference

            // "mutfak" klasöründeki resmi referans al
            val imageRef = storageRef.child("mutfak/$imageName.webp")

            Log.d("FirebaseStorage", "Resim yükleniyor: mutfak/$imageName.webp")

            // Resmin URL'sini al
            imageRef.downloadUrl.addOnSuccessListener { uri ->
                // Fragment hala yaşıyor mu kontrol et
                if (isAdded && context != null) {
                    // Glide ile resmi yükle
                    Glide.with(requireContext())
                        .load(uri)
                        .placeholder(R.drawable.placeholder_image)
                        .error(R.drawable.error_image)
                        .into(imageView)

                    Log.d("FirebaseStorage", "Resim başarıyla yüklendi: $uri")
                }
            }.addOnFailureListener { exception ->
                // Hata durumunda
                Log.e("FirebaseStorage", "Resim yükleme hatası: ${exception.message}", exception)

                // Hata durumunda varsayılan resmi göster
                imageView.setImageResource(R.drawable.default_image)
            }
        } catch (e: Exception) {
            Log.e("FirebaseStorage", "Genel hata: ${e.message}", e)
            imageView.setImageResource(R.drawable.default_image)
        }
    }

    //Direkt viewmodel Kullanamadigimiz icin burası sart
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val tempViewModel: MutfakDetayViewModel by viewModels()
        viewModel = tempViewModel
    }
}