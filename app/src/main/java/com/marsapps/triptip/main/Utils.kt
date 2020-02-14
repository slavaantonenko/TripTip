package com.marsapps.triptip.main

import android.content.res.Resources
import android.graphics.Rect
import android.view.View
import androidx.recyclerview.widget.RecyclerView.ItemDecoration
import com.google.android.gms.common.util.Predicate
import java.util.*

class Utils {

    companion object {

        fun calculateNoOfColumns(resources: Resources, itemWidth: Int): Int {
            return resources.displayMetrics.widthPixels / itemWidth
        }

        /**
         * RecyclerView item decoration - give equal margin around grid item
         */
        class GridSpacingItemDecoration(private val spanCount: Int, private val spacing: Int, private val includeEdge: Boolean) : ItemDecoration() {
            override fun getItemOffsets(outRect: Rect, view: View, parent: androidx.recyclerview.widget.RecyclerView, state: androidx.recyclerview.widget.RecyclerView.State) {

                val position = parent.getChildAdapterPosition(view) // item position
                val column = position % spanCount // item column

                if (includeEdge) {
                    outRect.left = spacing - column * spacing / spanCount // spacing - column * ((1f / spanCount) * spacing)
                    outRect.right = (column + 1) * spacing / spanCount // (column + 1) * ((1f / spanCount) * spacing)
                }
                else {
                    outRect.left = column * spacing / spanCount // column * ((1f / spanCount) * spacing)
                    outRect.right = spacing - (column + 1) * spacing / spanCount // spacing - (column + 1) * ((1f /    spanCount) * spacing)
                    if (position >= spanCount) outRect.top = spacing // item top
                }
            }
        }

        fun <T> filter(target: List<T>, predicate: Predicate<T>): List<T> {
            val result: MutableList<T> = ArrayList()
            for (element in target) {
                if (predicate.apply(element)) {
                    result.add(element)
                }
            }
            return result
        }
    }
}