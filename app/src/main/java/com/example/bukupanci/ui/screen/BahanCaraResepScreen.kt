package com.example.bukupanci.ui.screen

import android.content.res.Configuration
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FilledTonalIconButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
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
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.bukupanci.ui.theme.BukuPanciTheme

// ============ KOMPONEN KECIL (dipakai Stateless di bawah) ============

// Judul bagian + keterangan singkat
@Composable
fun HeaderBagian(
    judul: String,
    keterangan: String,
    modifier: Modifier = Modifier
) {
    Column(modifier = modifier) {
        Text(text = judul, style = MaterialTheme.typography.titleLarge)
        Text(
            text = keterangan,
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )
    }
}

// Satu baris bahan: kolom teks + tombol hapus
@Composable
fun ItemBahan(
    nomor: Int,
    bahan: String,
    onBahanChange: (String) -> Unit,
    onHapusClick: () -> Unit,
    isHapusEnabled: Boolean
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)
    ) {
        Row(
            modifier = Modifier.padding(horizontal = 12.dp, vertical = 8.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            OutlinedTextField(
                value = bahan,
                onValueChange = onBahanChange,
                label = { Text("Bahan $nomor") },
                placeholder = { Text("mis. 500 gr paha ayam fillet") },
                singleLine = true,
                modifier = Modifier.weight(1f)
            )
            IconButton(onClick = onHapusClick, enabled = isHapusEnabled) {
                Icon(imageVector = Icons.Default.Close, contentDescription = "Hapus bahan")
            }
        }
    }
}

// Satu kartu langkah memasak: nomor + judul tahap + isi langkah
@Composable
fun ItemLangkah(
    nomor: Int,
    judul: String,
    isi: String,
    onJudulChange: (String) -> Unit,
    onIsiChange: (String) -> Unit,
    onHapusClick: () -> Unit,
    isHapusEnabled: Boolean
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)
    ) {
        Column(
            modifier = Modifier.padding(12.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                // Lingkaran bernomor
                Box(
                    modifier = Modifier
                        .size(32.dp)
                        .clip(CircleShape)
                        .background(MaterialTheme.colorScheme.primary),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = nomor.toString(),
                        style = MaterialTheme.typography.labelLarge,
                        color = MaterialTheme.colorScheme.onPrimary
                    )
                }
                OutlinedTextField(
                    value = judul,
                    onValueChange = onJudulChange,
                    label = { Text("Judul tahap") },
                    singleLine = true,
                    modifier = Modifier.weight(1f)
                )
                IconButton(onClick = onHapusClick, enabled = isHapusEnabled) {
                    Icon(imageVector = Icons.Default.Delete, contentDescription = "Hapus langkah")
                }
            }
            OutlinedTextField(
                value = isi,
                onValueChange = onIsiChange,
                label = { Text("Isi langkah") },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(120.dp)
            )
        }
    }
}

// Penghitung satu satuan waktu: [-] 05 [+] dengan label di bawahnya
@Composable
fun PenghitungWaktu(
    label: String,
    nilai: Int,
    nilaiMaks: Int,
    onKurangClick: () -> Unit,
    onTambahClick: () -> Unit
) {
    // Angka di bawah 10 diberi nol di depan, mis. 5 -> "05"
    val teksNilai = if (nilai < 10) "0$nilai" else "$nilai"

    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(4.dp)
        ) {
            FilledTonalIconButton(onClick = onKurangClick, enabled = nilai > 0) { Text("-") }
            Text(text = teksNilai, style = MaterialTheme.typography.headlineMedium)
            FilledTonalIconButton(onClick = onTambahClick, enabled = nilai < nilaiMaks) { Text("+") }
        }
        Text(
            text = label,
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )
    }
}

// ============ STATEFUL: pengelola state ============
@Composable
fun BahanCaraResepScreen(
    judulDraf: String = "",
    onBackClick: () -> Unit = {},
    onNextClick: () -> Unit = {}
) {
    // Input pengguna -> rememberSaveable. Awalnya masing-masing punya satu kolom kosong.
    var daftarBahan by rememberSaveable { mutableStateOf(listOf("")) }
    // Judul dan isi langkah disimpan di dua list; posisi (index) yang sama = langkah yang sama
    var daftarJudulLangkah by rememberSaveable { mutableStateOf(listOf("")) }
    var daftarIsiLangkah by rememberSaveable { mutableStateOf(listOf("")) }
    var menit by rememberSaveable { mutableStateOf(0) }
    var detik by rememberSaveable { mutableStateOf(0) }

    StatelessBahanCaraResep(
        judulDraf = judulDraf,
        daftarBahan = daftarBahan,
        // Ganti isi bahan pada posisi index saja, sisanya tetap
        onBahanChange = { index, teks ->
            daftarBahan = daftarBahan.mapIndexed { i, lama -> if (i == index) teks else lama }
        },
        // Buang bahan pada posisi index
        onBahanHapus = { index ->
            daftarBahan = daftarBahan.filterIndexed { i, _ -> i != index }
        },
        onBahanTambah = { daftarBahan = daftarBahan + "" },
        // Saran cepat: buang kolom kosong dulu, lalu tambahkan saran
        onSaranClick = { saran ->
            daftarBahan = daftarBahan.filter { it.isNotBlank() } + saran
        },
        daftarJudulLangkah = daftarJudulLangkah,
        daftarIsiLangkah = daftarIsiLangkah,
        onJudulLangkahChange = { index, teks ->
            daftarJudulLangkah = daftarJudulLangkah.mapIndexed { i, lama -> if (i == index) teks else lama }
        },
        onIsiLangkahChange = { index, teks ->
            daftarIsiLangkah = daftarIsiLangkah.mapIndexed { i, lama -> if (i == index) teks else lama }
        },
        // Dua list harus dihapus bersamaan agar tetap sejajar
        onLangkahHapus = { index ->
            daftarJudulLangkah = daftarJudulLangkah.filterIndexed { i, _ -> i != index }
            daftarIsiLangkah = daftarIsiLangkah.filterIndexed { i, _ -> i != index }
        },
        onLangkahTambah = {
            daftarJudulLangkah = daftarJudulLangkah + ""
            daftarIsiLangkah = daftarIsiLangkah + ""
        },
        menit = menit,
        onMenitKurang = { menit = menit - 1 },
        onMenitTambah = { menit = menit + 1 },
        detik = detik,
        onDetikKurang = { detik = detik - 1 },
        onDetikTambah = { detik = detik + 1 },
        onBackClick = onBackClick,
        onNextClick = onNextClick
    )
}

// ============ STATELESS: hanya menampilkan UI ============
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun StatelessBahanCaraResep(
    judulDraf: String,
    daftarBahan: List<String>,
    onBahanChange: (Int, String) -> Unit,
    onBahanHapus: (Int) -> Unit,
    onBahanTambah: () -> Unit,
    onSaranClick: (String) -> Unit,
    daftarJudulLangkah: List<String>,
    daftarIsiLangkah: List<String>,
    onJudulLangkahChange: (Int, String) -> Unit,
    onIsiLangkahChange: (Int, String) -> Unit,
    onLangkahHapus: (Int) -> Unit,
    onLangkahTambah: () -> Unit,
    menit: Int,
    onMenitKurang: () -> Unit,
    onMenitTambah: () -> Unit,
    detik: Int,
    onDetikKurang: () -> Unit,
    onDetikTambah: () -> Unit,
    onBackClick: () -> Unit,
    onNextClick: () -> Unit
) {
    val saranBahan = listOf("Garam", "Lada bubuk", "Minyak goreng")
    // Saran yang sudah ada di daftar bahan tidak ditampilkan lagi
    val saranTersedia = saranBahan.filter { !daftarBahan.contains(it) }

    // Validasi sederhana: minimal satu bahan dan satu isi langkah terisi
    val isBahanValid = daftarBahan.filter { it.isNotBlank() }.isNotEmpty()
    // Judul tahap dan isi langkah wajib terisi di SEMUA langkah (Step.stepTitle tidak boleh kosong)
    val isLangkahValid = daftarJudulLangkah.filter { it.isBlank() }.isEmpty() &&
            daftarIsiLangkah.filter { it.isBlank() }.isEmpty()
    val isFormValid = isBahanValid && isLangkahValid

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
            // Tahap 2.1: label draf (hanya muncul kalau judul sudah ada) + indikator langkah
            if (judulDraf.isNotBlank()) {
                Text(
                    text = "Menulis draf: $judulDraf",
                    style = MaterialTheme.typography.bodyMedium,
                    fontWeight = FontWeight.Bold
                )
            }
            IndikatorLangkah(langkahSaatIni = 2)

            // Tahap 2.2: bagian bahan-bahan
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                HeaderBagian(
                    judul = "Bahan-Bahan",
                    keterangan = "Tulis satu bahan per kolom",
                    modifier = Modifier.weight(1f)
                )
                OutlinedButton(onClick = onBahanTambah) {
                    Icon(imageVector = Icons.Default.Add, contentDescription = null)
                    Spacer(modifier = Modifier.width(4.dp))
                    Text("Tambah")
                }
            }

            Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                daftarBahan.forEachIndexed { index, bahan ->
                    ItemBahan(
                        nomor = index + 1,
                        bahan = bahan,
                        onBahanChange = { onBahanChange(index, it) },
                        onHapusClick = { onBahanHapus(index) },
                        // Bahan terakhir tidak boleh dihapus
                        isHapusEnabled = daftarBahan.size > 1
                    )
                }
            }

            // Tahap 2.3: saran cepat
            if (saranTersedia.isNotEmpty()) {
                Text(
                    text = "Saran cepat:",
                    style = MaterialTheme.typography.labelLarge,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
                LazyRow(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    items(saranTersedia, key = { it }) { saran ->
                        Card(
                            modifier = Modifier.clickable { onSaranClick(saran) },
                            colors = CardDefaults.cardColors(
                                containerColor = MaterialTheme.colorScheme.surfaceVariant,
                                contentColor = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                        ) {
                            Text(
                                text = "+ $saran",
                                modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp),
                                fontWeight = FontWeight.Medium
                            )
                        }
                    }
                }
            }

            // Tahap 2.4: bagian langkah memasak
            HeaderBagian(
                judul = "Langkah Memasak",
                keterangan = "Instruksi ringkas dan runut"
            )

            Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
                daftarJudulLangkah.forEachIndexed { index, judulLangkah ->
                    ItemLangkah(
                        nomor = index + 1,
                        judul = judulLangkah,
                        isi = daftarIsiLangkah[index],
                        onJudulChange = { onJudulLangkahChange(index, it) },
                        onIsiChange = { onIsiLangkahChange(index, it) },
                        onHapusClick = { onLangkahHapus(index) },
                        // Langkah terakhir tidak boleh dihapus
                        isHapusEnabled = daftarJudulLangkah.size > 1
                    )
                }
            }

            OutlinedButton(
                onClick = onLangkahTambah,
                modifier = Modifier.fillMaxWidth()
            ) {
                Icon(imageVector = Icons.Default.Add, contentDescription = null)
                Spacer(modifier = Modifier.width(4.dp))
                Text("Tambah Langkah Berikutnya")
            }

            // Tahap 2.5: estimasi waktu memasak
            HeaderBagian(
                judul = "Estimasi Waktu Memasak",
                keterangan = "Akan dikonversi menjadi timer interaktif di mode masak"
            )

            Card(
                modifier = Modifier.fillMaxWidth(),
                elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(12.dp),
                    horizontalArrangement = Arrangement.SpaceEvenly,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    PenghitungWaktu(
                        label = "Menit",
                        nilai = menit,
                        nilaiMaks = 600,
                        onKurangClick = onMenitKurang,
                        onTambahClick = onMenitTambah
                    )
                    Text(text = ":", style = MaterialTheme.typography.headlineMedium)
                    PenghitungWaktu(
                        label = "Detik",
                        nilai = detik,
                        nilaiMaks = 59,
                        onKurangClick = onDetikKurang,
                        onTambahClick = onDetikTambah
                    )
                }
            }

            // Tahap 2.6: kartu info
            Card(
                modifier = Modifier.fillMaxWidth(),
                colors = CardDefaults.cardColors(
                    containerColor = MaterialTheme.colorScheme.surfaceVariant,
                    contentColor = MaterialTheme.colorScheme.onSurfaceVariant
                )
            ) {
                Column(modifier = Modifier.padding(12.dp)) {
                    Text(
                        text = "Tersimpan di Buku Pribadi",
                        style = MaterialTheme.typography.labelLarge
                    )
                    Text(
                        text = "Catatan resep ini bersifat rahasia dan disimpan langsung di memori perangkat offline Anda.",
                        style = MaterialTheme.typography.bodyMedium
                    )
                }
            }

            // Petunjuk singkat kalau tombol Lanjut masih mati
            if (!isFormValid) {
                Text(
                    text = "Isi minimal satu bahan, dan semua judul tahap beserta isi langkahnya.",
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }

            // Tahap 2.7: tombol navigasi (satu-satunya Filled Button di layar ini: Lanjut)
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                OutlinedButton(onClick = onBackClick) {
                    Text("Kembali")
                }
                Button(
                    onClick = onNextClick,
                    enabled = isFormValid,
                    modifier = Modifier.weight(1f)
                ) {
                    Text("Lanjut: Foto", style = MaterialTheme.typography.labelLarge)
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
fun PreviewBahanCaraResep() {
    BukuPanciTheme {
        BahanCaraResepScreen(judulDraf = "Ayam Bakar Madu Pedas")
    }
}