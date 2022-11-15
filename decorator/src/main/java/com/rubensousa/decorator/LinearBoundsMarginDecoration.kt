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

/**
 * An item decoration that applies margin to the bounds of the RecyclerView
 *
 * @param leftMargin margin to be applied at the left side of an item
 *
 * @param topMargin margin to be applied at the top side of an item
 *
 * @param rightMargin margin to be applied at the right side of an item
 *
 * @param bottomMargin margin to be applied at the bottom side of an item
 *
 * @param orientation the orientation of the RecyclerView. Default is [RecyclerView.VERTICAL]
 *
 * @param inverted true if the LayoutManager is inverted and items are laid out
 * from the bottom to the top or from the right to the left.
 *
 * @param decorationLookup an optional [DecorationLookup] to filter positions
 * that shouldn't have this decoration applied to
 *
 * Any property change should be followed by [RecyclerView.invalidateItemDecorations]
 */
class LinearBoundsMarginDecoration(
    @Px private var leftMargin: Int = 0,
    @Px private var topMargin: Int = 0,
    @Px private var rightMargin: Int = 0,
    @Px private var bottomMargin: Int = 0,
    private var orientation: Int = RecyclerView.VERTICAL,
    private var inverted: Boolean = false,
    decorationLookup: DecorationLookup? = null
) : AbstractMarginDecoration(decorationLookup) {

    companion object {

        /**
         * Creates a [LinearBoundsMarginDecoration] that applies the same margin
         * to the top and bottom sides
         */
        @JvmStatic
        fun createVertical(
            @Px verticalMargin: Int,
            orientation: Int = RecyclerView.VERTICAL,
            inverted: Boolean = false,
            decorationLookup: DecorationLookup? = null
        ): LinearBoundsMarginDecoration {
            return LinearBoundsMarginDecoration(
                leftMargin = 0,
                rightMargin = 0,
                topMargin = verticalMargin,
                bottomMargin = verticalMargin,
                orientation = orientation,
                inverted = inverted,
                decorationLookup = decorationLookup
            )
        }

        /**
         * Creates a [LinearBoundsMarginDecoration] that applies the same margin
         * to the left and right sides
         */
        @JvmStatic
        fun createHorizontal(
            @Px horizontalMargin: Int,
            orientation: Int = RecyclerView.HORIZONTAL,
            inverted: Boolean = false,
            decorationLookup: DecorationLookup? = null
        ): LinearBoundsMarginDecoration {
            return LinearBoundsMarginDecoration(
                leftMargin = horizontalMargin,
                rightMargin = horizontalMargin,
                topMargin = 0,
                bottomMargin = 0,
                orientation = orientation,
                inverted = inverted,
                decorationLookup = decorationLookup
            )
        }

        /**
         * Creates a [LinearBoundsMarginDecoration] that applies the same margin to all sides
         */
        @JvmStatic
        fun create(
            @Px margin: Int,
            orientation: Int = RecyclerView.VERTICAL,
            inverted: Boolean = false,
            decorationLookup: DecorationLookup? = null
        ): LinearBoundsMarginDecoration {
            return LinearBoundsMarginDecoration(
                leftMargin = margin,
                topMargin = margin,
                rightMargin = margin,
                bottomMargin = margin,
                orientation = orientation,
                inverted = inverted,
                decorationLookup = decorationLookup
            )
        }
    }

    fun getLeftMargin() = leftMargin

    fun getTopMargin() = topMargin

    fun getRightMargin() = rightMargin

    fun getBottomMargin() = bottomMargin

    fun getOrientation() = orientation

    fun isInverted() = inverted

    fun setMargin(margin: Int) {
        setMargin(margin, margin, margin, margin)
    }

    fun setMargin(left: Int = 0, top: Int = 0, right: Int = 0, bottom: Int = 0) {
        leftMargin = left
        topMargin = top
        rightMargin = right
        bottomMargin = bottom
    }

    fun setOrientation(orientation: Int) {
        this.orientation = orientation
    }

    /**
     * @param inverted true if the LayoutManager is inverted and items are laid out
     * from the bottom to the top or from the right to the left.
     */
    fun setInverted(inverted: Boolean) {
        this.inverted = inverted
    }

    override fun getItemOffsets(
        outRect: Rect,
        view: View,
        position: Int,
        parent: RecyclerView,
        state: RecyclerView.State
    ) {
        if (orientation == RecyclerView.VERTICAL) {
            applyVerticalOffsets(outRect, position, state.itemCount)
        } else {
            applyHorizontalOffsets(outRect, position, state.itemCount)
        }
    }

    private fun applyVerticalOffsets(outRect: Rect, position: Int, itemCount: Int) {
        outRect.left = leftMargin
        outRect.right = rightMargin

        if (position == 0) {
            if (!inverted) {
                outRect.top = topMargin
            } else {
                outRect.bottom = bottomMargin
            }
        }
        if (position == itemCount - 1) {
            if (!inverted) {
                outRect.bottom = bottomMargin
            } else {
                outRect.top = topMargin
            }
        }
    }

    private fun applyHorizontalOffsets(outRect: Rect, position: Int, itemCount: Int) {
        outRect.top = topMargin
        outRect.bottom = bottomMargin

        if (position == 0) {
            if (!inverted) {
                outRect.left = leftMargin
            } else {
                outRect.right = rightMargin
            }
        }

        if (position == itemCount - 1) {
            if (!inverted) {
                outRect.right = rightMargin
            } else {
                outRect.left = leftMargin
            }
        }
    }

}
