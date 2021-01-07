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
import androidx.core.content.res.ResourcesCompat
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import io.cabriole.decorator.ColumnProvider
import io.cabriole.decorator.GridDividerDecoration
import io.cabriole.decorator.sample.R
import io.cabriole.decorator.sample.extensions.dpToPx

class GridDividerDelegate(private val resources: Resources) : DecorationDelegate() {

    companion object {
        const val DEFAULT_COLUMNS = 3
        const val DEFAULT_SIZE = 2
        const val DEFAULT_WIDTH_MARGIN = 16
        const val DEFAULT_HEIGHT_MARGIN = 16
    }

    private var decoration = GridDividerDecoration.create(
        color = ResourcesCompat.getColor(resources, R.color.colorDivider, null),
        size = resources.dpToPx(getDefaultSizeDp()),
        columnProvider = object : ColumnProvider {
            override fun getNumberOfColumns(): Int = DEFAULT_COLUMNS
        },
        widthMargin = resources.dpToPx(getDefaultWidthMarginDp()),
        heightMargin = resources.dpToPx(getDefaultHeightMarginDp())
    )

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
        decoration.setDividerSize(size)
    }

    override fun setVerticalMargin(margin: Int) {
        decoration.setHeightMargin(margin)
    }

    override fun setHorizontalMargin(margin: Int) {
        decoration.setWidthMargin(margin)
    }

    override fun getSize(): Int = decoration.getDividerSize()

    override fun getDefaultSizeDp(): Int = DEFAULT_SIZE

    override fun getDefaultHeightMarginDp(): Int = DEFAULT_HEIGHT_MARGIN

    override fun getDefaultWidthMarginDp(): Int = DEFAULT_WIDTH_MARGIN

    override fun hasVerticalAndHorizontalMargin(): Boolean = true

    override fun getNumberOfItems(): Int = DEFAULT_COLUMNS * 10

}
