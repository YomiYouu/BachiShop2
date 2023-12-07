package com.example.bachishop2

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.graphics.BitmapFactory
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.bachishop2.databinding.ActivityCuentaBinding
import java.net.URL


class Cuenta : AppCompatActivity() {
    private lateinit var sharedPreferences: SharedPreferences
    private lateinit var binding: ActivityCuentaBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_cuenta)

        binding = ActivityCuentaBinding.inflate(layoutInflater)
        setContentView(binding.root)

        sharedPreferences = getSharedPreferences("MyPreferences", MODE_PRIVATE)
       /* val url = URL(sharedPreferences.getString("foto", ""))
        val bmp = BitmapFactory.decodeStream(url.openConnection().getInputStream())
        binding.perfil.setImageBitmap(bmp)*/

        binding.cuentaa.text = "Email: " + sharedPreferences.getString("correo", "")

        binding.closeSesion.setOnClickListener {
            val intent = Intent(this, Cuenta::class.java)
        triggerRebirth(applicationContext, intent)
        }
        binding.volver2.setOnClickListener {
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
        }
    }

    fun triggerRebirth(context: Context, nextIntent: Intent) {
        sharedPreferences.edit().clear().commit()


        val KEY_RESTART_INTENT = "restartIntent"

        val intent = Intent(context, MainActivity::class.java)
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        intent.putExtra(KEY_RESTART_INTENT, nextIntent)
        context.startActivity(intent)

        if (context is Activity) {
            context.finish()
        }

        Runtime.getRuntime().exit(0)
    }
}



