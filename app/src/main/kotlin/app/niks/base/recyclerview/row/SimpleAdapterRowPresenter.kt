package app.niks.base.recyclerview.row

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.annotation.LayoutRes
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import androidx.recyclerview.widget.RecyclerView
import app.niks.base.recyclerview.binding.BindingViewHolder
import kotlin.reflect.KClass

class SimpleAdapterRowPresenter<ID, T : RecyclerRowItem<ID>>
constructor(
    @LayoutRes
    private val layoutId: Int,
    private val clazz: KClass<*>? = null
) : AdapterRowPresenter<ID, T>() {
    override fun isForViewType(item: T, position: Int): Boolean {
        return clazz?.let { clazz -> clazz == item::class } ?: true
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val binding: ViewDataBinding = DataBindingUtil.inflate(LayoutInflater.from(parent.context), layoutId, parent, false)
        return BindingViewHolder(binding)
    }
}
