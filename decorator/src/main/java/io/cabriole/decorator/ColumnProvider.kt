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

package io.cabriole.decorator

/**
 * Provides the number of fixed columns used by a LayoutManager
 * for [GridMarginDecoration] and [GridBoundsMarginDecoration]
 *
 * This is required because the Android TV leanback library has a different GridLayoutManager
 * that doesn't extend the regular GridLayoutManager included in androidx.recyclerview
 */
interface ColumnProvider {
    fun getNumberOfColumns(): Int
}
