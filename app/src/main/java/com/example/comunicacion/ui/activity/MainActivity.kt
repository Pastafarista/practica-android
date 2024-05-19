package com.example.comunicacion.ui.activity

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.AdapterView
import android.widget.AdapterView.OnItemSelectedListener
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.comunicacion.ProductoActivity

import com.example.comunicacion.adapters.ProductosAdapter
import com.example.comunicacion.databinding.ActivityMainBinding
import com.example.comunicacion.model.Producto
import com.example.comunicacion.model.Usuario
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import com.google.gson.Gson

class MainActivity : AppCompatActivity() {

    // Variables
    private lateinit var database: FirebaseDatabase
    private lateinit var binding: ActivityMainBinding
    private lateinit var adaptadoProducto: ProductosAdapter
    private lateinit var adaptadorCategorias: ArrayAdapter<String>
    private lateinit var correo: String
    private var perfil: Char? = null

    override fun onCreate(savedInstanceState: Bundle?){
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Instanciar los adaptadores y las listas
       instancias()

        // Personalizar el spinner y el recycler
        personalizarSpinner()
        personalizarRecycler()

        // Rellena el adaptador de productos
        getProductos()

        // Saludar al usuario con el correo y su nombre de perfil
        val id = intent.getStringExtra("userId")

        // Obtener el correo y el perfil del usuario de la base de datos
        database.getReference("users").child(id!!).get().addOnSuccessListener {
            val user = it.getValue(Usuario::class.java)!!

            // Mostramos el texto de saludo y la incial del perfil
            binding.textoSaludo.text = "Hola ${user.nombre}!"
            binding.textoPerfil.text = user.nombre[0].toString().toUpperCase()
        }.addOnFailureListener {
            Log.e("ERROR", it.message.toString())
        }

        // Listener del spinner para filtrar los productos por categoría
        binding.spinnerMarca.onItemSelectedListener = object : OnItemSelectedListener {
            override fun onItemSelected(
                parent: AdapterView<*>?,
                view: View?,
                position: Int,
                id: Long
            ) {
                val seleccion = parent!!.adapter.getItem(position) as String

                // Filtrar los productos por categoría
                adaptadoProducto.filtrarPorCategoria(seleccion)
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {
                Toast.makeText(applicationContext, "No se ha seleccionado nada", Toast.LENGTH_SHORT).show()
            }
        }
    }

    // Obtener los productos de la base de datos
    fun getProductos() {
        var categorias_repetidas: ArrayList<String> = ArrayList<String>()

        // Obtenemos los objetos de la base de datos
        database.getReference("products").addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                for (data in snapshot.children) {
                    try{
                        // Convertimos el objeto snapshot de firebase a un objeto de tipo Producto
                        val producto = data.getValue(Producto::class.java)!!

                        // Rellenamos el adaptador de productos
                        adaptadoProducto.addProducto(producto)

                        // Rellenamos el adaptador de categorías
                        if (!categorias_repetidas.contains(producto.category)){
                            categorias_repetidas.add(producto.category)
                            adaptadorCategorias.add(producto.category)
                        }

                    } catch (e: Exception){
                        e.message?.let { Log.e("ERROR", it) }
                    }
                }
            }

            override fun onCancelled(error: DatabaseError) {
                Log.e("ERROR", error.message)
            }
        })
    }

    // Instanciar el adaptador de productos y el adaptador de marcas
    fun instancias(){
        database = FirebaseDatabase.getInstance("https://practica-android-9f602-default-rtdb.europe-west1.firebasedatabase.app/")

        adaptadoProducto = ProductosAdapter(this)

        val categorias: ArrayList<String> = ArrayList<String>()

        // Crear el adaptador de las categorías
        adaptadorCategorias = ArrayAdapter(
            applicationContext,
            android.R.layout.simple_spinner_item,
            categorias
        )
    }

    fun personalizarRecycler(){
        adaptadoProducto.setOnItemClickListener(object : ProductosAdapter.OnItemClickListener {
            override fun onItemClick(position: Int) {

                val intent = Intent(this@MainActivity, ProductoActivity::class.java)
                intent.putExtra("titulo", adaptadoProducto.listaFiltrada[position].title)
                intent.putExtra("descripcion", adaptadoProducto.listaFiltrada[position].description)
                intent.putExtra("precio", adaptadoProducto.listaFiltrada[position].price.toString())
                intent.putExtra("imagen", adaptadoProducto.listaFiltrada[position].image)
                intent.putExtra("categoria", adaptadoProducto.listaFiltrada[position].category)

                startActivity(intent)
            }
        })

        binding.recyclerModelos.adapter = adaptadoProducto

        binding.recyclerModelos.layoutManager =
            LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)
    }

    fun personalizarSpinner(){
        adaptadorCategorias.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        binding.spinnerMarca.adapter = adaptadorCategorias
    }
}