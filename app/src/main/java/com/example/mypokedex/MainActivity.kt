package com.example.mypokedex

import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.lifecycle.lifecycleScope
import com.example.mypokedex.auth.AuthenticationHandler
import com.example.mypokedex.databinding.ActivityMainBinding
import com.example.mypokedex.model.user.User
import kotlinx.coroutines.launch

class MainActivity : AppCompatActivity(), AuthenticationHandler.AuthCallBack {

    private lateinit var viewBinding : ActivityMainBinding
    private lateinit var authHandler : AuthenticationHandler

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        viewBinding = ActivityMainBinding.inflate(layoutInflater)
        authHandler = AuthenticationHandler(this)
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
                lifecycleScope.launch {
                    authHandler.signIn(getSignInUsername(), getSignInPassword(), this@MainActivity)
                }
            }
        }

        viewBinding.signUpButton.setOnClickListener{
            if(checkSignUpTextFields()){
                changeBlockingFrameVisibility()
                changeProgressBarVisibility()
                lifecycleScope.launch {
                   authHandler.signUp(getSignUpUsername(), getSignUpPassword(), this@MainActivity)
                }
            }
        }
    }

    override fun onSuccess(user: User) {
        runOnUiThread {
            changeBlockingFrameVisibility()
            changeProgressBarVisibility()
            Toast.makeText(this, "Authentication successful", Toast.LENGTH_SHORT).show()
        }
    }

    override fun onFailure(msg : String) {
        runOnUiThread {
            changeBlockingFrameVisibility()
            changeProgressBarVisibility()
            Toast.makeText(this, msg, Toast.LENGTH_SHORT).show()
        }
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