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

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.rubensousa.decorator.sample.DecorationFragmentArgs
import com.rubensousa.decorator.sample.R
import com.rubensousa.decorator.sample.databinding.ScreenDecorationBinding

class DecorationFragment : Fragment(R.layout.screen_decoration),
    DecorationOptionController.OnOptionChangedListener {

    companion object {

        const val ARG_LINEAR_DECORATION = 0
        const val ARG_GRID_DECORATION = 1
        const val ARG_GRID_SPAN_DECORATION = 2
        const val ARG_LINEAR_DIVIDER_DECORATION = 3
        const val ARG_GRID_DIVIDER_DECORATION = 4
        const val ARG_LINEAR_BOUNDS_DECORATION = 5
        const val ARG_GRID_BOUNDS_DECORATION = 6
        const val ARG_GRID_SPAN_BOUNDS_DECORATION = 7
    }

    private val binding get() = _binding!!
    private val args by navArgs<DecorationFragmentArgs>()
    private var _binding: ScreenDecorationBinding? = null
    private lateinit var listController: DecorationListController
    private lateinit var optionController: DecorationOptionController

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        _binding = ScreenDecorationBinding.bind(view)

        binding.toolbar.setTitle(args.decorationTitleResource)
        binding.toolbar.setNavigationOnClickListener {
            findNavController().navigateUp()
        }

        listController = DecorationListController(this, args.decorationType)
        listController.setup(recyclerView = binding.decorationRecyclerView)

        optionController = DecorationOptionController(this, this)
        optionController.setupSwitches(
            orientationSwitch = binding.orientationSwitch,
            inversionSwitch = binding.invertedSwitch
        )
        optionController.setupSeekBars(
            delegate = listController.getDelegate(),
            sizeSeekBar = binding.sizeSeekBar,
            sizeTextView = binding.paddingTextView,
            widthMarginSeekBar = binding.widthMarginSeekBar,
            widthMarginTextView = binding.marginTextView,
            heightMarginSeekBar = binding.heightMarginSeekBar,
            heightMarginTextView = binding.heightMarginTextView
        )
    }

    override fun onDestroyView() {
        super.onDestroyView()
        listController.onDestroyView()
        _binding = null
    }

    override fun onInvertedChanged(inverted: Boolean) {
        listController.setInverted(inverted)
    }

    override fun onOrientationChanged(isVertical: Boolean) {
        listController.setVertical(isVertical)
    }

    override fun onDecorationSizeChanged(size: Int) {
        listController.setDecorationSize(size)
    }

    override fun onDecorationWidthMarginChanged(margin: Int) {
        listController.setWidthMargin(margin)
    }

    override fun onDecorationHeightMarginChanged(margin: Int) {
        listController.setHeightMargin(margin)
    }

}
