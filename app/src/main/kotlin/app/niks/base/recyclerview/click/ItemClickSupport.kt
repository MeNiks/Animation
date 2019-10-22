package app.niks.base.recyclerview.click

import android.view.View
import androidx.recyclerview.widget.RecyclerView
import app.niks.base.recyclerview.RxRecyclerView
import com.niks.animationdemo.R
import io.reactivex.subjects.PublishSubject

class ItemClickSupport
private constructor(
    private val mRecyclerView: RxRecyclerView
) {

    private fun detach(view: RecyclerView) {
        view.removeOnChildAttachStateChangeListener(mAttachListener)
        view.setTag(R.id.item_click_support, null)
    }

    val itemClickObserver: PublishSubject<ClickEvent> = PublishSubject.create<ClickEvent>()
    val itemLongClickObserver: PublishSubject<ClickEvent> = PublishSubject.create<ClickEvent>()

    private val mOnClickListener = View.OnClickListener { v ->
        val holder = mRecyclerView.getChildViewHolder(v)
        val pos = holder.adapterPosition
        val id = mRecyclerView.adapter!!.getItemId(pos)
        itemClickObserver.onNext(ClickEvent.ITEM_CLICK(pos, id))
    }

    private val mOnLogClickListener = View.OnLongClickListener { v ->
        val holder = mRecyclerView.getChildViewHolder(v)
        val pos = holder.adapterPosition
        val id = mRecyclerView.adapter!!.getItemId(pos)
        itemLongClickObserver.onNext(ClickEvent.ITEM_LONG_CLICK(pos, id))
        true
    }

    private val mAttachListener = object : RecyclerView.OnChildAttachStateChangeListener {
        override fun onChildViewAttachedToWindow(view: View) {
            view.setOnClickListener(mOnClickListener)
            view.setOnLongClickListener(mOnLogClickListener)
        }

        override fun onChildViewDetachedFromWindow(view: View) {
            view.setOnClickListener(null)
            view.setOnLongClickListener(null)
        }
    }

    init {
        mRecyclerView.setTag(R.id.item_click_support, this)
        mRecyclerView.addOnChildAttachStateChangeListener(mAttachListener)
    }

    companion object {
        fun addTo(view: RxRecyclerView): ItemClickSupport {
            val tag = view.getTag(R.id.item_click_support)
            return if (tag != null && tag is ItemClickSupport) {
                tag
            } else ItemClickSupport(view)
        }

        fun removeFrom(view: RecyclerView): ItemClickSupport? {
            val support = view.getTag(R.id.item_click_support) as ItemClickSupport
            support.detach(view)
            return support
        }
    }
}
