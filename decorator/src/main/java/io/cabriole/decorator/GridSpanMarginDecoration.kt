/*
 * Copyright (c) 2020. Cabriole
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

package io.cabriole.decorator

import android.graphics.Rect
import android.view.View
import androidx.annotation.Px
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView

/**
 * An item decoration that applies a fixed margin to all sides for a grid.
 *
 * Unlike [GridMarginDecoration], this one supports grids with different span sizes
 * but requires a [GridLayoutManager].
 *
 * @param margin margin to be applied to all sides of an item
 *
 * @param gridLayoutManager the [GridLayoutManager] used by the [RecyclerView]
 *
 * @param decorationLookup an optional [DecorationLookup] to filter positions
 * that shouldn't have this decoration applied to
 *
 * Any property change in [margin] or [gridLayoutManager]
 * should be followed by [RecyclerView.invalidateItemDecorations]
 */
class GridSpanMarginDecoration(
    @Px private var margin: Int,
    private var gridLayoutManager: GridLayoutManager,
    private var decorationLookup: DecorationLookup? = null
) : AbstractMarginDecoration(decorationLookup) {

    fun getMargin() = margin

    fun getGridLayoutManager() = gridLayoutManager

    fun setMargin(margin: Int) {
        this.margin = margin
    }

    fun setGridLayoutManager(layoutManager: GridLayoutManager) {
        this.gridLayoutManager = layoutManager
    }

    /**
     * @param decorationLookup an optional [DecorationLookup] to filter positions
     * that shouldn't have this decoration applied to
     */
    fun setDecorationLookup(decorationLookup: DecorationLookup?) {
        this.decorationLookup = decorationLookup
    }

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
        val startPadding = margin * ((columns - columnIndex) / columns.toFloat())
        val endPadding = margin * ((columnIndex + 1 + (spanSize - 1)) / columns.toFloat())

        val isInFirstLine = position <= columnIndex

        // Our position plus the size we occupy will be greater
        // or equal than the item count if we're in the last line.
        val isInLastLine = position + (columns - columnIndex - spanSize) >= itemCount - 1
        val applyHalfBottomPadding = isInFirstLine != isInLastLine

        outRect.left = startPadding.toInt()
        outRect.right = endPadding.toInt()

        if (isInFirstLine) {
            if (!gridLayoutManager.reverseLayout) {
                outRect.top = margin
                if (applyHalfBottomPadding) {
                    outRect.bottom = margin / 2
                } else {
                    outRect.bottom = margin
                }
            } else {
                outRect.bottom = margin
                if (applyHalfBottomPadding) {
                    outRect.top = margin / 2
                } else {
                    outRect.top = margin
                }
            }
        } else if (isInLastLine) {
            if (!gridLayoutManager.reverseLayout) {
                outRect.top = margin / 2
                outRect.bottom = margin
            } else {
                outRect.top = margin
                outRect.bottom = margin / 2
            }
        } else {
            outRect.top = margin / 2
            outRect.bottom = margin / 2
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
        val startPadding = margin * ((columns - columnIndex) / columns.toFloat())
        val endPadding = margin * ((columnIndex + 1 + (spanSize - 1)) / columns.toFloat())

        val isInFirstLine = position <= columnIndex

        // Our position plus the size we occupy will be greater
        // or equal than the item count if we're in the last line.
        val isInLastLine = position + (columns - columnIndex - spanSize) >= itemCount - 1
        val applyHalfBottomPadding = isInFirstLine != isInLastLine

        outRect.top = startPadding.toInt()
        outRect.bottom = endPadding.toInt()

        if (isInFirstLine) {
            if (!gridLayoutManager.reverseLayout) {
                outRect.left = margin
                if (applyHalfBottomPadding) {
                    outRect.right = margin / 2
                } else {
                    outRect.right = margin
                }
            } else {
                outRect.right = margin
                if (applyHalfBottomPadding) {
                    outRect.left = margin / 2
                } else {
                    outRect.left = margin
                }
            }
        } else if (isInLastLine) {
            if (!gridLayoutManager.reverseLayout) {
                outRect.left = margin / 2
                outRect.right = margin
            } else {
                outRect.left = margin
                outRect.right = margin / 2
            }
        } else {
            outRect.left = margin / 2
            outRect.right = margin / 2
        }
    }

}
