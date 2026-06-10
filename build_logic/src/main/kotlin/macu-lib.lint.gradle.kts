plugins {
    id("com.diffplug.spotless")
}

spotless {
    lineEndings = com.diffplug.spotless.LineEnding.UNIX
    java {
        licenseHeaderFile(rootProject.file("HEADER"))
        removeUnusedImports()
        importOrder(
            "java",
            "javax",
            "",
            "net.minecraft",
            "com.mojang",
            "net.fabricmc",
            "net.neoforged",
            "",
            "com.macuguita"
        )
        leadingSpacesToTabs()
        trimTrailingWhitespace()
    }
    kotlin {
        ktlint()
    }
}
