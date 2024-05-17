package com.example.pokedex_kotlin.domain

data class PokemonType(
    val name: String
){
    val formattedName
        get() = name.capitalize()
}