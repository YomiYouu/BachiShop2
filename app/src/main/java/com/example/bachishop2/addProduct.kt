package com.example.bachishop2

import android.app.AlertDialog
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import com.example.bachishop2.databinding.ActivityAddProductBinding
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase

class addProduct : AppCompatActivity() {
    private lateinit var binding: ActivityAddProductBinding
    private lateinit var db: FirebaseFirestore
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_product)


        binding = ActivityAddProductBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.volver4.setOnClickListener {
            finish()
        }
        binding.subir.setOnClickListener {
        if(comprobar()) {
            AlertDialog.Builder(this)
                .setTitle("Su producto esta a punto de publicarse")
                .setMessage("Estas seguro de que desea subir este producto?")
                .setCancelable(false)
                .setPositiveButton(android.R.string.ok, {
                        dialog, which -> aniadil()
                    Toast.makeText(
                        baseContext,
                        "Producto añadido con exito",
                        Toast.LENGTH_LONG,
                    ).show()
                    finish()
                })
                .setNegativeButton(android.R.string.cancel, {dialog, which ->
                    Snackbar.make(binding.root, "no se ha subido el producto", Snackbar.LENGTH_LONG).show()
                }).show()

        }
        else{
                //Tremendo toast
            AlertDialog.Builder(this)
                .setTitle("ATENCION")
                .setMessage("No deje ningun campo vacio")
                .setCancelable(false)
                .setPositiveButton(android.R.string.ok, {
                        dialog, which ->
                }).show()
        }
        }

    }
    private fun aniadil(){
        val db = Firebase.firestore
        var name: String = binding.peluxeName.text.toString()
        var plesio: Float = binding.plesio.text.toString().toFloat()
        var desc: String = binding.description.text.toString()
        var categoria: String = binding.spinner.selectedItem.toString()
        when(categoria){
            "Animal marino" -> {
                categoria = "marinos"
            }
            "Animal terrestre" -> {
                categoria = "terrestres"
            }
            "Ave" -> {
                categoria = "aves"
            }
            "Reptil" -> {
                categoria = "reptiles"
            }
            "Animal fantastico" -> {
                categoria = "fantasticos"
            }
        }

        val item = hashMapOf(
            "descripcion" to desc,
            "nombre" to name,
            "img" to "custom",
            "precio" to plesio,
        )

// Add a new document with a generated ID
        db.collection(categoria)
            .add(item)
            .addOnSuccessListener { documentReference ->
                Log.d("TAG", "DocumentSnapshot added with ID: ${documentReference.id}")
            }
            .addOnFailureListener { e ->
                Log.w("TAG", "Error adding document", e)
            }
    }
    private fun comprobar():Boolean{
       if (binding.peluxeName.text.isEmpty()){
           return false
       }
        if(binding.plesio.text.isEmpty()){
            return false
        }
        if (binding.description.text.isEmpty()){
            return false
        }
        if (binding.spinner.selectedItem.toString().equals("Categoria")){
            return false
        }
        return true
    }
}