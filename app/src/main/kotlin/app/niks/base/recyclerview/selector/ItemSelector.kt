package app.niks.base.recyclerview.selector

import app.niks.base.recyclerview.adapter.ItemizedPagedRecyclerAdapter
import app.niks.base.recyclerview.click.ClickEvent
import app.niks.base.recyclerview.row.RecyclerRowItem
import app.niks.base.recyclerview.row.SelectableRecyclerRowItem

abstract class ItemSelector<ID, T : RecyclerRowItem<ID>>
constructor(
    val adapter: ItemizedPagedRecyclerAdapter<ID, T>
) {

    private var isEnabled: Boolean = false

    val selectedItemList = arrayListOf<T>()

    protected var selectedIds = arrayListOf<ID>()

    protected val idVsPositionMap = hashMapOf<ID, Int>()

    abstract fun toggleSelection(position: Int, item: T)

    fun handleSelection(clickEvent: ClickEvent): Boolean {
        if (!isEnabled) {
            return false
        }
        if (clickEvent is ClickEvent.ITEM_LONG_CLICK) {
            return true
        }
        val item = adapter.getItem(clickEvent.itemPosition)
        item?.let { rowItem ->
            toggleSelection(clickEvent.itemPosition, rowItem)
        }
        return true
    }

    fun enable() {
        isEnabled
    }

    fun disable() {
        isEnabled = false
        selectedItemList.clear()
        idVsPositionMap.clear()
        selectedIds.clear()
    }

    fun isItemSelected(id: ID): Boolean {
        return selectedIds.contains(id)
    }

    fun isItemSelected(position: Int): Boolean {
        return idVsPositionMap.values.find { pos -> pos == position } != null
    }

    fun addSelectedIds(ids: List<ID>) {
        selectedIds.addAll(ids)
    }

    fun addSelectedItems(items: List<T>) {
        selectedItemList.addAll(items)
        for (item in items) {
            selectedIds.add(item.getItemId())
        }
    }

    fun addSelectedItem(position: Int, item: T) {

        item as SelectableRecyclerRowItem<ID>

        item.isSelected = true

        if (!selectedIds.contains(item.getItemId())) {
            selectedItemList.add(item)
            selectedIds.add(item.getItemId())
        }
        idVsPositionMap[item.getItemId()] = position
    }
}
