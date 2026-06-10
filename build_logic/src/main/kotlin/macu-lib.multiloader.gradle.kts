plugins {
    id("macu-lib.java")
    id("macu-lib.loom")
}

// Seriously, you should not worry about it, definitely not a hack.
// https://github.com/gradle/gradle/issues/15383#issuecomment-779893192
val libs = the<org.gradle.accessors.dm.LibrariesForLibs>()

val fabric: SourceSet by sourceSets.creating {
    this.compileClasspath += sourceSets.main.get().compileClasspath
    this.runtimeClasspath += sourceSets.main.get().runtimeClasspath
}

val neoforge: SourceSet by sourceSets.creating {
    this.compileClasspath += sourceSets.main.get().compileClasspath
    this.runtimeClasspath += sourceSets.main.get().runtimeClasspath
}

repositories {
    maven {
        name = "Neoforged"
        url = uri("https://maven.neoforged.net/releases")
    }
}

dependencies {
    compileOnly(libs.fabric.loader)
    localRuntime(libs.fabric.loader)

    "fabricCompileOnly"(libs.fabric.api)
    localRuntime(libs.fabric.api)

    "neoforgeCompileOnly"(libs.neoforge)
    "neoforgeCompileOnly"(libs.neoforge.loader)

    "neoforgeImplementation"(sourceSets.main.get().output)
    "fabricImplementation"(sourceSets.main.get().output)

    localRuntime(fabric.output)
}

tasks.jar {
    from(fabric.output)
    from(neoforge.output)
}

tasks.named<Jar>("sourcesJar") {
    from(fabric.java.sourceDirectories)
    from(fabric.resources.sourceDirectories)
    from(neoforge.java.sourceDirectories)
    from(neoforge.resources.sourceDirectories)
}
