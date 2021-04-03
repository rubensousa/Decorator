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

package com.rubensousa.decorator.sample.decorations

import android.content.Context
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.rubensousa.decorator.GridSpanBoundsMarginDecoration
import com.rubensousa.decorator.sample.extensions.dpToPx

class GridSpanBoundsDelegate(private val context: Context) : DecorationDelegate() {

    companion object {
        const val DEFAULT_COLUMNS = 4
    }

    private var decoration = GridSpanBoundsMarginDecoration(
        leftMargin = context.resources.dpToPx(getDefaultSizeDp()),
        topMargin = context.resources.dpToPx(getDefaultSizeDp()),
        rightMargin = context.resources.dpToPx(getDefaultSizeDp()),
        bottomMargin = context.resources.dpToPx(getDefaultSizeDp()),
        gridLayoutManager = GridLayoutManager(context, DEFAULT_COLUMNS)
    )

    override fun createLayoutManager(context: Context): RecyclerView.LayoutManager {
        val layoutManager = GridLayoutManager(
            context,
            DEFAULT_COLUMNS,
            decoration.getGridLayoutManager().orientation,
            decoration.getGridLayoutManager().reverseLayout
        )
        layoutManager.spanSizeLookup = object : GridLayoutManager.SpanSizeLookup() {
            override fun getSpanSize(position: Int): Int {
                if (position.rem(5) == 0) {
                    return DEFAULT_COLUMNS
                } else {
                    return 1
                }
            }
        }
        decoration.setGridLayoutManager(layoutManager)
        return layoutManager
    }

    override fun getDecoration(): RecyclerView.ItemDecoration {
        return decoration
    }

    override fun setInverted(inverted: Boolean) {
        decoration.getGridLayoutManager().reverseLayout = inverted
    }

    override fun setOrientation(orientation: Int) {
        decoration.getGridLayoutManager().orientation = orientation
    }

    override fun setSize(size: Int) {
        decoration.setMargin(size)
    }

    override fun getSize(): Int = decoration.getLeftMargin()

    override fun getNumberOfItems(): Int = DEFAULT_COLUMNS * 10

}
