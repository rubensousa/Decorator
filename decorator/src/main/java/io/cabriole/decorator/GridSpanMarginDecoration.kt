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
 * @param horizontalMargin margin to be applied to the start and end side of an item
 *
 * @param verticalMargin margin to be applied to the top and bottom sides of an item
 *
 * @param gridLayoutManager the [GridLayoutManager] used by the [RecyclerView]
 *
 * @param decorationLookup an optional [DecorationLookup] to filter positions
 * that shouldn't have this decoration applied to
 *
 * Any property change should be followed by [RecyclerView.invalidateItemDecorations]
 */
class GridSpanMarginDecoration(
    @Px private var horizontalMargin: Int,
    @Px private var verticalMargin: Int,
    private var gridLayoutManager: GridLayoutManager,
    private var decorationLookup: DecorationLookup? = null
) : AbstractMarginDecoration(decorationLookup) {

    companion object {

        /**
         * Creates a [GridSpanMarginDecoration] that applies the same margin to all sides
         */
        @JvmStatic
        fun create(
            @Px margin: Int,
            gridLayoutManager: GridLayoutManager,
            decorationLookup: DecorationLookup? = null
        ): GridSpanMarginDecoration {
            return GridSpanMarginDecoration(
                verticalMargin = margin,
                horizontalMargin = margin,
                gridLayoutManager = gridLayoutManager,
                decorationLookup = decorationLookup
            )
        }

        @JvmStatic
        fun createVertical(
            @Px verticalMargin: Int,
            gridLayoutManager: GridLayoutManager,
            decorationLookup: DecorationLookup? = null
        ): GridSpanMarginDecoration {
            return GridSpanMarginDecoration(
                verticalMargin = verticalMargin,
                horizontalMargin = 0,
                gridLayoutManager = gridLayoutManager,
                decorationLookup = decorationLookup
            )
        }

        @JvmStatic
        fun createHorizontal(
            @Px horizontalMargin: Int,
            gridLayoutManager: GridLayoutManager,
            decorationLookup: DecorationLookup? = null
        ): GridSpanMarginDecoration {
            return GridSpanMarginDecoration(
                horizontalMargin = horizontalMargin,
                verticalMargin = 0,
                gridLayoutManager = gridLayoutManager,
                decorationLookup = decorationLookup
            )
        }
    }

    fun getHorizontalMargin() = horizontalMargin

    fun getVerticalMargin() = verticalMargin

    fun getGridLayoutManager() = gridLayoutManager

    fun setMargin(margin: Int) {
        horizontalMargin = margin
        verticalMargin = margin
    }

    fun setHorizontalMargin(margin: Int) {
        horizontalMargin = margin
    }

    fun setVerticalMargin(margin: Int) {
        verticalMargin = margin
    }

    fun setGridLayoutManager(layoutManager: GridLayoutManager) {
        gridLayoutManager = layoutManager
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
        val startPadding = horizontalMargin * ((columns - columnIndex) / columns.toFloat())
        val endPadding = horizontalMargin * ((columnIndex + 1 + (spanSize - 1)) / columns.toFloat())

        val isInFirstLine = position <= columnIndex

        // Our position plus the size we occupy will be greater
        // or equal than the item count if we're in the last line.
        val isInLastLine = position + (columns - columnIndex - spanSize) >= itemCount - 1
        val applyHalfBottomPadding = isInFirstLine != isInLastLine

        outRect.left = startPadding.toInt()
        outRect.right = endPadding.toInt()

        if (isInFirstLine) {
            if (!gridLayoutManager.reverseLayout) {
                outRect.top = verticalMargin
                if (applyHalfBottomPadding) {
                    outRect.bottom = verticalMargin / 2
                } else {
                    outRect.bottom = verticalMargin
                }
            } else {
                outRect.bottom = verticalMargin
                if (applyHalfBottomPadding) {
                    outRect.top = verticalMargin / 2
                } else {
                    outRect.top = verticalMargin
                }
            }
        } else if (isInLastLine) {
            if (!gridLayoutManager.reverseLayout) {
                outRect.top = verticalMargin / 2
                outRect.bottom = verticalMargin
            } else {
                outRect.top = verticalMargin
                outRect.bottom = verticalMargin / 2
            }
        } else {
            outRect.top = verticalMargin / 2
            outRect.bottom = verticalMargin / 2
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
        val startPadding = verticalMargin * ((columns - columnIndex) / columns.toFloat())
        val endPadding = verticalMargin * ((columnIndex + 1 + (spanSize - 1)) / columns.toFloat())

        val isInFirstLine = position <= columnIndex

        // Our position plus the size we occupy will be greater
        // or equal than the item count if we're in the last line.
        val isInLastLine = position + (columns - columnIndex - spanSize) >= itemCount - 1
        val applyHalfBottomPadding = isInFirstLine != isInLastLine

        outRect.top = startPadding.toInt()
        outRect.bottom = endPadding.toInt()

        if (isInFirstLine) {
            if (!gridLayoutManager.reverseLayout) {
                outRect.left = horizontalMargin
                if (applyHalfBottomPadding) {
                    outRect.right = horizontalMargin / 2
                } else {
                    outRect.right = horizontalMargin
                }
            } else {
                outRect.right = horizontalMargin
                if (applyHalfBottomPadding) {
                    outRect.left = horizontalMargin / 2
                } else {
                    outRect.left = horizontalMargin
                }
            }
        } else if (isInLastLine) {
            if (!gridLayoutManager.reverseLayout) {
                outRect.left = horizontalMargin / 2
                outRect.right = horizontalMargin
            } else {
                outRect.left = horizontalMargin
                outRect.right = horizontalMargin / 2
            }
        } else {
            outRect.left = horizontalMargin / 2
            outRect.right = horizontalMargin / 2
        }
    }

}
