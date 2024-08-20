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
 * This annotation is used to specify the name of the shared preferences
 * or any other storage mechanism that requires a named configuration.
 * It is typically applied to a class or data class to define the name
 * under which all the key-value pairs associated with that class will
 * be stored.
 *
 * <p>This annotation is particularly useful in libraries or applications
 * where you need to organize and manage multiple preference files or
 * storage configurations. By using this annotation, you can easily
 * group related preferences under a single name.</p>
 *
 * <pre>
 * {@code
 * @SharedPrefName(name = "UserPreferences")
 * data class UserPreferences(
 *     @PrefKey("user_name")
 *     val username: String,
 *
 *     @PrefKey("is_logged_in")
 *     val isLoggedIn: Boolean
 * )
 * }
 * </pre>
 *
 * <p>This annotation is retained at runtime, which means it can be accessed
 * via reflection during the execution of the program. It is targeted at
 * classes, meaning it can be applied to data classes or regular classes
 * that represent a collection of key-value pairs for storage.</p>
 *
 * <h2>Usage Example:</h2>
 *
 * <pre>{@code
 * @SharedPrefName(name = "AppSettings")
 * data class AppSettings(
 *     @PrefKey("theme")
 *     val theme: String,
 *
 *     @PrefKey("notifications_enabled")
 *     val notificationsEnabled: Boolean
 * )
 * }</pre>
 *
 * <p>In the example above, the {@code AppSettings} data class is associated
 * with the shared preferences name "AppSettings". All keys defined within
 * this class (e.g., {@code theme}, {@code notificationsEnabled}) will be
 * stored under this shared preferences name.</p>
 *
 * <p>This annotation is essential in environments where multiple named
 * configurations are required, ensuring that related preferences are grouped
 * together logically under a single name.</p>
 *
 * @see Retention
 * @see Target
 * @see AnnotationRetention.RUNTIME
 * @see AnnotationTarget.CLASS
 *
 * @param name The name of the shared preferences or configuration file
 *             associated with the annotated class. This name will be used
 *             to store and retrieve all key-value pairs defined within the class.
 */
@Retention(AnnotationRetention.RUNTIME)
@Target(AnnotationTarget.CLASS)
annotation class SharedPrefName(val name: String)
