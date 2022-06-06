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
import androidx.recyclerview.widget.RecyclerView

/**
 * A base [RecyclerView.ItemDecoration] that checks if item offsets should be applied
 * for a given position using [DecorationLookup]
 */
abstract class AbstractMarginDecoration(private var decorationLookup: DecorationLookup?) :
    RecyclerView.ItemDecoration() {

    final override fun getItemOffsets(
        outRect: Rect,
        view: View,
        parent: RecyclerView,
        state: RecyclerView.State
    ) {
        val lm = parent.layoutManager as RecyclerView.LayoutManager
        val layoutParams = view.layoutParams as RecyclerView.LayoutParams
        val position = layoutParams.absoluteAdapterPosition
        if (shouldApplyDecorationAt(position, lm.itemCount)) {
            getItemOffsets(outRect, view, position, parent, state, lm)
        }
    }

    /**
     * @param decorationLookup an optional [DecorationLookup] to filter positions
     * that shouldn't have this decoration applied to
     */
    fun setDecorationLookup(decorationLookup: DecorationLookup?) {
        this.decorationLookup = decorationLookup
    }

    /**
     * @return true if decoration will be applied for [position]
     * or false if position is not valid
     * or [decorationLookup] doesn't allow decoration for this [position]
     */
    fun shouldApplyDecorationAt(position: Int, itemCount: Int): Boolean {
        if (position == RecyclerView.NO_POSITION) {
            return false
        }
        return decorationLookup?.shouldApplyDecoration(position, itemCount) ?: true
    }

    abstract fun getItemOffsets(
        outRect: Rect,
        view: View,
        position: Int,
        parent: RecyclerView,
        state: RecyclerView.State,
        layoutManager: RecyclerView.LayoutManager
    )

}
