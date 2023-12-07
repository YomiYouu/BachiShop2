package com.example.bachishop2.ui.home

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.core.view.get
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.example.bachishop2.R
import com.example.bachishop2.databinding.FragmentHomeBinding
import com.example.bachishop2.producto
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import java.io.File

class HomeFragment : Fragment() {

    private var _binding: FragmentHomeBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!
    private var lista1: MutableList<Item> = mutableListOf()
    private var lista2: MutableList<Item> = mutableListOf()
    private var lista3: MutableList<Item> = mutableListOf()
    private var lista4: MutableList<Item> = mutableListOf()
    private var lista5: MutableList<Item> = mutableListOf()
    private lateinit var sharedPreferences: SharedPreferences

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val homeViewModel = ViewModelProvider(this).get(HomeViewModel::class.java)
        sharedPreferences = requireActivity().getSharedPreferences("MyPreferences", Context.MODE_PRIVATE);
        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        val root: View = binding.root

        val textView: TextView = binding.textHome
        homeViewModel.text.observe(viewLifecycleOwner) {
            textView.text = it
        }
        val listImg1 = binding.listado1
        val listImg2 = binding.listado2
        val listImg3 = binding.listado3
        val listImg4 = binding.listado4
        val listImg5 = binding.listado5
        //Nombre en firebase/Lista de items / Layouts
       LoadDataFromFirebase("marinos",lista1,listImg1)
       LoadDataFromFirebase("terrestres",lista2,listImg2)
       LoadDataFromFirebase("aves",lista3,listImg3)
       LoadDataFromFirebase("reptiles",lista4,listImg4)
       LoadDataFromFirebase("fantasticos",lista5,listImg5)


        return root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
    private fun saveData(item: Item) {
        val editor = sharedPreferences.edit()
        editor.putString("nombre", item.name)
        editor.putFloat("precio", item.price)
        editor.putString("img", item.image)
        editor.putString("desc", item.des)
        editor.apply()
    }

    private fun LoadDataFromFirebase(collectionName:String,list: MutableList<Item>, listaImagenes: LinearLayout){
        val db = Firebase.firestore
        db.collection(collectionName)
            .get()
            .addOnSuccessListener { result ->
                var i : Int = 0
                for (document in result) {
                    Log.d("TAG", "${document.id} => ${document.data}")
                    val item = Item(document.get("nombre") as String, (document.get("precio") as Double).toFloat(), document.get("descripcion") as String, document.get("img") as String)
                    list.add(item)
                    listaImagenes[i].setOnClickListener{
                        val intent = Intent(this.context, producto::class.java)
                        saveData(item)
                        startActivity(intent)
                    }
                    i++

                }
            }
            .addOnFailureListener { exception ->
                Log.w("TAG", "Error getting documents.", exception)
            }

}
class Item constructor(nombre: String, precio: Float, descripcion: String, img: String){
        val name: String = nombre
        val price: Float = precio
        val des: String = descripcion
        val image: String = img

}}