package ru.stroykrep.app.ui.common

import android.graphics.Rect
import android.view.View
import androidx.recyclerview.widget.RecyclerView

class SpaceItemDecoration(
    private val spacing: Int,
    private val spanCount: Int = 1
) : RecyclerView.ItemDecoration() {

    override fun getItemOffsets(
        outRect: Rect, view: View, parent: RecyclerView, state: RecyclerView.State
    ) {
        val position = parent.getChildAdapterPosition(view)
        if (position == RecyclerView.NO_POSITION) return
        val half = spacing / 2
        when (spanCount) {
            1 -> {
                outRect.left = spacing
                outRect.right = spacing
                outRect.top = if (position == 0) spacing else 0
                outRect.bottom = spacing
            }
            else -> {
                outRect.left = half
                outRect.right = half
                outRect.top = half
                outRect.bottom = half
            }
        }
    }
}
