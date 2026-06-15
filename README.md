# macu lib

This is a library for my mods, like multi-platform item & block registering and adding supporter perks, like different cosmetics.

For examples of how to use api refer to the test mod in the GitHub repository

---

## Developers

Add this mod to your `build.gradle(.kts)`

```gradle
// Add the Macuguita maven repo to your repositories block
repositories {
  maven {
    name = "macuguita maven"
    url = "https://maven.macuguita.com/releases/"
  }
}

// Add the mod as a dependency in your environment
dependencies {
  // Prior to Minecraft 26.x, use "modImplementation" instead
  implementation("com.macuguita:macu_lib-<loader>:${project.macu_lib_version}")
}
```

---

## Support

If you encounter any issues or have suggestions, feel free to:

- Open an issue on GitHub.

- Send me a DM on Discord @macuguita

- Message me on any platform I linked in my website: [macuguita.com](https://macuguita.com/)

- You are free to use this mod in any Modpack in this platform

---

## Credits

- Developer: macuguita

<sup><sup>If you are a supporter, this mod connects to persista.macuguita.com to load cosmetics or other data stored by dependent mods. You can request data deletion by contacting gdpr@macuguita.com</sup></sup>
