package io.cabriole.decorator

/**
 * Allows combining multiple [DecorationLookup]
 */
class MergeDecorationLookup private constructor(private val delegates: List<DecorationLookup>) :
    DecorationLookup {

    companion object {

        @JvmStatic
        fun of(vararg decorationLookups: DecorationLookup): MergeDecorationLookup {
            return MergeDecorationLookup(decorationLookups.toList())
        }

        @JvmStatic
        fun of(list: List<DecorationLookup>): MergeDecorationLookup {
            return MergeDecorationLookup(list)
        }

    }

    override fun shouldApplyDecoration(position: Int, itemCount: Int): Boolean {
        delegates.forEach { delegate ->
            if (!delegate.shouldApplyDecoration(position, itemCount)) {
                return false
            }
        }
        return true
    }

}
