package app.niks.base.activity

import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import androidx.lifecycle.ViewModelProviders
import app.niks.base.databinding.AppDataBindingComponent
import app.niks.base.vm.BaseViewModel
import app.niks.base.vm.MessageType
import com.crashlytics.android.Crashlytics
import com.niks.animationdemo.BR
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable

open class BaseActivity<B : ViewDataBinding, V : BaseViewModel> : AppCompatActivity() {

    var dataBindingComponent = AppDataBindingComponent()
    var binding: B? = null

    protected var viewModel: V? = null
    protected lateinit var compositeDisposable: CompositeDisposable

    fun onCreate(savedInstanceState: Bundle?, layoutResID: Int, modelClass: Class<V>) {
        super.onCreate(savedInstanceState)
        // Here through dagger viewmodel factory can be optimize
        viewModel = ViewModelProviders.of(this/*,factory*/).get(modelClass)
        binding = DataBindingUtil.inflate(layoutInflater, layoutResID, null, false, dataBindingComponent)
        binding?.setVariable(BR.vm, viewModel)
        setContentView(binding!!.root)
    }

    override fun onStart() {
        super.onStart()
        compositeDisposable = CompositeDisposable()
        initializeListeners()
    }

    open fun initializeListeners() {
        viewModel?.let { viewmodel ->
            compositeDisposable
                .addAll(
                    viewmodel
                        .notifyMessage
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe({ messageType ->
                            if (messageType is MessageType.ShowToast) {
                                Toast.makeText(this, messageType.message, Toast.LENGTH_SHORT).show()
                            }
                        }, {
                            Crashlytics.log("toast failed")
                        })
                )
        }
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
}
