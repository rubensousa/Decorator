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

package com.rubensousa.decorator.sample.decorations

import android.content.Context
import androidx.recyclerview.widget.RecyclerView

abstract class DecorationDelegate {

    abstract fun getDecoration(): RecyclerView.ItemDecoration

    abstract fun createLayoutManager(context: Context): RecyclerView.LayoutManager

    abstract fun getSize(): Int

    open fun getExtraDecorations(): List<RecyclerView.ItemDecoration> {
        return emptyList()
    }

    open fun setOrientation(orientation: Int) {

    }

    open fun setInverted(inverted: Boolean) {

    }

    open fun setSize(size: Int) {

    }

    open fun setHorizontalMargin(margin: Int) {

    }

    open fun setVerticalMargin(margin: Int) {

    }

    open fun getNumberOfItems(): Int = 30

    open fun getDefaultWidthMarginDp() = 16

    open fun getDefaultMaxWidthMarginDp() = 16

    open fun getDefaultHeightMarginDp() = 16

    open fun getDefaultSizeDp() = 8

    open fun hasVerticalAndHorizontalMargin() = false

}