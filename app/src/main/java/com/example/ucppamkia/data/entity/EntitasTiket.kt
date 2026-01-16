package com.example.ucppamkia.data.entity

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey

@Entity(
    tableName = "tiket",
    foreignKeys = [
        ForeignKey(
            entity = EntitasRute::class,
            parentColumns = ["idRute"],
            childColumns = ["idRuteFK"],
            onDelete = ForeignKey.CASCADE
        ),
        ForeignKey(
            entity = EntitasPendaki::class,
            parentColumns = ["idPendaki"],
            childColumns = ["idPendakiFK"],
            onDelete = ForeignKey.CASCADE
        )
    ],
    indices = [Index("idRuteFK"), Index("idPendakiFK")]
)
data class EntitasTiket(
    @PrimaryKey(autoGenerate = true)
    val idTiket: Int = 0,
    val kodeTiket: String,
    val idRuteFK: Int,
    val idPendakiFK: Int,
    val tanggalPendakian: String,
    val tanggalTurun: String,
    val dibuatPada: Long = System.currentTimeMillis()
)