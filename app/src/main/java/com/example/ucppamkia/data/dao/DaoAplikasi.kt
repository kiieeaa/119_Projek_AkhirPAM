package com.example.ucppamkia.data.dao

import androidx.room.*
import com.example.ucppamkia.data.entity.*
import kotlinx.coroutines.flow.Flow

// DAO untuk mengelola akses database aplikasi (admin, rute, pendaki, dan tiket).
@Dao
interface DaoAplikasi {

    // ===== ADMIN =====

    // Mengecek login admin berdasarkan username dan password.
    @Query("SELECT * FROM admin WHERE namaPengguna = :u AND kataSandi = :p LIMIT 1")
    suspend fun cekLogin(u: String, p: String): EntitasAdmin?

    // Menyimpan atau memperbarui data admin.
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun tambahAdmin(admin: EntitasAdmin)


    // ===== RUTE =====

    // Menambahkan data rute baru.
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun tambahRute(rute: EntitasRute)

    // Mengambil seluruh data rute dalam bentuk Flow.
    @Query("SELECT * FROM rute")
    fun ambilSemuaRute(): Flow<List<EntitasRute>>

    // Mengambil satu rute berdasarkan nama.
    @Query("SELECT * FROM rute WHERE namaRute = :nama LIMIT 1")
    suspend fun ambilRuteBerdasarkanNama(nama: String): EntitasRute?


    // ===== PENDAKI =====

    // Menambahkan data pendaki dan mengembalikan ID.
    @Insert
    suspend fun tambahPendaki(pendaki: EntitasPendaki): Long

    // Memperbarui data pendaki.
    @Update
    suspend fun perbaruiPendaki(pendaki: EntitasPendaki)


    // ===== TIKET =====

    // Menambahkan data tiket baru.
    @Insert
    suspend fun tambahTiket(tiket: EntitasTiket)

    // Memperbarui data tiket.
    @Update
    suspend fun perbaruiTiket(tiket: EntitasTiket)

    // Menghapus data tiket.
    @Delete
    suspend fun hapusTiket(tiket: EntitasTiket)

    // Mengambil semua tiket beserta detail relasinya secara realtime.
    @Transaction
    @Query("SELECT * FROM tiket ORDER BY idTiket DESC")
    fun ambilSemuaTiketDenganDetail(): Flow<List<DetailTiket>>
}
