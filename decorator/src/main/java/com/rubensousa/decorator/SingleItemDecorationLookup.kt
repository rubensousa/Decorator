package com.rubensousa.decorator

import androidx.recyclerview.widget.RecyclerView

/**
 * A [DecorationLookup] that disables decoration when there's only a single item
 */
class SingleItemDecorationLookup : DecorationLookup {

    override fun shouldApplyDecoration(
        viewHolder: RecyclerView.ViewHolder,
        itemCount: Int
    ): Boolean {
        if (viewHolder.layoutPosition == 0 && itemCount == 1) {
            return false
        }
        return true
    }

}
