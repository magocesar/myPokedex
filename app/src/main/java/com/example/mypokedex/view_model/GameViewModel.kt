package com.example.mypokedex.view_model


import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.example.mypokedex.api.PokemonRepository
import com.example.mypokedex.dao.user.UserDao
import com.example.mypokedex.model.pokemon.Pokemon
import kotlinx.coroutines.launch
import javax.inject.Inject
import kotlin.random.Random

class GameViewModel @Inject constructor(userDao: UserDao) : BaseViewModel(userDao) {

    val pokemons = MutableLiveData<List<Pokemon?>>()
    val selectedPokemon = MutableLiveData<Pokemon?>()

    fun refreshGame(){
        viewModelScope.launch {
            getPokemons()
        }
    }

    fun gameWon(){
        viewModelScope.launch {
            currentUser.value?.let {
                it.whoIsThatPokemonPoints += 1
                userDao.updateUser(it)
            }
        }
    }

    fun gameLost(){
        viewModelScope.launch {
            currentUser.value?.let {
                if(it.whoIsThatPokemonPoints > 0){
                    it.whoIsThatPokemonPoints -= 1
                    userDao.updateUser(it)
                }
            }
        }
    }

    private suspend fun getPokemons(){
        //Get 4 random numbers between 1 and 151
        val random = Random(System.nanoTime())
        val randomNumbers = mutableSetOf<Int>()
        while(randomNumbers.size < 4){
            val randomNumber = random.nextInt(1, 152)
            if(!randomNumbers.contains(randomNumber)){
                randomNumbers.add(randomNumber)
            }
        }

        val pokemonsList = mutableListOf<Pokemon?>()

        //Get the pokemon for each number
        randomNumbers.let {
            for (number in it){
                val pokemonApiResult = PokemonRepository.getPokemon(number)
                pokemonApiResult?.let {
                    pokemonsList.add(
                        Pokemon(
                            pokemonApiResult.id,
                            pokemonApiResult.name,
                            pokemonApiResult.types.map { type ->
                                type.type
                            }
                        )
                    )
                }
            }
        }

        //Set the pokemons to the live data
        pokemons.postValue(pokemonsList)
        selectedPokemon.postValue(pokemonsList.random())
    }


}