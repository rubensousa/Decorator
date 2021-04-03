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

import android.widget.SeekBar
import android.widget.TextView
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import com.google.android.material.switchmaterial.SwitchMaterial
import com.rubensousa.decorator.sample.decorations.DecorationDelegate
import com.rubensousa.decorator.sample.extensions.dpToPx
import com.rubensousa.decorator.sample.R

class DecorationOptionController(
    private val fragment: Fragment,
    private val onOptionChangedListener: OnOptionChangedListener
) {

    interface OnOptionChangedListener {

        fun onInvertedChanged(inverted: Boolean)

        fun onOrientationChanged(isVertical: Boolean)

        fun onDecorationSizeChanged(size: Int)

        fun onDecorationWidthMarginChanged(margin: Int)

        fun onDecorationHeightMarginChanged(margin: Int)
    }

    fun setupSwitches(orientationSwitch: SwitchMaterial, inversionSwitch: SwitchMaterial) {
        orientationSwitch.setOnCheckedChangeListener { buttonView, isChecked ->
            onOptionChangedListener.onOrientationChanged(isChecked)
        }
        inversionSwitch.setOnCheckedChangeListener { buttonView, isChecked ->
            onOptionChangedListener.onInvertedChanged(isChecked)
        }
    }

    fun setupSeekBars(
        delegate: DecorationDelegate,
        sizeSeekBar: SeekBar,
        sizeTextView: TextView,
        widthMarginSeekBar: SeekBar,
        widthMarginTextView: TextView,
        heightMarginSeekBar: SeekBar,
        heightMarginTextView: TextView
    ) {
        widthMarginSeekBar.progress = delegate.getDefaultWidthMarginDp()
        heightMarginSeekBar.progress = delegate.getDefaultHeightMarginDp()
        sizeSeekBar.progress = delegate.getDefaultSizeDp()
        widthMarginSeekBar.max = delegate.getDefaultMaxWidthMarginDp()

        sizeTextView.setText(
            fragment.getString(
                R.string.decorator_option_padding,
                sizeSeekBar.progress
            )
        )

        widthMarginTextView.setText(
            fragment.getString(
                R.string.decorator_divider_margin_width,
                widthMarginSeekBar.progress
            )
        )

        heightMarginTextView.setText(
            fragment.getString(
                R.string.decorator_divider_margin_height,
                heightMarginSeekBar.progress
            )
        )

        sizeSeekBar.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener {
            override fun onProgressChanged(seekBar: SeekBar?, progress: Int, fromUser: Boolean) {
                sizeTextView.setText(
                    fragment.getString(
                        R.string.decorator_option_padding,
                        progress
                    )
                )
                onOptionChangedListener.onDecorationSizeChanged(fragment.dpToPx(progress))
            }

            override fun onStartTrackingTouch(seekBar: SeekBar?) {
            }

            override fun onStopTrackingTouch(seekBar: SeekBar?) {
            }
        })

        widthMarginSeekBar.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener {
            override fun onProgressChanged(seekBar: SeekBar?, progress: Int, fromUser: Boolean) {
                widthMarginTextView.setText(
                    fragment.getString(
                        R.string.decorator_divider_margin_width,
                        progress
                    )
                )
                onOptionChangedListener.onDecorationWidthMarginChanged(fragment.dpToPx(progress))
            }

            override fun onStartTrackingTouch(seekBar: SeekBar?) {
            }

            override fun onStopTrackingTouch(seekBar: SeekBar?) {
            }
        })

        heightMarginSeekBar.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener {
            override fun onProgressChanged(seekBar: SeekBar?, progress: Int, fromUser: Boolean) {
                heightMarginTextView.setText(
                    fragment.getString(
                        R.string.decorator_divider_margin_height,
                        progress
                    )
                )
                onOptionChangedListener.onDecorationHeightMarginChanged(fragment.dpToPx(progress))
            }

            override fun onStartTrackingTouch(seekBar: SeekBar?) {
            }

            override fun onStopTrackingTouch(seekBar: SeekBar?) {
            }
        })


        if (!delegate.hasVerticalAndHorizontalMargin()) {
            widthMarginSeekBar.isVisible = false
            widthMarginTextView.isVisible = false
            heightMarginSeekBar.isVisible = false
            heightMarginTextView.isVisible = false
        }
    }

}