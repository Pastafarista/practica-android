package com.example.comunicacion.ui.activity

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.ContactsContract.Data
import android.util.Log
import android.view.View
import android.view.View.OnClickListener
import android.widget.AdapterView
import android.widget.AdapterView.OnItemSelectedListener
import android.widget.ArrayAdapter
import androidx.recyclerview.widget.LinearLayoutManager
import com.android.volley.toolbox.JsonArrayRequest
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.Volley

import com.example.comunicacion.R
import com.example.comunicacion.adapters.ModeloAdapter
import com.example.comunicacion.adapters.ProductosAdapter
import com.example.comunicacion.data.DataSet
import com.example.comunicacion.databinding.ActivityMainBinding
import com.example.comunicacion.model.Marca
import com.example.comunicacion.model.Modelo
import com.example.comunicacion.model.Producto
import com.google.android.material.snackbar.Snackbar
import com.google.gson.Gson
import org.json.JSONArray

class MainActivity : AppCompatActivity() {

    // Variables
    private lateinit var binding: ActivityMainBinding
    private lateinit var adaptadoProducto: ProductosAdapter
    private lateinit var adaptadorMarcas: ArrayAdapter<String>
    private lateinit var correo: String
    private var perfil: Char? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Instanciar los adaptadores y las listas
        instancias()

        // Personalizar el spinner y el recycler
        personalizarSpinner()
        personalizarRecycler()

        // Realizar la petición JSON
        realizarPeticionJSON()



        // Saludar al usuario con el correo y su nombre de perfil
        correo = intent.getStringExtra("correo")!!
        perfil = intent.getStringExtra("perfil")!!.get(0)
        binding.textoSaludo.setText("Bienvenido $correo")
        binding.textoPerfil.setText("$perfil")

        binding.spinnerMarca.onItemSelectedListener = object : OnItemSelectedListener {
            override fun onItemSelected(
                parent: AdapterView<*>?,
                view: View?,
                position: Int,
                id: Long
            ) {
                val seleccion = parent!!.adapter.getItem(position) as String

                Snackbar.make(binding.root, "Has seleccionado $seleccion", Snackbar.LENGTH_LONG).show()

            }

            override fun onNothingSelected(parent: AdapterView<*>?) {
                Snackbar.make(binding.root, "No has seleccionado nada", Snackbar.LENGTH_LONG).show()
            }
        }
    }

    fun realizarPeticionJSON() {

        // URL de la API
        val url = "https://fakestoreapi.com/products"

        var categorias_repetidas: ArrayList<String> = ArrayList<String>()

        // crear la peticion
        val peticion: JsonArrayRequest = JsonArrayRequest(url,{
            val productos_json: JSONArray = it

            for (i in 0..productos_json.length() - 1){
                val productoJSON = productos_json.getJSONObject(i)
                var producto = Gson().fromJson(productoJSON.toString(), Producto::class.java)

                // Añadir el producto al adaptador
                adaptadoProducto.addProducto(producto)

                // Añadir la categoría al spinner
                if (!categorias_repetidas.contains(producto.category)){
                    categorias_repetidas.add(producto.category)
                    adaptadorMarcas.add(producto.category)
                }

            }
        },{
            Log.wtf("ERROR", it.localizedMessage)
        })

        // lanzar la peticion
        Volley.newRequestQueue(applicationContext).add(peticion)
    }

    // Instanciar el adaptador de productos y el adaptador de marcas
    fun instancias(){
        adaptadoProducto = ProductosAdapter(this)

        val categorias: ArrayList<String> = ArrayList<String>()

        // Crear el adaptador de las categorías
        adaptadorMarcas = ArrayAdapter(
            applicationContext,
            android.R.layout.simple_spinner_item,
            categorias
        )
    }

    fun personalizarRecycler(){
        binding.recyclerModelos.adapter = adaptadoProducto
        binding.recyclerModelos.layoutManager =
            LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)
    }

    fun personalizarSpinner(){
        adaptadorMarcas.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        binding.spinnerMarca.adapter = adaptadorMarcas
    }
}