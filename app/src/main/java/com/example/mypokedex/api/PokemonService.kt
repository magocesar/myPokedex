package com.example.mypokedex.api

import com.example.mypokedex.api.model.PokemonApiResult
import com.example.mypokedex.api.model.PokemonsApiResult
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface PokemonService {
    @GET("pokemon")
    suspend fun listPokemons(@Query("limit") limit: Int) : PokemonsApiResult

    @GET("pokemon/{id}")
    suspend fun getPokemon(@Path("id") id : Int) : PokemonApiResult



}