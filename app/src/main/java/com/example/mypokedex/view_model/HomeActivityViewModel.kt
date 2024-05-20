package com.example.mypokedex.view_model

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.example.mypokedex.api.PokemonRepository
import com.example.mypokedex.dao.user.UserDao
import com.example.mypokedex.model.pokemon.Pokemon
import kotlinx.coroutines.launch

class HomeActivityViewModel(userDao: UserDao) : BaseViewModel(userDao) {

    private val _pokemons = MutableLiveData<List<Pokemon?>>()
    val pokemons: LiveData<List<Pokemon?>> = _pokemons

    fun fetchPokemons() {
        viewModelScope.launch {
            val pokemonsApiResult = PokemonRepository.listPokemons()
            pokemonsApiResult?.results?.let {
                val pokemons: List<Pokemon?> = it.map { pokemonResult ->
                    val number = pokemonResult.url.replace("https://pokeapi.co/api/v2/pokemon/", "").replace("/", "").toInt()
                    val pokemonApiResult = PokemonRepository.getPokemon(number)

                    pokemonApiResult?.let {
                        Pokemon(
                            pokemonApiResult.id,
                            pokemonApiResult.name,
                            pokemonApiResult.types.map { type ->
                                type.type
                            }
                        )
                    }
                }
                _pokemons.postValue(pokemons)
            }
        }
    }
}