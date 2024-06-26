import org.jetbrains.compose.desktop.application.dsl.TargetFormat

plugins {
    kotlin("jvm")
    id("org.jetbrains.compose")
}

group = "hu.oszlanyizsolt"
version = "1.0-SNAPSHOT"

repositories {
    mavenCentral()
    maven("https://maven.pkg.jetbrains.space/public/p/compose/dev")
    google()
}

dependencies {
    // Note, if you develop a library, you should use compose.desktop.common.
    // compose.desktop.currentOs should be used in launcher-sourceSet
    // (in a separate module for demo project and in testMain).
    // With compose.desktop.common you will also lose @Preview functionality
    implementation(compose.desktop.currentOs)

    implementation(compose.materialIconsExtended)

    implementation("org.jetbrains.kotlinx:kotlinx-datetime:0.6.0")
    implementation("me.walkerknapp:devolay:2.1.0:integrated")
}

compose.desktop {
    application {
        mainClass = "MainKt"

        nativeDistributions {
            targetFormats(TargetFormat.Dmg, TargetFormat.Msi, TargetFormat.Deb)
            packageName = "countdown"
            packageVersion = "1.0.0"

            buildTypes.release.proguard {
                isEnabled.set(false)
                obfuscate.set(false)
            }

        }

        jvmArgs(
            "-Dapple.awt.application.appearance=system"
        )
    }
}
