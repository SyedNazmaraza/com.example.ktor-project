package com.example.model.customerModel

import kotlinx.serialization.Serializable

@Serializable
data class customerres<T>(
    var data :T ,
    var success : Boolean
)