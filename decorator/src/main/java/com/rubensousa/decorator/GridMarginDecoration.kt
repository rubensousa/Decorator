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
import androidx.recyclerview.widget.RecyclerView
import kotlin.math.ceil

/**
 * An item decoration that applies a fixed margin to all sides for a grid.
 *
 * This only works for grids with fixed span rows.
 * For variable spanned rows, use [GridSpanMarginDecoration].
 *
 * @param horizontalMargin margin to be applied to the start and end side of an item
 *
 * @param verticalMargin margin to be applied to the top and bottom sides of an item
 *
 * @param columnProvider a [ColumnProvider] that provides the number of columns
 *
 * @param orientation the orientation of the LayoutManager used by the RecyclerView.
 * Default is [RecyclerView.VERTICAL]
 *
 * @param inverted true if the LayoutManager is inverted and items are laid out
 * from the bottom to the top or from the right to the left. Default is false.
 *
 * @param decorationLookup an optional [DecorationLookup] to filter positions
 * that shouldn't have this decoration applied to
 *
 * Any property change should be followed by [RecyclerView.invalidateItemDecorations]
 */
class GridMarginDecoration(
    @Px private var horizontalMargin: Int,
    @Px private var verticalMargin: Int,
    private var columnProvider: ColumnProvider,
    private var orientation: Int = RecyclerView.VERTICAL,
    private var inverted: Boolean = false,
    decorationLookup: DecorationLookup? = null
) : AbstractMarginDecoration(decorationLookup) {

    companion object {

        /**
         * Creates a [GridMarginDecoration] that applies the same margin to all sides
         */
        @JvmStatic
        fun create(
            @Px margin: Int,
            columnProvider: ColumnProvider,
            orientation: Int = RecyclerView.VERTICAL,
            inverted: Boolean = false,
            decorationLookup: DecorationLookup? = null
        ): GridMarginDecoration {
            return GridMarginDecoration(
                verticalMargin = margin,
                horizontalMargin = margin,
                columnProvider = columnProvider,
                orientation = orientation,
                inverted = inverted,
                decorationLookup = decorationLookup
            )
        }

        @JvmStatic
        fun createVertical(
            @Px verticalMargin: Int,
            columnProvider: ColumnProvider,
            orientation: Int = RecyclerView.VERTICAL,
            inverted: Boolean = false,
            decorationLookup: DecorationLookup? = null
        ): GridMarginDecoration {
            return GridMarginDecoration(
                verticalMargin = verticalMargin,
                horizontalMargin = 0,
                columnProvider = columnProvider,
                orientation = orientation,
                inverted = inverted,
                decorationLookup = decorationLookup
            )
        }

        @JvmStatic
        fun createHorizontal(
            @Px horizontalMargin: Int,
            columnProvider: ColumnProvider,
            orientation: Int = RecyclerView.VERTICAL,
            inverted: Boolean = false,
            decorationLookup: DecorationLookup? = null
        ): GridMarginDecoration {
            return GridMarginDecoration(
                horizontalMargin = horizontalMargin,
                verticalMargin = 0,
                columnProvider = columnProvider,
                orientation = orientation,
                inverted = inverted,
                decorationLookup = decorationLookup
            )
        }
    }

    fun getHorizontalMargin() = horizontalMargin

    fun getVerticalMargin() = verticalMargin

    fun getOrientation() = orientation

    fun isInverted() = inverted

    /**
     * @param margin margin to be applied to all sides of an item
     */
    fun setMargin(margin: Int) {
        this.horizontalMargin = margin
        this.verticalMargin = margin
    }

    fun setHorizontalMargin(margin: Int) {
        this.horizontalMargin = margin
    }

    fun setVerticalMargin(margin: Int) {
        this.verticalMargin = margin
    }

    /**
     * @param columnProvider a [ColumnProvider] that provides the number of columns
     */
    fun setColumnProvider(columnProvider: ColumnProvider) {
        this.columnProvider = columnProvider
    }

    /**
     * @param orientation the orientation of the LayoutManager used by the RecyclerView.
     * Default is [RecyclerView.VERTICAL]
     */
    fun setOrientation(orientation: Int) {
        this.orientation = orientation
    }

    /**
     * @param isInverted true if the LayoutManager is inverted and items are laid out
     * from the bottom to the top or from the right to the left. Default is false.
     */
    fun setInverted(isInverted: Boolean) {
        this.inverted = isInverted
    }

    override fun getItemOffsets(
        outRect: Rect,
        view: View,
        position: Int,
        parent: RecyclerView,
        state: RecyclerView.State
    ) {
        val columns = columnProvider.getNumberOfColumns()
        val columnIndex = position.rem(columns)

        val lines = ceil(state.itemCount / columns.toDouble()).toInt()
        val lineIndex = position / columns

        if (orientation == RecyclerView.VERTICAL) {
            applyVerticalOffsets(outRect, columns, columnIndex, lines, lineIndex)
        } else {
            applyHorizontalOffsets(outRect, columns, columnIndex, lines, lineIndex)
        }
    }

    private fun applyVerticalOffsets(
        outRect: Rect,
        columns: Int,
        columnIndex: Int,
        lines: Int,
        lineIndex: Int
    ) {
        val startPadding = horizontalMargin * ((columns - columnIndex) / columns.toFloat())
        val endPadding = horizontalMargin * ((columnIndex + 1) / columns.toFloat())
        outRect.left = startPadding.toInt()
        outRect.right = endPadding.toInt()

        if (lineIndex == 0) {
            if (!inverted) {
                outRect.top = verticalMargin
                if (lines > 1) {
                    outRect.bottom = verticalMargin / 2
                } else {
                    outRect.bottom = verticalMargin
                }
            } else {
                outRect.bottom = verticalMargin
                if (lines > 1) {
                    outRect.top = verticalMargin / 2
                } else {
                    outRect.top = verticalMargin
                }
            }
        } else if (lineIndex == lines - 1) {
            if (!inverted) {
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
        columns: Int,
        columnIndex: Int,
        lines: Int,
        lineIndex: Int
    ) {
        val startPadding = verticalMargin * ((columns - columnIndex) / columns.toFloat())
        val endPadding = verticalMargin * ((columnIndex + 1) / columns.toFloat())

        outRect.top = startPadding.toInt()
        outRect.bottom = endPadding.toInt()

        if (lineIndex == 0) {
            if (!inverted) {
                outRect.left = horizontalMargin
                if (lines > 1) {
                    outRect.right = horizontalMargin / 2
                } else {
                    outRect.right = horizontalMargin
                }
            } else {
                outRect.right = horizontalMargin
                if (lines > 1) {
                    outRect.left = horizontalMargin / 2
                } else {
                    outRect.left = horizontalMargin
                }
            }
        } else if (lineIndex == lines - 1) {
            if (!inverted) {
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
