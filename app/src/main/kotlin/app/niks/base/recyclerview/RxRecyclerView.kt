package app.niks.base.recyclerview

import android.content.Context
import android.util.AttributeSet
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import app.niks.base.recyclerview.click.ClickEvent
import app.niks.base.recyclerview.click.ItemClickSupport
import io.reactivex.subjects.PublishSubject

open class RxRecyclerView : RecyclerView {

    private lateinit var mItemClickSupport: ItemClickSupport

    constructor(context: Context) : this(context, null)
    constructor(context: Context, attrs: AttributeSet?) : this(context, attrs, 0)
    constructor(context: Context, attrs: AttributeSet?, defStyle: Int) : super(context, attrs, defStyle) {
        init()
    }

    open fun setup() {
    }

    private fun init() {
        if (isInEditMode) {
            return
        }
        mItemClickSupport = ItemClickSupport.addTo(this)
        setup()
    }

    fun getItemClickObserver(): PublishSubject<ClickEvent> {
        return mItemClickSupport.itemClickObserver
    }

    fun getItemLongClickObserver(): PublishSubject<ClickEvent> {
        return mItemClickSupport.itemLongClickObserver
    }

    fun adjustGridSpanCount(itemCount: Int, spanCount: Int) {
        if (layoutManager is GridLayoutManager) {
            if (itemCount in 1..(spanCount - 1)) {
                layoutManager = GridLayoutManager(context, itemCount, GridLayoutManager.HORIZONTAL, false)
            }
        }
    }
}
