# 2.0.2

- Moved `setDecorationLookup` to `AbstractMarginDecoration`
- Added new `shouldApplyDecorationAt` to `AbstractMarginDecoration`

# 2.0.1

- Added support for `ConcatAdapter`. `DecorationLookup` now receives the absolute adapter position.

# 2.0.0

Changed artifact to: com.rubensousa:decorator

# 1.3.1

#### LinearMarginDecoration

- Fixed margins being applied incorrectly when `addBeforeFirstPosition` and `addAfterLastPosition` were set

# 1.3.0

#### New DecorationLookup defaults

- Added `MergeDecorationLookup` to combine multiple `DecorationLookup`
- Added `SingleItemDecorationLookup` to disable decorations when there's only a single item

#### LinearDividerDecoration

- Added `addBeforeFirstPosition` and `addAfterLastPosition` parameter to control dividers at the first and last positions
- Fixed `LinearDividerDecoration` reporting wrong positions in its `DecorationLookup` [#14](https://github.com/rubensousa/Decorator/issues/14)
- Fixed `LinearDividerDecoration` not applying dividers correctly at the last position [#13](https://github.com/rubensousa/Decorator/issues/13)

#### LinearMarginDecoration

- Added `addBeforeFirstPosition` and `addAfterLastPosition` parameter to control margin at the first and last positions

#### GridSpanMarginDecoration & GridSpanBoundsMarginDecoration

- Added support for defining vertical and horizontal margins separately [#8](https://github.com/rubensousa/Decorator/issues/8) [#9](https://github.com/rubensousa/Decorator/issues/9)


# 1.2.0

- Fixed decorations not being applied correctly when the itemCount is 1
- Added `createVertical` and `createHorizontal` helpers to:
    - `LinearMarginDecoration`
    - `LinearBoundsMarginDecoration`
    - `GridBoundsMarginDecoration`

# 1.1.0

- Added `GridSpanBoundsMarginDecoration` to apply margins to the bounds of RecyclerViews that use a GridLayoutManager with different span sizes

# 1.0.0

- Initial release