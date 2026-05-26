plugins {
    `java-library`
    `maven-publish`
    idea
    id("net.fabricmc.fabric-loom")
}

// Seriously, you should not worry about it, definitely not a hack.
// https://github.com/gradle/gradle/issues/15383#issuecomment-779893192
val libs = the<org.gradle.accessors.dm.LibrariesForLibs>()

tasks.withType<Wrapper> {
    gradleVersion = "9.5.0"
    distributionSha256Sum = "a3c4ba4aca8f0075688b9c5b18939fd28e8cb4357c227da5c1d9f38343791439"
    distributionType = Wrapper.DistributionType.ALL
}

repositories {
    mavenLocal()
    mavenCentral()
    val exclusiveRepos: List<Triple<String, String, List<String>>> = listOf(
        Triple(
            "macuguita Maven",
            "https://maven.macuguita.com/releases/",
            listOf("com.macuguita", "folk.sisby", "org.quiltmc")
        ),
        Triple(
            "Neoforge",
            "https://maven.neoforged.net/releases",
            listOf("net.neoforged", "cpw.mods", "new.minecraftforge")
        ),
        Triple(
            "Terraformers (Mod Menu)",
            "https://maven.terraformersmc.com/releases/",
            listOf("com.terraformersmc", "dev.emi")
        ),
        Triple("Modrinth", "https://api.modrinth.com/maven", listOf("maven.modrinth")),
    )

    exclusiveRepos.forEach { (name, url, groups) ->
        if (groups.isNotEmpty()) {
            exclusiveContent {
                forRepository {
                    maven {
                        this.name = name
                        setUrl(url)
                    }
                }
                filter {
                    groups.forEach { includeGroupAndSubgroups(it) }
                }
            }
        } else {
            maven {
                this.name = name
                setUrl(url)
            }
        }
    }
}

fun prop(name: String): String = rootProject.providers.gradleProperty(name).get()

version = libs.versions.mod.get()
group = prop("props.maven_group")

base {
    archivesName.set(prop("props.mod_id"))
}

val fabric: SourceSet by sourceSets.creating {
    this.compileClasspath += sourceSets.main.get().compileClasspath
    this.runtimeClasspath += sourceSets.main.get().runtimeClasspath
}

val neoforge: SourceSet by sourceSets.creating {
    this.compileClasspath += sourceSets.main.get().compileClasspath
    this.runtimeClasspath += sourceSets.main.get().runtimeClasspath
}

dependencies {
    // To change the versions see the gradle.properties file
    minecraft(libs.minecraft)
    api(libs.yumi.foundation)
    include(libs.yumi.foundation)
    implementation(libs.yumi.foundation)

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

java {
    withSourcesJar()

    sourceCompatibility = JavaVersion.VERSION_25
    targetCompatibility = JavaVersion.VERSION_25
}

tasks.jar {
    inputs.property("projectName", project.name)

    from("LICENSE") {
        rename { "${it}_${project.name}" }
    }

    from(fabric.output)
    from(neoforge.output)
}

tasks.named<Jar>("sourcesJar") {
    from(fabric.java.sourceDirectories)
    from(fabric.resources.sourceDirectories)
    from(neoforge.java.sourceDirectories)
    from(neoforge.resources.sourceDirectories)
}

fun shouldBeExcluded(file: File): Boolean {
    if (file.isDirectory) {
        val excludedFolderNames = setOf("run", "build", ".kotlin")

        return file.name in excludedFolderNames
    }

    return false
}

idea {
    module {
        isDownloadSources = true
        isDownloadJavadoc = true

        excludeDirs.addAll(
            rootDir.walkTopDown().filter(::shouldBeExcluded)
        )
    }
}
