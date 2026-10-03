package com.example.bukupanci.ui.screen

import android.content.res.Configuration
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.bukupanci.ui.theme.BukuPanciTheme
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.FilledTonalIconButton
import androidx.compose.ui.Alignment
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel

// ============ STATEFUL: pengelola state ============
@Composable
fun InfoDasarResepScreen(
    viewModel: TambahResepViewModel = viewModel(),
    onBackClick: () -> Unit = {},
    onNextClick: () -> Unit = {}
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    StatelessInfoDasarResep(
        judulResep = uiState.judul,
        onJudulResepChange = { viewModel.onJudulChange(it) },
        porsi = uiState.porsi,
        onPorsiChange = { viewModel.onPorsiChange(it) },
        deskripsi = uiState.deskripsi,
        onDeskripsiChange = { viewModel.onDeskripsiChange(it) },
        onBackClick = onBackClick,
        onNextClick = onNextClick
    )
}

// ============ STATELESS: hanya menampilkan UI ============
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun StatelessInfoDasarResep(
    judulResep: String,
    onJudulResepChange: (String) -> Unit,
    porsi: Int,
    onPorsiChange: (Int) -> Unit,
    deskripsi: String,
    onDeskripsiChange: (String) -> Unit,
    onBackClick: () -> Unit,
    onNextClick: () -> Unit
) {
    // Validasi sederhana: judul tidak boleh kosong / hanya spasi
    val isJudulValid = judulResep.isNotBlank()
    val isJudulHanyaSpasi = judulResep.isNotEmpty() && judulResep.isBlank()

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Tambah Resep") },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.primary,
                    titleContentColor = MaterialTheme.colorScheme.onPrimary,
                    navigationIconContentColor = MaterialTheme.colorScheme.onPrimary
                ),
                navigationIcon = {
                    IconButton(onClick = onBackClick) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = "Kembali"
                        )
                    }
                }
            )
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .verticalScroll(rememberScrollState())
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            // Tahap 1.1: indikator langkah (langkah 1)
            IndikatorLangkah(langkahSaatIni = 1)

            // Tahap 1.2: judul dan keterangan bagian
            Text(
                text = "Info Dasar",
                style = MaterialTheme.typography.titleLarge
            )
            Text(
                text = "Beri nama dan gambaran singkat untuk resep yang akan kamu tulis.",
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )

            // Tahap 1.3: kolom judul resep
            OutlinedTextField(
                value = judulResep,
                onValueChange = onJudulResepChange,
                label = { Text("Judul resep") },
                isError = isJudulHanyaSpasi,
                supportingText = {
                    if (isJudulHanyaSpasi) Text("Judul tidak boleh hanya spasi")
                },
                singleLine = true,
                modifier = Modifier.fillMaxWidth(),
                shape = MaterialTheme.shapes.medium
            )

            // Tahap 1.4: jumlah porsi (pola tombol - dan + dari DetailProductScreen praktikum)
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(text = "Jumlah porsi", style = MaterialTheme.typography.bodyLarge)
                Row(verticalAlignment = Alignment.CenterVertically) {
                    FilledTonalIconButton(
                        onClick = { onPorsiChange(porsi - 1) },
                        enabled = porsi > 1
                    ) { Text("-") }

                    Text(
                        text = porsi.toString(),
                        style = MaterialTheme.typography.titleLarge,
                        modifier = Modifier.padding(horizontal = 16.dp)
                    )

                    FilledTonalIconButton(
                        onClick = { onPorsiChange(porsi + 1) },
                        enabled = porsi < 50
                    ) { Text("+") }
                }
            }

            // Tahap 1.5: deskripsi singkat (opsional, pola kolom Pesan di HubungiKamiScreen)
            OutlinedTextField(
                value = deskripsi,
                onValueChange = onDeskripsiChange,
                label = { Text("Deskripsi singkat (opsional)") },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(120.dp),
                shape = MaterialTheme.shapes.medium
            )

            // Tahap 1.6: tombol lanjut (satu-satunya Filled Button di layar ini)
            Button(
                onClick = onNextClick,
                enabled = isJudulValid,
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Lanjut: Bahan & Cara", style = MaterialTheme.typography.labelLarge)
            }
        }
    }
}

@Preview(name = "Light", showBackground = true)
@Preview(
    name = "Dark",
    showBackground = true,
    uiMode = Configuration.UI_MODE_NIGHT_YES
)
@Composable
fun PreviewInfoDasarResep() {
    BukuPanciTheme {
        InfoDasarResepScreen()
    }
}