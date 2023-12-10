package com.example.bachishop2


import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.graphics.BitmapFactory
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.forEach
import com.example.bachishop2.databinding.ActivityIniciarSesionBinding
import com.example.bachishop2.ui.home.HomeFragment
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.ApiException
import com.google.firebase.Firebase
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.GoogleAuthProvider
import com.google.firebase.auth.auth
import java.net.URL


private var GOOGLE_SIGN_IN = 100
class IniciarSesion : AppCompatActivity() {
    private lateinit var  binding: ActivityIniciarSesionBinding
    private lateinit var auth: FirebaseAuth
    private lateinit var sharedPreferences: SharedPreferences
    private lateinit var menu: Menu

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        sharedPreferences = getSharedPreferences("MyPreferences", Context.MODE_PRIVATE)
        // Initialize Firebase Auth
        auth = Firebase.auth


        binding = ActivityIniciarSesionBinding.inflate(layoutInflater)
        val view = binding.root

        setContentView(view)
        binding.btn1.setOnClickListener {
            if(binding.user.text.isEmpty()&&binding.password.text.isEmpty()){
                Toast.makeText(baseContext, "Por favor no deje ningun campo vacio", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            val imail = binding.user.text.toString()
            val pass = binding.password.text.toString()

            iniciarSesion(imail, pass)


        }
        binding.crear.setOnClickListener {
            val intent = Intent(this, crear_cuenta::class.java)
            startActivity(intent)
        }
        binding.returnn.setOnClickListener {
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
        }
        binding.goga.setOnClickListener {
            val googleConf =
                GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                    .requestIdToken(getString(R.string.client_id))
                    .requestEmail()
                    .build()

            val googleClient = GoogleSignIn.getClient(this@IniciarSesion, googleConf)
            googleClient.signOut()
            startActivityForResult(googleClient.signInIntent, GOOGLE_SIGN_IN)

        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == GOOGLE_SIGN_IN) {
            val task = GoogleSignIn.getSignedInAccountFromIntent(data)
            try {
                val account = task.getResult(ApiException::class.java)

                if (account != null) {
                    val credential = GoogleAuthProvider.getCredential(account.idToken, null)
                    auth.signInWithCredential(credential).addOnCompleteListener {
                        if (it.isSuccessful) {
                            val intent = Intent(this, MainActivity::class.java)
                            val email: String = it.result.user?.email as String
                            val img: String = it.result.user?.photoUrl.toString()

                            Toast.makeText(
                                baseContext,
                                "Cuenta iniciada con exito.",
                                Toast.LENGTH_LONG,
                            ).show()
                            val editor = sharedPreferences.edit()
                            editor.putString("correoG", email)
                            editor.apply()
                            saveData(email, img)
                            startActivity(intent)
                        } else {

                            Toast.makeText(
                                baseContext,
                                "Error al iniciar sesion",
                                Toast.LENGTH_LONG,
                            ).show()
                        }
                    }
                }
            } catch (e: ApiException) {
                Toast.makeText(
                    baseContext,
                    "Error Desconocido",
                    Toast.LENGTH_LONG,
                ).show()
            }
        }
    }

    private fun iniciarSesion(email:String, password:String){
        auth.signInWithEmailAndPassword(email, password)
            .addOnCompleteListener(this) { task ->
                if (task.isSuccessful) {
                    // Sign in success, update UI with the signed-in user's information
                    Log.d("compilo", "Cuenta iniciada")
                    Toast.makeText(
                        baseContext,
                        "Cuenta iniciada con exito.",
                        Toast.LENGTH_LONG,
                    ).show()
                    val user = auth.currentUser?.email as String
                    saveData(user, "")
                    val intent = Intent(this, MainActivity::class.java)
                    startActivity(intent)
                    //updateUI(user)
                } else {
                    // If sign in fails, display a message to the user.
                    Log.w("NoCompilo", "Algo salio mal", task.exception)
                    Toast.makeText(
                        baseContext,
                        "Error al iniciar sesion, compruebe sus credenciales.",
                        Toast.LENGTH_SHORT,
                    ).show()
                    val intent = Intent(this, IniciarSesion::class.java)
                    startActivity(intent)
                    //updateUI(null)
                }
            }
    }
    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.herramienta_de_barras, menu)
        this.menu = menu
        return true
    }
    private fun saveData(email: String, photoUrl: String){

        val editor = sharedPreferences.edit()

        if(photoUrl!=""){
            editor.putString("foto", photoUrl)
        }

        editor.putString("correo", email)
        editor.apply()

    }
}