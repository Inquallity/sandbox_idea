internal enum class Type(private val id: Int) {
    F(255), S(120), T(450), FO(123);

    companion object {
        fun fromList(list: List<Obj>): Type? {
            val idList = values().map { it.id }
            val item = list.find { idList.contains(it.type) }
            return values().firstOrNull { it.id == item?.type }
        }
    }
}
