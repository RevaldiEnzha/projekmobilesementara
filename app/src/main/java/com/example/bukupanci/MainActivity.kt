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

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            BukuPanciTheme {
                val navController = rememberNavController()
                NavHost(navController = navController, startDestination = "home") {

                    // ===== BAGIAN RAJA: halaman Home =====
                    // Raja tinggal mengganti isi blok ini dengan HomeScreen miliknya
                    composable("home") {
                        HomeSementara(
                            onTambahResepClick = {
                                navController.navigate("info_dasar") {
                                    launchSingleTop = true
                                }
                            }
                        )
                    }

                    // ===== BAGIAN KAMU: alur tambah resep =====
                    // Tahap 4.1: layar 1 (Info Dasar)
                    composable("info_dasar") {
                        InfoDasarResepScreen(
                            onBackClick = { navController.popBackStack() },
                            onNextClick = { judul ->
                                navController.navigate("bahan_cara/${Uri.encode(judul)}") {
                                    launchSingleTop = true
                                }
                            }
                        )
                    }

                    // Tahap 4.2: layar 2 (Bahan & Cara)
                    composable(
                        route = "bahan_cara/{judul}",
                        arguments = listOf(navArgument("judul") { type = NavType.StringType })
                    ) { backStackEntry ->
                        val judul = backStackEntry.arguments?.getString("judul") ?: ""
                        BahanCaraResepScreen(
                            judulDraf = judul,
                            onBackClick = { navController.popBackStack() },
                            onNextClick = {
                                navController.navigate("foto/${Uri.encode(judul)}") {
                                    launchSingleTop = true
                                }
                            }
                        )
                    }

                    // Tahap 4.3: layar 3 (Foto)
                    composable(
                        route = "foto/{judul}",
                        arguments = listOf(navArgument("judul") { type = NavType.StringType })
                    ) { backStackEntry ->
                        val context = LocalContext.current
                        val judul = backStackEntry.arguments?.getString("judul") ?: ""
                        FotoResepScreen(
                            judulDraf = judul,
                            onBackClick = { navController.popBackStack() },
                            onSaveClick = {
                                // Sementara: penyimpanan belum dikerjakan
                                Toast.makeText(context, "Fitur simpan menyusul", Toast.LENGTH_SHORT).show()
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