package app.niks.base.vm

import androidx.lifecycle.ViewModel
import app.niks.base.retrofit.NoInternetException
import app.niks.base.userdata.Creator
import app.niks.base.userdata.ViewComponents
import com.jakewharton.rxrelay2.PublishRelay
import com.niks.animationdemo.R
import io.reactivex.disposables.CompositeDisposable
import retrofit2.HttpException

abstract class BaseViewModel : ViewModel() {

    val isLoading = PublishRelay.create<Boolean>()

    val notifyMessage = PublishRelay.create<MessageType>()

    val viewEvents = PublishRelay.create<ViewComponents>()

    var compositeDisposable = CompositeDisposable()

    override fun onCleared() {
        compositeDisposable.dispose()
        super.onCleared()
    }

    fun handleError(throwable: Throwable) {
        when (throwable) {
            is HttpException -> handleHttpErrorCodes(throwable)
            is NoInternetException -> {
                notifyMessage.accept(MessageType.ShowToast(Creator.context.getString(R.string.no_internet)))
            }
            else -> {
                notifyMessage.accept(MessageType.ShowToast(Creator.context.getString(R.string.unexpected)))
            }
        }
    }

    private fun handleHttpErrorCodes(httpException: HttpException) {
        when (httpException.code()) {
            400,
            401 -> {
                notifyMessage.accept(MessageType.ShowToast(Creator.context.getString(R.string.unexpected_code, httpException.code())))
            }
            500, 503 -> {
                notifyMessage.accept(MessageType.ShowToast(Creator.context.getString(R.string.unexpected_code, httpException.code())))
            }
            else -> {
                notifyMessage.accept(MessageType.ShowToast(Creator.context.getString(R.string.unexpected_code, httpException.code())))
            }
        }
    }
}
