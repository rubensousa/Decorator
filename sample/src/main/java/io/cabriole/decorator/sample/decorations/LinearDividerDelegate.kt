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
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import io.cabriole.decorator.LinearDividerDecoration
import io.cabriole.decorator.LinearMarginDecoration
import io.cabriole.decorator.sample.R
import io.cabriole.decorator.sample.extensions.dpToPx

class LinearDividerDelegate(private val resources: Resources) : DecorationDelegate() {

    companion object {
        const val DEFAULT_SIZE = 4
        const val DEFAULT_WIDTH_MARGIN = 16
        const val DEFAULT_HEIGHT_MARGIN = 4
        const val DEFAULT_SPACE_MARGIN = 16
    }

    private var marginDecoration = LinearMarginDecoration.createHorizontal(
        resources.dpToPx(DEFAULT_SPACE_MARGIN)
    )
    private var decoration = LinearDividerDecoration.create(
        color = ResourcesCompat.getColor(resources, R.color.colorDivider, null),
        size = resources.dpToPx(getDefaultSizeDp()),
        leftMargin = resources.dpToPx(getDefaultWidthMarginDp()),
        topMargin = resources.dpToPx(getDefaultHeightMarginDp()),
        rightMargin = resources.dpToPx(getDefaultWidthMarginDp()),
        bottomMargin = resources.dpToPx(getDefaultHeightMarginDp())
    )

    override fun getSize(): Int = decoration.getDividerSize()

    override fun createLayoutManager(context: Context): RecyclerView.LayoutManager {
        return LinearLayoutManager(context, decoration.getOrientation(), decoration.isInverted())
    }

    override fun getExtraDecorations(): List<RecyclerView.ItemDecoration> {
        return listOf(marginDecoration)
    }

    override fun getDecoration(): RecyclerView.ItemDecoration {
        return decoration
    }

    override fun setInverted(inverted: Boolean) {
        decoration.setInverted(inverted)
        marginDecoration.setInverted(inverted)
    }

    override fun setOrientation(orientation: Int) {
        decoration.setOrientation(orientation)
        marginDecoration.setOrientation(orientation)
        if (orientation == RecyclerView.VERTICAL) {
            marginDecoration.setHorizontalMargin(resources.dpToPx(DEFAULT_SPACE_MARGIN))
            marginDecoration.setVerticalMargin(0)
        } else {
            marginDecoration.setHorizontalMargin(0)
            marginDecoration.setVerticalMargin(resources.dpToPx(DEFAULT_SPACE_MARGIN))
        }
    }

    override fun setSize(size: Int) {
        decoration.setDividerSize(size)
    }

    override fun setVerticalMargin(margin: Int) {
        if (decoration.getOrientation() == RecyclerView.VERTICAL) {
            decoration.setVerticalMargin(margin)
        } else {
            decoration.setHorizontalMargin(margin)
        }
    }

    override fun setHorizontalMargin(margin: Int) {
        if (decoration.getOrientation() == RecyclerView.VERTICAL) {
            decoration.setHorizontalMargin(margin)
        } else {
            decoration.setVerticalMargin(margin)
        }
    }

    override fun hasVerticalAndHorizontalMargin(): Boolean = true

    override fun getDefaultSizeDp(): Int = DEFAULT_SIZE

    override fun getDefaultWidthMarginDp(): Int = DEFAULT_WIDTH_MARGIN

    override fun getDefaultHeightMarginDp(): Int = DEFAULT_HEIGHT_MARGIN

    override fun getDefaultMaxWidthMarginDp(): Int = 64


}
