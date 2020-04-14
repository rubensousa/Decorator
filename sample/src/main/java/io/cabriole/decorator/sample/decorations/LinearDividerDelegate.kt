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
    }

    private var marginDecoration = LinearMarginDecoration.create(resources.dpToPx(16))
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
    }

    override fun setSize(size: Int) {
        decoration.setDividerSize(size)
    }

    override fun setHeightMargin(heightMargin: Int) {
        if (decoration.getOrientation() == RecyclerView.VERTICAL) {
            decoration.setMargin(
                top = heightMargin,
                bottom = heightMargin,
                left = decoration.getLeftMargin(),
                right = decoration.getRightMargin()
            )
        } else {
            decoration.setMargin(
                left = heightMargin,
                right = heightMargin,
                top = decoration.getTopMargin(),
                bottom = decoration.getBottomMargin()
            )
        }
    }

    override fun setWidthMargin(widthMargin: Int) {
        if (decoration.getOrientation() == RecyclerView.VERTICAL) {
            decoration.setMargin(
                left = widthMargin,
                right = widthMargin,
                top = decoration.getTopMargin(),
                bottom = decoration.getBottomMargin()
            )
        } else {
            decoration.setMargin(
                left = decoration.getLeftMargin(),
                right = decoration.getRightMargin(),
                top = widthMargin,
                bottom = widthMargin
            )
        }
    }

    override fun hasWidthOrHeightMargin(): Boolean = true

    override fun getDefaultSizeDp(): Int = DEFAULT_SIZE

    override fun getDefaultWidthMarginDp(): Int = DEFAULT_WIDTH_MARGIN

    override fun getDefaultHeightMarginDp(): Int = DEFAULT_HEIGHT_MARGIN

    override fun getDefaultMaxWidthMarginDp(): Int = 64


}
