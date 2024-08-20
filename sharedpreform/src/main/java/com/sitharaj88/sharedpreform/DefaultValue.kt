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
 * This annotation is used to specify a default value for a field.
 * The annotated field's default value is provided as a string
 * and can be utilized in various contexts where a default value
 * is required, such as in serialization, deserialization, or
 * when generating code for preferences or configurations.
 *
 * <p>For example, this annotation can be used in a Kotlin data
 * class to provide default values for properties that might be
 * used in conjunction with shared preferences or other storage
 * mechanisms.</p>
 *
 * <pre>
 * {@code
 * @DefaultValue("Guest")
 * val username: String
 * }
 * </pre>
 *
 * <p>This annotation is retained at runtime, which means it
 * can be accessed via reflection during the execution of the
 * program. It is targeted at fields, meaning it can only be
 * applied to properties or variables within classes or data
 * classes.</p>
 *
 * <h2>Usage Example:</h2>
 *
 * <pre>{@code
 * @SharedPrefName(name = "UserPreferences")
 * data class UserPreferences(
 *     @DefaultValue("Guest")
 *     @PrefKey("username")
 *     val username: String,
 *
 *     @DefaultValue("true")
 *     @PrefKey("isLoggedIn")
 *     val isLoggedIn: Boolean
 * )
 * }</pre>
 *
 * <p>In the example above, the {@code username} field has a default value of
 * "Guest" and {@code isLoggedIn} has a default value of "true". These values
 * can be used by code generation tools or reflection-based utilities to
 * automatically initialize fields when no explicit value is provided.</p>
 *
 * @see Retention
 * @see Target
 * @see AnnotationRetention.RUNTIME
 * @see AnnotationTarget.FIELD
 *
 * @param value The default value to assign to the annotated field.
 */
@Retention(AnnotationRetention.RUNTIME)
@Target(AnnotationTarget.FIELD)
annotation class DefaultValue(val value: String)
