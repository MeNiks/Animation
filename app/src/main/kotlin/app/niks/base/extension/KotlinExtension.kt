package app.niks.base.extension

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.location.LocationManager
import android.net.ConnectivityManager
import android.net.Uri
import android.preference.PreferenceManager
import android.util.DisplayMetrics
import android.util.Patterns
import android.util.TypedValue
import android.view.LayoutInflater
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.EditText
import android.widget.Toast
import androidx.annotation.Dimension
import androidx.fragment.app.Fragment
import androidx.paging.PagedList
import app.niks.base.recyclerview.datasourcefactory.ListPositionDataSource
import app.niks.base.recyclerview.paging.BackgroundThreadExecutor
import app.niks.base.recyclerview.paging.UiThreadExecutor
import com.google.gson.JsonArray
import com.google.gson.JsonNull
import com.google.gson.JsonObject
import com.google.gson.JsonParser
import io.reactivex.Flowable
import io.reactivex.Observable
import io.reactivex.Single
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import org.json.JSONException
import org.json.JSONObject
import timber.log.Timber

// Rx Java
fun <T> Observable<T>.applyOnIOMainSchedulers(): Observable<T> {
    return subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread())
}

fun <T> Observable<T>.applyOnIOSchedulers(): Observable<T> {
    return subscribeOn(Schedulers.io()).observeOn(Schedulers.io())
}

fun <T> Flowable<T>.applyOnIOMainSchedulers(): Flowable<T> {
    return subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread())
}

fun <T> Single<T>.applyOnIOMainSchedulers(): Single<T> {
    return subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread())
}

fun <T> Observable<T>.transformErrorToResult(): Observable<Result<T>> {
    return this.map { it.asResult() }
        .onErrorReturn { it.asErrorResult() }
}

fun <T> T.asResult(): Result<T> {
    return Result.Success(data = this)
}

fun <T> Throwable.asErrorResult(): Result<T> {
    return Result.Error(throwable = this)
}
fun handleThrowable(throwable: Throwable) {
    Timber.e(throwable)
}

private const val PAGE_SIZE = 10
private const val ENABLE_PLACEHOLDERS = false
private const val INITIAL_LOAD_SIZE_HINT = 20
private const val PREFETCH_DISTANCE = 5

fun <T> List<T>.toPagedList(): PagedList<T> {
    return PagedList.Builder(
        ListPositionDataSource<T>(this),
        PagedList.Config.Builder()
            .setPageSize(PAGE_SIZE)
            .setEnablePlaceholders(ENABLE_PLACEHOLDERS)
            .build())
        .setNotifyExecutor(UiThreadExecutor())
        .setFetchExecutor(BackgroundThreadExecutor())
        .build()
}

sealed class Result<out T> {
    data class Success<out T>(val data: T) : Result<T>()
    data class Error<out T>(val throwable: Throwable) : Result<T>()
}

// Gson
fun String.toGsonJsonObject(): JsonObject {
    if (isBlank())
        return JsonObject()
    val parser = JsonParser()
    return parser.parse(this).asJsonObject
}

fun String.toGsonJsonArray(): JsonArray {
    if (isBlank()) {
        return JsonArray()
    }
    val parser = JsonParser()
    return parser.parse(this).asJsonArray
}

fun String?.isValidEmail(): Boolean {
    return !this.isNullOrEmpty() && Patterns.EMAIL_ADDRESS.matcher(this).matches()
}

fun JsonObject.isNotNull(key: String): Boolean {
    return this.has(key) && this.get(key) !is JsonNull
}

// Edittext
fun EditText.isEmpty(): Boolean {
    return trimText().isEmpty()
}

fun EditText?.trimText(): String {
    if (this == null) return ""
    return text.toString().trim()
}

fun EditText?.openKeyboard() {
    this ?: return
    requestFocus()
    val imm = context.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
    imm.showSoftInput(this, InputMethodManager.SHOW_IMPLICIT)
}

// Activity
fun Activity?.hideKeyboard() {
    this ?: return
    val imm = getSystemService(Activity.INPUT_METHOD_SERVICE) as InputMethodManager
    var view: View? = currentFocus
    if (view == null) {
        view = View(this)
    }
    imm.hideSoftInputFromWindow(view.windowToken, 0)
}

// Shared Preference
inline fun SharedPreferences.Edit(func: SharedPreferences.Editor.() -> Unit) {
    val editor = edit()
    editor.func()
    editor.apply()
}

// Context
@Suppress("unchecked_cast")
fun <T> Context.getService(name: String): T {
    return getSystemService(name) as T
}

val Context.layoutInflater: LayoutInflater
    get() = getService(Context.LAYOUT_INFLATER_SERVICE)

val Context.locationManager: LocationManager
    get() = getService(Context.LOCATION_SERVICE)

val Context.connectivityManager: ConnectivityManager
    get() = getService(Context.CONNECTIVITY_SERVICE)

val Context.inputMethodManager: InputMethodManager
    get() = getService(Context.INPUT_METHOD_SERVICE)

val Context.defaultSharedPreferences: SharedPreferences
    get() = PreferenceManager.getDefaultSharedPreferences(this)

val Context.defaultSharedPreferencesEditor: SharedPreferences.Editor
    get() = PreferenceManager.getDefaultSharedPreferences(this).edit()

@SuppressLint("MissingPermission")
fun Context.helpMakeCall(contact_number: String) {
    startActivity(Intent(Intent.ACTION_CALL, Uri.parse("tel:" + contact_number)))
}

fun Context.helpOpenUri(string_uri: String) {
    startActivity(Intent(Intent.ACTION_VIEW, Uri.parse(string_uri)))
}

fun Context.myToast(message: String) {
    Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
}

fun Context.convertDpToPixel(@Dimension(unit = Dimension.DP) dp: Float): Float {
    return TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp, resources.displayMetrics)
}

fun Context.convertSpToPixel(@Dimension(unit = Dimension.SP) sp: Float): Float {
    return TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_SP, sp, resources.displayMetrics)
}

// Fragment
fun Fragment.myToast(message: String) {
    Toast.makeText(activity, message, Toast.LENGTH_SHORT).show()
}

// Json Object
fun JSONObject.getValidBoolean(key: String, default_value: Boolean): Boolean {

    try {
        if (!isNull(key) && get(key) !is JSONException) {
            return getBoolean(key)
        }
    } catch (e: Exception) {
    }

    return default_value
}

fun JSONObject.getValidInt(key: String, default_value: Int): Int {

    try {
        if (!isNull(key) && get(key) !is JSONException) {
            return getInt(key)
        }
    } catch (e: Exception) {
    }

    return default_value
}

fun JSONObject.getValidLong(key: String, default_value: Long): Long {

    try {
        if (!isNull(key) && get(key) !is JSONException) {
            return getLong(key)
        }
    } catch (e: Exception) {
    }

    return default_value
}

fun JSONObject.getValidString(key: String, default_value: String): String {

    try {
        if (!isNull(key) && get(key) !is JSONException) {
            return getString(key)
        }
    } catch (e: Exception) {
    }

    return default_value
}

fun View?.setVisibleOrGone(isVisible: Boolean) {
    this ?: return
    visibility = if (isVisible)
        View.VISIBLE
    else
        View.GONE
}

fun View?.setVisibleOrInvisible(isVisible: Boolean) {
    this ?: return
    visibility = if (isVisible)
        View.VISIBLE
    else
        View.INVISIBLE
}