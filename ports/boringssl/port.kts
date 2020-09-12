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
    override val name = "boringssl"
    override val version = "20200911"
    override val mavenVersion = "${version}"
    override val licensePath = "src/LICENSE"

    override val license = License(
        "License",
        "https://boringssl.googlesource.com/boringssl/+/refs/heads/master/LICENSE"
    )

    override val modules = listOf(
        Module("crypto"),
        Module("ssl")
    )

    override fun configure(
        toolchain: Toolchain,
        sourceDirectory: File,
        buildDirectory: File,
        installDirectory: File,
        workingDirectory: File
    ): Result<Unit, String> {
        buildDirectory.mkdirs()
        installDirectory.mkdirs()
        sourceDirectory.resolve("src/include/openssl")
            .copyRecursively(installDirectory.resolve("include/openssl"), overwrite = true)
        return Result.Ok(Unit)
    }

    override fun build(
        toolchain: Toolchain,
        buildDirectory: File
    ): Result<Unit, String> =
        executeProcessStep(
            listOf(
                "${toolchain.ndk.path.absolutePath}/ndk-build",
                "NDK_PROJECT_PATH=.",
                "NDK_APPLICATION_MK=../../src/Application.mk",
                "APP_BUILD_SCRIPT=../../src/Android.mk",
                "APP_ABI=${toolchain.abi.abiName}",
                "APP_PLATFORM=android-${toolchain.api}",
                "NDK_ALL_ABIS=${toolchain.abi.abiName}",
                "NDK_OUT=.",
                "NDK_LIBS_OUT=.",
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
    ): Result<Unit, String> {
        installDirectory.mkdirs()
        buildDirectory.resolve("local/${toolchain.abi.abiName}")
            .copyRecursively(installDirectory.resolve("lib"), overwrite = true)
        return Result.Ok(Unit)
    }
}
