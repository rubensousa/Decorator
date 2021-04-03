package com.rubensousa.decorator

/**
 * A [DecorationLookup] that disables decoration when there's only a single item
 */
class SingleItemDecorationLookup : DecorationLookup {

    override fun shouldApplyDecoration(position: Int, itemCount: Int): Boolean {
        if (position == 0 && itemCount == 1) {
            return false
        }
        return true
    }

}