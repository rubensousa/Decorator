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
import androidx.recyclerview.widget.RecyclerView

/**
 * An item decoration that applies a fixed margin to all sides for a grid.
 *
 * This only works for grids with fixed span rows.
 * For variable spanned rows, use [GridSpanMarginDecoration].
 *
 * @param margin margin to be applied to all sides of an item
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
    @Px private var margin: Int,
    private var columnProvider: ColumnProvider,
    private var orientation: Int = RecyclerView.VERTICAL,
    private var inverted: Boolean = false,
    private var decorationLookup: DecorationLookup? = null
) : AbstractMarginDecoration(decorationLookup) {

    fun getMargin() = margin

    fun getOrientation() = orientation

    fun isInverted() = inverted

    /**
     * @param margin padding to be applied to all sides of an item
     */
    fun setMargin(margin: Int) {
        this.margin = margin
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
        val columns = columnProvider.getNumberOfColumns()
        val columnIndex = position.rem(columns)

        val lines = Math.ceil(layoutManager.itemCount / columns.toDouble()).toInt()
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
        val startPadding = margin * ((columns - columnIndex) / columns.toFloat())
        val endPadding = margin * ((columnIndex + 1) / columns.toFloat())
        outRect.left = startPadding.toInt()
        outRect.right = endPadding.toInt()

        if (lineIndex == 0) {
            if (!inverted) {
                outRect.top = margin
                if (lines > 1) {
                    outRect.bottom = margin / 2
                } else {
                    outRect.bottom = margin
                }
            } else {
                outRect.bottom = margin
                if (lines > 1) {
                    outRect.top = margin / 2
                } else {
                    outRect.top = margin
                }
            }
        } else if (lineIndex == lines - 1) {
            if (!inverted) {
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
        columns: Int,
        columnIndex: Int,
        lines: Int,
        lineIndex: Int
    ) {
        val startPadding = margin * ((columns - columnIndex) / columns.toFloat())
        val endPadding = margin * ((columnIndex + 1) / columns.toFloat())

        outRect.top = startPadding.toInt()
        outRect.bottom = endPadding.toInt()

        if (lineIndex == 0) {
            if (!inverted) {
                outRect.left = margin
                if (lines > 1) {
                    outRect.right = margin / 2
                } else {
                    outRect.right = margin
                }
            } else {
                outRect.right = margin
                if (lines > 1) {
                    outRect.left = margin / 2
                } else {
                    outRect.left = margin
                }
            }
        } else if (lineIndex == lines - 1) {
            if (!inverted) {
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
