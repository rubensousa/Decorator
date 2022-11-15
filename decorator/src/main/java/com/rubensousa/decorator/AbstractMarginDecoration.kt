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
import androidx.recyclerview.widget.RecyclerView.ViewHolder

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
        val layoutParams = view.layoutParams as RecyclerView.LayoutParams

        /**
         *  We need to use the layout position since it's the only valid
         *  source of truth at this stage.
         *  The item could've been removed and adapter position
         *  in that case is set to RecyclerView.NO_POSITION
         */
        val position = layoutParams.viewLayoutPosition
        val viewHolder = parent.getChildViewHolder(view)
        if (shouldApplyDecorationAt(viewHolder, state.itemCount)) {
            getItemOffsets(outRect, view, position, parent, state)
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
     * @return true if decoration will be applied for [viewHolder]
     * or false if[decorationLookup] doesn't allow decoration for this [viewHolder]
     */
    fun shouldApplyDecorationAt(viewHolder: ViewHolder, itemCount: Int): Boolean {
        return decorationLookup?.shouldApplyDecoration(viewHolder, itemCount) ?: true
    }

    abstract fun getItemOffsets(
        outRect: Rect,
        view: View,
        position: Int,
        parent: RecyclerView,
        state: RecyclerView.State
    )

}
