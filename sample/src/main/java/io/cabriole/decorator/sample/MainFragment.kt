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

package io.cabriole.decorator.sample

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import io.cabriole.decorator.sample.databinding.ScreenOptionsBinding
import io.cabriole.decorator.sample.model.OptionModel

class MainFragment : Fragment(R.layout.screen_options) {

    private var _binding: ScreenOptionsBinding? = null
    private val binding get() = _binding!!

    private lateinit var listController: MainListController

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        _binding = ScreenOptionsBinding.bind(view)

        listController = MainListController(this)
        listController.setup(binding.recyclerView)

        loadOptions()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun loadOptions() {
        val optionList = mutableListOf<OptionModel>()
        optionList.add(
            OptionModel(
                DecorationFragment.ARG_LINEAR_DECORATION,
                R.string.decorator_linear_margin,
                R.string.decorator_linear_margin_summary
            )
        )
        optionList.add(
            OptionModel(
                DecorationFragment.ARG_GRID_DECORATION,
                R.string.decorator_grid_margin,
                R.string.decorator_grid_margin_summary
            )
        )
        optionList.add(
            OptionModel(
                DecorationFragment.ARG_GRID_SPAN_DECORATION,
                R.string.decorator_grid_span_margin,
                R.string.decorator_grid_span_margin_summary
            )
        )
        optionList.add(
            OptionModel(
                DecorationFragment.ARG_LINEAR_DIVIDER_DECORATION,
                R.string.decorator_linear_divider,
                R.string.decorator_linear_divider_summary
            )
        )
        optionList.add(
            OptionModel(
                DecorationFragment.ARG_GRID_DIVIDER_DECORATION,
                R.string.decorator_grid_divider,
                R.string.decorator_grid_divider_summary
            )
        )
        optionList.add(
            OptionModel(
                DecorationFragment.ARG_LINEAR_BOUNDS_DECORATION,
                R.string.decorator_linear_bounds_margin,
                R.string.decorator_linear_bounds_margin_summary
            )
        )
        optionList.add(
            OptionModel(
                DecorationFragment.ARG_GRID_BOUNDS_DECORATION,
                R.string.decorator_grid_bounds_margin,
                R.string.decorator_grid_bounds_margin_summary
            )
        )
        listController.submitList(optionList)
    }

}
