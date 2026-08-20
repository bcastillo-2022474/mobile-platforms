package `lab-001`

fun main() {
    val products = arrayListOf(
        Product("Celular"),
        Product("Tablet"),
        Product("Computadora", 20),
        Product("Impresora", 15),
        Product("Monitor", 5)
    )
    do {
        println("Presione 1 para ver todos los productos")
        println("Presione 2 para buscar un producto")
        println("Presione 3 para actualizar el stock de un producto")
        println("Presione 4 para ver los productos disponibles")
        println("Presione 5 para salir del programa")

        val option = readln().toIntOrNull()
        if (option == null) {
            println("Opcion invalida, por favor trata de nuevo")
            continue
        }

        when (option) {
            1 -> {
                showAllProducts(products)
                println()
            }

            2 -> {
                println("Ingresa el nombre del producto que quieras ver")
                val name = readln()

                val product = getProductByName(name, products)
                if (product == null) {
                    println("Producto no encontrado")
                    continue;
                }
                println("Producto con nombre ${product.name} y stock ${product.stock} disponible\n")
            }
            3 -> {
                println("Ingresa el nombre del producto que quieras actualizar su stock")
                val nameInput = readln()
                var stockInput: Int? = null
                while (true) {
                    println("Ingresa la cantidad nueva de stock")
                    stockInput = readln().toIntOrNull()
                    if (stockInput != null) {
                        break
                    }
                }
                updateProduct(name = nameInput, stock = stockInput, products = products)
                println()
            }
            4 -> {
                showStats(products = products)
                println()
            }
            5 -> {
                println("Saliendo del programa\n")
            }
        }
    } while (option != 5)

}

fun showAllProducts(products: List<Product>) {
    products.forEach { product ->
        println("${product.name} - ${product.stock}")
    }
}

fun getProductByName(name: String, products: List<Product>): Product? {
    return products.find { product -> product.name == name }
}

fun updateProduct(name: String, stock: Int, products: List<Product>): Product? {
    val product = getProductByName(name, products)
    if (product == null) {
        println("Producto no encontrado")
        return null
    }
    return product.apply { this.stock = stock }
}

fun showStats(products: List<Product>) {
    val activeProducts = products.filter { p -> p.isDisponible() }
    println("tenemos ${activeProducts.size} productos disponibles")
    activeProducts.forEach { p -> println("${p.name} - ${p.stock}") }
}


data class Product(val name: String, var stock: Int = 10) {
    // getters
    fun isDisponible(): Boolean {
        return stock > 0
    }
}