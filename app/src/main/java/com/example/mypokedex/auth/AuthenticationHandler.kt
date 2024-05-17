package com.example.mypokedex.auth

import android.content.Context
import com.example.mypokedex.dao.user.UserDao
import com.example.mypokedex.database.UserDatabase
import com.example.mypokedex.model.user.User
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class AuthenticationHandler(context: Context) {

    interface AuthCallBack {
        fun onSuccess(user: User)
        fun onFailure(msg : String)
    }

    private val userDao : UserDao = UserDatabase.
                                    getDatabase(context).
                                    userDao()

    private suspend fun performAuthOperation(username: String, password: String, operation: suspend (String, String) -> User?, callBack: AuthCallBack) {
        return withContext(Dispatchers.IO) {
            try {
                val user = operation(username, password)
                user?.let { callBack.onSuccess(it) } ?: callBack.onFailure("Operation failed")
            } catch (e: Exception) {
                callBack.onFailure(e.message ?: "Operation failed")
            }
        }
    }

    suspend fun signUp(username: String, password: String, callBack: AuthCallBack) {
        performAuthOperation(username, password, { u, p ->
            val newUser = User(u, p)
            try {
                userDao.insertUser(newUser)
                newUser
            } catch (e: Exception) {
                throw Exception("Username already exists")
            }
        }, callBack)
    }

    suspend fun signIn(username: String, password: String, callBack: AuthCallBack) {
        performAuthOperation(username, password, { u, p ->
            val user = userDao.getUserByUsername(u) ?: throw Exception("User not found")
            if(user.password != p) throw Exception("Incorrect password") else user
        }, callBack)
    }
}