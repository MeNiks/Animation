package app.niks.base.recyclerview.adapter

import android.view.ViewGroup
import androidx.collection.SparseArrayCompat
import androidx.recyclerview.widget.RecyclerView
import app.niks.base.recyclerview.row.AdapterRowPresenter
import app.niks.base.recyclerview.row.AdapterRowPresenterFactory
import app.niks.base.recyclerview.row.RecyclerRowItem
import app.niks.base.recyclerview.row.SelectableRecyclerRowItem
import app.niks.base.recyclerview.selector.ItemSelector

class AdapterRowPresenterManager<ID, T : RecyclerRowItem<ID>>() {
    private var presenters: SparseArrayCompat<AdapterRowPresenter<ID, T>> = SparseArrayCompat()

    private var fallbackPresenter: AdapterRowPresenter<ID, T>? = null

    private var presenterFactory: AdapterRowPresenterFactory<ID, T>? = null

    private var itemSelector: ItemSelector<ID, T>? = null

    constructor(presenterFactory: AdapterRowPresenterFactory<ID, T>?) : this() {
        this.presenterFactory = presenterFactory
    }

    fun setItemSelector(itemSelector: ItemSelector<ID, T>?) {
        this.itemSelector = itemSelector
    }

    fun addViewPresenter(presenter: AdapterRowPresenter<ID, T>) {

        var viewType = presenters.size()

        while (presenters.get(viewType) != null) {
            viewType++
        }
        addViewPresenter(viewType, false, presenter)
    }

    fun addViewPresenter(viewType: Int, delegate: AdapterRowPresenter<ID, T>) {
        addViewPresenter(viewType, false, delegate)
    }

    private fun addViewPresenter(viewType: Int, allowRelpacningDelegate: Boolean, delegate: AdapterRowPresenter<ID, T>) {
        if (viewType == FALLBACK_DELEGATE_VIEW_TYPE) {
            throw IllegalArgumentException("This view type = $FALLBACK_DELEGATE_VIEW_TYPE is reserved for fallback adapter delegate (see setFallbackDelegate()) . Please use another view type.")
        }

        if (!allowRelpacningDelegate && presenters.get(viewType) != null) {
            throw java.lang.IllegalArgumentException("An AdapterDelefate is already register for this viewType= $viewType")
        }

        presenters.put(viewType, delegate)
    }

    fun removeDelegate(delegate: AdapterRowPresenter<ID, T>) {
        val indexToRemove = presenters.indexOfValue(delegate)
        if (indexToRemove >= 0) {
            presenters.removeAt(indexToRemove)
        }
    }

    fun removeDelegate(viewType: Int) {
        presenters.remove(viewType)
    }

    internal fun getItemViewType(item: T, position: Int): Int {
        if (presenterFactory != null) {
            val viewType = presenterFactory!!.getViewType(item)
            if (presenters.get(viewType) == null) {
                presenters.put(viewType, presenterFactory!!.getItemPresenter(viewType))
            }
            return viewType
        }

        val delegatesCount = presenters.size()
        for (i in 0 until delegatesCount) {
            val delegate = presenters.valueAt(i)
            if (delegate.isForViewType(item, position)) {
                return presenters.keyAt(i)
            }
        }

        if (fallbackPresenter != null) {
            return FALLBACK_DELEGATE_VIEW_TYPE
        }
        throw NullPointerException("No AdapterDelegate added ")
    }

    internal fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val delegate = getDelegateForViewType(viewType)
                ?: throw NullPointerException("No delegate found for item at viewType : $viewType")
        return delegate.onCreateViewHolder(parent, viewType)
    }

    internal fun onBindViewHolder(item: T, position: Int, viewHolder: RecyclerView.ViewHolder, vararg payloads: Any?) {
        var delegate = getDelegateForViewType(viewHolder.itemViewType)
                ?: throw NullPointerException("No delegate found for item at position = $position for viewType = ${viewHolder.itemViewType}")

        if (item is SelectableRecyclerRowItem<*>) {
            itemSelector?.let { selector ->
                if (selector.isItemSelected(item.getItemId())) {
                    selector.addSelectedItem(position, item)
                    item.isSelected = true
                } else {
                    item.isSelected = false
                }
            }
        }
        delegate.onBindViewHolder(item, position, viewHolder, payloads)
    }

    fun onViewRecycled(viewHolder: RecyclerView.ViewHolder) {
        val delegate = getDelegateForViewType(viewHolder.itemViewType)
                ?: throw NullPointerException("No delegate found for item at position = ${viewHolder.adapterPosition} for viewType = ${viewHolder.itemViewType}")
        delegate.onViewRecycled(viewHolder)
    }

    fun onFailedToRecycleView(viewHolder: RecyclerView.ViewHolder): Boolean {
        val delegate = getDelegateForViewType(viewHolder.itemViewType)
                ?: throw NullPointerException("No delegate found for item at position = ${viewHolder.adapterPosition} for viewType = ${viewHolder.itemViewType}")
        return delegate.onFailedToRecycleView(viewHolder)
    }

    fun onViewAttachedToWindow(viewHolder: RecyclerView.ViewHolder) {
        val delegate = getDelegateForViewType(viewHolder.itemViewType)
                ?: throw NullPointerException("No delegate found for item at position = ${viewHolder.adapterPosition} for viewType = ${viewHolder.itemViewType}")
        delegate.onViewAttachedToWindow(viewHolder)
    }

    fun onViewDetachedFromWindow(viewHolder: RecyclerView.ViewHolder) {
        val delegate = getDelegateForViewType(viewHolder.itemViewType)
                ?: throw NullPointerException("No delegate found for item at position = ${viewHolder.adapterPosition} for viewType = ${viewHolder.itemViewType}")
        delegate.onViewDetachedFromWindow(viewHolder)
    }

    fun setFallbackDelegate(fallbackDelegate: AdapterRowPresenter<ID, T>?) {
        this.fallbackPresenter = fallbackPresenter
    }

    fun getViewType(delegate: AdapterRowPresenter<ID, T>?): Int {

        if (delegate == null) {
            throw NullPointerException("Delegate is null")
        }
        val index = presenters.indexOfValue(delegate)
        if (index == -1) {
            return -1
        }
        return presenters.keyAt(index)
    }

    private fun getDelegateForViewType(viewType: Int): AdapterRowPresenter<ID, T>? {
        if (presenterFactory != null) {
            presenterFactory?.getItemPresenter(viewType)
        }
        return presenters.get(viewType, fallbackPresenter)
    }

    companion object {
        internal const val FALLBACK_DELEGATE_VIEW_TYPE = Integer.MAX_VALUE - 1
    }
}
