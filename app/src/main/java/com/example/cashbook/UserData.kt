package com.example.cashbook

import java.io.Serializable

data class UserData(
    var id :Int,
    val Amount: Int,
    val notes: String,
    val dateYear: String,
    val Time: String,
    val type: String
): Serializable
