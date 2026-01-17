package com.example.ucppamkia.ui.screens

// Import warna dan tema UI
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

// Import Android & Compose
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
import com.example.ucppamkia.data.source.DataRinjani
import com.example.ucppamkia.viewmodel.AppViewModel
import com.example.ucppamkia.ui.theme.*
import com.example.ucppamkia.ui.components.*
import java.util.Calendar

// ========================
// SCREEN FORM INPUT TIKET
// ========================
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TicketFormScreen(navController: NavController, viewModel: AppViewModel, idTiket: Int) {

    // Mengambil context Android
    val context = LocalContext.current

    // Mengambil semua tiket dan rute dari ViewModel
    val semuaTiket by viewModel.semuaTiket.collectAsState()
    val semuaRute by viewModel.semuaRute.collectAsState()

    // Mencari tiket berdasarkan id (jika edit)
    val detail = semuaTiket.find { it.tiket.idTiket == idTiket }

    // Menentukan apakah halaman dalam mode edit atau input baru
    val isEdit = idTiket != 0

    // --------------------
    // STATE DATA INPUT
    // --------------------
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
    var kewarganegaraan by remember {
        mutableStateOf(detail?.pendaki?.kewarganegaraan ?: "Indonesia")
    }

    // --------------------
    // STATE VALIDASI ERROR
    // --------------------
    var isNameError by remember { mutableStateOf(false) }
    var isIdError by remember { mutableStateOf(false) }
    var isPhoneError by remember { mutableStateOf(false) }
    var isRouteError by remember { mutableStateOf(false) }

    var idErrorMessage by remember { mutableStateOf("") }
    var phoneErrorMessage by remember { mutableStateOf("") }

    // State dialog konfirmasi
    var showConfirmDialog by remember { mutableStateOf(false) }

    // --------------------
    // DATE PICKER FUNCTION
    // --------------------
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

    // --------------------
    // LAYOUT UTAMA SCREEN
    // --------------------
    Scaffold(
        topBar = {
            // TopAppBar dengan background gradient
            Surface(
                modifier = Modifier.fillMaxWidth(),
                shadowElevation = 4.dp
            ) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(
                            brush = Brush.horizontalGradient(
                                colors = listOf(
                                    GradientElectricStart,
                                    ElectricBlue,
                                    NeonBlue
                                )
                            )
                        )
                ) {
                    TopAppBar(
                        title = {
                            Text(
                                if (isEdit) "Edit Data Tiket" else "Input Tiket Baru",
                                fontWeight = FontWeight.Bold,
                                color = Color.White,
                                style = MaterialTheme.typography.titleLarge
                            )
                        },
                        // Tombol kembali ke halaman sebelumnya
                        navigationIcon = {
                            IconButton(onClick = { navController.popBackStack() }) {
                                Icon(
                                    Icons.AutoMirrored.Filled.ArrowBack,
                                    "Back",
                                    tint = Color.White
                                )
                            }
                        },
                        colors = TopAppBarDefaults.topAppBarColors(containerColor = Color.Transparent)
                    )
                }
            }
        },
        containerColor = Color(0xFF0A1929) // Warna background utama
    ) { p ->

        Box(modifier = Modifier.fillMaxSize()) {

            // Background animasi bintang
            StarryBackground(
                modifier = Modifier.fillMaxSize(),
                starCount = 150,
                baseColor = Color(0xFF0A1929)
            )

            // Animasi lingkaran melayang
            FloatingCircles(
                modifier = Modifier.fillMaxSize(),
                circleCount = 6,
                colors = listOf(ElectricBlue, NeonBlue, TurquoiseBlue)
            )

            // Konten utama form (scrollable)
            Column(
                Modifier
                    .padding(p)
                    .padding(20.dp)
                    .verticalScroll(rememberScrollState())
            ) {

                // Judul section pendaki
                SectionTitle("Informasi Pendaki")

                // Input nama lengkap
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

                // Row jenis ID dan nomor identitas
                Row(
                    Modifier.fillMaxWidth().height(IntrinsicSize.Min),
                    horizontalArrangement = Arrangement.spacedBy(12.dp),
                    verticalAlignment = Alignment.Top
                ) {

                    // Dropdown jenis identitas
                    BlueDropdownField(
                        label = "Jenis ID",
                        options = listOf("KTP", "Paspor", "KTM", "NIK"),
                        selected = jenisId,
                        modifier = Modifier.weight(0.35f)
                    ) {
                        jenisId = it
                        isIdError = false
                    }

                    // Input nomor identitas
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

                // Input nomor HP
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

                // Dropdown kewarganegaraan
                BlueDropdownField(
                    label = "Kewarganegaraan",
                    options = listOf("Indonesia", "WNA"),
                    selected = kewarganegaraan,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    kewarganegaraan = it
                }

                // Input tempat lahir dan tanggal lahir
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

                // Dropdown pemilihan jalur pendakian
                BlueDropdownField(
                    label = "Pilih Jalur Pendakian",
                    options = semuaRute.map { it.namaRute },
                    selected = ruteTerpilih,
                    modifier = Modifier.fillMaxWidth(),
                    isError = isRouteError,
                    errorMessage = "Jalur pendakian wajib dipilih!",
                    onSelect = {
                        ruteTerpilih = it
                        isRouteError = false
                    }
                )

                // Input tanggal naik dan turun
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

                // Tombol simpan / update tiket
                GradientButton(
                    onClick = {
                        // Validasi data input sebelum simpan
                        val nameValid =
                            namaLengkap.all { it.isLetter() || it.isWhitespace() } && namaLengkap.isNotEmpty()

                        val idValid = if (jenisId == "Paspor") {
                            noIdentitas.all { it.isLetterOrDigit() }
                        } else {
                            noIdentitas.all { it.isDigit() }
                        } && noIdentitas.isNotEmpty()

                        val isDigitOnly = noHp.all { it.isDigit() }
                        val isLengthValid = noHp.length in 11..13
                        val phoneValid = isDigitOnly && isLengthValid && noHp.isNotEmpty()

                        val routeValid = ruteTerpilih.isNotEmpty()

                        isNameError = !nameValid
                        isIdError = !idValid
                        idErrorMessage =
                            if (jenisId == "Paspor") "Paspor hanya huruf & angka!" else "ID selain Paspor harus angka!"

                        isPhoneError = !phoneValid
                        phoneErrorMessage =
                            if (!isDigitOnly) "Hanya boleh angka!" else "Harus 11-13 digit!"

                        isRouteError = !routeValid

                        val otherFieldsValid =
                            tglPendakian.isNotEmpty() && tglTurun.isNotEmpty() && kewarganegaraan.isNotEmpty()

                        if (nameValid && idValid && phoneValid && routeValid && otherFieldsValid) {
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

            // Dialog konfirmasi sebelum simpan data
            if (showConfirmDialog) {
                AlertDialog(
                    onDismissRequest = { showConfirmDialog = false },
                    title = { Text(if (isEdit) "Konfirmasi Perubahan" else "Konfirmasi Simpan") },
                    text = { Text("Apakah data sudah benar?\n\nNama: $namaLengkap\nNegara: $kewarganegaraan\nID: $jenisId - $noIdentitas\nHP: $noHp\nRute: $ruteTerpilih\nTanggal: $tglPendakian s/d $tglTurun") },
                    confirmButton = {
                        Button(
                            onClick = {
                                viewModel.simpanTiket(
                                    idTiket,
                                    detail?.pendaki?.idPendaki ?: 0,
                                    namaLengkap,
                                    jenisKelamin,
                                    jenisId,
                                    noIdentitas,
                                    tempatLahir,
                                    tanggalLahir,
                                    noHp,
                                    kewarganegaraan,
                                    ruteTerpilih,
                                    tglPendakian,
                                    tglTurun,
                                    detail?.tiket?.kodeTiket ?: ""
                                )
                                Toast.makeText(
                                    context,
                                    if (isEdit) "Data Diperbarui!" else "Data Disimpan!",
                                    Toast.LENGTH_SHORT
                                ).show()
                                showConfirmDialog = false
                                navController.popBackStack()
                            },
                            colors = ButtonDefaults.buttonColors(containerColor = BluePrimary)
                        ) { Text("Ya, Simpan") }
                    },
                    dismissButton = {
                        TextButton(onClick = { showConfirmDialog = false }) {
                            Text("Batal", color = BluePrimary)
                        }
                    },
                    containerColor = Color.White,
                    titleContentColor = Color.Black,
                    textContentColor = Color.Black
                )
            }
        }
    }
}

// ========================
// COMPONENT: JUDUL SECTION
// ========================
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
            color = BluePrimary,
            style = MaterialTheme.typography.titleLarge
        )
        Spacer(Modifier.width(8.dp))
        Box(
            modifier = Modifier
                .weight(1f)
                .height(3.dp)
                .background(
                    brush = Brush.horizontalGradient(
                        colors = listOf(NeonBlue, Color.Transparent)
                    ),
                    shape = RoundedCornerShape(2.dp)
                )
        )
    }
}

// ========================
// COMPONENT: TEXT FIELD BIRU
// ========================
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
) {
    Column(modifier = modifier.padding(bottom = 12.dp)) {
        OutlinedTextField(
            value = value, onValueChange = onValueChange,
            label = { Text(label) },
            leadingIcon = icon?.let {
                {
                    Icon(it, null, tint = if (isError) AlertRed else BlueAccent)
                }
            },
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(12.dp),
            keyboardOptions = KeyboardOptions(keyboardType = keyboardType),
            isError = isError,
            colors = OutlinedTextFieldDefaults.colors(
                focusedBorderColor = BluePrimary,
                focusedLabelColor = BluePrimary,
                unfocusedBorderColor = BlueAccent.copy(0.5f),
                cursorColor = BluePrimary,
                focusedTextColor = Color.White,
                unfocusedTextColor = Color.White,
                errorBorderColor = AlertRed,
                errorLabelColor = AlertRed,
                errorCursorColor = AlertRed
            )
        )
        if (isError && errorMessage.isNotEmpty()) {
            Text(
                text = errorMessage,
                color = AlertRed,
                fontSize = 11.sp,
                modifier = Modifier.padding(start = 4.dp, top = 2.dp)
            )
        }
    }
}

// ========================
// COMPONENT: DATE PICKER FIELD
// ========================
@Composable
fun DateField(
    label: String,
    value: String,
    modifier: Modifier = Modifier,
    onClick: () -> Unit
) {
    Column(modifier = modifier.padding(bottom = 12.dp)) {
        OutlinedTextField(
            value = value,
            onValueChange = {},
            readOnly = true,
            label = { Text(label) },
            trailingIcon = {
                Icon(
                    Icons.Default.CalendarToday,
                    null,
                    tint = BlueAccent,
                    modifier = Modifier.clickable(onClick = onClick)
                )
            },
            modifier = Modifier
                .fillMaxWidth()
                .clickable(onClick = onClick),
            shape = RoundedCornerShape(12.dp),
            colors = OutlinedTextFieldDefaults.colors(
                focusedBorderColor = BluePrimary,
                focusedLabelColor = BluePrimary,
                unfocusedBorderColor = BlueAccent.copy(0.5f),
                cursorColor = BluePrimary,
                focusedTextColor = Color.White,
                unfocusedTextColor = Color.White
            )
        )
    }
}

// ========================
// COMPONENT: DROPDOWN FIELD
// ========================
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
) {
    var expanded by remember { mutableStateOf(false) }

    Column(modifier = modifier.padding(bottom = 12.dp)) {
        ExposedDropdownMenuBox(
            expanded = expanded,
            onExpandedChange = { expanded = !expanded }
        ) {
            OutlinedTextField(
                value = selected.ifEmpty { label },
                onValueChange = {},
                readOnly = true,
                trailingIcon = {
                    ExposedDropdownMenuDefaults.TrailingIcon(expanded = expanded)
                },
                modifier = Modifier.menuAnchor().fillMaxWidth(),
                shape = RoundedCornerShape(12.dp),
                isError = isError,
                colors = OutlinedTextFieldDefaults.colors(
                    focusedBorderColor = if (isError) AlertRed else BluePrimary,
                    unfocusedBorderColor = if (isError) AlertRed else BlueAccent.copy(0.5f),
                    focusedLabelColor = if (isError) AlertRed else BluePrimary,
                    errorBorderColor = AlertRed,
                    errorLabelColor = AlertRed,
                    cursorColor = BluePrimary,
                    focusedTextColor = Color.White,
                    unfocusedTextColor = Color.White
                )
            )

            ExposedDropdownMenu(
                expanded = expanded,
                onDismissRequest = { expanded = false },
                modifier = Modifier.background(Color.White)
            ) {
                options.forEach { opt ->
                    DropdownMenuItem(
                        text = { Text(opt, color = Color.Black) },
                        onClick = {
                            onSelect(opt)
                            expanded = false
                        }
                    )
                }
            }
        }

        if (isError && errorMessage.isNotEmpty()) {
            Text(
                text = errorMessage,
                color = AlertRed,
                fontSize = 11.sp,
                modifier = Modifier.padding(start = 4.dp, top = 2.dp)
            )
        }
    }
}
