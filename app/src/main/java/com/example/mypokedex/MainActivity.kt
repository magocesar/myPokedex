package com.example.mypokedex

import UserViewModel
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.mypokedex.database.UserDatabase
import com.example.mypokedex.databinding.ActivityMainBinding
import com.example.mypokedex.model.user.User

class MainActivity : AppCompatActivity() {

    private lateinit var viewBinding : ActivityMainBinding
    private lateinit var userViewModel: UserViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        viewBinding = ActivityMainBinding.inflate(layoutInflater)
        userViewModel = UserViewModel(UserDatabase.getDatabase(this).userDao())
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
                userViewModel.signIn(getSignInUsername(), getSignInPassword(), object : UserViewModel.CallBack{
                    override fun onSuccess(user: User) {
                        changeBlockingFrameVisibility()
                        changeProgressBarVisibility()
                        Toast.makeText(this@MainActivity, "Welcome ${user.username}", Toast.LENGTH_SHORT).show()
                    }

                    override fun onFailure(msg: String) {
                        changeBlockingFrameVisibility()
                        changeProgressBarVisibility()
                        Toast.makeText(this@MainActivity, msg, Toast.LENGTH_SHORT).show()
                    }
                })
            }
        }

        viewBinding.signUpButton.setOnClickListener{
            if(checkSignUpTextFields()){
                changeBlockingFrameVisibility()
                changeProgressBarVisibility()
               userViewModel.signUp(getSignUpUsername(), getSignUpPassword(), object : UserViewModel.CallBack{
                   override fun onSuccess(user: User) {
                       changeBlockingFrameVisibility()
                       changeProgressBarVisibility()
                       Toast.makeText(this@MainActivity, "Welcome ${user.username}", Toast.LENGTH_SHORT).show()
                   }

                   override fun onFailure(msg: String) {
                       changeBlockingFrameVisibility()
                       changeProgressBarVisibility()
                       Toast.makeText(this@MainActivity, msg, Toast.LENGTH_SHORT).show()
                   }
               })
            }
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