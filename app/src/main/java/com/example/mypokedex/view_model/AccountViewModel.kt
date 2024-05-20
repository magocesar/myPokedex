package com.example.mypokedex.view_model

import android.database.sqlite.SQLiteConstraintException
import androidx.lifecycle.viewModelScope
import com.example.mypokedex.dao.user.UserDao
import com.example.mypokedex.model.user.User
import kotlinx.coroutines.launch

class AccountViewModel(userDao: UserDao) : BaseViewModel(userDao) {

    fun updateUser(newUsername : String, newPassword : String, callback: CallBack){
        viewModelScope.launch {
            if(newUsername.isBlank() || newPassword.isBlank()){
                callback.onFailure("Blank Username or Password")
            }

            val currentUserValue = currentUser.value
            if(currentUserValue != null){
                val updatedUser = User(currentUserValue.uid, newUsername, newPassword, currentUserValue.whoIsThatPokemonPoints)
                try{
                    userDao.updateUser(updatedUser)
                    currentUser.postValue(updatedUser)
                    callback.onSuccess()
                }catch (e : SQLiteConstraintException){
                    callback.onFailure("Update failed due to constraint violation")
                }catch (e : Exception){
                    callback.onFailure("Error")
                }
            }else{
                callback.onFailure("Error")
            }
        }

    }


}