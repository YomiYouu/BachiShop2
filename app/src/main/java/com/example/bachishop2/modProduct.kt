package com.example.bachishop2

import android.app.AlertDialog
import android.content.Intent
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import com.example.bachishop2.databinding.ActivityModProductBinding
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase

class modProduct : AppCompatActivity() {
    private lateinit var binding: ActivityModProductBinding
    private lateinit var db: FirebaseFirestore
    private lateinit var sharedPreferences: SharedPreferences
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_product)


        binding = ActivityModProductBinding.inflate(layoutInflater)
        setContentView(binding.root)

        sharedPreferences = getSharedPreferences("MyPreferences", MODE_PRIVATE)

        binding.volver5.setOnClickListener {
            finish()
        }
        binding.subil.setOnClickListener {
            if(comprobar()) {
                AlertDialog.Builder(this)
                    .setTitle("Su producto esta a punto de actualizarse")
                    .setMessage("Estas seguro de que desea actualizar este producto?")
                    .setCancelable(false)
                    .setPositiveButton(android.R.string.ok, {
                            dialog, which -> actualizar()
                        Toast.makeText(
                            baseContext,
                            "Producto actualizado con exito",
                            Toast.LENGTH_LONG,
                        ).show()
                        finish()
                    })
                    .setNegativeButton(android.R.string.cancel, {dialog, which ->
                        Snackbar.make(binding.root, "No se ha actualizado el producto", Snackbar.LENGTH_LONG).show()
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
    private fun actualizar(){
        val db = Firebase.firestore
        val itemRef = db.collection(sharedPreferences.getString("coleccion", "").toString()).document(sharedPreferences.getString("idProducto", "").toString())

        var name: String = binding.pelucheName.text.toString()
        var plesio: Float = binding.plesio2.text.toString().toFloat()
        var desc: String = binding.description2.text.toString()


        val item = hashMapOf(
            "descripcion" to desc,
            "nombre" to name,
            "img" to "custom",
            "precio" to plesio,
        )

// Add a new document with a generated ID
            itemRef.update(item as Map<String, Any>)
            .addOnSuccessListener { documentReference ->
                Log.d("TAG", "DocumentSnapshot added")
            }
            .addOnFailureListener { e ->
                Log.w("TAG", "Error adding document", e)
            }
    }
    private fun comprobar():Boolean{
        if (binding.pelucheName.text.isEmpty()){
            return false
        }
        if(binding.plesio2.text.isEmpty()){
            return false
        }
        if (binding.description2.text.isEmpty()){
            return false
        }

        return true
    }
}