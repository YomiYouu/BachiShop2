package com.example.bachishop2

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import com.example.bachishop2.databinding.ActivityCrearCuentaBinding
import com.google.firebase.analytics.FirebaseAnalytics
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase

class crear_cuenta : AppCompatActivity() {
    private lateinit var  binding: ActivityCrearCuentaBinding
    private lateinit var auth: FirebaseAuth
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        // Initialize Firebase Auth
        auth = Firebase.auth

        binding = ActivityCrearCuentaBinding.inflate(layoutInflater)
        val view = binding.root

        setContentView(view)

        binding.btnCreal.setOnClickListener {
            if(binding.crearUser.text.isEmpty()&&binding.crearPassword.text.isEmpty()&&binding.confirmarCreacionP.text.isEmpty()){
                Toast.makeText(baseContext, "Por favor no deje ningun campo vacio", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            if(binding.crearPassword.text.toString() != binding.confirmarCreacionP.text.toString()){
                Toast.makeText(baseContext, "Las dos contraseñas no coinciden", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }
            FirebaseAuth.getInstance()
            val email = binding.crearUser.text.toString();
            val pass = binding.crearPassword.text.toString();

            crearCuenta(email, pass)
        }

         binding.volverAIniciar.setOnClickListener {
             val intent = Intent(this, IniciarSesion::class.java)
             startActivity(intent)
         }
        binding.returnn2.setOnClickListener {
            finish()
        }
    }

    private fun crearCuenta(email:String, password:String){

        val analytics = FirebaseAnalytics.getInstance(this)
        val bundle = Bundle()
        bundle.putString("message", "Integracion de firebase completa")

        Log.v("UNATAF", email)
        Log.v("UNATAF", password)

        analytics.logEvent("InitScreen", bundle)
        auth.createUserWithEmailAndPassword(email, password)
            .addOnCompleteListener(this) { task ->
                if (task.isSuccessful) {
                    // Sign in success, update UI with the signed-in user's information
                    Log.d("SignUpApp", "Usuario creado con exito")
                    val user = auth.currentUser
                    Log.v("d", user.toString())
                    Toast.makeText(
                        baseContext,
                        "Usuario creado con exito",
                        Toast.LENGTH_LONG,
                    ).show()
                    val intent = Intent(this, IniciarSesion::class.java)
                    startActivity(intent)
                    //updateUI(user)
                } else {
                    // If sign in fails, display a message to the user.
                    Log.w("SignUpAppFailure", "Fallo al crear usuario", task.exception)
                    Toast.makeText(
                        baseContext,
                        "Error al crear usuario",
                        Toast.LENGTH_SHORT,
                    ).show()
                    // updateUI(null)
                }
            }
    }
}