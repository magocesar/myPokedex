package com.example.pokedex_kotlin.domain

class Pokemon (

    val number: Int,
    val name: String,
    val types: List<PokemonType>
) {
    val formattedNumber
        get() = number.toString().padStart(3, '0')

    val formattedName
        get() = name.capitalize()



    val imageUrl
        get() = "https://www.pokemon.com/static-assets/content-assets/cms2/img/pokedex/detail/$formattedNumber.png"




}

