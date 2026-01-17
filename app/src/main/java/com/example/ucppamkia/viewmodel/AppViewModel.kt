package com.example.ucppamkia.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.ucppamkia.data.database.AppDatabase
import com.example.ucppamkia.data.entity.*
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import java.util.UUID

class AppViewModel(application: Application) : AndroidViewModel(application) {

    // Mengambil DAO untuk akses database.
    private val dao = AppDatabase.getDatabase(application).daoAplikasi()

    // Menyimpan semua tiket dalam bentuk StateFlow.
    val semuaTiket = dao.ambilSemuaTiketDenganDetail()
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(), emptyList())

    // Menyimpan semua data rute dalam bentuk StateFlow.
    val semuaRute = dao.ambilSemuaRute()
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(), emptyList())

    // Fungsi untuk mengecek login admin.
    suspend fun login(u: String, p: String) = dao.cekLogin(u, p)

    // Fungsi untuk menyimpan atau memperbarui data tiket dan pendaki.
    fun simpanTiket(
        idTiket: Int, idPendaki: Int,
        namaLengkap: String, jenisKelamin: String,
        jenisIdentitas: String, nomorIdentitas: String,
        tempatLahir: String, tanggalLahir: String,
        nomorTelepon: String, kewarganegaraan: String,
        namaRute: String,
        tanggalPendakian: String,
        tanggalTurun: String,
        kodeLama: String = ""
    ) {
        viewModelScope.launch {

            // Mengambil data rute berdasarkan nama.
            val rute = dao.ambilRuteBerdasarkanNama(namaRute)

            if (rute != null) {

                // Membuat objek pendaki dari input user.
                val dataPendaki = EntitasPendaki(
                    idPendaki = idPendaki,
                    namaLengkap = namaLengkap,
                    jenisKelamin = jenisKelamin,
                    jenisIdentitas = jenisIdentitas,
                    nomorIdentitas = nomorIdentitas,
                    tempatLahir = tempatLahir,
                    tanggalLahir = tanggalLahir,
                    nomorTelepon = nomorTelepon,
                    kewarganegaraan = kewarganegaraan
                )

                // Menentukan apakah pendaki baru atau update data lama.
                var finalIdPendaki = idPendaki.toLong()
                if (idPendaki == 0) {
                    finalIdPendaki = dao.tambahPendaki(dataPendaki)
                } else {
                    dao.perbaruiPendaki(dataPendaki)
                }

                // Membuat kode tiket baru jika tiket baru.
                val kodeUnik = if (idTiket == 0 || kodeLama.isEmpty()) {
                    "RJN-" + UUID.randomUUID().toString().take(8).uppercase()
                } else {
                    kodeLama
                }

                // Membuat objek tiket.
                val dataTiket = EntitasTiket(
                    idTiket = idTiket,
                    kodeTiket = kodeUnik,
                    idRuteFK = rute.idRute,
                    idPendakiFK = finalIdPendaki.toInt(),
                    tanggalPendakian = tanggalPendakian,
                    tanggalTurun = tanggalTurun
                )

                // Menyimpan atau memperbarui data tiket.
                if (idTiket == 0) {
                    dao.tambahTiket(dataTiket)
                } else {
                    dao.perbaruiTiket(dataTiket)
                }
            }
        }
    }

    // Fungsi untuk menghapus tiket.
    fun hapusTiket(detailTiket: DetailTiket) =
        viewModelScope.launch { dao.hapusTiket(detailTiket.tiket) }

    // Fungsi untuk memfilter tiket berdasarkan bulan dan rute.
    fun filterTiket(
        list: List<DetailTiket>,
        bulan: String,
        rute: String
    ): List<DetailTiket> {
        return list.filter { item ->
            // 1. Ambil tanggal string
            val tanggalStr = item.tiket.tanggalPendakian

            // 2. Tentukan bulan dari database (Logika lebih fleksibel)
            // Kita split berdasarkan "/" ATAU "-"
            val parts = tanggalStr.split("/", "-")

            val bulanDiDb = when {
                // Jika format YYYY-MM-DD atau YYYY/MM/DD (Bulan di tengah/index 1)
                parts.size == 3 && parts[0].length == 4 -> parts[1]
                // Jika format DD-MM-YYYY atau DD/MM/YYYY (Bulan di tengah/index 1)
                parts.size == 3 -> parts[1]
                // Default jika gagal parsing
                else -> ""
            }

            // 3. Bandingkan (Handle kasus "1" vs "01" dengan toInt jika memungkinkan)
            val matchBulan = if (bulan.isEmpty()) true else {
                // Ubah keduanya ke Int agar "01" sama dengan "1"
                val inputBulanInt = bulan.toIntOrNull()
                val dbBulanInt = bulanDiDb.toIntOrNull()
                inputBulanInt != null && inputBulanInt == dbBulanInt
            }

            // Mengecek apakah rute sesuai.
            val matchRute =
                if (rute.isEmpty()) true
                else item.rute.namaRute == rute

            // Mengembalikan hasil filter.
            matchBulan && matchRute
        }
    }
}