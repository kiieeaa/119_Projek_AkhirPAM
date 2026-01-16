package com.example.ucppamkia.data.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.sqlite.db.SupportSQLiteDatabase
import com.example.ucppamkia.data.dao.DaoAplikasi
import com.example.ucppamkia.data.entity.*

// Konfigurasi database Room untuk menyimpan data aplikasi.
@Database(
    entities = [
        EntitasAdmin::class,
        EntitasRute::class,
        EntitasPendaki::class,
        EntitasTiket::class
    ],
    version = 15,
    exportSchema = false
)
abstract class AppDatabase : RoomDatabase() {

    // Menyediakan akses ke DAO aplikasi.
    abstract fun daoAplikasi(): DaoAplikasi

    companion object {

        // Menyimpan instance database agar tetap singleton.
        @Volatile
        private var INSTANCE: AppDatabase? = null

        // Membuat atau mengambil instance database.
        fun getDatabase(context: Context): AppDatabase {
            return INSTANCE ?: synchronized(this) {
                Room.databaseBuilder(
                    context.applicationContext,
                    AppDatabase::class.java,
                    "rinjani_db_indo_v3"
                )
                    .addCallback(object : Callback() {
                        override fun onCreate(db: SupportSQLiteDatabase) {
                            super.onCreate(db)

                            // Mengisi data admin awal (seed data).
                            db.execSQL(
                                "INSERT INTO admin (namaPengguna, kataSandi) VALUES ('admin', 'admin123')"
                            )

                            // Mengisi data rute awal.
                            val ruteList = listOf(
                                "('Via Sembalun', 'Sulit')",
                                "('Via Senaru', 'Sedang')",
                                "('Via Torean', 'Sulit')",
                                "('Via Aik Berik', 'Sedang')"
                            )
                            ruteList.forEach {
                                db.execSQL(
                                    "INSERT INTO rute (namaRute, tingkatKesulitan) VALUES $it"
                                )
                            }
                        }
                    })

                    // Menghapus dan membuat ulang database saat terjadi perubahan versi.
                    .fallbackToDestructiveMigration()

                    // Membangun database.
                    .build()
                    .also { INSTANCE = it }
            }
        }
    }
}
