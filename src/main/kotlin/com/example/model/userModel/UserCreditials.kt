package com.example.model.userModel

import kotlinx.serialization.Serializable
import org.mindrot.jbcrypt.BCrypt

@Serializable
data class UserCredentials(var username:String ,var password:String) {
    fun hashedPassword():String{
        return BCrypt.hashpw(password,BCrypt.gensalt())
    }

}