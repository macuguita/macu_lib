plugins {
    `java-library`
    `maven-publish`
    idea
    id("net.fabricmc.fabric-loom")
    id("macu-lib.idea")
    id("macu-lib.java")
    id("macu-lib.loom")
    id("macu-lib.multiloader")
    id("macu-lib.lint")
}

// Seriously, you should not worry about it, definitely not a hack.
// https://github.com/gradle/gradle/issues/15383#issuecomment-779893192
val libs = the<org.gradle.accessors.dm.LibrariesForLibs>()

fun prop(name: String): String = rootProject.providers.gradleProperty(name).get()

version = libs.versions.mod.get()
group = prop("props.maven_group")

base {
    archivesName.set(prop("props.mod_id"))
}

tasks.jar {
    inputs.property("projectName", project.name)

    from("LICENSE") {
        rename { "${it}_${project.name}" }
    }
}
