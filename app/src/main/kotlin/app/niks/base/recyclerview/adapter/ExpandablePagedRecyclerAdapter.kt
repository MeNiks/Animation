package app.niks.base.recyclerview.adapter

import androidx.paging.PagedList
import app.niks.base.extension.toPagedList
import app.niks.base.recyclerview.row.Expandable
import app.niks.base.recyclerview.row.RecyclerRowItem

class ExpandablePagedRecyclerAdapter<ID, T : RecyclerRowItem<ID>>
constructor(
    presenterManager: AdapterRowPresenterManager<ID, RecyclerRowItem<ID>>
) : ItemizedPagedRecyclerAdapter<ID, RecyclerRowItem<ID>>(presenterManager) {

    lateinit var expandableList: ArrayList<Expandable<ID>>

    fun submitExpandableList(pagedList: PagedList<Expandable<ID>>) {
        this.expandableList = ArrayList(pagedList.toList())
        super.submitList(getItems(pagedList))
    }

    private fun getItems(pagedList: PagedList<Expandable<ID>>): PagedList<RecyclerRowItem<ID>> {
        return pagedList.toList().flatMap { expandable ->
            if (expandable.isExpanded)
                arrayListOf(arrayListOf(expandable), expandable.items)
            else
                arrayListOf(arrayListOf(expandable))
        }.flatten().toPagedList()
    }
}
