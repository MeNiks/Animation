package app.niks.base.recyclerview.selector

import app.niks.base.recyclerview.adapter.ItemizedPagedRecyclerAdapter
import app.niks.base.recyclerview.row.RecyclerRowItem
import app.niks.base.recyclerview.row.SelectableRecyclerRowItem

class MultiItemSelector<ID, T : RecyclerRowItem<ID>>
constructor(
    adapter: ItemizedPagedRecyclerAdapter<ID, T>
) : ItemSelector<ID, T>(adapter) {

    override fun toggleSelection(position: Int, item: T) {
        if (selectedIds.contains(item.getItemId())) {
            removeSelection(position, item)
        } else {
            addSelection(position, item)
        }
    }

    private fun removeSelection(position: Int, item: T) {
        if (selectedIds.size <= 0) {
            throw IllegalStateException("Can not mark this item as un-selected.")
        }

        val selectedItem = selectedItemList.find { t -> t.getItemId() == item.getItemId() }

        if (selectedItem == null || item.getItemId() != selectedItem.getItemId())
            throw IllegalStateException("Cannot mark the item as unselected")

        if (selectedItem is SelectableRecyclerRowItem<*>) {
            selectedItem.let {
                selectedItem.isSelected = false
                selectedItemList.remove(selectedItem)
                idVsPositionMap.remove(selectedItem.getItemId())
                selectedIds.remove(selectedItem.getItemId())
                adapter.notifyDataSetChanged()
            }
        }
    }

    private fun addSelection(position: Int, selectedItem: T) {
        addSelection(position, selectedItem)
        adapter.notifyDataSetChanged()
    }
}
