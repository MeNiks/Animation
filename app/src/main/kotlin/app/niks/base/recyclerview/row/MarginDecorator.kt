package app.niks.base.recyclerview.row

import android.graphics.Rect
import android.view.View
import androidx.recyclerview.widget.RecyclerView

class MarginDecorator
constructor(
    val margin: Int
) : RecyclerView.ItemDecoration() {

    override fun getItemOffsets(outRect: Rect, view: View, parent: RecyclerView, state: RecyclerView.State) {
        val position = parent.getChildLayoutPosition(view)
        if (position == 0)
            outRect.top = margin
        outRect.left = margin
        outRect.right = margin
        outRect.bottom = margin
    }
}
