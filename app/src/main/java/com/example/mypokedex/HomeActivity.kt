package com.example.mypokedex

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.mypokedex.view.PokemonAdapter
import com.example.mypokedex.database.UserDatabase
import com.example.mypokedex.databinding.HomeActivityBinding
import com.example.mypokedex.utils.ActivityUtils
import com.example.mypokedex.view_model.BaseViewModel
import com.example.mypokedex.view_model.HomeActivityViewModel

class HomeActivity : AppCompatActivity() {

    private lateinit var viewBinding : HomeActivityBinding
    private lateinit var viewModel: HomeActivityViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewBinding = HomeActivityBinding.inflate(layoutInflater)
        viewModel = HomeActivityViewModel(UserDatabase.getDatabase(this).userDao())
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
                setRecyclerView()
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

        viewBinding.searchButton.setOnClickListener{

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

    private fun setRecyclerView(){
        val recyclerView = viewBinding.myRecyclerView
        recyclerView.layoutManager = LinearLayoutManager(this)

        viewModel.pokemons.observe(this, Observer { pokemons ->
            recyclerView.adapter = PokemonAdapter(pokemons)
        })

        viewModel.fetchPokemons()
    }
}