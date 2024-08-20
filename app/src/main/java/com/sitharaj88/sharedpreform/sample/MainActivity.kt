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

import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class MainActivity : AppCompatActivity() {
    private val samplePreferencesSharedPref: SamplePreferencesSharedPref by lazy { SamplePreferencesSharedPref(this) }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        // set all the values
        samplePreferencesSharedPref.setUsername("Sitharaj Seenivasan")
        samplePreferencesSharedPref.setAge(120)
        samplePreferencesSharedPref.setHeight(10.0f)
        samplePreferencesSharedPref.setIsLoggedIn(true)
        samplePreferencesSharedPref.setUserSet(setOf("Developer", "Open source contributor"))

        // print all the values
        println("Username: ${samplePreferencesSharedPref.getUsername()}")
        println("Age: ${samplePreferencesSharedPref.getAge()}")
        println("Height: ${samplePreferencesSharedPref.getHeight()}")
        println("Is Logged In: ${samplePreferencesSharedPref.getIsLoggedIn()}")
        println("User Set: ${samplePreferencesSharedPref.getUserSet()}")

        //set all the values asynchronously
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