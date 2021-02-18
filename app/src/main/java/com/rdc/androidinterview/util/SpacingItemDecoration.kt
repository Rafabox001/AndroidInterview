package com.rdc.androidinterview.util

import androidx.recyclerview.widget.RecyclerView
import android.graphics.Rect
import android.view.View


class SpacingItemDecoration(private val padding: Int) : RecyclerView.ItemDecoration() {

    override fun getItemOffsets(outRect: Rect, view: View, parent: RecyclerView, state: RecyclerView.State) {
        super.getItemOffsets(outRect, view, parent, state)
        outRect.top = padding
        outRect.bottom = padding
        outRect.left = padding
        outRect.right = padding
    }
}