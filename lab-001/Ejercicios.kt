package src

fun transformList(numeros: List<Int>, operacion: (el : Int) -> Int): List<Int> {
    val array = ArrayList<Int>()

    for (numero in numeros) {
        array.add(operacion(numero))
    }

    return array;
}

fun main() {
    val nums = listOf(1, 2, 3, 4)
    println(transformList(nums) { int -> int * int })
    println(transformList(nums) { int -> int + 10 })
    println(transformList(nums) { int -> int * -1 })
}