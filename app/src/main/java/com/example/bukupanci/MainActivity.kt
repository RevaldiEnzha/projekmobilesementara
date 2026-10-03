package com.example.bukupanci

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.example.bukupanci.ui.theme.BukuPanciTheme
import android.net.Uri
import android.widget.Toast
import androidx.compose.ui.platform.LocalContext
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.bukupanci.ui.screen.BahanCaraResepScreen
import com.example.bukupanci.ui.screen.FotoResepScreen
import com.example.bukupanci.ui.screen.InfoDasarResepScreen
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.ui.Alignment
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.bukupanci.ui.screen.TambahResepViewModel

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            BukuPanciTheme {
                val navController = rememberNavController()
                // Satu ViewModel dipakai bersama oleh tiga layar (dibuat di level Activity)
                val tambahResepViewModel: TambahResepViewModel = viewModel()

                NavHost(navController = navController, startDestination = "info_dasar") {

                    // Tahap 5.1: layar 1 (Info Dasar)
                    composable("info_dasar") {
                        InfoDasarResepScreen(
                            viewModel = tambahResepViewModel,
                            onNextClick = {
                                navController.navigate("bahan_cara") { launchSingleTop = true }
                            }
                        )
                    }

                    // Tahap 5.2: layar 2 (Bahan & Cara)
                    composable("bahan_cara") {
                        BahanCaraResepScreen(
                            viewModel = tambahResepViewModel,
                            onBackClick = { navController.popBackStack() },
                            onNextClick = {
                                navController.navigate("foto") { launchSingleTop = true }
                            }
                        )
                    }

                    // Tahap 5.3: layar 3 (Foto + simpan)
                    composable("foto") {
                        FotoResepScreen(
                            viewModel = tambahResepViewModel,
                            onBackClick = { navController.popBackStack() },
                            onSelesai = {
                                // Sementara: kembali ke form kosong. Nanti diarahkan ke halaman Recipes.
                                navController.navigate("info_dasar") {
                                    popUpTo("info_dasar") { inclusive = true }
                                }
                            }
                        )
                    }
                }
            }
        }
    }
}

// Halaman Home sementara: DIGANTI oleh HomeScreen milik Raja
@Composable
fun HomeSementara(onTambahResepClick: () -> Unit) {
    Scaffold { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp, Alignment.CenterVertically),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = "Halaman Home (sementara)",
                style = MaterialTheme.typography.titleLarge
            )
            Button(onClick = onTambahResepClick) {
                Text("Tambah Resep")
            }
        }
    }
}

@Composable
fun Greeting(name: String, modifier: Modifier = Modifier) {
    Text(
        text = "Hello $name!",
        modifier = modifier
    )
}

@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    BukuPanciTheme {
        Greeting("Android")
    }
}