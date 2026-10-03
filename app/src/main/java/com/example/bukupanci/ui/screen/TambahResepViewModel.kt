package com.example.bukupanci.ui.screen

import android.net.Uri
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.bukupanci.data.model.Recipe
import com.example.bukupanci.data.repository.RecipeRepository
import com.example.bukupanci.data.repository.StepDraft
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

// Semua data form tiga layar dikumpulkan di satu tempat
data class TambahResepUiState(
    val judul: String = "",
    val porsi: Int = 1,
    val deskripsi: String = "",
    val daftarBahan: List<String> = listOf(""),
    val daftarJudulLangkah: List<String> = listOf(""),
    val daftarIsiLangkah: List<String> = listOf(""),
    val menit: Int = 0,
    val detik: Int = 0,
    val imageUri: Uri? = null,
    val isMenyimpan: Boolean = false,
    val isTersimpan: Boolean = false,
    val pesanError: String? = null
)

class TambahResepViewModel : ViewModel() {

    private val _uiState = MutableStateFlow(TambahResepUiState())
    val uiState: StateFlow<TambahResepUiState> = _uiState.asStateFlow()

    // ---------- Layar 1: Info Dasar ----------
    fun onJudulChange(judul: String) {
        _uiState.value = _uiState.value.copy(judul = judul)
    }

    fun onPorsiChange(porsi: Int) {
        _uiState.value = _uiState.value.copy(porsi = porsi)
    }

    fun onDeskripsiChange(deskripsi: String) {
        _uiState.value = _uiState.value.copy(deskripsi = deskripsi)
    }

    // ---------- Layar 2: Bahan ----------
    fun onBahanChange(index: Int, teks: String) {
        val baru = _uiState.value.daftarBahan.mapIndexed { i, lama -> if (i == index) teks else lama }
        _uiState.value = _uiState.value.copy(daftarBahan = baru)
    }

    fun onBahanHapus(index: Int) {
        val baru = _uiState.value.daftarBahan.filterIndexed { i, _ -> i != index }
        _uiState.value = _uiState.value.copy(daftarBahan = baru)
    }

    fun onBahanTambah() {
        _uiState.value = _uiState.value.copy(daftarBahan = _uiState.value.daftarBahan + "")
    }

    // Saran cepat: buang kolom kosong dulu, lalu tambahkan saran
    fun onSaranClick(saran: String) {
        val baru = _uiState.value.daftarBahan.filter { it.isNotBlank() } + saran
        _uiState.value = _uiState.value.copy(daftarBahan = baru)
    }

    // ---------- Layar 2: Langkah ----------
    fun onJudulLangkahChange(index: Int, teks: String) {
        val baru = _uiState.value.daftarJudulLangkah.mapIndexed { i, lama -> if (i == index) teks else lama }
        _uiState.value = _uiState.value.copy(daftarJudulLangkah = baru)
    }

    fun onIsiLangkahChange(index: Int, teks: String) {
        val baru = _uiState.value.daftarIsiLangkah.mapIndexed { i, lama -> if (i == index) teks else lama }
        _uiState.value = _uiState.value.copy(daftarIsiLangkah = baru)
    }

    // Dua list harus diubah bersamaan agar posisinya tetap sejajar
    fun onLangkahHapus(index: Int) {
        _uiState.value = _uiState.value.copy(
            daftarJudulLangkah = _uiState.value.daftarJudulLangkah.filterIndexed { i, _ -> i != index },
            daftarIsiLangkah = _uiState.value.daftarIsiLangkah.filterIndexed { i, _ -> i != index }
        )
    }

    fun onLangkahTambah() {
        _uiState.value = _uiState.value.copy(
            daftarJudulLangkah = _uiState.value.daftarJudulLangkah + "",
            daftarIsiLangkah = _uiState.value.daftarIsiLangkah + ""
        )
    }

    // ---------- Layar 2: Waktu ----------
    fun onMenitChange(menit: Int) {
        _uiState.value = _uiState.value.copy(menit = menit)
    }

    fun onDetikChange(detik: Int) {
        _uiState.value = _uiState.value.copy(detik = detik)
    }

    // ---------- Layar 3: Foto ----------
    fun onImageUriChange(uri: Uri?) {
        _uiState.value = _uiState.value.copy(imageUri = uri)
    }

    // ---------- Simpan ----------
    // Repository dikirim lewat parameter supaya ViewModel ini tidak butuh Factory
    fun simpanResep(repository: RecipeRepository) {
        val state = _uiState.value
        if (state.isMenyimpan) return   // cegah tombol ditekan dua kali

        _uiState.value = state.copy(isMenyimpan = true, pesanError = null)

        viewModelScope.launch {
            try {
                // Waktu disimpan sebagai total detik; 0 berarti tidak diisi
                val totalDetik = state.menit * 60 + state.detik

                val recipe = Recipe(
                    title = state.judul.trim(),
                    imageSource = state.imageUri?.toString(),
                    cookingTimeEstimation = if (totalDetik > 0) totalDetik else null,
                    description = if (state.deskripsi.isBlank()) null else state.deskripsi.trim(),
                    servings = state.porsi
                )
                val bahan = state.daftarBahan
                    .filter { it.isNotBlank() }
                    .map { it.trim() }
                val langkah = state.daftarJudulLangkah.mapIndexed { index, judul ->
                    StepDraft(
                        title = judul.trim(),
                        description = state.daftarIsiLangkah[index].trim()
                    )
                }

                repository.saveNewRecipe(recipe, bahan, langkah)
                _uiState.value = _uiState.value.copy(isMenyimpan = false, isTersimpan = true)
            } catch (e: Exception) {
                // Pesan ramah untuk pengguna, bukan Exception mentah
                _uiState.value = _uiState.value.copy(
                    isMenyimpan = false,
                    pesanError = "Resep belum berhasil disimpan. Silakan coba lagi."
                )
            }
        }
    }

    // Kosongkan form setelah resep tersimpan
    fun resetForm() {
        _uiState.value = TambahResepUiState()
    }
}