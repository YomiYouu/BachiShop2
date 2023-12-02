package com.example.bachishop2

import android.content.Intent
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.bachishop2.databinding.ActivityMainBinding
import com.example.bachishop2.databinding.ActivityProductoBinding
import com.example.bachishop2.ui.home.HomeFragment

class producto : AppCompatActivity() {
    private lateinit var sharedPreferences: SharedPreferences
    private lateinit var binding: ActivityProductoBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_producto)

        binding = ActivityProductoBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.volver3.setOnClickListener {
            finish()
        }

        sharedPreferences = getSharedPreferences("MyPreferences", MODE_PRIVATE)

        binding.datos.text = sharedPreferences.getString("nombre", "")
        binding.prec.text = sharedPreferences.getString("precio", "")
        binding.descript.text = sharedPreferences.getString("desc", "")



    }
}