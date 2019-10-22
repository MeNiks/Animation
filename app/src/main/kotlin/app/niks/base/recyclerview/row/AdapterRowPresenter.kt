package app.niks.base.recyclerview.row

import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import app.niks.base.recyclerview.binding.BindingViewHolder

abstract class AdapterRowPresenter<ID, in T : RecyclerRowItem<ID>> {

    abstract fun isForViewType(item: T, position: Int): Boolean

    abstract fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder

    open fun onBindViewHolder(item: T, position: Int, holder: RecyclerView.ViewHolder, vararg payload: Any?) {
        val viewHolder: BindingViewHolder = holder as BindingViewHolder
        viewHolder.bind(item)
    }

    open fun toggleItemSelection(item: T, position: Int, holder: RecyclerView.ViewHolder) {
    }

    fun onViewRecycled(viewHolder: RecyclerView.ViewHolder) {}

    fun onFailedToRecycleView(holder: RecyclerView.ViewHolder): Boolean {
        return false
    }

    fun onViewAttachedToWindow(holder: RecyclerView.ViewHolder) {}

    fun onViewDetachedFromWindow(holder: RecyclerView.ViewHolder) {}
}
