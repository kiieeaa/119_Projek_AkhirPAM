package com.example.ucppamkia.data.entity

import androidx.room.Embedded
import androidx.room.Relation

// Menampung data tiket beserta relasi pendaki dan rute.
data class DetailTiket(

    // Menyisipkan seluruh data tiket ke dalam objek ini.
    @Embedded
    val tiket: EntitasTiket,

    // Menghubungkan tiket dengan data pendaki berdasarkan ID.
    @Relation(
        parentColumn = "idPendakiFK",
        entityColumn = "idPendaki"
    )
    val pendaki: EntitasPendaki,

    // Menghubungkan tiket dengan data rute berdasarkan ID.
    @Relation(
        parentColumn = "idRuteFK",
        entityColumn = "idRute"
    )
    val rute: EntitasRute
)
