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

import com.google.auto.service.AutoService
import com.squareup.kotlinpoet.*
import com.squareup.kotlinpoet.ParameterizedTypeName.Companion.parameterizedBy
import java.io.IOException
import javax.annotation.processing.AbstractProcessor
import javax.annotation.processing.ProcessingEnvironment
import javax.annotation.processing.Processor
import javax.annotation.processing.RoundEnvironment
import javax.annotation.processing.SupportedSourceVersion
import javax.lang.model.SourceVersion
import javax.lang.model.element.Element
import javax.lang.model.element.TypeElement
import javax.lang.model.type.TypeMirror
import javax.lang.model.util.ElementFilter
import javax.tools.Diagnostic

/**
 * A custom annotation processor for generating code related to shared preferences based on
 * annotations like {@link SharedPrefName}, {@link PrefKey}, and {@link DefaultValue}.
 *
 * <p>This processor automates the creation of shared preferences helper classes by processing
 * annotated data classes or classes and generating code that interacts with Android's
 * {@link android.content.SharedPreferences} API.</p>
 *
 * <p>The processor is automatically registered using the {@link AutoService} annotation, which
 * generates the necessary service files to make this processor discoverable by the Java
 * compiler's annotation processing tool.</p>
 *
 * <p>The processor is designed to support Java source code up to and including Java 8, as
 * indicated by the {@link SupportedSourceVersion} annotation with {@link SourceVersion#RELEASE_8}.</p>
 *
 * <h2>Usage Example:</h2>
 *
 * <pre>{@code
 * @SharedPrefName(name = "UserPreferences")
 * data class UserPreferences(
 *     @PrefKey("user_name")
 *     @DefaultValue("Guest")
 *     val username: String,
 *
 *     @PrefKey("is_logged_in")
 *     @DefaultValue("false")
 *     val isLoggedIn: Boolean
 * )
 * }</pre>
 *
 * <p>Given the above annotated data class, this processor will generate a corresponding helper
 * class to simplify reading and writing to shared preferences. The generated class will provide
 * methods to get and set the values associated with each annotated field.</p>
 *
 * <p>This processor is part of a build process and typically runs automatically during compilation,
 * provided that annotation processing is enabled and correctly configured in the build system.</p>
 *
 * <h2>Annotations:</h2>
 * <ul>
 * <li>{@link AutoService}: Registers the processor so it can be discovered and executed by the
 * Java compiler's annotation processing tool.</li>
 * <li>{@link SupportedSourceVersion}: Specifies the Java source version that this processor supports,
 * ensuring compatibility with Java 8 features.</li>
 * </ul>
 *
 * <h2>See Also:</h2>
 * <ul>
 * <li>{@link SharedPrefName}</li>
 * <li>{@link PrefKey}</li>
 * <li>{@link DefaultValue}</li>
 * <li>{@link android.content.SharedPreferences}</li>
 * <li>{@link AutoService}</li>
 * <li>{@link SupportedSourceVersion}</li>
 * </ul>
 *
 * @see SharedPrefName
 * @see PrefKey
 * @see DefaultValue
 * @see android.content.SharedPreferences
 * @see AutoService
 * @see SupportedSourceVersion
 * @see AbstractProcessor
 */
@AutoService(Processor::class)
@SupportedSourceVersion(SourceVersion.RELEASE_8)
class SharedPreferenceProcessor : AbstractProcessor() {

    private lateinit var messager: javax.annotation.processing.Messager

    override fun init(processingEnv: ProcessingEnvironment) {
        super.init(processingEnv)
        this.messager = processingEnv.messager
    }

    override fun getSupportedAnnotationTypes(): MutableSet<String> {
        return mutableSetOf(PrefKey::class.java.name, DefaultValue::class.java.name, SharedPrefName::class.java.name)
    }

    override fun process(annotations: MutableSet<out TypeElement>, roundEnv: RoundEnvironment): Boolean {
        val classElements = roundEnv.getElementsAnnotatedWith(SharedPrefName::class.java)

        classElements.forEach { classElement ->
            val fields = ElementFilter.fieldsIn(classElement.enclosedElements)
            if (fields.isEmpty()) {
                messager.printMessage(Diagnostic.Kind.ERROR, "No fields annotated with @PrefKey found in class ${classElement.simpleName}.")
                return true
            }

            try {
                generateSharedPrefClass(classElement, fields)
            } catch (e: Exception) {
                messager.printMessage(Diagnostic.Kind.ERROR, "Failed to generate code for class ${classElement.simpleName}: ${e.message}")
            }
        }

        return true
    }

    private fun generateSharedPrefClass(classElement: Element, fields: List<Element>) {
        val packageName = processingEnv.elementUtils.getPackageOf(classElement).toString()
        val className = "${classElement.simpleName}SharedPref"
        val prefName = classElement.getAnnotation(SharedPrefName::class.java).name

        val classBuilder = TypeSpec.classBuilder(className)
            .addModifiers(KModifier.PUBLIC, KModifier.FINAL)
            .primaryConstructor(
                FunSpec.constructorBuilder()
                    .addParameter("context", ClassName("android.content", "Context"))
                    .addParameter(
                        ParameterSpec.builder("dispatcher", ClassName("kotlinx.coroutines", "CoroutineDispatcher"))
                            .defaultValue("Dispatchers.IO")
                            .build()
                    )
                    .build()
            )
            .addProperty(
                PropertySpec.builder("sharedPreferences", ClassName("android.content", "SharedPreferences"))
                    .initializer("context.getSharedPreferences(\"$prefName\", Context.MODE_PRIVATE)")
                    .addModifiers(KModifier.PRIVATE)
                    .build()
            )
            .addProperty(
                PropertySpec.builder("dispatcher", ClassName("kotlinx.coroutines", "CoroutineDispatcher"))
                    .initializer("dispatcher")
                    .addModifiers(KModifier.PRIVATE)
                    .build()
            )

        fields.forEach { field ->
            val fieldName = field.simpleName.toString()
            val prefKey = field.getAnnotation(PrefKey::class.java).key
            val async = field.getAnnotation(PrefKey::class.java).async
            val fieldType = field.asType().toString()
            val defaultValue = getDefaultValue(field)

            // Determine if the type should be nullable
            val typeName = mapTypeToKotlinPoetTypeName(field.asType())

            // Generate sync get and set functions
            val getFun = FunSpec.builder("get${fieldName.capitalize()}")
                .addModifiers(KModifier.PUBLIC)
                .returns(typeName)
                .apply {
                    if (fieldType == "java.lang.String" || fieldType == "kotlin.String") {
                        addStatement(
                            "return sharedPreferences.getString(\"$prefKey\", $defaultValue)"
                        )
                    } else if (fieldType == "java.util.Set<java.lang.String>" || fieldType == "kotlin.collections.Set<kotlin.String>") {
                        addStatement(
                            "return sharedPreferences.getStringSet(\"$prefKey\", $defaultValue)"
                        )
                    } else {
                        addStatement(
                            "return sharedPreferences.get${getSharedPrefTypeMethod(fieldType)}(\"$prefKey\", $defaultValue)"
                        )
                    }
                }
                .build()

            val setFun = FunSpec.builder("set${fieldName.capitalize()}")
                .addModifiers(KModifier.PUBLIC)
                .addParameter("value", typeName)
                .apply {
                    if (fieldType == "java.lang.String" || fieldType == "kotlin.String") {
                        addStatement("sharedPreferences.edit().putString(\"$prefKey\", value).apply()")
                    } else if (fieldType == "java.util.Set<java.lang.String>" || fieldType == "kotlin.collections.Set<kotlin.String>") {
                        addStatement("sharedPreferences.edit().putStringSet(\"$prefKey\", value).apply()")
                    } else {
                        addStatement("sharedPreferences.edit().put${getSharedPrefTypeMethod(fieldType)}(\"$prefKey\", value).apply()")
                    }
                }
                .build()

            classBuilder.addFunction(getFun)
            classBuilder.addFunction(setFun)

            // Generate async get and set functions if async = true
            if (async) {
                val getFunAsync = FunSpec.builder("get${fieldName.capitalize()}Async")
                    .addModifiers(KModifier.PUBLIC, KModifier.SUSPEND)
                    .returns(typeName)
                    .apply {
                        if (fieldType == "java.lang.String" || fieldType == "kotlin.String") {
                            addStatement(
                                "return withContext(dispatcher) { sharedPreferences.getString(\"$prefKey\", $defaultValue) }"
                            )
                        } else if (fieldType == "java.util.Set<java.lang.String>" || fieldType == "kotlin.collections.Set<kotlin.String>") {
                            addStatement(
                                "return withContext(dispatcher) { sharedPreferences.getStringSet(\"$prefKey\", $defaultValue) }"
                            )
                        } else {
                            addStatement(
                                "return withContext(dispatcher) { sharedPreferences.get${getSharedPrefTypeMethod(fieldType)}(\"$prefKey\", $defaultValue) }"
                            )
                        }
                    }
                    .build()
                classBuilder.addFunction(getFunAsync)

                val setFunAsync = FunSpec.builder("set${fieldName.capitalize()}Async")
                    .addModifiers(KModifier.PUBLIC, KModifier.SUSPEND)
                    .addParameter("value", typeName)
                    .apply {
                        if (fieldType == "java.lang.String" || fieldType == "kotlin.String") {
                            addStatement("withContext(dispatcher) { sharedPreferences.edit().putString(\"$prefKey\", value).apply() }")
                        } else if (fieldType == "java.util.Set<java.lang.String>" || fieldType == "kotlin.collections.Set<kotlin.String>") {
                            addStatement("withContext(dispatcher) { sharedPreferences.edit().putStringSet(\"$prefKey\", value).apply() }")
                        } else {
                            addStatement("withContext(dispatcher) { sharedPreferences.edit().put${getSharedPrefTypeMethod(fieldType)}(\"$prefKey\", value).apply() }")
                        }
                    }
                    .build()
                classBuilder.addFunction(setFunAsync)
            }
        }

        val file = FileSpec.builder(packageName, className)
            .addType(classBuilder.build())
            .addImport("android.content", "Context")
            .addImport("android.content", "SharedPreferences")
            .addImport("kotlinx.coroutines", "CoroutineDispatcher")
            .addImport("kotlinx.coroutines", "Dispatchers")
            .addImport("kotlinx.coroutines", "withContext")
            .build()

        try {
            file.writeTo(processingEnv.filer)
        } catch (e: IOException) {
            messager.printMessage(Diagnostic.Kind.ERROR, "Failed to write generated class: ${e.message}")
        }
    }

    private fun getSharedPrefTypeMethod(fieldType: String): String {
        return when (fieldType) {
            "java.lang.String", "kotlin.String" -> "String"
            "int", "java.lang.Integer", "kotlin.Int" -> "Int"
            "boolean", "java.lang.Boolean", "kotlin.Boolean" -> "Boolean"
            "float", "java.lang.Float", "kotlin.Float" -> "Float"
            "long", "java.lang.Long", "kotlin.Long" -> "Long"
            "java.util.Set<java.lang.String>", "kotlin.collections.Set<kotlin.String>" -> "StringSet"
            else -> throw IllegalArgumentException("Unsupported type: $fieldType")
        }
    }

    private fun getDefaultValue(field: Element): String {
        val defaultValue = field.getAnnotation(DefaultValue::class.java)?.value ?: ""
        return when (field.asType().toString()) {
            "java.lang.String", "kotlin.String" -> "\"$defaultValue\""
            "int", "java.lang.Integer", "kotlin.Int" -> defaultValue
            "boolean", "java.lang.Boolean", "kotlin.Boolean" -> defaultValue // true or false
            "float", "java.lang.Float", "kotlin.Float" -> defaultValue
            "long", "java.lang.Long", "kotlin.Long" -> defaultValue
            "java.util.Set<java.lang.String>", "kotlin.collections.Set<kotlin.String>" -> "null"
            else -> throw IllegalArgumentException("Unsupported type for default value: ${field.asType()}")
        }
    }

    private fun mapTypeToKotlinPoetTypeName(typeMirror: TypeMirror): TypeName {
        return when (typeMirror.toString()) {
            "java.lang.String", "kotlin.String" -> ClassName("kotlin", "String").copy(nullable = true)
            "int", "java.lang.Integer", "kotlin.Int" -> ClassName("kotlin", "Int")
            "boolean", "java.lang.Boolean", "kotlin.Boolean" -> ClassName("kotlin", "Boolean")
            "float", "java.lang.Float", "kotlin.Float" -> ClassName("kotlin", "Float")
            "long", "java.lang.Long", "kotlin.Long" -> ClassName("kotlin", "Long")
            "java.util.Set<java.lang.String>", "kotlin.collections.Set<kotlin.String>" ->
                ClassName("kotlin.collections", "Set")
                    .parameterizedBy(ClassName("kotlin", "String").copy(nullable = true))
                    .copy(nullable = true)
            else -> throw IllegalArgumentException("Unsupported type: ${typeMirror.toString()}")
        }
    }
}
