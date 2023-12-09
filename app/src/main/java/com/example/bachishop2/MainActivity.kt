package com.example.bachishop2

import android.app.AlertDialog
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.widget.Button
import com.google.android.material.bottomnavigation.BottomNavigationView
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import com.example.bachishop2.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private lateinit var sharedPreferences: SharedPreferences
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val navView: BottomNavigationView = binding.navView

        val navController = findNavController(R.id.nav_host_fragment_activity_main)
        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        val appBarConfiguration = AppBarConfiguration(
            setOf(
                R.id.navigation_home, R.id.navigation_dashboard, R.id.navigation_notifications
            )
            
        )
        setupActionBarWithNavController(navController, appBarConfiguration)
        navView.setupWithNavController(navController)
    }
    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.herramienta_de_barras, menu)
        sharedPreferences = getSharedPreferences("MyPreferences", Context.MODE_PRIVATE)
        if(sharedPreferences.contains("correo")){
            menu?.getItem(1)?.isVisible = false
            menu?.getItem(0)?.isVisible = true
        }



        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.itemCrear -> {
                val intent = Intent(this, IniciarSesion::class.java)
                startActivity(intent)

            }
            R.id.itemCuenta -> {
                val intent = Intent(this, Cuenta::class.java)
                startActivity(intent)
            }
            R.id.itemSensol -> {
                val intent = Intent(this, Sensor::class.java)
                startActivity(intent)
            }
            R.id.itemAñadir -> {
                if (!sharedPreferences.contains("correo")){

                    AlertDialog.Builder(this)
                        .setTitle("ATENCION")
                        .setMessage("Debes iniciar sesion para poder añadir un producto nuevo")
                        .setCancelable(false)
                        .setPositiveButton(android.R.string.ok, {
                                dialog, which ->
                            run {
                                val intent = Intent(this, IniciarSesion::class.java)
                                startActivity(intent)
                            }
                        }).show()
                }else {
                    val intent = Intent(this, addProduct::class.java)
                    startActivity(intent)
                }
            }
        }
        return super.onOptionsItemSelected(item)
    }
}
