package com.example.mypokedex.model.pokemon

import android.graphics.Bitmap
import kotlinx.coroutines.suspendCancellableCoroutine

data class Pokemon(
    val number : Int,
    val name : String,
    val types : List<PokemonType>
){
    val formattedNumber = number.toString().padStart(3, '0')
    val imageUrl = "https://www.pokemon.com/static-assets/content-assets/cms2/img/pokedex/detail/$formattedNumber.png"
}
