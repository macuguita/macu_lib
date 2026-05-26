plugins {
    `java-gradle-plugin`
    `kotlin-dsl`
}

val javaVersion = 25

repositories {
    gradlePluginPortal()
    mavenCentral()

    exclusiveContent {
        forRepository {
            maven {
                name = "FabricMC's Maven"
                url = uri("https://maven.fabricmc.net/")
            }
        }
        filter {
            includeGroupAndSubgroups("net.fabricmc")
            includeGroupAndSubgroups("fabric-loom")
        }
    }
}

dependencies {
    // https://maven.fabricmc.net/fabric-loom/fabric-loom.gradle.plugin/
    implementation(libs.gradle.loom)

    // A bit of a hack you definitely should not worry about.
    // https://github.com/gradle/gradle/issues/15383#issuecomment-779893192
    implementation(files(libs.javaClass.superclass.protectionDomain.codeSource.location))
}

java {
    sourceCompatibility = JavaVersion.toVersion(javaVersion)
    targetCompatibility = JavaVersion.toVersion(javaVersion)
}

kotlin {
    compilerOptions {
        jvmTarget = org.jetbrains.kotlin.gradle.dsl.JvmTarget.fromTarget(javaVersion.toString())
    }
}
