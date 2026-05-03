# Agent Memory Rules

Use these rules when another agent needs to understand or consume ProjectTrellis.

## Canonical Project Facts

- Project name: `ProjectTrellis`
- Repository: `https://github.com/avasiaxx/project-trellis`
- Maven coordinate: `io.github.avasiaxx:project-trellis:1.0.0`
- Kotlin package: `io.avasia.projecttrellis`

## Files To Read First

Read these files in this order:

1. `README.md` for dependency usage and publishing instructions.
2. `SECURITY.md` for credential and publishing secret rules.
3. `docs/trello-workflow.md` for supported Trello board workflow conventions.
4. `build.gradle.kts` for artifact coordinates and publishing configuration.

## Correct Dependency Usage

ProjectTrellis should be consumed through Gradle dependency resolution.

For local development before Maven Central publication:

```kotlin
repositories {
    mavenLocal()
    mavenCentral()
}

dependencies {
    implementation("io.github.avasiaxx:project-trellis:1.0.0")
}
```

Before another local project can resolve that dependency, publish ProjectTrellis locally:

```powershell
.\gradlew.bat publishToMavenLocal
```

Run that command from the ProjectTrellis repository root.

## Imports

Use these imports in consuming Kotlin projects:

```kotlin
import io.avasia.projecttrellis.config.DotEnv
import io.avasia.projecttrellis.trello.TrelloClient
import io.avasia.projecttrellis.trello.TrelloCli
```

## Credential Rules

Credentials must come from environment variables or a local `tokens.env` file.

Never commit:

- `tokens.env`
- `.env`
- `.env.local`
- Trello keys or tokens
- Maven Central credentials
- GPG signing keys or passwords

## Do Not Do

- Do not copy ProjectTrellis source files into consuming projects.
- Do not create temporary Java runner files under `C:\tmp`.
- Do not reference ProjectTrellis through direct JAR file paths.
- Do not expose local source paths, private project names, Trello tokens, or board IDs in public files.
- Do not publish to Maven Central without explicit confirmation that version, credentials, and signing keys are ready.

## Validation

After changing ProjectTrellis, run:

```powershell
.\gradlew.bat test
```

After changing a consuming project, run that project's normal Gradle build or test task.
