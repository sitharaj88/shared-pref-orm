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

package com.sitharaj88.sharedpreform

/**
 * This annotation is used to specify a key for a field, typically
 * when working with shared preferences, configurations, or other
 * key-value storage mechanisms. It provides a way to associate a
 * specific key with a field, and optionally indicates whether
 * operations on this field should be performed asynchronously.
 *
 * <p>This annotation can be applied to fields in a data class or
 * a regular class. It is particularly useful in code generation
 * scenarios where keys need to be automatically assigned to fields
 * for storage and retrieval purposes.</p>
 *
 * <pre>
 * {@code
 * @PrefKey("user_name", async = true)
 * val username: String
 * }
 * </pre>
 *
 * <p>This annotation is retained at runtime, which means it can
 * be accessed via reflection during the execution of the program.
 * It is targeted at fields, meaning it can only be applied to
 * properties or variables within classes or data classes.</p>
 *
 * <h2>Usage Example:</h2>
 *
 * <pre>{@code
 * @SharedPrefName(name = "UserPreferences")
 * data class UserPreferences(
 *     @PrefKey("user_name")
 *     val username: String,
 *
 *     @PrefKey("is_logged_in", async = true)
 *     val isLoggedIn: Boolean
 * )
 * }</pre>
 *
 * <p>In the example above, the {@code username} field is associated
 * with the key "user_name" and the {@code isLoggedIn} field is associated
 * with the key "is_logged_in". The {@code async} parameter for the
 * {@code isLoggedIn} field indicates that operations on this field
 * should be handled asynchronously.</p>
 *
 * <p>This annotation is particularly useful in libraries or applications
 * where you need to map fields to specific keys in a structured way,
 * and optionally handle operations in a non-blocking manner using
 * coroutines or other asynchronous techniques.</p>
 *
 * @see Retention
 * @see Target
 * @see AnnotationRetention.RUNTIME
 * @see AnnotationTarget.FIELD
 *
 * @param key The key associated with the annotated field. This key will
 *            be used to store and retrieve the field's value in key-value
 *            storage mechanisms like shared preferences.
 * @param async A flag indicating whether operations on this field should
 *              be performed asynchronously. The default value is {@code false}.
 */
@Retention(AnnotationRetention.RUNTIME)
@Target(AnnotationTarget.FIELD)
annotation class PrefKey(val key: String, val async: Boolean = false)
