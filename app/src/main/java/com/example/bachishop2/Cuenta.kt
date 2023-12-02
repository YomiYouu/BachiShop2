package com.example.bachishop2

import android.content.SharedPreferences
import android.graphics.BitmapFactory
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.bachishop2.databinding.ActivityCuentaBinding
import java.net.URL


class Cuenta : AppCompatActivity() {
    private lateinit var sharedPreferences: SharedPreferences
    private lateinit var  binding: ActivityCuentaBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_cuenta)

        binding = ActivityCuentaBinding.inflate(layoutInflater)
        setContentView(binding.root)

        sharedPreferences = getSharedPreferences("MyPreferences", MODE_PRIVATE)
        val url = URL(sharedPreferences.getString("foto", ""))
        val bmp = BitmapFactory.decodeStream(url.openConnection().getInputStream())
        binding.perfil.setImageBitmap(bmp)
        binding.cuentaa.text = "Email: " + sharedPreferences.getString("correo", "")

    }

}

