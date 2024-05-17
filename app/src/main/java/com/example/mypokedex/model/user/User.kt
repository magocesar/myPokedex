package com.example.mypokedex.model.user

import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey

@Entity(indices = [Index(value = ["username"], unique = true)])
class User(
    @PrimaryKey(autoGenerate = true) val uid : Int,
    var username: String,
    var password: String,
    var whoIsThatPokemonPoints : Int,
){
    constructor(username: String, password: String) : this(
        uid = 0,
        username = username,
        password = password,
        whoIsThatPokemonPoints = 0,
    )
}