/*
 * Copyright 2024 Sitharaj Seenivasan
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

package com.sitharaj88.sharedpreform.sample

import com.sitharaj88.sharedpreform.DefaultValue
import com.sitharaj88.sharedpreform.PrefKey
import com.sitharaj88.sharedpreform.SharedPrefName


@SharedPrefName(name = "SamplePreferences")
data class SamplePreferences(
    @PrefKey(key = "username", async = true) @DefaultValue(value = "Guest") val username: String,
    @PrefKey(key = "isLoggedIn", async = true) @DefaultValue(value = "false") val isLoggedIn: Boolean,
    @PrefKey(key = "age", async = true) @DefaultValue(value = "0") val age: Int,
    @PrefKey(key = "height", async = true) @DefaultValue(value = "0.0f") val height: Float,
    @PrefKey(key = "userSet", async = true) @DefaultValue(value = "") val userSet: Set<String>
)
