package app.niks.base.recyclerview.selector

import app.niks.base.recyclerview.adapter.ItemizedPagedRecyclerAdapter
import app.niks.base.recyclerview.row.RecyclerRowItem
import app.niks.base.recyclerview.row.SelectableRecyclerRowItem

class SingleItemSelector<ID, T : RecyclerRowItem<ID>>
constructor(
    adapter: ItemizedPagedRecyclerAdapter<ID, T>
) : ItemSelector<ID, T>(adapter) {

    override fun toggleSelection(position: Int, item: T) {

        if (selectedIds.contains(item.getItemId())) {
            removeSelection()
        } else {
            removeSelection()
            addSelection(position, item)
        }
    }

    private fun removeSelection() {
        if (idVsPositionMap.size <= 0) {

            return
        }
        val selectedItem = selectedItemList[0]
        val previousSelectedPosition = idVsPositionMap[selectedItem.getItemId()]
        if (selectedItem is SelectableRecyclerRowItem<*>) {
            previousSelectedPosition?.apply {
                selectedItem.isSelected = false
                selectedItemList.clear()
                selectedIds.clear()
                adapter.notifyItemChanged(previousSelectedPosition)
            } ?: throw IllegalStateException("Cannot mark this item as un-selected.")
        }
    }

    private fun addSelection(position: Int, selectedItem: T) {
        addSelectedItem(position, selectedItem)
        adapter.notifyItemChanged(position)
    }
}
