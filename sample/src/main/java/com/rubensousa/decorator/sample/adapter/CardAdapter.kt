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

package com.rubensousa.decorator.sample.adapter

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.rubensousa.decorator.sample.R
import com.rubensousa.decorator.sample.model.CardModel

class CardAdapter(
    private val recyclerView: RecyclerView,
    private val layoutId: Int = R.layout.list_card
) : RecyclerView.Adapter<CardAdapter.VH>() {

    private val items = ArrayList<CardModel>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): VH {
        return VH(
            LayoutInflater.from(parent.context).inflate(
                layoutId,
                parent,
                false
            )
        ).also { viewHolder ->
            viewHolder.itemView.setOnClickListener {
                val position = viewHolder.bindingAdapterPosition
                if (position in 0 until itemCount) {
                    items.removeAt(position)
                    notifyItemRemoved(position)
                    // Make sure the item decorations are still applied
                    // after the remove animation is done
                    recyclerView.post {
                        recyclerView.itemAnimator?.isRunning {
                            recyclerView.invalidateItemDecorations()
                        }
                    }
                }
            }
        }
    }

    @SuppressLint("NotifyDataSetChanged")
    fun setItems(numberOfItems: Int) {
        repeat(numberOfItems) { id ->
            items.add(CardModel(id))
        }
        notifyDataSetChanged()
    }

    override fun getItemCount(): Int = items.size

    override fun onBindViewHolder(holder: VH, position: Int) {}

    class VH(view: View) : RecyclerView.ViewHolder(view)
}