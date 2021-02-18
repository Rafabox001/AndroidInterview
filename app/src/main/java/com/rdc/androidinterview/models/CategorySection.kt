package com.rdc.androidinterview.models

data class CategorySection(
    val uuid: String,
    val name: String,
    var itemsVisible: Boolean = true,
    val menuItems: List<MenuItem>
) {
    override fun equals(other: Any?): Boolean {
        if (javaClass != other?.javaClass) return false

        other as CategorySection

        if (uuid != other.uuid) return false
        if (name != other.name) return false
        if (itemsVisible != other.itemsVisible) return false
        if (menuItems != other.menuItems) return false

        return true
    }

    override fun toString(): String {
        return "Category(uuid=$uuid, " +
                "name='$name', " +
                "itemsVisible='$itemsVisible', " +
                "menuItems='$menuItems')"
    }
}