package com.example.pokedex_kotlin.api

import com.example.pokedex_kotlin.api.model.PokemonApiResult
import com.example.pokedex_kotlin.api.model.PokemonsApiResult
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object PokemonRepository {

    private val service: PokemonService
    //https://pokeapi.co/api/v2/pokemon/?limit=151
    init {
        val retrofit = Retrofit.Builder()
            .baseUrl("https://pokeapi.co/api/v2/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()


        service = retrofit.create(PokemonService::class.java)
    }


    fun listPokemons(limit: Int = 151): PokemonsApiResult? {
        val call = service.listPokemons(limit)

        return call.execute().body()


    }

    fun getPokemon(number: Int): PokemonApiResult? {
        val call = service.getPokemon(number)

        return call.execute().body()


    }
}