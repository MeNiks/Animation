package app.niks.base.databinding

import android.widget.ImageView
import androidx.databinding.BindingAdapter
import app.niks.base.userdata.Creator

class ViewBindingUtils {
    companion object {

        @JvmStatic
        @BindingAdapter("imageUrl", "cornerRadius")
        fun loadImage(imageView: ImageView, imageUrl: String, cornerRadius: Int) {
            Creator.imageLoader.loadImage(imageView, imageUrl, cornerRadius)
        }
    }
}
