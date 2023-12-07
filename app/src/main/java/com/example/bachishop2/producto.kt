package com.example.bachishop2

import android.content.Intent
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.bachishop2.databinding.ActivityMainBinding
import com.example.bachishop2.databinding.ActivityProductoBinding
import com.example.bachishop2.ui.home.HomeFragment
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

class producto : AppCompatActivity() {
    private lateinit var sharedPreferences: SharedPreferences
    private lateinit var binding: ActivityProductoBinding
    private var nombre: String? = null
    private var precio: Float? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_producto)


        binding = ActivityProductoBinding.inflate(layoutInflater)
        setContentView(binding.root)

        sharedPreferences = getSharedPreferences("MyPreferences", MODE_PRIVATE)
        nombre = sharedPreferences.getString("nombre", "")
        precio = sharedPreferences.getFloat("precio", 0f)
        binding.datos.text = sharedPreferences.getString("nombre", "")
        binding.prec.text = sharedPreferences.getFloat("precio", 0f).toString()
        binding.descript.text = sharedPreferences.getString("desc", "")

        binding.volver3.setOnClickListener {
            finish()
        }
        val editor = sharedPreferences.edit()
        binding.cestilla.setOnClickListener {
            var list:MutableList<String> = getList("carrito")
            list.add(nombre.toString())
            var precioAnterior: Float = sharedPreferences.getFloat("cuenta", 0f)
            println(precio)
            precioAnterior += precio as Float
            saveList("carrito", list)
            editor.putFloat("cuenta", precioAnterior)
            editor.apply()
            finish()
        }





    }
    private val gson = Gson()

    fun saveList(key: String, list: MutableList<String>) {
        val json = gson.toJson(list)
        sharedPreferences.edit().putString(key, json).apply()
    }

    fun getList(key: String): MutableList<String> {
        val json = sharedPreferences.getString(key, null)
        return if (json != null && json != "") {
            val type = object : TypeToken<List<String>>() {}.type
            gson.fromJson(json, type)
        } else {
            mutableListOf()
        }
    }
}