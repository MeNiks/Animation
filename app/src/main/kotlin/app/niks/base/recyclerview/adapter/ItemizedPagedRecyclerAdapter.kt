package app.niks.base.recyclerview.adapter

import android.view.ViewGroup
import androidx.paging.PagedListAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import app.niks.base.recyclerview.row.RecyclerRowItem

open class ItemizedPagedRecyclerAdapter<ID, T : RecyclerRowItem<ID>>
constructor(
    protected var presenterManager: AdapterRowPresenterManager<ID, T>
) : PagedListAdapter<T, RecyclerView.ViewHolder>(object : DiffUtil.ItemCallback<T>() {
    override fun areItemsTheSame(oldItem: T, newItem: T): Boolean {
        return oldItem.getItemId() == newItem.getItemId()
    }

    override fun areContentsTheSame(oldItem: T, newItem: T): Boolean {
        return oldItem == newItem
    }
}) {

    private var maxVisibleCount: Int = -1

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return presenterManager.onCreateViewHolder(parent, viewType)
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val item = getItem(position)
        presenterManager.onBindViewHolder(item!!, position, holder, null)
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int, payloads: MutableList<Any>) {
        super.onBindViewHolder(holder, position, payloads)
        val item = getItem(position)
        presenterManager.onBindViewHolder(item!!, position, holder, payloads)
    }

    override fun getItemViewType(position: Int): Int {
        val item = getItem(position)
        return presenterManager.getItemViewType(item!!, position)
    }

    override fun onViewRecycled(holder: RecyclerView.ViewHolder) {
        presenterManager.onViewRecycled(holder)
    }

    override fun onFailedToRecycleView(holder: RecyclerView.ViewHolder): Boolean {
        return presenterManager.onFailedToRecycleView(holder)
    }

    override fun onViewAttachedToWindow(holder: RecyclerView.ViewHolder) {
        presenterManager.onViewAttachedToWindow(holder)
    }

    override fun onViewDetachedFromWindow(holder: RecyclerView.ViewHolder) {
        presenterManager.onViewDetachedFromWindow(holder)
    }

    public override fun getItem(position: Int): T? {
        return super.getItem(position)
    }

    public fun getItem(id: Long): T? {
        return currentList?.find { t: T -> t.getItemId() == id }
    }

    override fun getItemCount(): Int {
        if (maxVisibleCount <= 0) {
            return super.getItemCount()
        }
        if (maxVisibleCount < super.getItemCount()) {
            return maxVisibleCount
        }
        return super.getItemCount()
    }

    fun setMaxVisibleItemCount(maxVisibleItemCount: Int) {
        maxVisibleCount = maxVisibleItemCount
    }

    fun getItemById(id: ID): T? {
        return currentList?.find { it.getItemId() == id }
    }
}
