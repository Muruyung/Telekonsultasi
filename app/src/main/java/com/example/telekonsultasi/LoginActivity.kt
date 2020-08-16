package com.example.telekonsultasi

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.AlarmClock.EXTRA_MESSAGE
import android.view.View
import android.widget.EditText
import android.widget.Toast

class LoginActivity : AppCompatActivity() {

    private lateinit var pref : SharedPreferences

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        // Mengambil data session / shared preferences
        pref = getSharedPreferences("user_details", Context.MODE_PRIVATE)

        // Jika sudah pernah melakukan login, maka lanjut ke halaman selanjutnya
        if (pref.contains("username") && pref.contains("password")){
            openNextActivity()
        }
    }

    // Fungsi untuk membuka halaman selanjutnya
    private fun openNextActivity(){
        // Deklarasi variabel intent sebagai halaman selanjutnya
        val intent = Intent(this, MainActivity::class.java).apply {
            putExtra(EXTRA_MESSAGE, "Berhasil Login")
        }
        startActivity(intent) // Membuka halaman selanjutnya
        finish()
    }

    @SuppressLint("CommitPrefEdits", "ShowToast")
    // Fungsi ketika tombol masuk ditekan
    fun onButtonClick(v : View?){
        // Deklarasi dan inisialisasi variabel data username dan password
        val username = findViewById<EditText>(R.id.username).text.toString()
        val password = findViewById<EditText>(R.id.password).text.toString()

        val edit : SharedPreferences.Editor // Deklarasi editor shared preferences untuk mengedit isi data session

        // Kondisi jika username atau password kosong
        if (username == "" || password == ""){
            // Memunculkan popup toast peringatan
            val backToast = Toast.makeText(baseContext, "Username dan Password harus diisi !!!", Toast.LENGTH_SHORT)
            backToast.show()
        }else// Kondisi jika username dan password benar
        if ((username == "dokter" && password == "dokter123") || (username == "pasien" && password == "pasien123")){
            // Mengedit isi session sesuai dengan username dan password
            edit = pref.edit()
            edit.putString("username", username)
            edit.putString("password", password)
            edit.apply()

            openNextActivity() // Membuka halaman selanjutnya
        }else{ // Kondisi jika username atau password salah
            // Memunculkan popup toast peringatan
            val backToast = Toast.makeText(baseContext, "Username atau Password salah !!!", Toast.LENGTH_SHORT)
            backToast.show()
        }
    }
}