package com.example.bukupanci.ui.screen

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.bukupanci.ui.theme.BukuPanciTheme

// Komponen reusable: indikator langkah berbentuk teks (dipakai di 3 layar tambah resep)
@Composable
fun IndikatorLangkah(
    langkahSaatIni: Int,
    modifier: Modifier = Modifier
) {
    val daftarLangkah = listOf("Info Dasar", "Bahan & Cara", "Foto")

    Column(
        modifier = modifier.fillMaxWidth(),
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        Text(
            text = "LANGKAH $langkahSaatIni DARI ${daftarLangkah.size}",
            style = MaterialTheme.typography.labelLarge,
            color = MaterialTheme.colorScheme.primary
        )

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            daftarLangkah.forEachIndexed { index, namaLangkah ->
                val isAktif = index + 1 == langkahSaatIni
                Text(
                    text = "${index + 1}. $namaLangkah",
                    style = MaterialTheme.typography.bodyMedium,
                    fontWeight = if (isAktif) FontWeight.Bold else FontWeight.Normal,
                    color = if (isAktif) {
                        MaterialTheme.colorScheme.primary
                    } else {
                        MaterialTheme.colorScheme.onSurfaceVariant
                    }
                )
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun PreviewIndikatorLangkah() {
    BukuPanciTheme {
        IndikatorLangkah(langkahSaatIni = 2)
    }
}