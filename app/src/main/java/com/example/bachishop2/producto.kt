package com.example.bachishop2

import android.app.AlertDialog
import android.content.Intent
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.core.view.isVisible
import com.example.bachishop2.databinding.ActivityMainBinding
import com.example.bachishop2.databinding.ActivityProductoBinding
import com.example.bachishop2.ui.home.HomeFragment
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
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
        var email: String? = sharedPreferences.getString("correo", "")
        binding.imgno.setImageResource(sharedPreferences.getInt("img", 0))
        val product:String = sharedPreferences.getString("idProducto", "").toString()
        val coleccion:String = sharedPreferences.getString("coleccion", "").toString()
        if (!email.equals("ryunex03@gmail.com")){
            binding.del.isVisible = false
            binding.mod.isVisible = false
        }
        binding.del.setOnClickListener {
            val db = Firebase.firestore
            AlertDialog.Builder(this)
                .setTitle("Esta a punto de eliminar un producto.")
                .setMessage("Estas seguro de eliminar el producto?")
                .setCancelable(false)
                .setPositiveButton(android.R.string.ok, {
                        dialog, which -> db.collection(coleccion).document(product)
                    .delete()
                    .addOnSuccessListener { Log.d("TAG", "DocumentSnapshot successfully deleted!") }
                    .addOnFailureListener { e -> Log.w("TAG", "Error deleting document", e) }
                    Toast.makeText(
                        baseContext,
                        "Producto eliminado con exito",
                        Toast.LENGTH_LONG,
                    ).show()
                    val intent = Intent(this, MainActivity::class.java)
                    startActivity(intent)
                })
                .setNegativeButton(android.R.string.cancel, {dialog, which ->
                    Snackbar.make(binding.root, "no se ha borrado el producto", Snackbar.LENGTH_LONG).show()
                }).show()


        }
        binding.mod.setOnClickListener {
            val intent = Intent (this, modProduct::class.java)
            startActivity(intent)
            finish()
        }



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