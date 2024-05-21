package com.example.mypokedex.view_model

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.mypokedex.dao.user.UserDao
import com.example.mypokedex.model.user.User
import kotlinx.coroutines.launch
import javax.inject.Inject

class MainActivityViewModel @Inject constructor(private val userDao: UserDao) : ViewModel() {

    interface CallBack {
        fun onSuccess(user: User)
        fun onFailure(msg: String)
    }

    fun signUp(username: String, password: String, callBack: CallBack) {
        viewModelScope.launch {
            performAuthOperation(username, password, { u, p ->
                val newUser = User(u, p)
                try {
                    val uidArray = userDao.insertUser(newUser)
                    newUser.uid = uidArray[0].toInt()
                    newUser
                } catch (e: Exception) {
                    throw Exception("Username already exists")
                }
            }, callBack)
        }
    }

    fun signIn(username: String, password: String, callBack: CallBack) {
        viewModelScope.launch {
            performAuthOperation(username, password, { u, p ->
                val user = userDao.getUserByUsername(u)
                if(user == null) throw Exception("User not found")
                if(user.password != p) throw Exception("Incorrect password") else user
            }, callBack)
        }
    }

    private suspend fun performAuthOperation(username: String, password: String, operation: suspend (String, String) -> User?, callBack: CallBack) {
        try {
            val user = operation(username, password)
            user?.let { callBack.onSuccess(it) } ?: callBack.onFailure("Operation failed")
        } catch (e: Exception) {
            callBack.onFailure(e.message ?: "An error occurred")
        }
    }
}