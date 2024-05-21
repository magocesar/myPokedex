package com.example.mypokedex

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.mypokedex.database.UserDatabase
import com.example.mypokedex.databinding.AccountActivityBinding
import com.example.mypokedex.model.user.User
import com.example.mypokedex.utils.ActivityUtils
import com.example.mypokedex.utils.ToastUtil
import com.example.mypokedex.view_model.AccountViewModel
import com.example.mypokedex.view_model.BaseViewModel
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class AccountActivity : AppCompatActivity(){

    private lateinit var viewBinding : AccountActivityBinding
    @Inject
    lateinit var viewModel : AccountViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewBinding = AccountActivityBinding.inflate(layoutInflater)
        setContentView(viewBinding.root)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.account)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        val userUid = intent.getIntExtra("userId", -1)
        viewModel.setCurrentUserFromId(userUid, object : BaseViewModel.CallBack{
            override fun onFailure(msg : String) {
                ActivityUtils.navigateToActivity(this@AccountActivity, MainActivity::class.java)
            }

            override fun onSuccess() {
                setViewBindingTexts(viewModel.currentUser.value)
                setOnClickListeners()
                setViewModelObserver()
            }
        })
    }

    private fun setViewModelObserver(){
        viewModel.currentUser.observe(this) { user ->
            setViewBindingTexts(user)
        }
    }

    private fun setViewBindingTexts(user : User?){
        if (user != null) {
            viewBinding.usernameText.text = user.username
            viewBinding.passwordText.text = user.password
            viewBinding.pointsText.text = user.whoIsThatPokemonPoints.toString()
        }
    }

    private fun setOnClickListeners(){
        viewBinding.updateUserButton.setOnClickListener{
            if(checkIfUsernameAndPasswordNotEmpty()){
                viewModel.updateUser(
                    viewBinding.editUsername.text.toString(),
                    viewBinding.editPassword.text.toString(),
                    object : BaseViewModel.CallBack {
                        override fun onFailure(msg: String) {
                            ToastUtil.showToast(this@AccountActivity, msg)
                        }

                        override fun onSuccess() {
                            ToastUtil.showToast(this@AccountActivity, "User data Updated")
                        }
                    }
                )
            }
        }

        viewBinding.returnButton.setOnClickListener {
            val extras = Bundle().apply {
                putInt("userId", viewModel.currentUser.value?.uid ?: -1)
            }
            ActivityUtils.navigateToActivity(this@AccountActivity, HomeActivity::class.java, extras)
        }
    }

    private fun checkIfUsernameAndPasswordNotEmpty() : Boolean{
        return viewBinding.editUsername.text.toString().isNotEmpty() &&
                viewBinding.editPassword.text.toString().isNotEmpty()
    }


}