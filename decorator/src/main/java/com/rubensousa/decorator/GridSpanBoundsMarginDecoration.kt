/*
 * Copyright (c) 2021. Rúben Sousa
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.rubensousa.decorator

import android.graphics.Rect
import android.view.View
import androidx.annotation.Px
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView

/**
 *  A [RecyclerView.ItemDecoration] that applies a margin to the bounds of the RecyclerView
 *
 * Unlike [GridBoundsMarginDecoration], this one supports grids with different span sizes
 * but requires a [GridLayoutManager].
 *
 * @param leftMargin margin to be applied to the left bound
 *
 * @param topMargin margin to be applied to the top bound
 *
 * @param rightMargin margin to be applied to the right bound
 *
 * @param bottomMargin margin to be applied to the bottom bound
 *
 * @param gridLayoutManager the [GridLayoutManager] used by the [RecyclerView]
 *
 * @param decorationLookup an optional [DecorationLookup] to filter positions
 * that shouldn't have this decoration applied to
 *
 * Any property change should be followed by [RecyclerView.invalidateItemDecorations]
 *
 */
class GridSpanBoundsMarginDecoration(
    @Px private var leftMargin: Int = 0,
    @Px private var topMargin: Int = 0,
    @Px private var rightMargin: Int = 0,
    @Px private var bottomMargin: Int = 0,
    private var gridLayoutManager: GridLayoutManager,
    decorationLookup: DecorationLookup? = null
) : AbstractMarginDecoration(decorationLookup) {

    fun setMargin(margin: Int) {
        setMargin(left = margin, top = margin, right = margin, bottom = margin)
    }

    fun setMargin(left: Int = 0, top: Int = 0, right: Int = 0, bottom: Int = 0) {
        this.leftMargin = left
        this.topMargin = top
        this.rightMargin = right
        this.bottomMargin = bottom
    }

    fun setGridLayoutManager(layoutManager: GridLayoutManager) {
        this.gridLayoutManager = layoutManager
    }

    fun getGridLayoutManager() = gridLayoutManager

    fun getLeftMargin() = leftMargin

    fun getTopMargin() = topMargin

    fun getRightMargin() = rightMargin

    fun getBottomMargin() = bottomMargin

    override fun getItemOffsets(
        outRect: Rect,
        view: View,
        position: Int,
        parent: RecyclerView,
        state: RecyclerView.State,
        layoutManager: RecyclerView.LayoutManager
    ) {
        val layoutParams = view.layoutParams as GridLayoutManager.LayoutParams
        val columnIndex = layoutParams.spanIndex

        if (columnIndex == GridLayoutManager.LayoutParams.INVALID_SPAN_ID) {
            return
        }

        val itemCount = layoutManager.itemCount
        val columns = gridLayoutManager.spanCount

        if (gridLayoutManager.orientation == RecyclerView.VERTICAL) {
            applyVerticalOffsets(
                outRect,
                position,
                itemCount,
                columns,
                columnIndex,
                layoutParams.spanSize
            )
        } else {
            applyHorizontalOffsets(
                outRect,
                position,
                itemCount,
                columns,
                columnIndex,
                layoutParams.spanSize
            )
        }
    }

    private fun applyVerticalOffsets(
        outRect: Rect,
        position: Int,
        itemCount: Int,
        columns: Int,
        columnIndex: Int,
        spanSize: Int
    ) {
        val isInFirstLine = position <= columnIndex

        // Our position plus the size we occupy will be greater
        // or equal than the item count if we're in the last line.
        val isInLastLine = position + (columns - columnIndex - spanSize) >= itemCount - 1

        if (columnIndex == 0) {
            outRect.left = leftMargin
        }

        if (columnIndex + spanSize >= columns) {
            outRect.right = rightMargin
        }

        if (isInFirstLine) {
            if (!gridLayoutManager.reverseLayout) {
                outRect.top = topMargin
            } else {
                outRect.bottom = bottomMargin
            }
        } else if (isInLastLine) {
            if (!gridLayoutManager.reverseLayout) {
                outRect.bottom = bottomMargin
            } else {
                outRect.top = topMargin
            }
        }
    }

    private fun applyHorizontalOffsets(
        outRect: Rect,
        position: Int,
        itemCount: Int,
        columns: Int,
        columnIndex: Int,
        spanSize: Int
    ) {
        val isInFirstLine = position <= columnIndex

        // Our position plus the size we occupy will be greater
        // or equal than the item count if we're in the last line.
        val isInLastLine = position + (columns - columnIndex - spanSize) >= itemCount - 1

        if (columnIndex == 0) {
            outRect.top = topMargin
        }

        if (columnIndex + spanSize >= columns) {
            outRect.bottom = bottomMargin
        }

        if (isInFirstLine) {
            if (!gridLayoutManager.reverseLayout) {
                outRect.left = leftMargin
            } else {
                outRect.right = rightMargin
            }
        } else if (isInLastLine) {
            if (!gridLayoutManager.reverseLayout) {
                outRect.right = rightMargin
            } else {
                outRect.left = leftMargin
            }
        }
    }

}
