import org.jetbrains.compose.compose
import org.jetbrains.compose.desktop.application.dsl.TargetFormat

buildscript {
    dependencies {
        classpath("com.guardsquare:proguard-gradle:7.2.1")
    }
}

plugins {
    kotlin("multiplatform")
    id("org.jetbrains.compose")
}

group = "com.skyd.player"
version = "1.0-SNAPSHOT"

repositories {
    google()
    mavenCentral()
    maven("https://maven.pkg.jetbrains.space/public/p/compose/dev")
}

kotlin {
    jvm {
        compilations.all {
            kotlinOptions.jvmTarget = "11"
        }
        withJava()
    }
    sourceSets {
        val jvmMain by getting {
            dependencies {
                implementation(compose.desktop.currentOs)
                @OptIn(org.jetbrains.compose.ExperimentalComposeLibrary::class)
                implementation(compose.material3)
                implementation(compose.materialIconsExtended)
                implementation("uk.co.caprica:vlcj:4.7.0")
            }
        }
        val jvmTest by getting
    }
}

compose.desktop {
    application {
        mainClass = "MainKt"
        nativeDistributions {
            targetFormats(TargetFormat.Dmg, TargetFormat.Msi, TargetFormat.Deb)
            packageName = "UPlayer"
            packageVersion = "1.0.0"
        }
    }
}
