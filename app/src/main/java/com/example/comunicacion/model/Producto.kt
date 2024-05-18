package com.example.comunicacion.model
import java.io.Serializable

class Producto(
    var id: Int,
    var title: String,
    var description: String,
    var price: Double,
    var category: String,
    var image: String
) {
    constructor() : this(0, "", "", 0.0, "", "")
}