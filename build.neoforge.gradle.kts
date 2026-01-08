plugins {
    id("net.neoforged.moddev")
    id ("dev.kikugie.postprocess.jsonlang")
    id("me.modmuss50.mod-publish-plugin")
    id("maven-publish")
}

tasks.named<ProcessResources>("processResources") {
    fun prop(name: String) = project.property(name) as String

    val props = HashMap<String, String>().apply {
        this["version"] = prop("mod.version") + "+" + prop("deps.minecraft")
        this["minecraft"] = prop("mod.mc_dep_forgelike")
        this["javaVersion"] = if (stonecutter.eval(stonecutter.current.version, ">=26.1")) "JAVA_25" else "JAVA_21"
    }

    filesMatching(listOf("fabric.mod.json", "META-INF/neoforge.mods.toml", "META-INF/mods.toml", "${prop("mod.id")}.mixins.json")) {
        expand(props)
    }
}

version = "${property("mod.version")}+${property("deps.minecraft")}-neoforge"
base.archivesName = property("mod.id") as String

jsonlang {
    languageDirectories = listOf("assets/${property("mod.id")}/lang")
    prettyPrint = true
}

repositories {
    mavenLocal()
    val exclusiveRepos: List<Triple<String, String, List<String>>> = listOf(
        Triple("Minecraft Forge", "https://maven.minecraftforge.net", emptyList()),
        Triple("shedaniel (Cloth Config)", "https://maven.shedaniel.me/", listOf("me.shedaniel")),
        Triple("Xander Maven", "https://maven.isxander.dev/releases/", listOf("dev.isxander", "org.quiltmc.parsers")),
        Triple("Terraformers (Mod Menu)", "https://maven.terraformersmc.com/releases/", listOf("com.terraformersmc", "dev.emi")),
        Triple("Wisp Forest Maven", "https://maven.wispforest.io/releases/", listOf("io.wispforest")),
        Triple("Modrinth", "https://api.modrinth.com/maven", listOf("maven.modrinth")),
        Triple("Sisby Maven", "https://repo.sleeping.town/", listOf("folk.sisby")),
        Triple("Parchment Mappings", "https://maven.parchmentmc.org", listOf("org.parchmentmc")),
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

val testmod by sourceSets.creating {
    compileClasspath += sourceSets["main"].compileClasspath
    runtimeClasspath += sourceSets["main"].runtimeClasspath
}

dependencies {
    "testmodImplementation"(sourceSets.main.map { it.output })
}

neoForge {
    version = property("deps.neoforge") as String
    validateAccessTransformers = true

    if (hasProperty("deps.parchment")) parchment {
        val (mc, ver) = (property("deps.parchment") as String).split(':')
        mappingsVersion = ver
        minecraftVersion = mc
    }

    mods {
        register(property("mod.id") as String) {
            sourceSet(sourceSets["main"])
        }
        register("macu_lib_tests") {
            sourceSet(sourceSets["testmod"])
        }
    }

    runs {
        register("client") {
            gameDirectory = file("run/")
            client()

            sourceSet = sourceSets["main"]
            loadedMods.set(listOf(mods[property("mod.id") as String]))
        }
        register("clientMacuguita") {
            gameDirectory = file("run/")
            client()

            sourceSet = sourceSets["main"]
            loadedMods.set(listOf(mods[property("mod.id") as String]))

            programArguments.add("--username=macuguita")
            programArguments.add("--uuid=0e56050b-ee27-478a-a345-d2b384919081")
        }
        register("server") {
            gameDirectory = file("run/")
            server()

            sourceSet = sourceSets["main"]
            loadedMods.set(listOf(mods[property("mod.id") as String]))

            programArguments.add("--nogui")
        }

        register("testmodClient") {
            gameDirectory = file("run")
            client()

            sourceSet = sourceSets["testmod"]
            loadedMods.set(
                listOf(
                    mods[property("mod.id") as String],
                    mods["macu_lib_tests"]
                )
            )
        }
        register("testmodServer") {
            gameDirectory = file("run")
            server()

            sourceSet = sourceSets["testmod"]
            loadedMods.set(
                listOf(
                    mods[property("mod.id") as String],
                    mods["macu_lib_tests"]
                )
            )

            programArguments.add("--nogui")
        }
    }
    sourceSets["main"].resources.srcDir("src/main/generated")
}

dependencies {
    // McQoy
    implementation("folk.sisby:kaleido-config:${property("deps.kaleido")}")
    jarJar("folk.sisby:kaleido-config:${property("deps.kaleido")}")

    compileOnly("org.jspecify:jspecify:1.0.0")

    if (hasProperty("deps.mcqoy")) {
        implementation("maven.modrinth:mcqoy:${property("deps.mcqoy")}")
    }

    // YACL  - required by McQoy
    if (hasProperty("deps.yacl")) {
        runtimeOnly("dev.isxander:yet-another-config-lib:${property("deps.yacl")}-neoforge")
    }
}

stonecutter {
    replacements.string {
        direction = eval(current.version, ">1.21.10")
        replace("ResourceLocation", "Identifier")
    }
}

tasks {
    processResources {
        exclude("**/fabric.mod.json", "**/*.accesswidener", "**/mods.toml")
    }

    named("createMinecraftArtifacts") {
        dependsOn("stonecutterGenerate")
    }

    register<Copy>("buildAndCollect") {
        group = "build"
        from(jar.map { it.archiveFile })
        into(rootProject.layout.buildDirectory.file("libs/${project.property("mod.version")}"))
        dependsOn("build")
    }
}

java {
    withSourcesJar()
    val javaCompat = if (stonecutter.eval(stonecutter.current.version, ">=26.1")) {
        JavaVersion.VERSION_25
    } else {
        JavaVersion.VERSION_21
    }
    sourceCompatibility = javaCompat
    targetCompatibility = javaCompat
}

val additionalVersionsStr = findProperty("publish.additionalVersions") as String?
val additionalVersions: List<String> = additionalVersionsStr
    ?.split(",")
    ?.map { it.trim() }
    ?.filter { it.isNotEmpty() }
    ?: emptyList()

publishMods {
    file = tasks.jar.map { it.archiveFile.get() }
    additionalFiles.from(tasks.named<org.gradle.jvm.tasks.Jar>("sourcesJar").map { it.archiveFile.get() })

    type = STABLE
    displayName = "${property("mod.name")} ${property("mod.version")} for ${stonecutter.current.version} NeoForge"
    version = "${property("mod.version")}+${property("deps.minecraft")}-neoforge"
    changelog = provider { rootProject.file("CHANGELOG-LATEST.md").readText() }
    modLoaders.add("neoforge")

    modrinth {
        projectId = property("publish.modrinth") as String
        accessToken = env.MODRINTH_API_KEY.orNull()
        minecraftVersions.add(property("deps.minecraft").toString())
        minecraftVersions.addAll(additionalVersions)
    }

    curseforge {
        projectId = property("publish.curseforge") as String
        accessToken = env.CURSEFORGE_API_KEY.orNull()
        minecraftVersions.add(stonecutter.current.version)
        minecraftVersions.addAll(additionalVersions)
    }
}

publishing {
    publications {
        create<MavenPublication>("mavenJava") {
            groupId = property("mod.group") as String
            artifactId = (property("mod.id") as String) + "-neoforge"
            version = property("mod.version") as String
            from(components["java"])
        }
    }
    repositories {
        mavenLocal()
    }
}
