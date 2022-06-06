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
 * An item decoration that applies a margin to all sides of an item
 *
 * @param leftMargin margin to be applied at the left side of an item
 *
 * @param topMargin margin to be applied at the top side of an item
 *
 * @param rightMargin margin to be applied at the right side of an item
 *
 * @param bottomMargin margin to be applied at the bottom side of an item
 *
 * @param orientation the orientation of the LayoutManager used by the RecyclerView.
 * Default is [RecyclerView.VERTICAL]
 *
 * @param inverted true if the LayoutManager is inverted and items are laid out
 * from the bottom to the top or from the right to the left.  Default is false.
 *
 * @param decorationLookup an optional [DecorationLookup] to filter positions
 * that shouldn't have this decoration applied to
 *
 * Any property change should be followed by [RecyclerView.invalidateItemDecorations]
 */
class LinearMarginDecoration(
    @Px private var leftMargin: Int = 0,
    @Px private var topMargin: Int = 0,
    @Px private var rightMargin: Int = 0,
    @Px private var bottomMargin: Int = 0,
    private var orientation: Int = RecyclerView.VERTICAL,
    private var inverted: Boolean = false,
    private var addBeforeFirstPosition: Boolean = true,
    private var addAfterLastPosition: Boolean = true,
    decorationLookup: DecorationLookup? = null
) : AbstractMarginDecoration(decorationLookup) {

    companion object {

        /**
         * Creates a [LinearMarginDecoration] that applies the same margin
         * to the top and bottom sides
         */
        @JvmStatic
        fun createVertical(
            @Px verticalMargin: Int,
            orientation: Int = RecyclerView.VERTICAL,
            inverted: Boolean = false,
            addBeforeFirstPosition: Boolean = true,
            addAfterLastPosition: Boolean = true,
            decorationLookup: DecorationLookup? = null
        ): LinearMarginDecoration {
            return LinearMarginDecoration(
                leftMargin = 0,
                rightMargin = 0,
                topMargin = verticalMargin,
                bottomMargin = verticalMargin,
                orientation = orientation,
                inverted = inverted,
                addBeforeFirstPosition = addBeforeFirstPosition,
                addAfterLastPosition = addAfterLastPosition,
                decorationLookup = decorationLookup
            )
        }

        /**
         * Creates a [LinearMarginDecoration] that applies the same margin
         * to the left and right sides
         */
        @JvmStatic
        fun createHorizontal(
            @Px horizontalMargin: Int,
            orientation: Int = RecyclerView.HORIZONTAL,
            inverted: Boolean = false,
            addBeforeFirstPosition: Boolean = true,
            addAfterLastPosition: Boolean = true,
            decorationLookup: DecorationLookup? = null
        ): LinearMarginDecoration {
            return LinearMarginDecoration(
                leftMargin = horizontalMargin,
                rightMargin = horizontalMargin,
                topMargin = 0,
                bottomMargin = 0,
                orientation = orientation,
                inverted = inverted,
                addBeforeFirstPosition = addBeforeFirstPosition,
                addAfterLastPosition = addAfterLastPosition,
                decorationLookup = decorationLookup
            )
        }

        /**
         * Creates a [LinearMarginDecoration] that applies the same margin to all sides
         */
        @JvmStatic
        fun create(
            @Px margin: Int,
            orientation: Int = RecyclerView.VERTICAL,
            inverted: Boolean = false,
            addBeforeFirstPosition: Boolean = true,
            addAfterLastPosition: Boolean = true,
            decorationLookup: DecorationLookup? = null
        ): LinearMarginDecoration {
            return LinearMarginDecoration(
                leftMargin = margin,
                topMargin = margin,
                rightMargin = margin,
                bottomMargin = margin,
                orientation = orientation,
                inverted = inverted,
                addBeforeFirstPosition = addBeforeFirstPosition,
                addAfterLastPosition = addAfterLastPosition,
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

    fun setHorizontalMargin(margin: Int) {
        leftMargin = margin
        rightMargin = margin
    }

    fun setVerticalMargin(margin: Int) {
        topMargin = margin
        bottomMargin = margin
    }

    /**
     * @param left margin to be applied at the left side of an item
     *
     * @param top margin to be applied at the top side of an item
     *
     * @param right margin to be applied at the right side of an item
     *
     * @param bottom margin to be applied at the bottom side of an item
     */
    fun setMargin(left: Int = 0, top: Int = 0, right: Int = 0, bottom: Int = 0) {
        leftMargin = left
        topMargin = top
        rightMargin = right
        bottomMargin = bottom
    }

    /**
     * @param orientation the orientation of the LayoutManager used by the RecyclerView.
     * Default is [RecyclerView.VERTICAL]
     */
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

    private fun applyVerticalOffsets(outRect: Rect, position: Int, itemCount: Int) {
        if (position == 0) {
            if (!inverted) {
                if (position == itemCount - 1) {
                    outRect.bottom = getEdgeMargin(addAfterLastPosition, bottomMargin)
                } else {
                    outRect.bottom = bottomMargin / 2
                }
                outRect.top = getEdgeMargin(addBeforeFirstPosition, topMargin)
            } else {
                if (position == itemCount - 1) {
                    outRect.top = getEdgeMargin(addAfterLastPosition, topMargin)
                } else {
                    outRect.top = topMargin / 2
                }
                outRect.bottom = getEdgeMargin(addBeforeFirstPosition, bottomMargin)
            }
        } else if (position == itemCount - 1) {
            if (!inverted) {
                outRect.top = topMargin / 2
                outRect.bottom = getEdgeMargin(addAfterLastPosition, bottomMargin)
            } else {
                outRect.bottom = bottomMargin / 2
                outRect.top = getEdgeMargin(addAfterLastPosition, topMargin)
            }
        } else {
            outRect.top = topMargin / 2
            outRect.bottom = bottomMargin / 2
        }
        outRect.left = leftMargin
        outRect.right = rightMargin
    }

    private fun applyHorizontalOffsets(outRect: Rect, position: Int, itemCount: Int) {
        if (position == 0) {
            if (!inverted) {
                if (position == itemCount - 1) {
                    outRect.right = getEdgeMargin(addAfterLastPosition, rightMargin)
                } else {
                    outRect.right = rightMargin / 2
                }
                outRect.left = getEdgeMargin(addBeforeFirstPosition, leftMargin)
            } else {
                if (position == itemCount - 1) {
                    outRect.left = getEdgeMargin(addAfterLastPosition, leftMargin)
                } else {
                    outRect.left = leftMargin / 2
                }
                outRect.right = getEdgeMargin(addBeforeFirstPosition, rightMargin)
            }
        } else if (position == itemCount - 1) {
            if (!inverted) {
                outRect.left = leftMargin / 2
                outRect.right = getEdgeMargin(addAfterLastPosition, rightMargin)
            } else {
                outRect.right = rightMargin / 2
                outRect.left = getEdgeMargin(addAfterLastPosition, leftMargin)
            }
        } else {
            outRect.left = leftMargin / 2
            outRect.right = rightMargin / 2
        }
        outRect.top = topMargin
        outRect.bottom = bottomMargin
    }

    private fun getEdgeMargin(apply: Boolean, margin: Int): Int {
        return if (apply) {
            margin
        } else {
            0
        }
    }

}
