# ProjectTrellis

A small Kotlin/JVM service for growing project plans into Trello board structure, plus an optional CLI wrapper you can run locally. It is designed to be imported as a dependency into other Kotlin projects.

## Project Shape

- Library package: `io.avasia.projecttrellis`
- Maven Central artifact: `io.github.avasiaxx:project-trellis:1.0.0`
- JVM toolchain: 17
- External runtime dependencies: none

## Use As A Dependency

After the artifact is published to Maven Central, add it to another Kotlin/Gradle project:

```kotlin
repositories {
    mavenCentral()
}

dependencies {
    implementation("io.github.avasiaxx:project-trellis:1.0.0")
}
```

For local development, you can also include this project as a composite build:

```kotlin
// settings.gradle.kts in the consuming project
includeBuild("../project-trellis")
```

```kotlin
dependencies {
    implementation("io.github.avasiaxx:project-trellis")
}
```

Or publish it to your local Maven cache:

```powershell
gradle publishToMavenLocal
```

## Publish To Maven Central

ProjectTrellis uses the Central Portal publishing flow through the `com.vanniktech.maven.publish` Gradle plugin.

Before publishing, create or verify a namespace in the Central Portal. For this repository, the intended Maven Central namespace is:

```text
io.github.avasiaxx
```

Set Central Portal user-token credentials and signing credentials outside the repository:

```powershell
$env:ORG_GRADLE_PROJECT_mavenCentralUsername="<central portal token username>"
$env:ORG_GRADLE_PROJECT_mavenCentralPassword="<central portal token password>"
$env:ORG_GRADLE_PROJECT_signingInMemoryKey="<ascii-armored private gpg key>"
$env:ORG_GRADLE_PROJECT_signingInMemoryKeyPassword="<gpg key password>"
```

Upload for manual review/publish in Central Portal:

```powershell
gradle publishToMavenCentral
```

Upload and automatically release after validation:

```powershell
gradle publishAndReleaseToMavenCentral
```

## Basic Kotlin Usage

```kotlin
import io.avasia.projecttrellis.config.DotEnv
import io.avasia.projecttrellis.trello.TrelloClient

val env = DotEnv.load()
val trello = TrelloClient(
    key = env.require("TRELLO_KEY"),
    token = env.require("TRELLO_TOKEN"),
)

println(trello.getBoards())
```

## Optional CLI

Copy `.env.example` to `tokens.env`, fill in your Trello credentials, then run:

```powershell
gradle run --args="trello help"
gradle run --args="trello boards"
gradle run --args="trello lists"
gradle run --args="trello create-list Backlog"
gradle run --args="trello create-card <listId> ""Card title"" ""Card description"""
gradle run --args="trello move-card <cardId> <listId>"
gradle run --args="trello comment <cardId> ""Comment text"""
gradle run --args="trello bootstrap-board"
```

`bootstrap-board` is not idempotent. Run it only on a fresh board unless duplicate lists, labels, and starter cards are acceptable.

## Configuration

Credentials can come from environment variables or `tokens.env`:

```text
TRELLO_KEY=
TRELLO_TOKEN=
TRELLO_BOARD_ID=
```

Keep `tokens.env` out of Git. This project ignores it by default.

## API Surface

`TrelloClient` currently wraps:

- `GET /1/members/me/boards`
- `GET /1/boards/{boardId}/lists`
- `POST /1/lists`
- `POST /1/labels`
- `POST /1/cards`
- `PUT /1/cards/{cardId}`
- `POST /1/cards/{cardId}/actions/comments`
