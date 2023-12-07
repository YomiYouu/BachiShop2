package com.example.bachishop2.ui.dashboard

import android.app.AlertDialog
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.util.Log
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.get
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.example.bachishop2.IniciarSesion
import com.example.bachishop2.MainActivity
import com.example.bachishop2.databinding.FragmentDashboardBinding
import com.example.bachishop2.producto
import com.example.bachishop2.ui.home.HomeFragment
import com.google.firebase.auth.FirebaseAuth
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

class DashboardFragment : Fragment() {
    private lateinit var sharedPreferences: SharedPreferences
    private var _binding: FragmentDashboardBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        sharedPreferences = requireActivity().getSharedPreferences("MyPreferences", Context.MODE_PRIVATE);
        val dashboardViewModel =
            ViewModelProvider(this).get(DashboardViewModel::class.java)

        _binding = FragmentDashboardBinding.inflate(inflater, container, false)
        val root: View = binding.root
        val mutableList: MutableList<String> = getList("carrito")
        /*val textView: TextView = binding.textDashboard
        dashboardViewModel.text.observe(viewLifecycleOwner) {
            textView.text = it
        }*/
        for (nombre in mutableList) {
            val textView = TextView(requireContext())
            textView.text = nombre
            textView.textSize = 18f
            textView.gravity = Gravity.CENTER
            val layoutParams = LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.WRAP_CONTENT
            )
            layoutParams.setMargins(0, 0, 0, 24) // Ajusta los márgenes según tus necesidades
            textView.layoutParams = layoutParams
            binding.layoutCesta.addView(textView)



        }
        val editor = sharedPreferences.edit()
        val precio = sharedPreferences.getFloat("cuenta", 0f)
        binding.presio.text = "Total: " + precio.toString() + "€"

        binding.compal.setOnClickListener {
            if (!sharedPreferences.contains("correo")){

                AlertDialog.Builder(this.context)
                    .setTitle("ATENCION")
                    .setMessage("Debes iniciar sesion para poder comprar")
                    .setCancelable(false)
                    .setPositiveButton(android.R.string.ok, {
                            dialog, which ->
                        run {
                            val intent = Intent(this.context, IniciarSesion::class.java)
                            startActivity(intent)
                        }
                    }).show()
            }else {
                editor.putFloat("cuenta", 0f)
                editor.putString("carrito", null)
                editor.apply()
                val intent = Intent(this.context, MainActivity::class.java)

                startActivity(intent)
            }
        }

        return root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
    private val gson = Gson()
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