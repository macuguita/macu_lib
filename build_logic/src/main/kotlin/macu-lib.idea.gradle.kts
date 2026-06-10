plugins {
    idea
}

fun shouldBeExcluded(file: File): Boolean {
    if (file.isDirectory) {
        val excludedFolderNames = setOf("run", "build", ".kotlin")

        if (file.name in excludedFolderNames) {
            val sourceParents = setOf("java", "kotlin", "groovy", "scala")
            return generateSequence(file.parentFile) { it.parentFile }
                .none { it.name in sourceParents }
        }
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
