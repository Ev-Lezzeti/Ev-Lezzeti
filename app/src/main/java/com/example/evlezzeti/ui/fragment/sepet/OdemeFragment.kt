package com.example.evlezzeti.ui.fragment.sepet

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.ContextThemeWrapper
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.navigation.findNavController
import com.example.evlezzeti.R
import com.example.evlezzeti.data.entity.Siparis
import com.example.evlezzeti.databinding.FragmentOdemeBinding
import com.example.evlezzeti.ui.viewmodel.OdemeViewModel
import com.example.evlezzeti.ui.viewmodel.SharedKullaniciViewModel
import com.example.evlezzeti.ui.viewmodel.SharedSepetViewModel
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import dagger.hilt.android.AndroidEntryPoint
import java.util.Calendar
import java.util.Locale

@AndroidEntryPoint
class OdemeFragment : Fragment() , OnMapReadyCallback {
    private lateinit var binding: FragmentOdemeBinding
    private lateinit var viewModel: OdemeViewModel
    private lateinit var mMap : GoogleMap
    private val sharedSepetViewModel: SharedSepetViewModel by activityViewModels()
    private val sharedKullaniciViewModel: SharedKullaniciViewModel by activityViewModels()
    private lateinit var kullaniciId: String
    private lateinit var konum :  LatLng
    private var isMapReady = false
    private var odemesekli = "Kapıda Ödeme"
    private lateinit var satirTextView: TextView
    private lateinit var indirimTextView: TextView
    private lateinit var mutfakAdTextView :TextView
    private var siparizOzet = ArrayList<String>()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentOdemeBinding.inflate(inflater, container, false)

        binding.odemeFragment = this

        kullaniciId = sharedKullaniciViewModel.kullaniciId.value ?: ""
        viewModel.kullaniciAl(kullaniciId)

        viewModel.kullanici.observe(viewLifecycleOwner){
            if (it != null) {
                binding.textViewAdres.text = it.kullaniciAdress
                konum = LatLng(it.kullaniciEnlem!!.toDouble(), it.kullaniciBoylam!!.toDouble())

                if (isMapReady) {
                    mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(konum, 16f))
                }
            }
        }
        //Tasarimdaki haritayi göstermek icin kod
        val mapFragment = SupportMapFragment.newInstance()
        childFragmentManager.beginTransaction()
            .replace(R.id.mapContainer, mapFragment)
            .commit()
        mapFragment.getMapAsync(this)

        val sepetListesi = sharedSepetViewModel.sepetListesi //Siparis listesi burada
        val fiyatListesi = sharedSepetViewModel.fiyat //Fiyat listesi burada
        val ozetLayout = binding.sepetOzetLinearLayout
        ozetLayout.removeAllViews() // eskiyi temizle

        var toplamFiyat = 0.0
        var indirimFiyati = 0.0

        mutfakAdTextView = TextView(ContextThemeWrapper(requireContext(), R.style.KucukTextStyle)).apply {
            text = "${sepetListesi[0].mutfakAd}"
            textSize = 18f
            setTextColor(resources.getColor(R.color.anaRenk, null))
            setPadding(0, 16, 0, 0)
        }
        ozetLayout.addView(mutfakAdTextView)

        for (sepet in sepetListesi) {
            val adet = sepet.yemekAdet?.toIntOrNull() ?: 0
            val fiyat = fiyatListesi[sepetListesi.indexOf(sepet)]
            val yemekAdi = sepet.yemekAd ?: "Bilinmeyen"

            toplamFiyat += fiyat

            satirTextView = TextView(ContextThemeWrapper(requireContext(), R.style.KucukTextStyle)).apply {
                text = "$adet x $yemekAdi   ${String.format("%.2f", fiyat)} ₺"
                siparizOzet.add(text.toString())
                textSize = 16f
                setTextColor(resources.getColor(R.color.black, null))
            }
            ozetLayout.addView(satirTextView)
        }

        // Toplam satırını ekle
        val toplamTextView = TextView(ContextThemeWrapper(requireContext(), R.style.KucukTextStyle)).apply {
            text = "Toplam: ${String.format("%.2f", toplamFiyat)} ₺"
            textSize = 18f
            setTextColor(resources.getColor(R.color.anaRenk, null))
            setPadding(0, 16, 0, 0)
        }
        ozetLayout.addView(toplamTextView)
        indirimFiyati = toplamFiyat * 0.33
        binding.textViewOdemeFiyat.text = "Toplam: ${String.format("%.2f", toplamFiyat - indirimFiyati)} ₺"

        indirimTextView = TextView(ContextThemeWrapper(requireContext(), R.style.KucukTextStyle)).apply {
            text = "İndirim: ${String.format("%.2f", indirimFiyati)} ₺"
            textSize = 18f
            setTextColor(resources.getColor(R.color.anaRenk, null))
            setPadding(0, 16, 0, 0)
        }
        ozetLayout.addView(indirimTextView)

        //Odeme sekli secimi
        val radioGroup = binding.odemeSecenekleriRadioGroup

        radioGroup.setOnCheckedChangeListener { _, checkedId ->
            when (checkedId) {
                R.id.radioKartlaOde -> {
                    //Kartla odeme
                    binding.radioKartlaOde.setTextColor(resources.getColor(R.color.anaRenk, null))
                    binding.radioKapidaOde.setTextColor(resources.getColor(R.color.black, null))
                    binding.kartBilgisiMCV.visibility = View.VISIBLE
                    odemesekli = "Kartla Ödeme"
                }
                R.id.radioKapidaOde -> {
                    //Kapida odeme
                    binding.radioKartlaOde.setTextColor(resources.getColor(R.color.black, null))
                    binding.radioKapidaOde.setTextColor(resources.getColor(R.color.anaRenk, null))
                    binding.kartBilgisiMCV.visibility = View.GONE
                    odemesekli = "Kapıda Ödeme"
                }
            }
        }
        //EditTexler icin format duzenleme
        binding.kartNumarasiEditText.addTextChangedListener(object : TextWatcher {
            private var isFormatting = false
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}

            override fun afterTextChanged(s: Editable?) {
                if (isFormatting) return
                isFormatting = true

                s?.let {
                    val unformatted = it.toString().replace(" ", "")
                    val formatted = unformatted.chunked(4).joinToString(" ")
                    if (formatted != it.toString()) {
                        binding.kartNumarasiEditText.setText(formatted)
                        binding.kartNumarasiEditText.setSelection(formatted.length)
                    }
                }

                isFormatting = false
            }
        })
        binding.sonKullanmaEditText.addTextChangedListener(object : TextWatcher {
            var isFormatting = false
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}

            override fun afterTextChanged(s: Editable?) {
                if (isFormatting) return

                isFormatting = true
                val input = s.toString().replace("/", "") // eski /'yi temizle
                val builder = StringBuilder()

                for (i in input.indices) {
                    builder.append(input[i])
                    if (i == 1 && input.length > 2) {
                        builder.append("/") // 2. karakterden sonra / ekle
                    }
                }

                binding.sonKullanmaEditText.setText(builder.toString())
                binding.sonKullanmaEditText.setSelection(builder.length) // imleci sona al
                isFormatting = false
            }
        })
        //Odeme buton islemleri
        binding.buttonOdemeYap.setOnClickListener {
            val takvim = Calendar.getInstance()
            val formatDuzen = java.text.SimpleDateFormat("dd.MM.yyyy HH:mm", Locale.getDefault())
            val duzenliZaman = formatDuzen.format(takvim.time)

            val kartSahibi= binding.kartSahibiEditText.text.toString()
            val kartNumarasi = binding.kartNumarasiEditText.text.toString()
            val sonKullanma = binding.sonKullanmaEditText.text.toString()
            val cvv = binding.cvvEditText.text.toString()

            //Odeme sekli secili mi
            if (binding.radioKapidaOde.isChecked || binding.radioKartlaOde.isChecked){
                //Kartla odeme secili
                if (binding.radioKartlaOde.isChecked ) {
                    //Kart bilgisi kontrol
                    if (kartSahibi.isNotEmpty() && kartNumarasi.isNotEmpty() && sonKullanma.isNotEmpty() && cvv.isNotEmpty()){
                        var siparis = Siparis(" ",
                            kullaniciId,
                            mutfakAdTextView.text.toString(),
                            siparizOzet.toString(),
                            binding.textViewOdemeFiyat.text.toString(),
                            binding.textViewOdemeFiyat.text.toString(),
                            odemesekli,
                            konum.latitude.toString(),
                            konum.longitude.toString(),
                            binding.textViewAdres.text.toString(),
                            duzenliZaman,
                            binding.kuryeNotEditText.text.toString() ?: " ",
                            "Sipariş Onaylandı")
                        viewModel.siparisEkle(siparis)
                        sharedSepetViewModel.sepetListeTemizle()
                        sharedSepetViewModel.yemekFiyatlariTemizle()
                        sharedSepetViewModel.toplamFiyatGuncelle(0.0)
                        Toast.makeText(requireContext(), "Ödeme Başarılı", Toast.LENGTH_SHORT).show()
                        binding.kartBilgisiMCV.visibility = View.GONE
                        it.findNavController().navigate(R.id.odemeFragmentToBottomNavMenuFragment)
                    }
                    //Kart bilgileri bos
                    else {
                        Toast.makeText(
                            requireContext(),
                            "Lütfen Kart Bilgilerinizi Eksiksiz Giriniz!",
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                }
                //Kapida odeme secili
                else{
                    var siparis = Siparis(" ",
                        kullaniciId,
                        mutfakAdTextView.text.toString(),
                        siparizOzet.toString(),
                        binding.textViewOdemeFiyat.text.toString(),
                        binding.textViewOdemeFiyat.text.toString(),
                        odemesekli,
                        konum.latitude.toString(),
                        konum.longitude.toString(),
                        binding.textViewAdres.text.toString(),
                        duzenliZaman,
                        binding.kuryeNotEditText.text.toString() ?: " ",
                        "Sipariş Onaylandı")
                    viewModel.siparisEkle(siparis)
                    sharedSepetViewModel.sepetListeTemizle()
                    sharedSepetViewModel.yemekFiyatlariTemizle()
                    sharedSepetViewModel.toplamFiyatGuncelle(0.0)
                    Toast.makeText(requireContext(), "Ödeme Başarılı, Siparişiniz Alındı!", Toast.LENGTH_SHORT).show()
                    it.findNavController().navigate(R.id.odemeFragmentToBottomNavMenuFragment)
                }
            }
            //Odeme sekli secili degil
            else{
                Toast.makeText(requireContext(), "Ödeme Seçimi Yapılmadı!", Toast.LENGTH_SHORT).show()
            }
        }



        return binding.root
    }

    override fun onMapReady(googleMap: GoogleMap) {
        mMap = googleMap
        isMapReady = true

        //Konum geldiyse haritayı guncelle
        if (::konum.isInitialized) {
            mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(konum, 16f))
        }
    }

    //Direkt viewmodel Kullanamadigimiz icin burasi sart
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val tempViewModel: OdemeViewModel by viewModels()
        viewModel = tempViewModel
    }

}