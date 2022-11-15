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

package com.rubensousa.decorator.sample

import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.DefaultItemAnimator
import androidx.recyclerview.widget.RecyclerView
import com.rubensousa.decorator.sample.adapter.CardAdapter
import com.rubensousa.decorator.sample.decorations.DecorationDelegate
import com.rubensousa.decorator.sample.decorations.GridBoundsDelegate
import com.rubensousa.decorator.sample.decorations.GridDividerDelegate
import com.rubensousa.decorator.sample.decorations.GridMarginDelegate
import com.rubensousa.decorator.sample.decorations.GridSpanBoundsDelegate
import com.rubensousa.decorator.sample.decorations.GridSpanDelegate
import com.rubensousa.decorator.sample.decorations.LinearBoundsDelegate
import com.rubensousa.decorator.sample.decorations.LinearDividerDelegate
import com.rubensousa.decorator.sample.decorations.LinearMarginDelegate

class DecorationListController(
    private val fragment: Fragment,
    private val decorationType: Int
) {

    private val decorations = mutableMapOf<Int, DecorationDelegate>()
    private var recyclerView: RecyclerView? = null

    init {
        createDelegates()
    }

    private fun createDelegates() {
        decorations[DecorationFragment.ARG_LINEAR_DECORATION] =
            LinearMarginDelegate(fragment.resources)
        decorations[DecorationFragment.ARG_LINEAR_BOUNDS_DECORATION] =
            LinearBoundsDelegate(fragment.resources)
        decorations[DecorationFragment.ARG_LINEAR_DIVIDER_DECORATION] =
            LinearDividerDelegate(fragment.resources)
        decorations[DecorationFragment.ARG_GRID_DECORATION] = GridMarginDelegate(fragment.resources)
        decorations[DecorationFragment.ARG_GRID_BOUNDS_DECORATION] =
            GridBoundsDelegate(fragment.resources)
        decorations[DecorationFragment.ARG_GRID_SPAN_DECORATION] =
            GridSpanDelegate(fragment.requireActivity())
        decorations[DecorationFragment.ARG_GRID_SPAN_BOUNDS_DECORATION] =
            GridSpanBoundsDelegate(fragment.requireActivity())
        decorations[DecorationFragment.ARG_GRID_DIVIDER_DECORATION] =
            GridDividerDelegate(fragment.resources)
    }

    fun getDelegate(): DecorationDelegate {
        return decorations[decorationType]
            ?: throw IllegalStateException("Delegate not found for $decorationType")
    }

    fun setup(recyclerView: RecyclerView) {
        val delegate = getDelegate()
        val itemAnimator = recyclerView.itemAnimator as DefaultItemAnimator
        itemAnimator.removeDuration = 3000
        recyclerView.layoutManager = delegate.createLayoutManager(fragment.requireActivity())
        val decorations = delegate.getExtraDecorations()
        recyclerView.addItemDecoration(delegate.getDecoration())
        decorations.forEach { decoration ->
            recyclerView.addItemDecoration(decoration)
        }
        val adapter = CardAdapter(recyclerView, R.layout.list_card)
        recyclerView.adapter = adapter
        this.recyclerView = recyclerView
        adapter.setItems(getDelegate().getNumberOfItems())
    }

    fun setInverted(inverted: Boolean) {
        val delegate = getDelegate()
        delegate.setInverted(inverted)
        recyclerView?.layoutManager = delegate.createLayoutManager(fragment.requireActivity())
        recyclerView?.invalidateItemDecorations()
    }

    fun setVertical(isVertical: Boolean) {
        val delegate = getDelegate()

        val orientation = if (isVertical) {
            RecyclerView.VERTICAL
        } else {
            RecyclerView.HORIZONTAL
        }

        delegate.setOrientation(orientation)

        val layoutId = if (isVertical) {
            R.layout.list_card
        } else {
            R.layout.list_card_horizontal
        }

        val adapter = CardAdapter(recyclerView!!, layoutId)
        recyclerView?.layoutManager = delegate.createLayoutManager(fragment.requireActivity())
        recyclerView?.adapter = adapter
        adapter.setItems(getDelegate().getNumberOfItems())
        recyclerView?.invalidateItemDecorations()
    }

    fun setDecorationSize(size: Int) {
        getDelegate().setSize(size)
        recyclerView?.invalidateItemDecorations()
    }

    fun setWidthMargin(margin: Int) {
        getDelegate().setHorizontalMargin(margin)
        recyclerView?.invalidateItemDecorations()
    }

    fun setHeightMargin(margin: Int) {
        getDelegate().setVerticalMargin(margin)
        recyclerView?.invalidateItemDecorations()
    }

    fun onDestroyView() {
        recyclerView = null
    }

}
