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

import android.graphics.Canvas
import android.graphics.Paint
import android.graphics.Rect
import android.view.View
import androidx.annotation.ColorInt
import androidx.annotation.Px
import androidx.recyclerview.widget.RecyclerView

/**
 * A [RecyclerView.ItemDecoration] that draws a divider between items.
 *
 * @param paint the paint used to draw in the canvas
 *
 * @param size the size in pixels of the section to be drawn,
 * height if [orientation] is [RecyclerView.VERTICAL]
 * or width if [orientation] is [RecyclerView.HORIZONTAL]
 *
 * @param widthMargin the margin in pixels between the divider and a view
 *
 * @param heightMargin the margin in pixels between the divider's top and the view's top
 * and the divider's bottom and view's bottom for the vertical orientation
 *
 * @param orientation the orientation of the RecyclerView. Default is [RecyclerView.VERTICAL]
 *
 * @param decorationLookup an optional [DecorationLookup] to filter positions
 * that shouldn't have this decoration applied to
 *
 * Any property change should be followed by [RecyclerView.invalidateItemDecorations]
 */
class GridDividerDecoration(
    private var paint: Paint,
    @Px private var size: Int,
    @Px private var widthMargin: Int = 0,
    @Px private var heightMargin: Int = 0,
    private val columnProvider: ColumnProvider,
    private var orientation: Int = RecyclerView.VERTICAL,
    private var inverted: Boolean = false,
    private var decorationLookup: DecorationLookup? = null
) : AbstractMarginDecoration(decorationLookup) {

    companion object {

        /**
         * Creates a [GridDividerDecoration] using a color
         */
        fun create(
            @ColorInt color: Int,
            size: Int,
            columnProvider: ColumnProvider,
            widthMargin: Int = 0,
            heightMargin: Int = 0,
            orientation: Int = RecyclerView.VERTICAL,
            inverted: Boolean = false
        ): GridDividerDecoration {
            val paint = Paint()
            paint.isAntiAlias = true
            paint.isDither = true
            paint.color = color
            return GridDividerDecoration(
                paint,
                size,
                widthMargin,
                heightMargin,
                columnProvider,
                orientation,
                inverted
            )
        }

    }

    fun setPaint(paint: Paint) {
        this.paint = paint
    }

    fun setDividerSize(@Px size: Int) {
        this.size = size
    }

    fun getDividerSize() = size

    fun setHeightMargin(margin: Int) {
        this.heightMargin = margin
    }

    fun getHeightMargin() = heightMargin

    fun setWidthMargin(margin: Int) {
        this.widthMargin = margin
    }

    fun getWidthMargin() = widthMargin

    fun setOrientation(orientation: Int) {
        this.orientation = orientation
    }

    fun getOrientation() = orientation

    /**
     * @param inverted true if the LayoutManager is inverted and items are laid out
     * from the bottom to the top or from the right to the left.
     */
    fun setInverted(inverted: Boolean) {
        this.inverted = inverted
    }

    fun isInverted() = inverted

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
        val itemCount = layoutManager.itemCount
        if (orientation == RecyclerView.VERTICAL) {
            applyVerticalOffsets(outRect, position, columns, itemCount)
        } else {
            applyHorizontalOffsets(outRect, position, columns, itemCount)
        }
    }

    override fun onDraw(c: Canvas, parent: RecyclerView, state: RecyclerView.State) {
        super.onDraw(c, parent, state)
        val layoutManager = parent.layoutManager ?: return
        val columns = columnProvider.getNumberOfColumns()
        val itemCount = layoutManager.itemCount
        for (i in 0 until parent.childCount) {
            val child = parent.getChildAt(i)
            val adapterPosition = parent.getChildAdapterPosition(child)
            if (canApplyDecorationAt(adapterPosition, itemCount)) {
                if (orientation == RecyclerView.VERTICAL) {
                    drawVertical(c, child, adapterPosition, itemCount, columns, layoutManager)
                } else {
                    drawHorizontal(c, child, adapterPosition, itemCount, columns, layoutManager)
                }
            }
        }
    }

    private fun canApplyDecorationAt(position: Int, itemCount: Int): Boolean {
        if (position == RecyclerView.NO_POSITION) {
            return false
        }
        if (decorationLookup == null) {
            return true
        }

        return decorationLookup!!.shouldApplyDecoration(position, itemCount)
    }

    private fun applyVerticalOffsets(outRect: Rect, position: Int, columns: Int, itemCount: Int) {
        val columnIndex = position.rem(columns)
        val lines = Math.ceil(itemCount / columns.toDouble()).toInt()
        val lineIndex = position / columns

        val margin = size + widthMargin * 2
        val halfMargin = Math.ceil(margin / 2.0).toInt()
        val startMargin = (margin * ((columns - columnIndex) / columns.toDouble())).toInt()
        val endMargin = (margin * ((columnIndex + 1) / columns.toDouble())).toInt()

        outRect.left = startMargin
        outRect.right = endMargin

        if (!inverted) {
            if (lineIndex == 0) {
                outRect.top = 0
            } else {
                outRect.top = halfMargin
            }
            if (lineIndex == lines - 1) {
                outRect.bottom = 0
            } else {
                outRect.bottom = halfMargin
            }
        } else {
            if (lineIndex == 0) {
                outRect.bottom = 0
            } else {
                outRect.bottom = halfMargin
            }
            if (lineIndex == lines - 1) {
                outRect.top = 0
            } else {
                outRect.top = halfMargin
            }
        }
    }

    private fun applyHorizontalOffsets(outRect: Rect, position: Int, columns: Int, itemCount: Int) {
        val columnIndex = position.rem(columns)
        val lines = Math.ceil(itemCount / columns.toDouble()).toInt()
        val lineIndex = position / columns

        val margin = size + widthMargin * 2
        val halfMargin = Math.ceil(margin / 2.0).toInt()
        val startMargin = (margin * ((columns - columnIndex) / columns.toDouble())).toInt()
        val endMargin = (margin * ((columnIndex + 1) / columns.toDouble())).toInt()

        outRect.top = startMargin
        outRect.bottom = endMargin

        if (!inverted) {
            if (lineIndex == 0) {
                outRect.left = 0
            } else {
                outRect.left = halfMargin
            }
            if (lineIndex == lines - 1) {
                outRect.right = 0
            } else {
                outRect.right = halfMargin
            }
        } else {
            if (lineIndex == 0) {
                outRect.right = 0
            } else {
                outRect.right = halfMargin
            }
            if (lineIndex == lines - 1) {
                outRect.left = 0
            } else {
                outRect.left = halfMargin
            }
        }
    }

    private fun getLeftPosition(position: Int, columns: Int): Int? {
        val column = position.rem(columns)
        if (column - 1 < 0) {
            return null
        }
        return position - 1
    }

    private fun getTopPosition(position: Int, columns: Int): Int? {
        if (position - columns < 0) {
            return null
        }
        return position - columns
    }

    private fun getRightPosition(position: Int, columns: Int, itemCount: Int): Int? {
        val column = position.rem(columns)
        if (column + 1 == columns) {
            return null
        }
        if (position + 1 >= itemCount) {
            return null
        }
        return position + 1
    }

    private fun getBottomPosition(position: Int, columns: Int, itemCount: Int): Int? {
        if (position + columns >= itemCount) {
            return null
        }
        return position + columns
    }

    /**
     * Check if the views at the bottom and right should have dividers.
     * If that's the case, draw the divider between those views
     */
    private fun drawVertical(
        canvas: Canvas,
        view: View,
        position: Int,
        itemCount: Int,
        columns: Int,
        layoutManager: RecyclerView.LayoutManager
    ) {
        val bottomPosition = if (!inverted) {
            getBottomPosition(position, columns, itemCount)
        } else {
            getTopPosition(position, columns)
        }
        if (bottomPosition != null && canApplyDecorationAt(bottomPosition, itemCount)) {
            canvas.drawRect(
                view.left.toFloat() + heightMargin,
                view.bottom.toFloat() + widthMargin,
                view.right.toFloat() - heightMargin,
                view.bottom.toFloat() + size + widthMargin,
                paint
            )
        }

        val rightPosition = getRightPosition(position, columns, itemCount)
        if (rightPosition != null && canApplyDecorationAt(rightPosition, itemCount)) {
            canvas.drawRect(
                view.right.toFloat() + widthMargin,
                view.top.toFloat() + heightMargin,
                view.right.toFloat() + widthMargin + size,
                view.bottom.toFloat() - heightMargin,
                paint
            )
        }
    }

    private fun drawHorizontal(
        canvas: Canvas,
        view: View,
        position: Int,
        itemCount: Int,
        columns: Int,
        layoutManager: RecyclerView.LayoutManager
    ) {
        val bottomPosition = if (!inverted) {
            getBottomPosition(position, columns, itemCount)
        } else {
            getTopPosition(position, columns)
        }
        if (bottomPosition != null && canApplyDecorationAt(bottomPosition, itemCount)) {
            canvas.drawRect(
                view.right.toFloat() + widthMargin,
                view.top.toFloat() + heightMargin,
                view.right.toFloat() + widthMargin + size,
                view.bottom.toFloat() - heightMargin,
                paint
            )
        }

        val rightPosition = getRightPosition(position, columns, itemCount)
        if (rightPosition != null && canApplyDecorationAt(rightPosition, itemCount)) {
            canvas.drawRect(
                view.left.toFloat() + heightMargin,
                view.bottom.toFloat() + widthMargin,
                view.right.toFloat() - heightMargin,
                view.bottom.toFloat() + widthMargin + size,
                paint
            )
        }
    }

}
