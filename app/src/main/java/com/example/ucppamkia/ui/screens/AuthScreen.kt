package com.example.ucppamkia.ui.screens

import AzureBlue
import ElectricBlue
import GlassWhite
import GradientElectricStart
import GradientNeonMid
import NeonBlue
import TurquoiseBlue
import android.widget.Toast
import androidx.compose.animation.core.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.*
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.airbnb.lottie.compose.*
import com.example.ucppamkia.R
import com.example.ucppamkia.ui.theme.*
import com.example.ucppamkia.ui.components.*
import com.example.ucppamkia.viewmodel.AppViewModel
import kotlinx.coroutines.launch

@Composable
fun AuthScreen(navController: NavController, viewModel: AppViewModel) {
    var username by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var passwordVisible by remember { mutableStateOf(false) }
    val scope = rememberCoroutineScope()
    val context = LocalContext.current

    // Animasi Lottie untuk tampilan login
    val composition by rememberLottieComposition(LottieCompositionSpec.RawRes(R.raw.anim_login))
    val progress by animateLottieCompositionAsState(
        composition,
        iterations = LottieConstants.IterateForever
    )

    // Animasi background bergerak secara terus-menerus
    val infiniteTransition = rememberInfiniteTransition(label = "background")
    val animatedOffset by infiniteTransition.animateFloat(
        initialValue = 0f,
        targetValue = 1000f,
        animationSpec = infiniteRepeatable(
            animation = tween(8000, easing = LinearEasing),
            repeatMode = RepeatMode.Reverse
        ),
        label = "offset"
    )

    Box(
        modifier = Modifier.fillMaxSize()
    ) {
        // Background bintang sebagai dekorasi layar
        StarryBackground(
            modifier = Modifier.fillMaxSize(),
            starCount = 200,
            baseColor = Color(0xFF0A1929)
        )

        // Lingkaran animasi untuk efek kedalaman tampilan
        FloatingCircles(
            modifier = Modifier.fillMaxSize(),
            circleCount = 8,
            colors = listOf(ElectricBlue, NeonBlue, AzureBlue, TurquoiseBlue)
        )

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(24.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            // Animasi Lottie pada bagian atas
            Box(
                modifier = Modifier
                    .size(200.dp)
                    .padding(bottom = 24.dp)
            ) {
                LottieAnimation(
                    composition = composition,
                    progress = { progress },
                    modifier = Modifier.fillMaxSize()
                )
            }

            // Judul dengan efek shimmer
            ShimmerText(
                text = "Admin Portal",
                style = MaterialTheme.typography.headlineLarge.copy(
                    fontWeight = FontWeight.Bold
                )
            )

            // Teks deskripsi halaman
            Text(
                "Masuk untuk mengelola tiket",
                fontSize = 16.sp,
                color = Color.White.copy(alpha = 0.9f),
                style = MaterialTheme.typography.bodyLarge
            )

            Spacer(Modifier.height(40.dp))

            // Kartu transparan bergaya glassmorphism
            GlassmorphicCard(
                modifier = Modifier.fillMaxWidth(),
                containerColor = GlassWhite,
                elevation = 16.dp,
                shape = RoundedCornerShape(28.dp)
            ) {
                // Input username dengan animasi fokus
                var usernameFocused by remember { mutableStateOf(false) }
                val usernameScale by animateFloatAsState(
                    targetValue = if (usernameFocused) 1.02f else 1f,
                    animationSpec = spring(dampingRatio = Spring.DampingRatioMediumBouncy),
                    label = "usernameScale"
                )

                OutlinedTextField(
                    value = username,
                    onValueChange = { username = it },
                    label = { Text("Username") },
                    leadingIcon = {
                        Icon(
                            Icons.Default.Person,
                            null,
                            tint = if (usernameFocused) NeonBlue else ElectricBlue
                        )
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .scale(usernameScale),
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = NeonBlue,
                        focusedLabelColor = NeonBlue,
                        cursorColor = NeonBlue,
                        focusedTextColor = Color.Black,
                        unfocusedTextColor = Color.Black,
                        unfocusedBorderColor = ElectricBlue.copy(alpha = 0.5f)
                    ),
                    shape = RoundedCornerShape(16.dp),
                    singleLine = true
                )

                // Mendeteksi status fokus input username
                LaunchedEffect(username) {
                    usernameFocused = username.isNotEmpty()
                }

                Spacer(Modifier.height(20.dp))

                // Input password dengan animasi fokus
                var passwordFocused by remember { mutableStateOf(false) }
                val passwordScale by animateFloatAsState(
                    targetValue = if (passwordFocused) 1.02f else 1f,
                    animationSpec = spring(dampingRatio = Spring.DampingRatioMediumBouncy),
                    label = "passwordScale"
                )

                OutlinedTextField(
                    value = password,
                    onValueChange = { password = it },
                    label = { Text("Password") },
                    leadingIcon = {
                        Icon(
                            Icons.Default.Lock,
                            null,
                            tint = if (passwordFocused) NeonBlue else ElectricBlue
                        )
                    },
                    trailingIcon = {
                        IconButton(onClick = { passwordVisible = !passwordVisible }) {
                            Icon(
                                if (passwordVisible) Icons.Default.Visibility
                                else Icons.Default.VisibilityOff,
                                null,
                                tint = if (passwordFocused) NeonBlue else ElectricBlue
                            )
                        }
                    },
                    visualTransformation = if (passwordVisible)
                        VisualTransformation.None
                    else
                        PasswordVisualTransformation(),
                    modifier = Modifier
                        .fillMaxWidth()
                        .scale(passwordScale),
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = NeonBlue,
                        focusedLabelColor = NeonBlue,
                        cursorColor = NeonBlue,
                        focusedTextColor = Color.Black,
                        unfocusedTextColor = Color.Black,
                        unfocusedBorderColor = ElectricBlue.copy(alpha = 0.5f)
                    ),
                    shape = RoundedCornerShape(16.dp),
                    singleLine = true
                )

                // Mendeteksi status fokus input password
                LaunchedEffect(password) {
                    passwordFocused = password.isNotEmpty()
                }

                Spacer(Modifier.height(32.dp))

                // Tombol login dengan warna gradasi
                GradientButton(
                    onClick = {
                        scope.launch {
                            if (viewModel.login(username, password) != null) {
                                navController.navigate("list_ticket") {
                                    popUpTo("login") { inclusive = true }
                                }
                            } else {
                                Toast.makeText(
                                    context,
                                    "Username atau Password salah!",
                                    Toast.LENGTH_SHORT
                                ).show()
                            }
                        }
                    },
                    text = "MASUK",
                    modifier = Modifier.fillMaxWidth(),
                    gradientColors = listOf(
                        GradientElectricStart,
                        ElectricBlue,
                        GradientNeonMid,
                        NeonBlue
                    ),
                    icon = Icons.Default.Login
                )
            }

            Spacer(Modifier.height(24.dp))

            // Teks footer aplikasi
            Text(
                "Sistem Manajemen Tiket Pendakian",
                color = Color.White.copy(alpha = 0.7f),
                fontSize = 12.sp,
                style = MaterialTheme.typography.bodySmall
            )
        }
    }
}
