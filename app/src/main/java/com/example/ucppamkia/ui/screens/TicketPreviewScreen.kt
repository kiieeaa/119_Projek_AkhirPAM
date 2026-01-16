package com.example.ucppamkia.ui.screens

import BlueAccent
import BlueLight
import BluePrimary
import BlueSurface
import ElectricBlue
import GradientElectricStart
import NeonBlue
import TextPrimary
import TextSecondary
import TurquoiseBlue
import android.content.Context
import android.print.PrintAttributes
import android.print.PrintManager
import android.webkit.WebView
import android.webkit.WebViewClient
import androidx.compose.animation.core.*
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.GenericShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Rect
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.PathEffect
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.ucppamkia.ui.theme.*
import com.example.ucppamkia.ui.components.*
import com.example.ucppamkia.viewmodel.AppViewModel

// Menampilkan halaman preview detail tiket dan tombol cetak.
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TicketPreviewScreen(
    navController: NavController,
    viewModel: AppViewModel,
    idTiket: Int
) {
    val context = LocalContext.current
    val semuaTiket by viewModel.semuaTiket.collectAsState()
    val detail = semuaTiket.find { it.tiket.idTiket == idTiket }

    Scaffold(
        topBar = {
            // AppBar dengan background gradasi
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
                            Text(
                                "E-Ticket Pass",
                                fontWeight = FontWeight.Bold,
                                color = Color.White
                            )
                        },
                        navigationIcon = {
                            IconButton(onClick = { navController.popBackStack() }) {
                                Icon(
                                    Icons.AutoMirrored.Filled.ArrowBack,
                                    "Back",
                                    tint = Color.White
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
        containerColor = Color(0xFF0A1929)
    ) { p ->
        Box(modifier = Modifier.fillMaxSize()) {

            // Background langit berbintang
            StarryBackground(
                modifier = Modifier.fillMaxSize(),
                starCount = 150,
                baseColor = Color(0xFF0A1929)
            )

            // Efek lingkaran animasi
            FloatingCircles(
                modifier = Modifier.fillMaxSize(),
                circleCount = 6,
                colors = listOf(ElectricBlue, NeonBlue, TurquoiseBlue)
            )

            if (detail != null) {
                Column(
                    Modifier
                        .padding(p)
                        .fillMaxSize()
                        .verticalScroll(rememberScrollState())
                        .padding(24.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {

                    // Kartu tiket model boarding pass
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clip(TicketShapeBlue(20.dp.value))
                            .background(Color.White)
                    ) {
                        Column {

                            // Header tiket dengan gradasi warna
                            Box(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .background(
                                        brush = Brush.linearGradient(
                                            colors = listOf(
                                                GradientElectricStart,
                                                ElectricBlue,
                                                NeonBlue,
                                                TurquoiseBlue
                                            )
                                        )
                                    )
                                    .padding(24.dp),
                            ) {
                                Row(verticalAlignment = Alignment.CenterVertically) {
                                    Icon(
                                        Icons.Default.Hiking,
                                        null,
                                        tint = Color.White,
                                        modifier = Modifier.size(40.dp)
                                    )
                                    Spacer(Modifier.width(16.dp))
                                    Column {
                                        Text(
                                            "TN. GUNUNG RINJANI",
                                            color = Color.White,
                                            fontWeight = FontWeight.ExtraBold,
                                            fontSize = 20.sp
                                        )
                                        Text(
                                            "Official Climbing Permit",
                                            color = BlueLight,
                                            fontSize = 12.sp
                                        )
                                    }
                                }
                            }

                            // Isi detail tiket
                            Column(Modifier.padding(24.dp)) {
                                Text(
                                    "KODE BOOKING",
                                    fontSize = 12.sp,
                                    color = TextSecondary
                                )
                                Text(
                                    detail.tiket.kodeTiket,
                                    fontWeight = FontWeight.Black,
                                    fontSize = 32.sp,
                                    color = BluePrimary
                                )

                                Spacer(Modifier.height(24.dp))
                                Divider(color = BlueLight, thickness = 1.dp)
                                Spacer(Modifier.height(24.dp))

                                // Data pendaki
                                LabelValueRow(
                                    "NAMA PENDAKI",
                                    detail.pendaki.namaLengkap
                                )
                                Spacer(Modifier.height(16.dp))
                                LabelValueRow(
                                    "IDENTITAS",
                                    "${detail.pendaki.jenisIdentitas} - ${detail.pendaki.nomorIdentitas}"
                                )
                                Spacer(Modifier.height(16.dp))
                                LabelValueRow(
                                    "JALUR",
                                    detail.rute.namaRute
                                )

                                Spacer(Modifier.height(24.dp))

                                // Informasi tanggal naik & turun
                                Row(
                                    Modifier
                                        .fillMaxWidth()
                                        .background(
                                            BlueSurface,
                                            RoundedCornerShape(12.dp)
                                        )
                                        .padding(16.dp),
                                    horizontalArrangement = Arrangement.SpaceBetween
                                ) {
                                    DateItem(
                                        "TANGGAL NAIK",
                                        detail.tiket.tanggalPendakian,
                                        Icons.Default.ArrowUpward
                                    )
                                    DateItem(
                                        "TANGGAL TURUN",
                                        detail.tiket.tanggalTurun,
                                        Icons.Default.ArrowDownward
                                    )
                                }

                                Spacer(Modifier.height(24.dp))
                                Text(
                                    "Tunjukkan tiket ini di pos pemeriksaan.",
                                    fontSize = 12.sp,
                                    color = TextSecondary,
                                    modifier = Modifier.align(Alignment.CenterHorizontally)
                                )
                            }
                        }
                    }

                    Spacer(Modifier.height(32.dp))

                    // Tombol cetak tiket
                    GradientButton(
                        onClick = {
                            val html = getHtmlBlue(
                                detail.pendaki.namaLengkap,
                                detail.rute.namaRute,
                                detail.tiket.tanggalPendakian,
                                detail.tiket.tanggalTurun,
                                detail.tiket.kodeTiket
                            )
                            doPrint(context, detail.pendaki.namaLengkap, html)
                        },
                        text = "CETAK TIKET (PDF/Print)",
                        modifier = Modifier.fillMaxWidth(),
                        gradientColors = listOf(
                            GradientElectricStart,
                            ElectricBlue,
                            NeonBlue,
                            TurquoiseBlue
                        ),
                        icon = Icons.Default.Print
                    )
                }
            }
        }
    }
}

// Menampilkan label dan nilai data tiket.
@Composable
fun LabelValueRow(label: String, value: String) {
    Column {
        Text(
            label,
            fontSize = 11.sp,
            color = TextSecondary,
            fontWeight = FontWeight.Bold
        )
        Text(
            value,
            fontSize = 18.sp,
            color = TextPrimary,
            fontWeight = FontWeight.SemiBold
        )
    }
}

// Menampilkan informasi tanggal dengan ikon.
@Composable
fun DateItem(
    label: String,
    date: String,
    icon: androidx.compose.ui.graphics.vector.ImageVector
) {
    Column {
        Row(verticalAlignment = Alignment.CenterVertically) {
            Icon(
                icon,
                null,
                tint = BlueAccent,
                modifier = Modifier.size(14.dp)
            )
            Spacer(Modifier.width(4.dp))
            Text(
                label,
                fontSize = 10.sp,
                color = TextSecondary,
                fontWeight = FontWeight.Bold
            )
        }
        Text(
            date,
            fontSize = 15.sp,
            color = BluePrimary,
            fontWeight = FontWeight.Bold
        )
    }
}

// Membentuk tampilan kartu tiket dengan lekukan samping.
fun TicketShapeBlue(cornerRadius: Float) = GenericShape { size, _ ->
    val holeRadius = 30f
    val centerY = 200f
    reset()
    moveTo(0f, 0f)
    lineTo(size.width, 0f)
    lineTo(size.width, centerY - holeRadius)
    arcTo(
        Rect(
            left = size.width - holeRadius,
            top = centerY - holeRadius,
            right = size.width + holeRadius,
            bottom = centerY + holeRadius
        ),
        -90f,
        -180f,
        false
    )
    lineTo(size.width, size.height)
    lineTo(0f, size.height)
    lineTo(0f, centerY + holeRadius)
    arcTo(
        Rect(
            left = -holeRadius,
            top = centerY - holeRadius,
            right = holeRadius,
            bottom = centerY + holeRadius
        ),
        90f,
        -180f,
        false
    )
    close()
}

// Menjalankan proses cetak HTML ke printer / PDF.
fun doPrint(ctx: Context, name: String, html: String) {
    val wv = WebView(ctx)
    wv.webViewClient = object : WebViewClient() {
        override fun onPageFinished(view: WebView, url: String) {
            (ctx.getSystemService(Context.PRINT_SERVICE) as PrintManager)
                .print(
                    "Tiket_$name",
                    view.createPrintDocumentAdapter("Tiket"),
                    PrintAttributes.Builder().build()
                )
        }
    }
    wv.loadDataWithBaseURL(null, html, "text/HTML", "UTF-8", null)
}



// REQUEST 2: HTML tanpa QR Code, desain lebih bersih
fun getHtmlBlue(n: String, r: String, dNaik: String, dTurun: String, c: String) = """
    <html>
    <head>
        <style>
            body { font-family: sans-serif; padding: 20px; color: #333; }
            .card { border: 2px solid #1565C0; border-radius: 10px; overflow: hidden; }
            .header { background: #1565C0; color: white; padding: 20px; text-align: center; }
            .content { padding: 20px; background: #f0f8ff; }
            .label { font-size: 10px; color: #666; font-weight: bold; text-transform: uppercase; }
            .value { font-size: 16px; font-weight: bold; margin-bottom: 15px; color: #000; }
            .dates { display: flex; justify-content: space-between; background: #e3f2fd; padding: 10px; border-radius: 5px; }
            .code-box { text-align: center; margin-top: 20px; border-top: 2px dashed #ccc; padding-top: 20px; }
            .code { font-size: 30px; font-weight: 900; letter-spacing: 5px; color: #1565C0; }
        </style>
    </head>
    <body>
        <div class="card">
            <div class="header">
                <h2>RINJANI NATIONAL PARK</h2>
                <p>OFFICIAL HIKING PERMIT</p>
            </div>
            <div class="content">
                <div class="label">NAMA PENDAKI</div><div class="value">$n</div>
                <div class="label">JALUR PENDAKIAN</div><div class="value">$r</div>
                
                <div class="dates">
                    <div><div class="label">TANGGAL NAIK</div><div class="value" style="margin:0;">$dNaik</div></div>
                    <div><div class="label">TANGGAL TURUN</div><div class="value" style="margin:0;">$dTurun</div></div>
                </div>

                <div class="code-box">
                    <div class="label">KODE BOOKING</div>
                    <div class="code">$c</div>
                </div>
            </div>
        </div>
    </body>
    </html>
""".trimIndent()
