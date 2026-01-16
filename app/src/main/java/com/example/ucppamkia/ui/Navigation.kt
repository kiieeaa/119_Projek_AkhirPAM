package com.example.ucppamkia.ui

import androidx.compose.runtime.Composable
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.ucppamkia.ui.screens.*
import com.example.ucppamkia.viewmodel.AppViewModel

// Mengatur seluruh navigasi antar screen aplikasi.
@Composable
fun AppNavigation(viewModel: AppViewModel) {

    // Controller untuk mengelola perpindahan halaman.
    val navController = rememberNavController()

    // NavHost sebagai pusat rute navigasi.
    NavHost(
        navController = navController,
        startDestination = "login"
    ) {

        // Halaman login / autentikasi.
        composable("login") {
            AuthScreen(navController, viewModel)
        }

        // Halaman daftar tiket.
        composable("list_ticket") {
            TicketListScreen(navController, viewModel)
        }

        // Halaman form tambah / edit tiket berdasarkan id.
        composable(
            route = "form_ticket/{id}",
            arguments = listOf(navArgument("id") { type = NavType.IntType })
        ) {
            val id = it.arguments?.getInt("id") ?: 0
            TicketFormScreen(navController, viewModel, id)
        }

        // Halaman preview detail tiket berdasarkan id.
        composable(
            route = "preview_ticket/{id}",
            arguments = listOf(navArgument("id") { type = NavType.IntType })
        ) {
            val id = it.arguments?.getInt("id") ?: 0
            TicketPreviewScreen(navController, viewModel, id)
        }
    }
}
