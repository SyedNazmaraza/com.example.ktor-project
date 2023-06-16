package com.example.model.customerModel

import kotlinx.serialization.Serializable


@Serializable
data class customerReq(val name:String,var age:Int){}
