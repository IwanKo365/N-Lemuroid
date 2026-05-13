@file:Suppress("ktlint")

pluginManagement {
    repositories {
        gradlePluginPortal()
        google()
    }
}

rootProject.name = "N-Lemuroid"

plugins {
    id("org.gradle.toolchains.foojay-resolver-convention") version "0.10.0"
}

include(
    ":retrograde-util",
    ":retrograde-app-shared",
    ":lemuroid-touchinput",
    ":lemuroid-app",
    ":lemuroid-metadata-libretro-db",
    ":lemuroid-app-ext-free",
    ":lemuroid-app-ext-play",
    ":baselineprofile"
)

fun includeOptionalProject(name: String, path: String) {
    val dir = File(path)
    if (dir.exists() && (File(dir, "build.gradle").exists() || File(dir, "build.gradle.kts").exists())) {
        include(name)
        project(name).projectDir = dir
    } else {
        println("Warning: Optional project $name not found at ${dir.absolutePath}. Skipping.")
    }
}

includeOptionalProject(":bundled-cores", "lemuroid-cores/bundled-cores")

fun usePlayDynamicFeatures(): Boolean {
    val task = gradle.startParameter.taskRequests.toString()
    return task.contains("Play") && task.contains("Dynamic")
}

if (usePlayDynamicFeatures()) {
    val cores = listOf(
        "lemuroid_core_desmume",
        "lemuroid_core_dosbox_pure",
        "lemuroid_core_fbneo",
        "lemuroid_core_fceumm",
        "lemuroid_core_gambatte",
        "lemuroid_core_genesis_plus_gx",
        "lemuroid_core_handy",
        "lemuroid_core_mame2003_plus",
        "lemuroid_core_mednafen_ngp",
        "lemuroid_core_mednafen_pce_fast",
        "lemuroid_core_mednafen_wswan",
        "lemuroid_core_melonds",
        "lemuroid_core_mgba",
        "lemuroid_core_mupen64plus_next_gles3",
        "lemuroid_core_pcsx_rearmed",
        "lemuroid_core_ppsspp",
        "lemuroid_core_prosystem",
        "lemuroid_core_snes9x",
        "lemuroid_core_stella",
        "lemuroid_core_citra"
    )

    cores.forEach { core ->
        includeOptionalProject(":$core", "lemuroid-cores/$core")
    }
}

