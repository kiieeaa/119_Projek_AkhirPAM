package com.example.ucppamkia.ui.screens

import AlertRed
import BlueAccent
import BlueDark
import BlueLight
import BluePrimary
import BlueSurface
import ComplementOrange
import ElectricBlue
import ErrorRed
import GradientElectricStart
import NeonBlue
import TextSecondary
import TurquoiseBlue
import androidx.compose.animation.core.*
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.Logout
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.airbnb.lottie.compose.*
import com.example.ucppamkia.R
import com.example.ucppamkia.data.entity.DetailTiket
import com.example.ucppamkia.viewmodel.AppViewModel
import com.example.ucppamkia.ui.theme.*
import com.example.ucppamkia.ui.components.*
import kotlinx.coroutines.launch

// Menampilkan dashboard daftar tiket dengan filter, animasi background, dan aksi CRUD.
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TicketListScreen(navController: NavController, viewModel: AppViewModel) {
    val semuaTiket by viewModel.semuaTiket.collectAsState()
    val semuaRute by viewModel.semuaRute.collectAsState()

    val snackbarHostState = remember { SnackbarHostState() }
    val scope = rememberCoroutineScope()

    var activeMonth by remember { mutableStateOf("") }
    var activeRoute by remember { mutableStateOf("") }
    var showFilterSheet by remember { mutableStateOf(false) }
    var showDeleteDialog by remember { mutableStateOf<DetailTiket?>(null) }

    val filteredList = viewModel.filterTiket(semuaTiket, activeMonth, activeRoute)
    val isFilterActive = activeMonth.isNotEmpty() || activeRoute.isNotEmpty()

    val emptyComposition by rememberLottieComposition(
        LottieCompositionSpec.RawRes(R.raw.anim_empty)
    )
    val emptyProgress by animateLottieCompositionAsState(
        emptyComposition,
        iterations = LottieConstants.IterateForever
    )

    Scaffold(
        snackbarHost = { SnackbarHost(hostState = snackbarHostState) },
        topBar = {
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
                                    NeonBlue,
                                    TurquoiseBlue
                                )
                            )
                        )
                ) {
                    TopAppBar(
                        title = {
                            Column {
                                Text(
                                    "Dashboard Admin",
                                    fontWeight = FontWeight.Bold,
                                    color = Color.White,
                                    style = MaterialTheme.typography.titleLarge
                                )
                                Text(
                                    "Total Data: ${filteredList.size}",
                                    fontSize = 12.sp,
                                    color = Color.White.copy(alpha = 0.9f)
                                )
                            }
                        },
                        actions = {
                            IconButton(onClick = { showFilterSheet = true }) {
                                Icon(
                                    if (isFilterActive)
                                        Icons.Default.FilterListOff
                                    else
                                        Icons.Default.FilterList,
                                    "Filter",
                                    tint = Color.White
                                )
                            }
                            IconButton(
                                onClick = {
                                    navController.navigate("login") { popUpTo(0) }
                                }
                            ) {
                                Icon(
                                    Icons.AutoMirrored.Filled.Logout,
                                    "Exit",
                                    tint = ComplementOrange
                                )
                            }
                        },
                        colors = TopAppBarDefaults.topAppBarColors(
                            containerColor = Color.Transparent
                        )
                    )
                }
            }
        },
        floatingActionButton = {
            PulsingFAB(
                onClick = { navController.navigate("form_ticket/0") },
                icon = Icons.Default.Add,
                containerColor = ElectricBlue,
                contentDescription = "Tambah Tiket Baru"
            )
        },
        containerColor = BlueSurface
    ) { p ->
        Column(Modifier.padding(p)) {
            Box(modifier = Modifier.fillMaxSize()) {
                StarryBackground(
                    modifier = Modifier.fillMaxSize(),
                    starCount = 150,
                    baseColor = Color(0xFF0A1929)
                )

                FloatingCircles(
                    modifier = Modifier.fillMaxSize(),
                    circleCount = 6,
                    colors = listOf(ElectricBlue, NeonBlue, TurquoiseBlue)
                )

                Column {
                    if (isFilterActive) {
                        Surface(
                            color = BlueLight,
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Row(
                                Modifier.padding(16.dp),
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.SpaceBetween
                            ) {
                                Text(
                                    "Filter: ${
                                        if (activeMonth.isNotEmpty())
                                            "Bln $activeMonth"
                                        else ""
                                    } ${
                                        if (activeRoute.isNotEmpty())
                                            "+ $activeRoute"
                                        else ""
                                    }",
                                    fontSize = 12.sp,
                                    color = BlueDark
                                )
                                TextButton(
                                    onClick = {
                                        activeMonth = ""
                                        activeRoute = ""
                                    }
                                ) {
                                    Text("Reset", fontSize = 12.sp, color = AlertRed)
                                }
                            }
                        }
                    }

                    if (filteredList.isEmpty()) {
                        Box(
                            Modifier.fillMaxSize(),
                            contentAlignment = Alignment.Center
                        ) {
                            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                                LottieAnimation(
                                    composition = emptyComposition,
                                    progress = { emptyProgress },
                                    modifier = Modifier.size(200.dp)
                                )
                                Text("Belum ada data tiket.", color = TextSecondary)
                            }
                        }
                    } else {
                        LazyColumn(
                            contentPadding = PaddingValues(16.dp),
                            verticalArrangement = Arrangement.spacedBy(12.dp)
                        ) {
                            itemsIndexed(filteredList) { index, detail ->
                                AnimatedCard(
                                    delay = index * 50,
                                    modifier = Modifier.fillMaxWidth()
                                ) {
                                    TicketCardBlue(
                                        detail = detail,
                                        onClick = {
                                            navController.navigate(
                                                "preview_ticket/${detail.tiket.idTiket}"
                                            )
                                        },
                                        onEdit = {
                                            navController.navigate(
                                                "form_ticket/${detail.tiket.idTiket}"
                                            )
                                        },
                                        onDelete = {
                                            showDeleteDialog = detail
                                        }
                                    )
                                }
                            }
                        }
                    }
                }
            }
        }

        if (showFilterSheet) {
            ModalBottomSheet(
                onDismissRequest = { showFilterSheet = false },
                containerColor = Color.White
            ) {
                var tempMonth by remember { mutableStateOf(activeMonth) }
                var tempRoute by remember { mutableStateOf(activeRoute) }
                var expandedRoute by remember { mutableStateOf(false) }

                Column(
                    Modifier
                        .padding(24.dp)
                        .verticalScroll(rememberScrollState())
                ) {
                    Text(
                        "Filter Pencarian",
                        style = MaterialTheme.typography.headlineSmall,
                        color = BluePrimary
                    )

                    Spacer(Modifier.height(16.dp))
                    Text("Bulan", fontWeight = FontWeight.SemiBold)

                    @OptIn(ExperimentalLayoutApi::class)
                    FlowRow(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                        (1..12).map { it.toString().padStart(2, '0') }
                            .forEach { m ->
                                FilterChip(
                                    selected = tempMonth == m,
                                    onClick = {
                                        tempMonth =
                                            if (tempMonth == m) "" else m
                                    },
                                    label = { Text(m) }
                                )
                            }
                    }

                    Spacer(Modifier.height(16.dp))
                    Text("Jalur", fontWeight = FontWeight.SemiBold)

                    ExposedDropdownMenuBox(
                        expanded = expandedRoute,
                        onExpandedChange = {
                            expandedRoute = !expandedRoute
                        }
                    ) {
                        OutlinedTextField(
                            value = tempRoute.ifEmpty { "Semua Jalur" },
                            onValueChange = {},
                            readOnly = true,
                            trailingIcon = {
                                ExposedDropdownMenuDefaults.TrailingIcon(
                                    expanded = expandedRoute
                                )
                            },
                            modifier = Modifier.menuAnchor().fillMaxWidth()
                        )

                        ExposedDropdownMenu(
                            expanded = expandedRoute,
                            onDismissRequest = {
                                expandedRoute = false
                            }
                        ) {
                            DropdownMenuItem(
                                text = { Text("Semua Jalur") },
                                onClick = {
                                    tempRoute = ""
                                    expandedRoute = false
                                }
                            )
                            semuaRute.forEach { r ->
                                DropdownMenuItem(
                                    text = { Text(r.namaRute) },
                                    onClick = {
                                        tempRoute = r.namaRute
                                        expandedRoute = false
                                    }
                                )
                            }
                        }
                    }

                    Spacer(Modifier.height(32.dp))

                    Button(
                        onClick = {
                            activeMonth = tempMonth
                            activeRoute = tempRoute
                            showFilterSheet = false
                        },
                        modifier = Modifier.fillMaxWidth(),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = BluePrimary
                        )
                    ) {
                        Text("Terapkan Filter")
                    }
                }
            }
        }

        if (showDeleteDialog != null) {
            AlertDialog(
                onDismissRequest = { showDeleteDialog = null },
                title = { Text("Hapus Data") },
                text = {
                    Text(
                        "Hapus tiket atas nama ${
                            showDeleteDialog!!.pendaki.namaLengkap
                        }?"
                    )
                },
                confirmButton = {
                    Button(
                        onClick = {
                            viewModel.hapusTiket(showDeleteDialog!!)
                            showDeleteDialog = null
                            scope.launch {
                                snackbarHostState.showSnackbar(
                                    "Data berhasil dihapus"
                                )
                            }
                        },
                        colors = ButtonDefaults.buttonColors(
                            containerColor = AlertRed
                        )
                    ) {
                        Text("Hapus")
                    }
                },
                dismissButton = {
                    TextButton(
                        onClick = { showDeleteDialog = null }
                    ) {
                        Text("Batal")
                    }
                }
            )
        }
    }
}

// Menampilkan kartu tiket bergaya glassmorphism dengan tombol detail, edit, dan hapus.
@Composable
fun TicketCardBlue(
    detail: DetailTiket,
    onClick: () -> Unit,
    onEdit: () -> Unit,
    onDelete: () -> Unit
) {
    var isPressed by remember { mutableStateOf(false) }
    val scale by animateFloatAsState(
        targetValue = if (isPressed) 0.95f else 1f,
        animationSpec = spring(dampingRatio = Spring.DampingRatioMediumBouncy),
        label = "cardScale"
    )

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .scale(scale)
            .clickable(onClick = onClick),
        elevation = CardDefaults.cardElevation(defaultElevation = 8.dp),
        colors = CardDefaults.cardColors(
            containerColor = Color.White.copy(alpha = 0.25f)
        ),
        shape = RoundedCornerShape(24.dp)
    ) {
        Row(
            Modifier.padding(20.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(
                modifier = Modifier
                    .background(
                        brush = Brush.verticalGradient(
                            colors = listOf(
                                GradientElectricStart,
                                ElectricBlue,
                                NeonBlue
                            )
                        ),
                        shape = RoundedCornerShape(16.dp)
                    )
                    .padding(16.dp),
                contentAlignment = Alignment.Center
            ) {
                val d = detail.tiket.tanggalPendakian.split("/")
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Text(
                        d.getOrElse(0) { "01" },
                        fontWeight = FontWeight.Bold,
                        fontSize = 20.sp,
                        color = Color.White
                    )
                    Text(
                        d.getOrElse(1) { "Jan" },
                        fontSize = 12.sp,
                        color = Color.White.copy(alpha = 0.9f)
                    )
                }
            }

            Spacer(Modifier.width(16.dp))

            Column(Modifier.weight(1f)) {
                Text(
                    detail.pendaki.namaLengkap,
                    fontWeight = FontWeight.Bold,
                    fontSize = 16.sp,
                    color = Color.White
                )
                Spacer(Modifier.height(4.dp))
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(
                        Icons.Default.Terrain,
                        contentDescription = null,
                        tint = Color.White,
                        modifier = Modifier.size(16.dp)
                    )
                    Spacer(Modifier.width(4.dp))
                    Text(
                        detail.rute.namaRute,
                        fontSize = 14.sp,
                        color = Color.White.copy(alpha = 0.85f)
                    )
                }
            }

            IconButton(onClick = onEdit) {
                Icon(Icons.Default.Edit, null, tint = Color.White)
            }

            Spacer(modifier = Modifier.width(8.dp))

            IconButton(onClick = onDelete) {
                Icon(Icons.Default.Delete, null, tint = Color.White)
            }
        }
    }
}
