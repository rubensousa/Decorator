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

package com.rubensousa.decorator

import androidx.recyclerview.widget.RecyclerView

/**
 * Checks if a decoration should be applied for a given [RecyclerView.ViewHolder]
 */
interface DecorationLookup {

    /**
     * @param viewHolder the ViewHolder currently in layout
     *
     * @param itemCount the item count at the layout stage. See [RecyclerView.State.getItemCount]
     *
     * @return true if the ViewHolder should have decoration applied to
     */
    fun shouldApplyDecoration(viewHolder: RecyclerView.ViewHolder, itemCount: Int): Boolean

}
