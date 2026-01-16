package com.example.ucppamkia.data.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

// Entitas untuk menyimpan data admin pada tabel database.
@Entity(tableName = "admin")
data class EntitasAdmin(

    // Menyimpan username admin sebagai primary key.
    @PrimaryKey
    val namaPengguna: String,

    // Menyimpan kata sandi admin.
    val kataSandi: String
)
