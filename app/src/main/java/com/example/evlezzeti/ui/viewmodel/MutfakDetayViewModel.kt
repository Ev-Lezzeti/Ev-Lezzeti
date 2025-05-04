package com.example.evlezzeti.ui.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.evlezzeti.data.entity.Yemek
import com.example.evlezzeti.data.repo.Repository
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class MutfakDetayViewModel @Inject constructor (var rep : Repository): ViewModel(){
    var yemekListe = MutableLiveData<List<Yemek>>()

    private fun yemekYukle(){
        yemekListe = rep.yemekYukle()
    }

    init {
        yemekYukle()
    }
}