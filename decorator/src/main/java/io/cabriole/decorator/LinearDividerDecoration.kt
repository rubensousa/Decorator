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
 * @param leftMargin the left margin in pixels of the divider
 *
 * @param topMargin the top margin in pixels of the divider
 *
 * @param rightMargin the right margin in pixels of the divider
 *
 * @param bottomMargin the bottom margin in pixels of the divider
 *
 * @param orientation the orientation of the RecyclerView. Default is [RecyclerView.VERTICAL]
 *
 * @param inverted true if the LayoutManager is inverted and items are laid out
 * from the bottom to the top or from the right to the left. Default is false.
 *
 * @param decorationLookup an optional [DecorationLookup] to filter positions
 * that shouldn't have this decoration applied to
 *
 * Any property change should be followed by [RecyclerView.invalidateItemDecorations]
 */
class LinearDividerDecoration(
    private var paint: Paint,
    @Px private var size: Int,
    @Px private var leftMargin: Int = 0,
    @Px private var topMargin: Int = 0,
    @Px private var rightMargin: Int = 0,
    @Px private var bottomMargin: Int = 0,
    private var orientation: Int = RecyclerView.VERTICAL,
    private var inverted: Boolean = false,
    private var decorationLookup: DecorationLookup? = null
) : AbstractMarginDecoration(decorationLookup) {

    companion object {

        /**
         * Creates a [LinearDividerDecoration] using a color
         */
        @JvmStatic
        fun create(
            @ColorInt color: Int,
            size: Int,
            leftMargin: Int = 0,
            topMargin: Int = 0,
            rightMargin: Int = 0,
            bottomMargin: Int = 0,
            orientation: Int = RecyclerView.VERTICAL,
            inverted: Boolean = false,
            decorationLookup: DecorationLookup? = null
        ): LinearDividerDecoration {
            val paint = Paint()
            paint.isAntiAlias = true
            paint.isDither = true
            paint.color = color
            return LinearDividerDecoration(
                paint,
                size,
                leftMargin,
                topMargin,
                rightMargin,
                bottomMargin,
                orientation,
                inverted,
                decorationLookup
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

    fun setMargin(left: Int = 0, top: Int = 0, right: Int = 0, bottom: Int = 0) {
        leftMargin = left
        topMargin = top
        rightMargin = right
        bottomMargin = bottom
    }

    fun getLeftMargin() = leftMargin

    fun getTopMargin() = topMargin

    fun getRightMargin() = rightMargin

    fun getBottomMargin() = bottomMargin

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
        val itemCount = layoutManager.itemCount
        if (orientation == RecyclerView.VERTICAL) {
            applyVerticalOffsets(outRect, position, itemCount)
        } else {
            applyHorizontalOffsets(outRect, position, itemCount)
        }
    }

    override fun onDraw(c: Canvas, parent: RecyclerView, state: RecyclerView.State) {
        super.onDraw(c, parent, state)
        val layoutManager = parent.layoutManager ?: return
        val itemCount = layoutManager.itemCount
        for (i in 0 until parent.childCount) {
            val child = parent.getChildAt(i)
            val adapterPosition = parent.getChildAdapterPosition(child)
            if (shouldApplyDecorationAt(adapterPosition, itemCount)) {
                if (orientation == RecyclerView.VERTICAL) {
                    drawVertical(c, child, adapterPosition, itemCount, layoutManager)
                } else {
                    drawHorizontal(c, child, adapterPosition, itemCount, layoutManager)
                }
            }

        }
    }

    private fun shouldApplyDecorationAt(position: Int, itemCount: Int): Boolean {
        if (position == RecyclerView.NO_POSITION) {
            return false
        }
        if (decorationLookup == null) {
            return true
        }

        return decorationLookup!!.shouldApplyDecoration(position, itemCount)
    }

    private fun applyVerticalOffsets(outRect: Rect, position: Int, itemCount: Int) {
        if (!inverted) {
            if (position != 0 && shouldApplyDecorationAt(position - 1, itemCount)) {
                outRect.top = size / 2 + topMargin
            }
            if (position != itemCount - 1 && shouldApplyDecorationAt(position + 1, itemCount)) {
                outRect.bottom = size / 2 + bottomMargin
            }
        } else {
            if (position != 0 && shouldApplyDecorationAt(position - 1, itemCount)) {
                outRect.bottom = size / 2 + bottomMargin
            }
            if (position != itemCount - 1 && shouldApplyDecorationAt(position + 1, itemCount)) {
                outRect.top = size / 2 + topMargin
            }
        }
    }

    private fun applyHorizontalOffsets(outRect: Rect, position: Int, itemCount: Int) {
        if (!inverted) {
            if (position != 0 && shouldApplyDecorationAt(position - 1, itemCount)) {
                outRect.left = size / 2 + leftMargin
            }
            if (position != itemCount - 1 && shouldApplyDecorationAt(position + 1, itemCount)) {
                outRect.right = size / 2 + rightMargin
            }
        } else {
            if (position != 0 && shouldApplyDecorationAt(position - 1, itemCount)) {
                outRect.right = size / 2 + rightMargin
            }
            if (position != itemCount - 1 && shouldApplyDecorationAt(position + 1, itemCount)) {
                outRect.left = size / 2 + leftMargin
            }
        }
    }

    private fun drawVertical(
        canvas: Canvas,
        view: View,
        position: Int,
        itemCount: Int,
        layoutManager: RecyclerView.LayoutManager
    ) {
        val topPosition = if (!inverted) {
            position - 1
        } else {
            position + 1
        }
        // Draw half of the top decoration
        if (shouldApplyDecorationAt(topPosition, itemCount)) {
            // Real left = decoratedLeft + dividerSize/2 + leftMargin
            // Real bottom = decoratedBottom - dividerSize/2 - bottomMargin
            canvas.drawRect(
                layoutManager.getDecoratedLeft(view).toFloat() + leftMargin,
                layoutManager.getDecoratedTop(view).toFloat(),
                layoutManager.getDecoratedRight(view).toFloat() - rightMargin,
                layoutManager.getDecoratedTop(view).toFloat() + size / 2,
                paint
            )
        }

        val bottomPosition = if (!inverted) {
            position + 1
        } else {
            position - 1
        }
        // Draw half of the bottom decoration
        if (shouldApplyDecorationAt(bottomPosition, itemCount)) {
            canvas.drawRect(
                layoutManager.getDecoratedLeft(view).toFloat() + leftMargin,
                layoutManager.getDecoratedBottom(view).toFloat() - size / 2,
                layoutManager.getDecoratedRight(view).toFloat() - rightMargin,
                layoutManager.getDecoratedBottom(view).toFloat(),
                paint
            )
        }
    }

    private fun drawHorizontal(
        canvas: Canvas,
        view: View,
        position: Int,
        itemCount: Int,
        layoutManager: RecyclerView.LayoutManager
    ) {
        val leftPosition = if (!inverted) {
            position - 1
        } else {
            position + 1
        }
        // Draw half of the left decoration
        if (shouldApplyDecorationAt(leftPosition, itemCount)) {
            canvas.drawRect(
                layoutManager.getDecoratedLeft(view).toFloat(),
                layoutManager.getDecoratedTop(view).toFloat() + topMargin,
                layoutManager.getDecoratedLeft(view).toFloat() + size / 2,
                layoutManager.getDecoratedBottom(view).toFloat() - bottomMargin,
                paint
            )
        }

        val rightPosition = if (!inverted) {
            position + 1
        } else {
            position - 1
        }

        // Draw half of the right decoration
        if (shouldApplyDecorationAt(rightPosition, itemCount)) {
            canvas.drawRect(
                layoutManager.getDecoratedRight(view).toFloat() - size / 2,
                layoutManager.getDecoratedTop(view).toFloat() + topMargin,
                layoutManager.getDecoratedRight(view).toFloat(),
                layoutManager.getDecoratedBottom(view).toFloat() - bottomMargin,
                paint
            )
        }

    }

}
