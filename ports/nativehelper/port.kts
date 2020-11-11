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

object : Port() {
    override val name = "nativehelper"
    override val version = "20201111"
    override val mavenVersion = "${version}"
    override val licensePath = "NOTICE"

    override val license = License(
        "License",
        "https://android.googlesource.com/platform/libnativehelper/+/refs/heads/master/NOTICE"
    )

    override val modules = listOf(
        Module("nativehelper_header_only", headerOnly = true)
    )

    override fun configure(
        toolchain: Toolchain,
        sourceDirectory: File,
        buildDirectory: File,
        installDirectory: File,
        workingDirectory: File
    ): Result<Unit, String> {
        //buildDirectory.mkdirs()
        println(sourceDirectory.absolutePath)
        return executeProcessStep(
            listOf(
                "cmake",
                "-DCMAKE_BUILD_TYPE=Release",
                "-DCMAKE_TOOLCHAIN_FILE=${toolchain.ndk.path.absolutePath}/build/cmake/android.toolchain.cmake",
                "-DANDROID_ABI=${toolchain.abi.abiName}",
                "-DANDROID_NDK=${toolchain.ndk.path.absolutePath}",
                "-DANDROID_PLATFORM=android-${toolchain.api}",
                "-DCMAKE_ANDROID_ARCH_ABI=a${toolchain.abi.abiName}",
                "-DCMAKE_ANDROID_NDK=${toolchain.ndk.path.absolutePath}",
                "-DCMAKE_INSTALL_PREFIX=${installDirectory.absolutePath}",
                "-DCMAKE_SYSTEM_NAME=Android",
                "-DCMAKE_SYSTEM_VERSION=${toolchain.api}",
                "-GNinja",
                "${sourceDirectory.absolutePath}"
            ), buildDirectory,
            additionalEnvironment = mapOf(
                "ANDROID_NDK" to toolchain.ndk.path.absolutePath,
                "PATH" to "${toolchain.binDir}:${System.getenv("PATH")}"
            )
        )
    }

    override fun build(
        toolchain: Toolchain,
        buildDirectory: File
    ): Result<Unit, String> = executeProcessStep(
        listOf(
            "cmake",
            "--build",
            ".",
            "-j$ncpus"
        ), buildDirectory,
        additionalEnvironment = mapOf(
            "ANDROID_NDK" to toolchain.ndk.path.absolutePath,
            "PATH" to "${toolchain.binDir}:${System.getenv("PATH")}"
        )
    )

    override fun install(
        toolchain: Toolchain,
        buildDirectory: File,
        installDirectory: File
    ): Result<Unit, String> = executeProcessStep(
        listOf(
            "cmake",
            "--install",
            "."
        ), buildDirectory,
        additionalEnvironment = mapOf(
            "ANDROID_NDK" to toolchain.ndk.path.absolutePath,
            "PATH" to "${toolchain.binDir}:${System.getenv("PATH")}"
        )
    )
}
