plugins {
    id("net.fabricmc.fabric-loom")
}

// Seriously, you should not worry about it, definitely not a hack.
// https://github.com/gradle/gradle/issues/15383#issuecomment-779893192
val libs = the<org.gradle.accessors.dm.LibrariesForLibs>()

repositories {
    mavenCentral()
}

fabricApi {
    configureDataGeneration {
        client = true
    }
}

dependencies {
    minecraft(libs.minecraft)
    api(libs.yumi.foundation)
    include(libs.yumi.foundation)
    implementation(libs.yumi.foundation)
}
