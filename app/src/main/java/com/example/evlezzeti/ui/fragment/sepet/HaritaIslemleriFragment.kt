package com.example.evlezzeti.ui.fragment.sepet

import android.Manifest
import android.content.pm.PackageManager
import android.location.Geocoder
import android.location.Location
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.example.evlezzeti.R
import com.example.evlezzeti.data.entity.Kullanici
import com.example.evlezzeti.databinding.FragmentHaritaIslemleriBinding
import com.example.evlezzeti.ui.fragment.girissayfasi.KullaniciVerifiedArgs
import com.example.evlezzeti.ui.viewmodel.HaritaIslemleriViewModel
import com.example.evlezzeti.ui.viewmodel.SharedKullaniciViewModel
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import dagger.hilt.android.AndroidEntryPoint
import java.util.Locale

@AndroidEntryPoint
class HaritaIslemleriFragment : Fragment() ,OnMapReadyCallback{
    private lateinit var binding: FragmentHaritaIslemleriBinding
    private lateinit var viewModel: HaritaIslemleriViewModel
    private lateinit var fusedLocationClient: FusedLocationProviderClient
    private val konumIzinKodu = 1001
    private lateinit var mMap :GoogleMap
    private lateinit var kullaniciId :String
    private lateinit var kullaniciEPosta :String
    private val sharedKullaniciViewModel: SharedKullaniciViewModel by activityViewModels()
    private lateinit var kullanici: Kullanici
    private var kullaniciGuncelle = false
    val bundle: HaritaIslemleriFragmentArgs by navArgs()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_harita_islemleri, container, false)
        binding.haritaIslemleriFragment = this
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(requireActivity())

        sharedKullaniciViewModel.kullaniciId.observe(viewLifecycleOwner) {// sepet kayit icin kullanici id aldik
            kullaniciId = it
        }
        sharedKullaniciViewModel.kullaniciEposta.observe(viewLifecycleOwner){
            kullaniciEPosta = it
        }

        binding.buttonKonumAl.setOnClickListener {//Suanki konumu al
            konumuAl()
        }

        binding.buttonAdresKaydet.setOnClickListener {
            val adresBasligi = binding.adresBasligiEditText.text.toString().trim()
            val binaNo = binding.adresBinaNoEditText.text.toString().trim()
            val kat = binding.adresKatEditText.text.toString().trim()
            val daireNo = binding.adresDaireNoEditText.text.toString().trim()
            val telefon = binding.adresTelefonNoEditText.text.toString().trim()
            val isim = binding.adresIsimEditText.text.toString().trim()

            //Bos alan var mi
            if (adresBasligi.isEmpty() || binaNo.isEmpty() || kat.isEmpty() || daireNo.isEmpty() || telefon.isEmpty()) {
                Toast.makeText(requireContext(), "Lütfen tüm adres alanlarını doldurun!", Toast.LENGTH_SHORT).show()
                return@setOnClickListener // Tıklama olayını burada sonlandır
            }

            //Bos degilse islemlere devam
            if (::kullaniciId.isInitialized && ::kullaniciEPosta.isInitialized) {
                kullanici = Kullanici()
                kullanici.kullaniciId = kullaniciId
                kullanici.kullaniciAd = isim
                kullanici.ePosta = kullaniciEPosta
                kullanici.favoriler = "?" //Default favoriler bilgisi
                kullanici.kullaniciAdress = "${binding.textViewKonumBilgisi.text} , $adresBasligi , $binaNo , $kat , $daireNo , $isim"
                kullanici.kullaniciTelefon = telefon
                kullanici.kullaniciEnlem = mMap.cameraPosition.target.latitude.toString()
                kullanici.kullaniciBoylam = mMap.cameraPosition.target.longitude.toString()

                kullaniciGuncelle = bundle.kullaniciGuncelleme
                if (kullaniciGuncelle == false) {
                    viewModel.kullaniciKaydet(kullanici)
                    Toast.makeText(requireContext(), "${kullanici.kullaniciAd} adresi kaydedildi!", Toast.LENGTH_SHORT).show()
                }
                else{
                    viewModel.kullaniciGuncelle(kullanici)
                    Toast.makeText(requireContext(), "${kullanici.kullaniciAd} adresi guncellendi!", Toast.LENGTH_SHORT).show()
                }
                findNavController().navigateUp()
            } else {
                Toast.makeText(requireContext(), "Kullanıcı ID yüklenemedi!", Toast.LENGTH_SHORT).show()
            }
        }

        //Tasarimdaki haritayi göstermek icin kod
        val mapFragment = SupportMapFragment.newInstance()
        childFragmentManager.beginTransaction()
            .replace(R.id.mapContainer, mapFragment)
            .commit()
        mapFragment.getMapAsync(this)


        return binding.root
    }

    private fun konumuAl() {
        if (ContextCompat.checkSelfPermission(requireContext(), Manifest.permission.ACCESS_FINE_LOCATION)
            != PackageManager.PERMISSION_GRANTED) {
            // İzin yoksa kullanıcıdan izin iste
            requestPermissions(arrayOf(Manifest.permission.ACCESS_FINE_LOCATION), konumIzinKodu)
        } else {
            // İzin varsa konumu al
            fusedLocationClient.lastLocation.addOnSuccessListener { location: Location? ->
                if (location != null) {
                    val konumBilgisi = "Alınan Konum: ${location.latitude}, Boylam: ${location.longitude}"
                    //Burada konum bilgisini haritanın tam ortasına eklemek istiyorum
                    if (::mMap.isInitialized) {
                        val latLng = LatLng(location.latitude, location.longitude)
                        mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(latLng, 16f))
                    }
                } else {
                    binding.textViewKonumBilgisi.text = "Konum alınamadı."
                }
            }
        }
    }

    override fun onRequestPermissionsResult(
        requestCode: Int, permissions: Array<out String>, grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == konumIzinKodu) {
            if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                konumuAl()
            } else {
                binding.textViewKonumBilgisi.text = "Konum izni reddedildi."
            }
        }
    }

    override fun onMapReady(googleMap: GoogleMap) {
        mMap =googleMap
        //38.707905, 35.524604
        var konum = LatLng(38.707905, 35.524604)
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(konum,16f)) //Bu ekranda gosterilen yer

        mMap.setOnCameraIdleListener {
            val merkez = mMap.cameraPosition.target
            val enlem = merkez.latitude
            val boylam = merkez.longitude
            latLngToAdres(enlem, boylam)
        }


    }
    private fun latLngToAdres(latitude: Double, longitude: Double) { //Alinan konum bilgisini adrese ceviriyor
        val geocoder = Geocoder(requireContext(), Locale.getDefault())
        try {
            val adresListesi = geocoder.getFromLocation(latitude, longitude, 1)
            if (!adresListesi.isNullOrEmpty()) {
                val adres = adresListesi[0]
                val adresSatiri = adres.getAddressLine(0) // Tüm adresi tek satırda verir
                binding.textViewKonumBilgisi.text = adresSatiri
            } else {
                binding.textViewKonumBilgisi.text = "Adres bulunamadı."
            }
        } catch (e: Exception) {
            e.printStackTrace()
            binding.textViewKonumBilgisi.text = "Adres alınırken hata oluştu."
        }
    }
    //Direkt viewmodel Kullanamadigimiz icin burasi sart
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val tempViewModel: HaritaIslemleriViewModel by viewModels()
        viewModel = tempViewModel
    }

}
