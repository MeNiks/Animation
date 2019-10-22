package app.niks.base.image

import android.widget.ImageView
import app.niks.base.extension.convertDpToPixel
import app.niks.base.userdata.Creator
import com.bumptech.glide.request.RequestOptions
import jp.wasabeef.glide.transformations.RoundedCornersTransformation

open class ImageLoader {
    fun loadImage(imageView: ImageView, url: String) {
        var finalUrl = url
        if (!url.contains("http", true)) {
            finalUrl = "https:$url"
        }
        Creator
            .glideRequestManager
            .load(finalUrl)
            .into(imageView)
    }

    fun loadImage(imageView: ImageView, url: String, cornerRadius: Int) {
        var finalUrl = url
        if (!url.contains("http", true)) {
            finalUrl = "https:$url"
        }
        val contextRadiusInPixel = Creator.context.convertDpToPixel(cornerRadius)
        Creator
            .glideRequestManager
            .load(finalUrl)
            .apply(
                RequestOptions.bitmapTransform(
                    RoundedCornersTransformation(
                        contextRadiusInPixel.toInt(), 0, RoundedCornersTransformation.CornerType.ALL
                    )
                )
            )
            .into(imageView)
    }
}
