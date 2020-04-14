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

package io.cabriole.decorator.sample.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import io.cabriole.decorator.sample.R
import io.cabriole.decorator.sample.databinding.ListOptionBinding
import io.cabriole.decorator.sample.model.OptionModel

class OptionAdapter(private val onClickListener: OnOptionClickListener) :
    ListAdapter<OptionModel, OptionAdapter.VH>(
        DIFF_CALLBACK
    ) {

    companion object {
        private val DIFF_CALLBACK = object : DiffUtil.ItemCallback<OptionModel>() {

            override fun areItemsTheSame(oldItem: OptionModel, newItem: OptionModel): Boolean {
                return oldItem.getId() == newItem.getId()
            }

            override fun areContentsTheSame(oldItem: OptionModel, newItem: OptionModel): Boolean {
                return oldItem.equals(newItem)
            }

        }
    }

    interface OnOptionClickListener {
        fun onOptionClicked(optionModel: OptionModel)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): VH {
        return VH(
            LayoutInflater.from(parent.context).inflate(
                R.layout.list_option,
                parent,
                false
            ),
            onClickListener
        )
    }

    override fun onBindViewHolder(holder: VH, position: Int) {
        holder.bind(getItem(position))
    }


    class VH(view: View, private val onClickListener: OnOptionClickListener) :
        RecyclerView.ViewHolder(view), View.OnClickListener {

        private var option: OptionModel? = null
        private val binding = ListOptionBinding.bind(view)

        init {
            view.setOnClickListener(this)
        }

        fun bind(item: OptionModel) {
            option = item
            binding.optionTitleTextView.setText(item.titleResource)
            if (item.subtitleResource == null) {
                binding.optionSubtitleTextView.text = ""
                binding.optionSubtitleTextView.isVisible = false
            } else {
                binding.optionSubtitleTextView.isVisible = true
                binding.optionSubtitleTextView.setText(item.subtitleResource)
            }
        }

        override fun onClick(v: View?) {
            option?.let { item -> onClickListener.onOptionClicked(item) }
        }

    }
}