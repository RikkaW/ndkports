/*
 * Copyright (C) 2019 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.android.ndkports

import java.io.File

@Suppress("unused")
fun executeProcessStep(
    args: List<String>,
    workingDirectory: File,
    additionalEnvironment: Map<String, String>? = null
): Result<Unit, String> {
    val pb = ProcessBuilder(args)
        .redirectOutput(ProcessBuilder.Redirect.INHERIT)
        .redirectError(ProcessBuilder.Redirect.INHERIT)
        .directory(workingDirectory)

    if (additionalEnvironment != null) {
        pb.environment().putAll(additionalEnvironment)
    }

    return pb.start()
        .waitFor().let {
            if (it == 0) {
                Result.Ok(Unit)
            } else {
                Result.Error("Process failed with exit code $it")
            }
        }
}

fun installDirectoryForPort(
    name: String,
    workingDirectory: File,
    toolchain: Toolchain
): File = workingDirectory.parentFile.resolve("$name/install/${toolchain.abi}")

data class Module(
    val name: String,
    val includesPerAbi: Boolean = false,
    val dependencies: List<String> = emptyList()
)

abstract class Port {
    abstract val name: String
    abstract val version: String
    open val prefabVersion: CMakeCompatibleVersion
        get() = CMakeCompatibleVersion.parse(version)
    open val mavenVersion: String
        get() = version

    open val licensePath: String = "LICENSE"

    abstract val license: License

    open val dependencies: List<String> = emptyList()
    abstract val modules: List<Module>

    protected val ncpus = Runtime.getRuntime().availableProcessors()

    fun run(
        toolchain: Toolchain,
        sourceDirectory: File,
        buildDirectory: File,
        installDirectory: File,
        workingDirectory: File
    ): Result<Unit, String> {
        configure(
            toolchain,
            sourceDirectory,
            buildDirectory,
            installDirectory,
            workingDirectory
        ).onFailure { return Result.Error(it) }

        build(toolchain, buildDirectory).onFailure { return Result.Error(it) }

        install(
            toolchain,
            buildDirectory,
            installDirectory
        ).onFailure { return Result.Error(it) }

        return Result.Ok(Unit)
    }

    open fun extractSource(
        sourceTarball: File,
        sourceDirectory: File,
        workingDirectory: File
    ): Result<Unit, String> {
        sourceDirectory.mkdirs()
        return executeProcessStep(
            listOf(
                "tar",
                "xf",
                sourceTarball.absolutePath,
                "--strip-components=1"
            ), sourceDirectory
        )
    }

    open fun configure(
        toolchain: Toolchain,
        sourceDirectory: File,
        buildDirectory: File,
        installDirectory: File,
        workingDirectory: File
    ): Result<Unit, String> = Result.Ok(Unit)

    open fun build(
        toolchain: Toolchain,
        buildDirectory: File
    ): Result<Unit, String> = Result.Ok(Unit)

    open fun install(
        toolchain: Toolchain,
        buildDirectory: File,
        installDirectory: File
    ): Result<Unit, String> = Result.Ok(Unit)
}