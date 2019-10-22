package app.niks.base.recyclerview.click

sealed class ClickEvent(var itemPosition: Int, var itemId: Long) {
    data class ITEM_CLICK(val position: Int, val id: Long) : ClickEvent(position, id)
    data class ITEM_LONG_CLICK(val position: Int, val id: Long) : ClickEvent(position, id)
}
