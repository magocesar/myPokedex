package com.example.mypokedex.dao.user

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.example.mypokedex.model.user.User

@Dao
interface UserDao {
    @Insert(onConflict = OnConflictStrategy.ABORT)
    suspend fun insertUser(vararg user: User)

    @Update(onConflict = OnConflictStrategy.ABORT)
    suspend fun updateUser(user: User)

    @Delete
    suspend fun deleteUser(user: User)

    @Query("SELECT * FROM user WHERE username = :username")
    suspend fun getUserByUsername(username: String): User

    @Query("SELECT * FROM user WHERE uid = :id")
    suspend fun getUserById(id: Int): User

    @Query("SELECT * FROM user")
    suspend fun getAllUsers(): List<User>
}