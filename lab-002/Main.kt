package `lab-002`

import kotlin.collections.forEachIndexed


enum class ElementType {
    CADENA,
    ENTERO,
    BOOLEANO,
    DESCONOCIDO
}


data class ItemData(val originalPos: Int, val originalValue: Any?, val type: ElementType, val info: String) {
    override fun toString(): String {
        val typeStr = when (this.type) {
            ElementType.CADENA -> "cadena"
            ElementType.ENTERO -> "entero"
            ElementType.BOOLEANO -> "booleano"
            ElementType.DESCONOCIDO -> "desconocido"
        }

        return "'${this.originalValue}' estaba en la posición ${this.originalPos}, es de tipo ${typeStr} e info es ${this.info}"
    }
}


fun processList(input: List<Any?>?): MutableList<ItemData>? {
    if (input == null) return null;

    val positions = HashMap<Any, ArrayDeque<Int>>().apply {
        input.forEachIndexed { i, it ->
            if (it == null) return@forEachIndexed;
            val queue = this[it] ?: ArrayDeque<Int>()
            queue.add(i)
            this[it] = queue
        }
    }
    val nonNullishValues = input.filterNotNull()

    if (nonNullishValues.isEmpty()) return mutableListOf();

    // delete this
    return nonNullishValues.map { it ->
        when (it) {
            is String -> ItemData(positions[it]?.removeFirstOrNull() ?: -1, it, ElementType.CADENA, processString(it))
            is Int -> ItemData(positions[it]?.removeFirstOrNull() ?: -1, it, ElementType.ENTERO, processInt(it))
            is Boolean -> ItemData(positions[it]?.removeFirstOrNull() ?: -1, it, ElementType.BOOLEANO, processBoolean(it))
            else -> ItemData(positions[it]?.removeFirstOrNull() ?: -1, it, ElementType.DESCONOCIDO, "desconocido")
        }
    }.toMutableList()
}

fun processInt(num: Int): String {
    if (num % 10 == 0) return "M10"
    if (num % 5 == 0) return "M5"
    if (num % 2 == 0) return "M2"
    return "-"
}

fun processString(str: String): String {
    return "L${str.length}"
}

fun processBoolean(bool: Boolean): String {
    return if (bool) "verdadero" else "falso"
}
