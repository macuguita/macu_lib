plugins {
	`maven-publish`
	id("macu-lib.root")
	alias(libs.plugins.dotenv)
	alias(libs.plugins.mod.publish)
}

repositories {
    exclusiveContent {
        forRepository {
            maven {
                name = "Macuguita maven"
                url = uri("https://maven.macuguita.com/releases")
            }
        }
        filter {
            includeGroupAndSubgroups("com.macuguita")
            includeGroupAndSubgroups("folk.sisby")
            includeGroupAndSubgroups("org.quiltmc")
        }
    }
    exclusiveContent {
        forRepository {
            maven {
                name = "Terraformers (Mod menu)"
                url = uri("https://maven.terraformersmc.com/releases/")
            }
        }
        filter {
            includeGroupAndSubgroups("com.terraformersmc")
        }
    }
}

fun prop(name: String): String = providers.gradleProperty(name).get()

val testMod: SourceSet = sourceSets.create("testMod") {
    this.compileClasspath += sourceSets.main.get().compileClasspath
    this.runtimeClasspath += sourceSets.main.get().runtimeClasspath

    resources {
        srcDirs.add(rootProject.file("src/testmod/generated"))
    }
}

loom {
	accessWidenerPath.set(project.file("src/main/resources/macu_lib.classtweaker"))

    runs {
        register("testModClient") {
            client()
            name = "Testmod Client"

            programArgs.add("--username=macuguita")
            programArgs.add("--uuid=0e56050b-ee27-478a-a345-d2b384919081")

            source(testMod)
        }
        register("testModServer") {
            server()
            name = "Testmod Client"

            source(testMod)
        }
    }
}

val testModJar = tasks.register<Jar>("testModJar") {
    group = "build"
    description = "Builds the testmod jar"

    from(testMod.output)
    archiveClassifier.set("testmod")
}

dependencies {
	implementation(libs.kaleido.config)
	include(libs.kaleido.config)

    fabricRuntimeOnly(libs.modmenu)

    "testModCompileOnly"(libs.fabric.loader)
    "testModCompileOnly"(libs.fabric.api)
    "testModCompileOnly"(libs.neoforge)
    "testModCompileOnly"(libs.neoforge.loader)
    "testModImplementation"(sourceSets.main.get().output)
}

tasks.processResources {
	inputs.properties(
		"version" to version,
		"yumi_version" to libs.versions.yumi.get(),
		"kaleido_version" to libs.versions.kaleido.get(),
		"minecraft_fabric_version_range" to prop("deps.minecraft_fabric_version_range"),
		"minecraft_neoforge_version_range" to prop("deps.minecraft_neoforge_version_range")
	)

	filesMatching(listOf("fabric.mod.json", "META-INF/neoforge.mods.toml", "META-INF/jarjar/metadata.json")) {
		expand(
			"version" to version,
			"yumi_version" to libs.versions.yumi.get(),
			"kaleido_version" to libs.versions.kaleido.get(),
			"minecraft_fabric_version_range" to prop("deps.minecraft_fabric_version_range"),
			"minecraft_neoforge_version_range" to prop("deps.minecraft_neoforge_version_range")
		)
	}
}

publishing {
	publications {
		create<MavenPublication>("mavenJava") {
			groupId = prop("props.maven_group")
			artifactId = prop("props.mod_id")
			version = libs.versions.mod.get() + "+${libs.versions.minecraft.get()}"
			from(components["java"])
		}
	}
	repositories {
		mavenLocal()
		maven {
			name = "macuguita"
			url = uri("https://maven.macuguita.com/releases")

			credentials {
				username = env.MAVEN_USERNAME.orNull()
				password = env.MAVEN_KEY.orNull()
			}
		}
	}
}
