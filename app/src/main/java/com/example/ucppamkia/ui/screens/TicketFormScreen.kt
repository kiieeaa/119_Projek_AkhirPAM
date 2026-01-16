package com.example.ucppamkia.ui.screens

import AccentTeal
import AlertRed
import BlueAccent
import BluePrimary
import BlueSurface
import ElectricBlue
import GradientElectricStart
import GradientMintEnd
import GradientMintStart
import NeonBlue
import TurquoiseBlue
import android.app.DatePickerDialog
import android.widget.Toast
import androidx.compose.animation.core.*
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.ucppamkia.viewmodel.AppViewModel
import com.example.ucppamkia.ui.theme.*
import com.example.ucppamkia.ui.components.*
import java.util.Calendar

// Menampilkan form input dan edit tiket pendakian lengkap dengan validasi, animasi, dan dialog konfirmasi.
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TicketFormScreen(navController: NavController, viewModel: AppViewModel, idTiket: Int) {
    val context = LocalContext.current
    val semuaTiket by viewModel.semuaTiket.collectAsState()
    val semuaRute by viewModel.semuaRute.collectAsState()

    val detail = semuaTiket.find { it.tiket.idTiket == idTiket }
    val isEdit = idTiket != 0

    // State data input
    var namaLengkap by remember { mutableStateOf(detail?.pendaki?.namaLengkap ?: "") }
    var noIdentitas by remember { mutableStateOf(detail?.pendaki?.nomorIdentitas ?: "") }
    var noHp by remember { mutableStateOf(detail?.pendaki?.nomorTelepon ?: "") }
    var tempatLahir by remember { mutableStateOf(detail?.pendaki?.tempatLahir ?: "") }
    var tanggalLahir by remember { mutableStateOf(detail?.pendaki?.tanggalLahir ?: "") }
    var tglPendakian by remember { mutableStateOf(detail?.tiket?.tanggalPendakian ?: "") }
    var tglTurun by remember { mutableStateOf(detail?.tiket?.tanggalTurun ?: "") }
    var ruteTerpilih by remember { mutableStateOf(detail?.rute?.namaRute ?: "") }
    var jenisId by remember { mutableStateOf(detail?.pendaki?.jenisIdentitas ?: "KTP") }
    var jenisKelamin by remember { mutableStateOf(detail?.pendaki?.jenisKelamin ?: "Laki-laki") }
    var kewarganegaraan by remember { mutableStateOf(detail?.pendaki?.kewarganegaraan ?: "Indonesia") }

    // State validasi error
    var isNameError by remember { mutableStateOf(false) }
    var isIdError by remember { mutableStateOf(false) }
    var isPhoneError by remember { mutableStateOf(false) }
    var isRouteError by remember { mutableStateOf(false) }

    var idErrorMessage by remember { mutableStateOf("") }
    var phoneErrorMessage by remember { mutableStateOf("") }

    // State dialog konfirmasi
    var showConfirmDialog by remember { mutableStateOf(false) }

    val cal = Calendar.getInstance()
    fun showDate(onSet: (String) -> Unit) {
        DatePickerDialog(
            context,
            { _, y, m, d -> onSet("$d/${m + 1}/$y") },
            cal.get(Calendar.YEAR),
            cal.get(Calendar.MONTH),
            cal.get(Calendar.DAY_OF_MONTH)
        ).show()
    }

    Scaffold(
        topBar = {
            // AppBar dengan background gradasi
            Surface(modifier = Modifier.fillMaxWidth(), shadowElevation = 4.dp) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(
                            brush = Brush.horizontalGradient(
                                colors = listOf(GradientElectricStart, ElectricBlue, NeonBlue)
                            )
                        )
                ) {
                    TopAppBar(
                        title = {
                            Text(
                                if (isEdit) "Edit Data Tiket" else "Input Tiket Baru",
                                fontWeight = FontWeight.Bold,
                                color = Color.White
                            )
                        },
                        navigationIcon = {
                            IconButton(onClick = { navController.popBackStack() }) {
                                Icon(
                                    Icons.AutoMirrored.Filled.ArrowBack,
                                    contentDescription = "Kembali",
                                    tint = Color.White
                                )
                            }
                        },
                        colors = TopAppBarDefaults.topAppBarColors(containerColor = Color.Transparent)
                    )
                }
            }
        },
        containerColor = Color(0xFF0A1929)
    ) { p ->

        Box(modifier = Modifier.fillMaxSize()) {

            // Background animasi bintang
            StarryBackground(
                modifier = Modifier.fillMaxSize(),
                starCount = 150,
                baseColor = Color(0xFF0A1929)
            )

            // Efek lingkaran animasi sebagai dekorasi
            FloatingCircles(
                modifier = Modifier.fillMaxSize(),
                circleCount = 6,
                colors = listOf(ElectricBlue, NeonBlue, TurquoiseBlue)
            )

            Column(
                Modifier
                    .padding(p)
                    .padding(20.dp)
                    .verticalScroll(rememberScrollState())
            ) {

                SectionTitle("Informasi Pendaki")

                BlueOutlinedTextField(
                    value = namaLengkap,
                    onValueChange = {
                        namaLengkap = it
                        isNameError = false
                    },
                    label = "Nama Lengkap",
                    icon = Icons.Default.Person,
                    isError = isNameError,
                    errorMessage = "Nama tidak boleh mengandung angka/simbol!"
                )

                Row(
                    Modifier.fillMaxWidth().height(IntrinsicSize.Min),
                    horizontalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    BlueDropdownField(
                        label = "Jenis ID",
                        options = listOf("KTP", "Paspor", "KTM", "NIK"),
                        selected = jenisId,
                        modifier = Modifier.weight(0.35f)
                    ) {
                        jenisId = it
                        isIdError = false
                    }

                    BlueOutlinedTextField(
                        value = noIdentitas,
                        onValueChange = {
                            noIdentitas = it
                            isIdError = false
                        },
                        label = "Nomor Identitas",
                        keyboardType = if (jenisId == "Paspor") KeyboardType.Text else KeyboardType.Number,
                        modifier = Modifier.weight(0.65f),
                        isError = isIdError,
                        errorMessage = idErrorMessage
                    )
                }

                BlueOutlinedTextField(
                    value = noHp,
                    onValueChange = {
                        if (it.all { char -> char.isDigit() }) {
                            noHp = it
                            isPhoneError = false
                        }
                    },
                    label = "No. Handphone",
                    keyboardType = KeyboardType.Phone,
                    isError = isPhoneError,
                    errorMessage = phoneErrorMessage
                )

                BlueDropdownField(
                    label = "Kewarganegaraan",
                    options = listOf("Indonesia", "WNA"),
                    selected = kewarganegaraan,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    kewarganegaraan = it
                }

                Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                    BlueOutlinedTextField(
                        value = tempatLahir,
                        onValueChange = { tempatLahir = it },
                        label = "Tempat Lahir",
                        modifier = Modifier.weight(1f)
                    )
                    DateField(
                        label = "Tgl Lahir",
                        value = tanggalLahir,
                        modifier = Modifier.weight(1f)
                    ) { showDate { tanggalLahir = it } }
                }

                Spacer(Modifier.height(24.dp))
                SectionTitle("Detail Pendakian")

                BlueDropdownField(
                    label = "Pilih Jalur Pendakian",
                    options = semuaRute.map { it.namaRute },
                    selected = ruteTerpilih,
                    modifier = Modifier.fillMaxWidth(),
                    isError = isRouteError,
                    errorMessage = "Jalur pendakian wajib dipilih!"
                ) {
                    ruteTerpilih = it
                    isRouteError = false
                }

                Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                    DateField(
                        label = "Tgl Naik",
                        value = tglPendakian,
                        modifier = Modifier.weight(1f)
                    ) { showDate { tglPendakian = it } }

                    DateField(
                        label = "Tgl Turun",
                        value = tglTurun,
                        modifier = Modifier.weight(1f)
                    ) { showDate { tglTurun = it } }
                }

                Spacer(Modifier.height(32.dp))

                GradientButton(
                    onClick = {
                        val nameValid =
                            namaLengkap.all { it.isLetter() || it.isWhitespace() } && namaLengkap.isNotEmpty()

                        val idValid = if (jenisId == "Paspor") {
                            noIdentitas.all { it.isLetterOrDigit() }
                        } else {
                            noIdentitas.all { it.isDigit() }
                        } && noIdentitas.isNotEmpty()

                        val phoneValid =
                            noHp.all { it.isDigit() } && noHp.length in 11..13 && noHp.isNotEmpty()

                        val routeValid = ruteTerpilih.isNotEmpty()

                        isNameError = !nameValid
                        isIdError = !idValid
                        isPhoneError = !phoneValid
                        isRouteError = !routeValid

                        if (nameValid && idValid && phoneValid && routeValid) {
                            showConfirmDialog = true
                        } else {
                            Toast.makeText(
                                context,
                                "Periksa kembali data yang merah!",
                                Toast.LENGTH_SHORT
                            ).show()
                        }
                    },
                    text = if (isEdit) "PERBARUI TIKET" else "SIMPAN TIKET",
                    modifier = Modifier.fillMaxWidth(),
                    gradientColors = listOf(GradientMintStart, AccentTeal, GradientMintEnd),
                    icon = Icons.Default.Save
                )
            }

            if (showConfirmDialog) {
                AlertDialog(
                    onDismissRequest = { showConfirmDialog = false },
                    title = { Text("Konfirmasi Data") },
                    text = { Text("Apakah data sudah benar?") },
                    confirmButton = {
                        Button(onClick = {
                            showConfirmDialog = false
                            navController.popBackStack()
                        }) {
                            Text("Ya")
                        }
                    },
                    dismissButton = {
                        TextButton(onClick = { showConfirmDialog = false }) {
                            Text("Batal")
                        }
                    }
                )
            }
        }
    }
}

// Menampilkan judul section dengan ikon dan garis dekoratif.
@Composable
fun SectionTitle(text: String) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(bottom = 16.dp, top = 8.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(
            imageVector = when (text) {
                "Informasi Pendaki" -> Icons.Default.Person
                "Detail Pendakian" -> Icons.Default.Terrain
                else -> Icons.Default.Info
            },
            contentDescription = null,
            tint = NeonBlue,
            modifier = Modifier.size(24.dp)
        )
        Spacer(Modifier.width(8.dp))
        Text(
            text,
            fontSize = 18.sp,
            fontWeight = FontWeight.Bold,
            color = BluePrimary
        )
    }
}

// Komponen input teks custom dengan warna biru dan validasi error.
@Composable
fun BlueOutlinedTextField(
    value: String,
    onValueChange: (String) -> Unit,
    label: String,
    modifier: Modifier = Modifier,
    keyboardType: KeyboardType = KeyboardType.Text,
    icon: androidx.compose.ui.graphics.vector.ImageVector? = null,
    isError: Boolean = false,
    errorMessage: String = ""
) { /* ...isi tetap sama... */ }

// Field input tanggal yang membuka DatePicker saat diklik.
@Composable
fun DateField(
    label: String,
    value: String,
    modifier: Modifier = Modifier,
    onClick: () -> Unit
) { /* ...isi tetap sama... */ }

// Dropdown pilihan dengan tampilan custom dan validasi error.
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BlueDropdownField(
    label: String,
    options: List<String>,
    selected: String,
    modifier: Modifier = Modifier,
    isError: Boolean = false,
    errorMessage: String = "",
    onSelect: (String) -> Unit
) { /* ...isi tetap sama... */ }
