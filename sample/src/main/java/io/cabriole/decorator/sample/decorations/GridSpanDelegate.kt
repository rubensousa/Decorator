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

package io.cabriole.decorator.sample.decorations

import android.content.Context
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import io.cabriole.decorator.GridSpanMarginDecoration
import io.cabriole.decorator.sample.extensions.dpToPx

class GridSpanDelegate(private val context: Context) : DecorationDelegate() {

    companion object {
        const val DEFAULT_COLUMNS = 4
    }

    private var decoration = GridSpanMarginDecoration(
        context.resources.dpToPx(getDefaultSizeDp()),
        GridLayoutManager(context, DEFAULT_COLUMNS)
    )

    override fun getSize(): Int = decoration.getMargin()

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
                    return layoutManager.spanCount
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

    override fun getNumberOfItems(): Int = 31
}
