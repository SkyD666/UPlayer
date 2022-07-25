import org.jetbrains.compose.compose
import org.jetbrains.compose.desktop.application.dsl.TargetFormat

buildscript {
    dependencies {
        classpath("com.guardsquare:proguard-gradle:7.2.1")
        classpath("com.squareup.sqldelight:gradle-plugin:1.5.3")
    }
}

plugins {
    kotlin("jvm")
    id("org.jetbrains.compose")
    id("com.squareup.sqldelight") version "1.5.3"
}

repositories {
    google()
    mavenCentral()
    maven("https://maven.pkg.jetbrains.space/public/p/compose/dev")
    maven("https://oss.sonatype.org/content/repositories/snapshots")
}

dependencies {
    implementation(compose.desktop.currentOs)
    @OptIn(org.jetbrains.compose.ExperimentalComposeLibrary::class)
    implementation(compose.material3)
    implementation(compose.materialIconsExtended)
    implementation("uk.co.caprica:vlcj:4.7.3")
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-swing:1.6.2")
    implementation("com.squareup.sqldelight:sqlite-driver:1.5.3")
}

compose.desktop {
    application {
        mainClass = "MainKt"

        nativeDistributions {
            targetFormats(TargetFormat.Dmg, TargetFormat.Msi, TargetFormat.Deb)
            packageName = "UPlayer"
            packageVersion = "1.0.0"

            windows {
                menu = true
            }
        }
    }
}

sqldelight {
    database(name = "AppDatabase") {
        packageName = "com.skyd.db"
    }
}