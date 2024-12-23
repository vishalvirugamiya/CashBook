package com.example.cashbook

import java.io.Serializable

data class UserData(var id :Int,
                    val Amount: String,
                    val notes: String,
                    val dateYear: String,
                    val Time: String): Serializable
