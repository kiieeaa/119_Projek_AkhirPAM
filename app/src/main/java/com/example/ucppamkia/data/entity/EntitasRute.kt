package com.example.ucppamkia.data.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

// Entitas untuk menyimpan data rute pendakian di database.
@Entity(tableName = "rute")
data class EntitasRute(

    // ID rute sebagai primary key yang dibuat otomatis.
    @PrimaryKey(autoGenerate = true)
    val idRute: Int = 0,

    // Menyimpan nama rute pendakian.
    val namaRute: String,

    // Menyimpan tingkat kesulitan rute.
    val tingkatKesulitan: String
)
