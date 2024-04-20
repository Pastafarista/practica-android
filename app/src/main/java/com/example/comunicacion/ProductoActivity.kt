package com.example.comunicacion

import android.os.Bundle
import android.widget.ImageView
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.bumptech.glide.Glide

class ProductoActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_producto)

        val tituloProducto: TextView = findViewById(R.id.tituloProducto)
        val descripcionProducto: TextView = findViewById(R.id.descripcionProducto)
        val precioProducto: TextView = findViewById(R.id.precioProducto)
        val imagenProducto: ImageView = findViewById(R.id.imagenProducto)
        val categoriaProducto: TextView = findViewById(R.id.categoriaProducto)

        val bundle : Bundle? = intent.extras

        val titulo = bundle?.getString("titulo")
        val categoria = bundle?.getString("categoria")
        val precio = bundle?.getString("precio")
        val descripcion = bundle?.getString("descripcion")
        val imagen = bundle?.getString("imagen")

        tituloProducto.text = titulo
        categoriaProducto.text = "Categoria: $categoria"
        precioProducto.text = "Precio: $precio"
        descripcionProducto.text = descripcion
        Glide.with(this).load(imagen).into(imagenProducto)
    }
}