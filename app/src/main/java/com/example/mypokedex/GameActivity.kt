package com.example.mypokedex

import android.os.Bundle
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.lifecycle.Observer
import com.example.mypokedex.database.UserDatabase
import com.example.mypokedex.databinding.GameActivityBinding
import com.example.mypokedex.utils.ActivityUtils
import com.example.mypokedex.view_model.BaseViewModel
import com.example.mypokedex.view_model.GameViewModel
import com.squareup.picasso.Picasso
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class GameActivity : AppCompatActivity() {

    private lateinit var viewBinding : GameActivityBinding
    @Inject
    lateinit var viewModel : GameViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewBinding = GameActivityBinding.inflate(layoutInflater)
        setContentView(viewBinding.root)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.game)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        val userUid = intent.getIntExtra("userId", -1)
        viewModel.setCurrentUserFromId(userUid, object : BaseViewModel.CallBack{
            override fun onFailure(msg : String) {
                ActivityUtils.navigateToActivity(this@GameActivity, MainActivity::class.java)
            }

            override fun onSuccess() {
                setOnClickListeners()
                setViewModelObserver()
                viewModel.refreshGame()
            }
        })
    }

    private fun setOnClickListeners(){
        viewBinding.refreshButton.setOnClickListener{
            resetGame()
        }
        viewBinding.quitButton.setOnClickListener{
            val extras = Bundle().apply {
                putInt("userId", viewModel.currentUser.value?.uid ?: -1)
            }
            ActivityUtils.navigateToActivity(this@GameActivity, HomeActivity::class.java, extras, false)
        }

        viewBinding.btnOption1.setOnClickListener {
            checkButton(viewBinding.btnOption1)
        }

        viewBinding.btnOption2.setOnClickListener {
            checkButton(viewBinding.btnOption2)
        }

        viewBinding.btnOption3.setOnClickListener {
            checkButton(viewBinding.btnOption3)
        }

        viewBinding.btnOption4.setOnClickListener {
            checkButton(viewBinding.btnOption4)
        }
    }

    private fun resetGame(){
        viewModel.refreshGame()
        resetPokemonName()
        changeButtonState()
        resetButtonColors()
    }

    private fun setViewModelObserver(){
        viewModel.selectedPokemon.observe(this, Observer {

            //Set the silhouette image
            Picasso.get().load(viewModel.selectedPokemon.value?.imageUrl).into(viewBinding.imgPokemon)

            //Set the buttons text to the selected pokemons
            val randomPokemons = viewModel.pokemons.value
            if (randomPokemons != null) {
                viewBinding.btnOption1.text = randomPokemons[0]?.name?.capitalize()
                viewBinding.btnOption2.text = randomPokemons[1]?.name?.capitalize()
                viewBinding.btnOption3.text = randomPokemons[2]?.name?.capitalize()
                viewBinding.btnOption4.text = randomPokemons[3]?.name?.capitalize()
            }
        })
    }

    private fun checkButton(button : Button){
        //Check if the button text is the same as the selected pokemon
        if(button.text == viewModel.selectedPokemon.value?.name?.capitalize()){
            viewModel.gameWon()
            appearPokemonName(true)
        }else{
            viewModel.gameLost()
            appearPokemonName(false)
        }
        //Change the buttons colors
        setButtonColors()
        //Disable the buttons
        changeButtonState()
    }

    private fun appearPokemonName(win : Boolean){
        if(win){
            viewBinding.pokemonName.text = "It's ${viewModel.selectedPokemon.value?.name?.capitalize()}!"
        }else{
            viewBinding.pokemonName.text = "It was ${viewModel.selectedPokemon.value?.name?.capitalize()}!"
        }
    }

    private fun resetPokemonName(){
        viewBinding.pokemonName.text = ""
    }

    private fun setButtonColors(){
        //If the button text is the same as the selected pokemon, set the button color to green
        //If the button text is not the same as the selected pokemon, set the button color to red
        for(btn in listOf(viewBinding.btnOption1, viewBinding.btnOption2, viewBinding.btnOption3, viewBinding.btnOption4)){
            if(btn.text == viewModel.selectedPokemon.value?.name?.capitalize()){
                btn.setBackgroundColor(resources.getColor(R.color.green))
            }else{
                btn.setBackgroundColor(resources.getColor(R.color.red))
            }
        }
    }

    private fun resetButtonColors(){
        for(btn in listOf(viewBinding.btnOption1, viewBinding.btnOption2, viewBinding.btnOption3, viewBinding.btnOption4)){
            btn.setBackgroundColor(resources.getColor(R.color.black))
        }
    }

    private fun changeButtonState(){
        //if the buttons are disabled, enable them
        //if the buttons are enabled, disable them
        for(btn in listOf(viewBinding.btnOption1, viewBinding.btnOption2, viewBinding.btnOption3, viewBinding.btnOption4)){
            btn.isEnabled = !btn.isEnabled
        }
    }
}