package com.example.comunicacion.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.comunicacion.R
import com.example.comunicacion.model.Producto

class ProductosAdapter(var context: Context) :
    RecyclerView.Adapter<ProductosAdapter.MyHolder>() {

        private lateinit var listener: OnItemClickListener

        interface OnItemClickListener {
            fun onItemClick(position: Int)
        }

    fun setOnItemClickListener(listener: OnItemClickListener) {
        this.listener = listener
    }

    var listaDatos: ArrayList<Producto> = ArrayList() // Lista de productos
    var listaFiltrada: ArrayList<Producto> = ArrayList() // Lista de productos filtrados

    class MyHolder(itemView: View, listener: OnItemClickListener) : RecyclerView.ViewHolder(itemView) {
        var imagen: ImageView = itemView.findViewById(R.id.imagenFila)
        var titulo: TextView = itemView.findViewById(R.id.tituloFila)
        var subTitulo: TextView = itemView.findViewById(R.id.subtituloFila)

        init {
            itemView.setOnClickListener {
                listener.onItemClick(adapterPosition)
            }
        }

    }

    // crear la plantilla de cada fila
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyHolder {
        val view: View = LayoutInflater.from(context)
            .inflate(R.layout.item_recycler, parent, false)
        return MyHolder(view, listener)
    }

    // numero de elementos - filas que se tienen que pintar
    override fun getItemCount(): Int {
        return listaFiltrada.size
    }

    // asociar datos con plantilla
    override fun onBindViewHolder(holder: MyHolder, position: Int) {
        val producto = listaFiltrada[position]

        holder.titulo.setText(producto.title)
        holder.subTitulo.setText(producto.price.toString())

        Glide.with(context).load(producto.image)
            .placeholder(R.drawable.loading)
            .into(holder.imagen)
    }

    fun addProducto(producto: Producto) {

        // Añadir el producto a las dos listas
        listaDatos.add(producto)
        listaFiltrada.add(producto)

        notifyItemInserted(listaDatos.size - 1)
    }

    // Función que filtra los productos por categoría
    fun filtrarPorCategoria(categoria: String) {

        listaFiltrada.clear()

        for (producto in listaDatos) {
            if (producto.category == categoria) {
                listaFiltrada.add(producto)
            }
        }

        notifyDataSetChanged()
    }
}