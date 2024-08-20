
# shared-pref-orm

[![](https://jitpack.io/v/sitharaj88/shared-pref-orm.svg)](https://jitpack.io/#sitharaj88/shared-pref-orm)

`shared-pref-orm` is a Kotlin library that simplifies the use of Android's `SharedPreferences` by allowing you to define preferences using annotations. The library automatically generates boilerplate code for storing, retrieving, and updating your preferences, including support for both synchronous and asynchronous operations.

## Features

- **Annotation-based configuration**: Define your preferences using simple annotations.
- **Automatic code generation**: Generates helper classes for managing your preferences.
- **Asynchronous support**: Easily handle preferences in a non-blocking manner with coroutines.
- **Type safety**: Ensures that your preferences are accessed with the correct types.

## Getting Started

### 1. Add the JitPack repository to your build file

Add the following to your root `build.gradle.kts` or `build.gradle` at the end of repositories:

```kotlin
repositories {
    // Other repositories
    maven { url = uri("https://jitpack.io") }
}
```

### 2. Add the Dependency

Add the library dependency to your module `build.gradle.kts` or `build.gradle`:

```kotlin
dependencies {
    implementation("com.github.sitharaj88:shared-pref-orm:1.0.0")
    kapt("com.github.sitharaj88:shared-pref-orm:1.0.0")
}
```

### 3. Enable Kotlin Annotation Processing

Make sure that the Kotlin Annotation Processing Tool (KAPT) is enabled in your project:

```kotlin
apply plugin: 'kotlin-kapt'
```

## Usage

### 1. Define Your Preferences

You can define your preferences using a data class and the provided annotations:

```kotlin
package com.sitharaj88.sharedpreform.sample

import com.sitharaj88.sharedpreform.DefaultValue
import com.sitharaj88.sharedpreform.PrefKey
import com.sitharaj88.sharedpreform.SharedPrefName

/**
 * Sample data class representing the shared preferences.
 * Each field is annotated with @PrefKey and @DefaultValue to specify
 * the key and the default value to be used in SharedPreferences.
 */
@SharedPrefName(name = "SamplePreferences")
data class SamplePreferences(
    @PrefKey(key = "username", async = true) @DefaultValue(value = "Guest") val username: String,
    @PrefKey(key = "isLoggedIn", async = true) @DefaultValue(value = "false") val isLoggedIn: Boolean,
    @PrefKey(key = "age", async = true) @DefaultValue(value = "0") val age: Int,
    @PrefKey(key = "height", async = true) @DefaultValue(value = "0.0f") val height: Float,
    @PrefKey(key = "userSet", async = true) @DefaultValue(value = "") val userSet: Set<String>
)
```

### 2. Generated Code Example

Based on the above data class, the library will generate a helper class that looks like this:

```kotlin
import android.content.Context
import android.content.SharedPreferences
import kotlin.Boolean
import kotlin.Float
import kotlin.Int
import kotlin.String
import kotlin.collections.Set
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

/**
 * Generated helper class for managing SharedPreferences.
 * Provides both synchronous and asynchronous methods for accessing the preferences.
 */
public final class SamplePreferencesSharedPref(
  context: Context,
  private val dispatcher: CoroutineDispatcher = Dispatchers.IO,
) {
  // Reference to the SharedPreferences instance
  private val sharedPreferences: SharedPreferences =
      context.getSharedPreferences("SamplePreferences", Context.MODE_PRIVATE)

  // Synchronous methods for accessing preferences

  /** Retrieves the username from SharedPreferences */
  public fun getUsername(): String? = sharedPreferences.getString("username", "Guest")

  /** Stores the username in SharedPreferences */
  public fun setUsername(`value`: String?) {
    sharedPreferences.edit().putString("username", value).apply()
  }

  /** Retrieves the logged-in status from SharedPreferences */
  public fun getIsLoggedIn(): Boolean = sharedPreferences.getBoolean("isLoggedIn", false)

  /** Stores the logged-in status in SharedPreferences */
  public fun setIsLoggedIn(`value`: Boolean) {
    sharedPreferences.edit().putBoolean("isLoggedIn", value).apply()
  }

  /** Retrieves the age from SharedPreferences */
  public fun getAge(): Int = sharedPreferences.getInt("age", 0)

  /** Stores the age in SharedPreferences */
  public fun setAge(`value`: Int) {
    sharedPreferences.edit().putInt("age", value).apply()
  }

  /** Retrieves the height from SharedPreferences */
  public fun getHeight(): Float = sharedPreferences.getFloat("height", 0.0f)

  /** Stores the height in SharedPreferences */
  public fun setHeight(`value`: Float) {
    sharedPreferences.edit().putFloat("height", value).apply()
  }

  /** Retrieves the user set from SharedPreferences */
  public fun getUserSet(): Set<String?>? = sharedPreferences.getStringSet("userSet", null)

  /** Stores the user set in SharedPreferences */
  public fun setUserSet(`value`: Set<String?>?) {
    sharedPreferences.edit().putStringSet("userSet", value).apply()
  }

  // Asynchronous methods for accessing preferences

  /** Retrieves the username asynchronously from SharedPreferences */
  public suspend fun getUsernameAsync(): String? = withContext(dispatcher) {
    sharedPreferences.getString("username", "Guest")
  }

  /** Stores the username asynchronously in SharedPreferences */
  public suspend fun setUsernameAsync(`value`: String?) {
    withContext(dispatcher) {
      sharedPreferences.edit().putString("username", value).apply()
    }
  }

  /** Retrieves the logged-in status asynchronously from SharedPreferences */
  public suspend fun getIsLoggedInAsync(): Boolean = withContext(dispatcher) {
    sharedPreferences.getBoolean("isLoggedIn", false)
  }

  /** Stores the logged-in status asynchronously in SharedPreferences */
  public suspend fun setIsLoggedInAsync(`value`: Boolean) {
    withContext(dispatcher) {
      sharedPreferences.edit().putBoolean("isLoggedIn", value).apply()
    }
  }

  /** Retrieves the age asynchronously from SharedPreferences */
  public suspend fun getAgeAsync(): Int = withContext(dispatcher) {
    sharedPreferences.getInt("age", 0)
  }

  /** Stores the age asynchronously in SharedPreferences */
  public suspend fun setAgeAsync(`value`: Int) {
    withContext(dispatcher) {
      sharedPreferences.edit().putInt("age", value).apply()
    }
  }

  /** Retrieves the height asynchronously from SharedPreferences */
  public suspend fun getHeightAsync(): Float = withContext(dispatcher) {
    sharedPreferences.getFloat("height", 0.0f)
  }

  /** Stores the height asynchronously in SharedPreferences */
  public suspend fun setHeightAsync(`value`: Float) {
    withContext(dispatcher) {
      sharedPreferences.edit().putFloat("height", value).apply()
    }
  }

  /** Retrieves the user set asynchronously from SharedPreferences */
  public suspend fun getUserSetAsync(): Set<String?>? = withContext(dispatcher) {
    sharedPreferences.getStringSet("userSet", null)
  }

  /** Stores the user set asynchronously in SharedPreferences */
  public suspend fun setUserSetAsync(`value`: Set<String?>?) {
    withContext(dispatcher) {
      sharedPreferences.edit().putStringSet("userSet", value).apply()
    }
  }
}


        samplePreferencesSharedPref.setUserSet(setOf("Developer", "Open source contributor"))

        // Print all the values from SharedPreferences
        println("Username: ${samplePreferencesSharedPref.getUsername()}")
        println("Age: ${samplePreferencesSharedPref.getAge()}")
        println("Height: ${samplePreferencesSharedPref.getHeight()}")
        println("Is Logged In: ${samplePreferencesSharedPref.getIsLoggedIn()}")
        println("User Set: ${samplePreferencesSharedPref.getUserSet()}")

        // Set all the values asynchronously
        CoroutineScope(Dispatchers.IO).launch {
            samplePreferencesSharedPref.setUsernameAsync("Sitharaj Seenivasan")
            samplePreferencesSharedPref.setAgeAsync(120)
            samplePreferencesSharedPref.setHeightAsync(10.0f)
            samplePreferencesSharedPref.setIsLoggedInAsync(true)
            samplePreferencesSharedPref.setUserSetAsync(setOf("Developer", "Open source contributor"))

            println("Async Username: ${samplePreferencesSharedPref.getUsernameAsync()}")
            println("Async Age: ${samplePreferencesSharedPref.getAgeAsync()}")
            println("Async Height: ${samplePreferencesSharedPref.getHeightAsync()}")
            println("Async Is Logged In: ${samplePreferencesSharedPref.getIsLoggedInAsync()}")
            println("Async User Set: ${samplePreferencesSharedPref.getUserSetAsync()}")
        }
    }
}
```

## License

This library is licensed under the Apache License 2.0. See the [LICENSE](LICENSE) file for details.

## Contributing

Contributions are welcome! Please feel free to submit a pull request or open an issue.

## Author

**Sitharaj Seenivasan**

- [GitHub](https://github.com/sitharaj88)


