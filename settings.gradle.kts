pluginManagement {
	repositories {
		maven {
			name = "Fabric"
			url = uri("https://maven.fabricmc.net/")
		}
		mavenCentral()
		gradlePluginPortal()
	}
}

// Should match your modid
rootProject.name = "macu_lib"

includeBuild("build_logic")
