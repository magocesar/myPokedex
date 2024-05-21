package com.example.mypokedex

import android.os.Bundle
import android.view.View
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.mypokedex.database.UserDatabase
import com.example.mypokedex.databinding.ActivityMainBinding
import com.example.mypokedex.model.user.User
import com.example.mypokedex.utils.ActivityUtils
import com.example.mypokedex.utils.ToastUtil
import com.example.mypokedex.view_model.MainActivityViewModel
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    private lateinit var viewBinding : ActivityMainBinding
    @Inject
    lateinit var viewModel: MainActivityViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        viewBinding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(viewBinding.root)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        viewBinding.signInButton.setOnClickListener{
            if(checkSignInTextFields()){
                changeBlockingFrameVisibility()
                changeProgressBarVisibility()
                viewModel.signIn(getSignInUsername(), getSignInPassword(), object : MainActivityViewModel.CallBack{
                    override fun onSuccess(user: User) {
                        changeBlockingFrameVisibility()
                        changeProgressBarVisibility()
                        goToHomeActivity(user)
                    }

                    override fun onFailure(msg: String) {
                        changeBlockingFrameVisibility()
                        changeProgressBarVisibility()
                        ToastUtil.showToast(this@MainActivity, msg)
                    }
                })
            }
        }

        viewBinding.signUpButton.setOnClickListener{
            if(checkSignUpTextFields()){
                changeBlockingFrameVisibility()
                changeProgressBarVisibility()
               viewModel.signUp(getSignUpUsername(), getSignUpPassword(), object : MainActivityViewModel.CallBack{
                   override fun onSuccess(user: User) {
                       changeBlockingFrameVisibility()
                       changeProgressBarVisibility()
                       goToHomeActivity(user)
                   }

                   override fun onFailure(msg: String) {
                       changeBlockingFrameVisibility()
                       changeProgressBarVisibility()
                       ToastUtil.showToast(this@MainActivity, msg)
                   }
               })
            }
        }
    }

    private fun goToHomeActivity(user : User){
        val extras = Bundle().apply {
            putInt("userId", user.uid)
        }
        ActivityUtils.navigateToActivity(this@MainActivity, HomeActivity::class.java, extras, false)
    }

    private fun checkSignInTextFields(): Boolean {
        return getSignInUsername().isNotEmpty() && getSignInPassword().isNotEmpty()
    }

    private fun checkSignUpTextFields(): Boolean {
        return getSignUpUsername().isNotEmpty() && getSignUpPassword().isNotEmpty()
    }

    private fun getSignInUsername() : String {
        return viewBinding.signInUsername.text.toString()
    }

    private fun getSignInPassword() : String {
        return viewBinding.signInPassword.text.toString()
    }

    private fun getSignUpUsername() : String {
        return viewBinding.signUpUsername.text.toString()
    }

    private fun getSignUpPassword() : String {
        return viewBinding.signUpPassword.text.toString()
    }

    private fun changeProgressBarVisibility(){
        viewBinding.progressBar.visibility = if(viewBinding.progressBar.visibility == View.VISIBLE) View.GONE else View.VISIBLE
    }

    private fun changeBlockingFrameVisibility(){
        viewBinding.blockingFrame.visibility = if(viewBinding.blockingFrame.visibility == View.VISIBLE) View.GONE else View.VISIBLE
    }
}