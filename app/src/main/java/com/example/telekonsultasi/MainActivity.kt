package com.example.telekonsultasi

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.provider.AlarmClock
import android.view.View
import android.widget.ArrayAdapter
import android.widget.ListView
import android.widget.Toast
import androidx.activity.OnBackPressedCallback
import androidx.appcompat.app.AppCompatActivity
import org.jitsi.meet.sdk.JitsiMeet
import org.jitsi.meet.sdk.JitsiMeetActivity
import org.jitsi.meet.sdk.JitsiMeetConferenceOptions
import org.jitsi.meet.sdk.JitsiMeetUserInfo
import java.net.MalformedURLException
import java.net.URL
import kotlin.properties.Delegates

class MainActivity : AppCompatActivity() {
    private lateinit var pref : SharedPreferences
    private lateinit var backToast : Toast
    private var backPressedTime by Delegates.notNull<Long>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        pref = getSharedPreferences("user_details", Context.MODE_PRIVATE)

        // Inisialisasi opsi default pada Jitsi Meet Conference
        val serverURL: URL
        serverURL = try {
            URL("https://meet.jit.si")
        } catch (e: MalformedURLException) {
            e.printStackTrace()
            throw RuntimeException("Invalid server URL!")
        }
        val defaultOptions = JitsiMeetConferenceOptions.Builder()
            .setServerURL(serverURL)
            .setWelcomePageEnabled(false)
            .build()
        JitsiMeet.setDefaultConferenceOptions(defaultOptions)

        // Pemisah login pasien dan dokter
        val arr = arrayOfNulls<String>(1)
        val nama : String
        if (pref.getString("username", null) == "dokter"){
            arr[0] = "Hubungi Pasien"
            nama = "Dokter"
        }else{
            arr[0] = "Hubungi Dokter"
            nama = "Pasien"
        }

        // Menginisialisasi adapter untuk digunakan di ListView
        val adapter = ArrayAdapter(this, android.R.layout.simple_list_item_1, arr)

        val listView = findViewById<ListView>(R.id.isiList)
        listView.adapter = adapter

        // Kondisi ketika pilihan list di klik
        listView.setOnItemClickListener { _, _, _: Int, _: Long ->
            val setUser = Bundle()
            setUser.putString("displayName", nama)

            // Inisialisasi opsi tambahan pada Jitsi Meet Conference
            val userInfo = JitsiMeetUserInfo(setUser)
            val options = JitsiMeetConferenceOptions.Builder()
                .setRoom("dummyRoomTelekonsultasi123")
                .setUserInfo(userInfo)
                .build()
            // Membuka halaman Jitsi Meet Conference sesuai dengan opsi
            JitsiMeetActivity.launch(this, options)
        }
    }

    // Fungsi untuk membuka halaman sebelumnya
    private fun openBackActivity(){
        // Deklarasi variabel intent sebagai halaman selanjutnya
        val intent = Intent(this, LoginActivity::class.java).apply {
            putExtra(AlarmClock.EXTRA_MESSAGE, "Anda Keluar")
        }
        startActivity(intent) // Membuka halaman selanjutnya
        finish()
    }

    // Fungsi ketika mengklik tombol log out
    fun onButtonClick(v : View?) {
        val edit : SharedPreferences.Editor = pref.edit()
        // Menghapus isi session
        edit.remove("username")
        edit.remove("password")
        edit.apply()
        openBackActivity() // Kembali ke halaman sebelumnya
    }
}
