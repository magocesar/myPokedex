package com.example.mypokedex

import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.GridLayoutManager
import com.example.mypokedex.view.PokemonAdapter
import com.example.mypokedex.database.UserDatabase
import com.example.mypokedex.databinding.HomeActivityBinding
import com.example.mypokedex.utils.ActivityUtils
import com.example.mypokedex.view_model.BaseViewModel
import com.example.mypokedex.view_model.HomeActivityViewModel
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class HomeActivity : AppCompatActivity() {

    private lateinit var viewBinding : HomeActivityBinding
    @Inject
    lateinit var viewModel: HomeActivityViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewBinding = HomeActivityBinding.inflate(layoutInflater)
        setContentView(viewBinding.root)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.home)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        val userUid = intent.getIntExtra("userId", -1)
        viewModel.setCurrentUserFromId(userUid, object : BaseViewModel.CallBack{
            override fun onFailure(msg : String) {
                ActivityUtils.navigateToActivity(this@HomeActivity, MainActivity::class.java)
            }

            override fun onSuccess() {
                setOnClickListeners()
                setRecyclerViewAndViewObservers()
            }
        })
    }

    private fun setOnClickListeners(){
        viewBinding.myAccountButton.setOnClickListener{
            val extras = Bundle().apply {
                putInt("userId", viewModel.currentUser.value?.uid ?: -1)
            }
            ActivityUtils.navigateToActivity(this@HomeActivity, AccountActivity::class.java, extras, false)
        }

        viewBinding.refreshButton.setOnClickListener{
            viewModel.fetchPokemons()
        }

        viewBinding.gameButton.setOnClickListener {
            val extras = Bundle().apply {
                putInt("userId", viewModel.currentUser.value?.uid ?: -1)
            }
            ActivityUtils.navigateToActivity(this@HomeActivity, GameActivity::class.java, extras, false)
        }

        viewBinding.returnButton.setOnClickListener {
            ActivityUtils.navigateToActivity(this@HomeActivity, MainActivity::class.java)
        }
    }

    private fun setRecyclerViewAndViewObservers(){
        val recyclerView = viewBinding.myRecyclerView
        recyclerView.layoutManager = GridLayoutManager(this, 2)

        viewModel.pokemons.observe(this, Observer { pokemons ->
            recyclerView.adapter = PokemonAdapter(pokemons)
        })

        viewModel.loadingState.observe(this, Observer { loadingState ->
            if(loadingState){
                viewBinding.loadingProgressBar.visibility = View.VISIBLE
            } else {
                viewBinding.loadingProgressBar.visibility = View.GONE
            }
        })

        viewModel.fetchPokemons()
    }
}