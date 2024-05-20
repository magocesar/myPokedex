package com.example.mypokedex.api

import com.example.mypokedex.api.model.PokemonApiResult
import com.example.mypokedex.api.model.PokemonsApiResult
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object PokemonRepository {
    //https://pokeapi.co/api/v2/

    private val service : PokemonService

    init{
        val retrofit = Retrofit.Builder()
            .baseUrl("https://pokeapi.co/api/v2/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()

        service = retrofit.create(PokemonService::class.java)
    }

    suspend fun listPokemons(limit: Int = 151) : PokemonsApiResult?{
        return service.listPokemons(limit)
    }

    suspend fun getPokemon(id : Int) : PokemonApiResult{
        return service.getPokemon(id)
    }
}