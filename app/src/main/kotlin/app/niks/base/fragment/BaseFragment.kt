package app.niks.base.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProviders
import app.niks.base.databinding.AppDataBindingComponent
import app.niks.base.vm.BaseViewModel
import com.niks.animationdemo.BR
import io.reactivex.disposables.CompositeDisposable

abstract class BaseFragment<B : ViewDataBinding, V : BaseViewModel> :
    Fragment(), FragmentCallBack {

    var dataBindingComponent = AppDataBindingComponent()
    var binding: B? = null
    protected var viewModel: V? = null
    protected lateinit var compositeDisposable: CompositeDisposable

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
    }

    fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?, layoutResID: Int, modelClass: Class<V>): View {
        super.onCreateView(inflater, container, savedInstanceState)
        // Here through dagger viewmodel factory can be optimize
        viewModel = ViewModelProviders.of(this/*,factory*/).get(modelClass)
        binding = DataBindingUtil.inflate(inflater, layoutResID, container, false, dataBindingComponent)
        binding?.setVariable(BR.vm, viewModel)
        return binding!!.root
    }

    override fun onStart() {
        super.onStart()
        compositeDisposable = CompositeDisposable()
        initializeListeners()
    }

    override fun onStop() {
        super.onStop()
        compositeDisposable.dispose()
    }

    override fun onDestroy() {
        super.onDestroy()
        viewModel = null
        binding = null
    }

    open fun initializeListeners() {
    }

    override fun onBackPressed(): Boolean {
        return false
    }
}
