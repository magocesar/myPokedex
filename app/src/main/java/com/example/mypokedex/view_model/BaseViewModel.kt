package com.example.mypokedex.view_model

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.mypokedex.dao.user.UserDao
import com.example.mypokedex.model.user.User
import kotlinx.coroutines.launch

open class BaseViewModel(protected val userDao: UserDao) : ViewModel() {

    interface CallBack{
        fun onFailure(msg: String)
        fun onSuccess()
    }

    val currentUser : MutableLiveData<User> = MutableLiveData()

    fun setCurrentUserFromId(uid : Int, callBack: CallBack){
        viewModelScope.launch {
            if (uid == -1) callBack.onFailure("Error")

            val user = userDao.getUserById(uid)
            if(user == null){
                callBack.onFailure("Error")
            }else{
                currentUser.value = user
                callBack.onSuccess()
            }
        }
    }
}