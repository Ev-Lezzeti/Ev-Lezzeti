package com.example.evlezzeti.util

import android.view.View
import androidx.navigation.Navigation

fun Navigation.doTransition(it:View,id:Int){
    findNavController(it).navigate(id)
}
