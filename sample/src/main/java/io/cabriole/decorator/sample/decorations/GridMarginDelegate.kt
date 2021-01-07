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
import android.content.res.Resources
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import io.cabriole.decorator.ColumnProvider
import io.cabriole.decorator.GridMarginDecoration
import io.cabriole.decorator.sample.extensions.dpToPx

class GridMarginDelegate(private val resources: Resources) : DecorationDelegate() {

    companion object {
        const val DEFAULT_COLUMNS = 3
    }

    private var decoration = GridMarginDecoration.create(
        margin = resources.dpToPx(getDefaultSizeDp()),
        columnProvider = object : ColumnProvider {
            override fun getNumberOfColumns(): Int = DEFAULT_COLUMNS
        }
    )

    override fun getSize(): Int = if (decoration.getOrientation() == RecyclerView.VERTICAL) {
        decoration.getVerticalMargin()
    } else {
        decoration.getHorizontalMargin()
    }

    override fun createLayoutManager(context: Context): RecyclerView.LayoutManager {
        return GridLayoutManager(
            context,
            DEFAULT_COLUMNS,
            decoration.getOrientation(),
            decoration.isInverted()
        )
    }

    override fun getDecoration(): RecyclerView.ItemDecoration {
        return decoration
    }

    override fun setInverted(inverted: Boolean) {
        decoration.setInverted(inverted)
    }

    override fun setOrientation(orientation: Int) {
        decoration.setOrientation(orientation)
    }

    override fun setSize(size: Int) {
        decoration.setMargin(size)
    }

    override fun setVerticalMargin(margin: Int) {
        decoration.setVerticalMargin(margin)
    }

    override fun setHorizontalMargin(margin: Int) {
        decoration.setHorizontalMargin(margin)
    }

    override fun getNumberOfItems(): Int = DEFAULT_COLUMNS * 10

    override fun hasVerticalAndHorizontalMargin(): Boolean {
        return true
    }

}
