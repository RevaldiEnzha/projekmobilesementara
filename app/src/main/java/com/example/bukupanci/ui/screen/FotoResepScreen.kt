package com.example.bukupanci.ui.screen

import android.content.res.Configuration
import android.graphics.Bitmap
import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.bukupanci.ui.theme.BukuPanciTheme

// ============ STATEFUL: pengelola state ============
@Composable
fun FotoResepScreen(
    judulDraf: String = "",
    onBackClick: () -> Unit = {},
    onSaveClick: () -> Unit = {}
) {
    // Foto dari galeri (Uri kecil, aman disimpan rememberSaveable)
    var imageUri by rememberSaveable { mutableStateOf<Uri?>(null) }

    StatelessFotoResep(
        judulDraf = judulDraf,
        imageUri = imageUri,
        onGaleriDipilih = { imageUri = it },
        onFotoHapus = { imageUri = null },
        onBackClick = onBackClick,
        onSaveClick = onSaveClick
    )
}

// ============ STATELESS: hanya menampilkan UI ============
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun StatelessFotoResep(
    judulDraf: String,
    imageUri: Uri?,
    onGaleriDipilih: (Uri) -> Unit,
    onFotoHapus: () -> Unit,
    onBackClick: () -> Unit,
    onSaveClick: () -> Unit
) {
    // Tahap 3.1: launcher galeri (pola dari HubungiKamiScreen)
    val photoPickerLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.PickVisualMedia(),
        onResult = { uri ->
            // uri bernilai null kalau pengguna menutup pemilih tanpa memilih
            if (uri != null) onGaleriDipilih(uri)
        }
    )

    // Teks status foto, dipilih dengan if sebagai ekspresi
    val teksStatus = if (imageUri != null) {
        "Foto terpilih: ${imageUri.lastPathSegment ?: "tanpa nama"}"
    } else {
        "Belum ada foto. Langkah ini boleh dilewati."
    }

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
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            // Tahap 3.2: label draf + indikator langkah (langkah 3)
            if (judulDraf.isNotBlank()) {
                Text(
                    text = "Menulis draf: $judulDraf",
                    style = MaterialTheme.typography.bodyMedium,
                    fontWeight = FontWeight.Bold
                )
            }
            IndikatorLangkah(langkahSaatIni = 3)

            // Tahap 3.3: judul bagian
            HeaderBagian(
                judul = "Foto Masakan",
                keterangan = "Tambahkan foto agar resepmu lebih menarik (boleh dilewati)"
            )

            // Tahap 3.4: tombol pilih dari galeri
            OutlinedButton(
                onClick = {
                    photoPickerLauncher.launch(
                        PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly)
                    )
                },
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Pilih dari Galeri")
            }

            // Tahap 3.5: kartu status foto
            Card(
                modifier = Modifier.fillMaxWidth(),
                colors = CardDefaults.cardColors(
                    containerColor = MaterialTheme.colorScheme.surfaceVariant,
                    contentColor = MaterialTheme.colorScheme.onSurfaceVariant
                )
            ) {
                Column(
                    modifier = Modifier.padding(12.dp),
                    verticalArrangement = Arrangement.spacedBy(4.dp)
                ) {
                    Text(text = teksStatus, style = MaterialTheme.typography.bodyMedium)
                    // Tombol hapus hanya muncul kalau sudah ada foto
                    if (imageUri != null) {
                        TextButton(onClick = onFotoHapus) {
                            Text("Hapus foto")
                        }
                    }
                }
            }

            // Pendorong: membuat tombol bawah turun ke dasar layar
            Spacer(modifier = Modifier.weight(1f))

            // Tahap 3.6: tombol navigasi (satu-satunya Filled Button: Simpan)
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                OutlinedButton(onClick = onBackClick) {
                    Text("Kembali")
                }
                Button(
                    onClick = onSaveClick,
                    modifier = Modifier.weight(1f)
                ) {
                    Text("Simpan Resep", style = MaterialTheme.typography.labelLarge)
                }
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
fun PreviewFotoResep() {
    BukuPanciTheme {
        FotoResepScreen(judulDraf = "Ayam Bakar Madu Pedas")
    }
}