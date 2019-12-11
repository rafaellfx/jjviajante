package com.example.jjviajante.model

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.io.Serializable

@Entity
data class User (
    @PrimaryKey(autoGenerate = true)
    var id: Long = 0,
    var nome: String = "",
    var email: String = "",
    var senha: String = ""
) : Serializable