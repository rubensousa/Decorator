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
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import io.cabriole.decorator.LinearBoundsMarginDecoration
import io.cabriole.decorator.sample.extensions.dpToPx

class LinearBoundsDelegate(private val resources: Resources) : DecorationDelegate() {

    private var decoration = LinearBoundsMarginDecoration.create(resources.dpToPx(8))

    override fun getSize(): Int = decoration.getLeftMargin()

    override fun createLayoutManager(context: Context): RecyclerView.LayoutManager {
        return LinearLayoutManager(context, decoration.getOrientation(), decoration.isInverted())
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

}