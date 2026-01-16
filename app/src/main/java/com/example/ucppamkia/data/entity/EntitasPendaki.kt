package com.example.ucppamkia.data.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

// Entitas untuk menyimpan data pendaki pada database.
@Entity(tableName = "pendaki")
data class EntitasPendaki(

    // ID pendaki sebagai primary key yang dibuat otomatis.
    @PrimaryKey(autoGenerate = true)
    val idPendaki: Int = 0,

    // Menyimpan nama lengkap pendaki.
    val namaLengkap: String,

    // Menyimpan jenis kelamin pendaki.
    val jenisKelamin: String,

    // Menyimpan jenis identitas pendaki (KTP, Paspor, dll).
    val jenisIdentitas: String,

    // Menyimpan nomor identitas pendaki.
    val nomorIdentitas: String,

    // Menyimpan tempat lahir pendaki.
    val tempatLahir: String,

    // Menyimpan tanggal lahir pendaki.
    val tanggalLahir: String,

    // Menyimpan nomor telepon pendaki.
    val nomorTelepon: String,

    // Menyimpan kewarganegaraan pendaki.
    val kewarganegaraan: String
)
