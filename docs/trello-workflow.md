# Trello Workflow

This project includes a reusable Trello helper for Kotlin/JVM projects. It is intentionally small: credentials are loaded from environment variables or a local `tokens.env`, and API responses are returned as raw JSON strings so consuming projects can choose their own JSON library.

## Board Lists

The optional `bootstrap-board` command creates these default lists:

- Roadmap / Decisions
- Backlog
- Ready
- Building
- Review
- Done

## Labels

The optional `bootstrap-board` command creates these default labels:

- Foundation
- Database
- Desktop App
- Website
- Interactive Map
- Design
- Auth
- Sync
- Content Model
- GitHub

## Credential Strategy

Trello API credentials should live in a local `tokens.env` file or environment variables and must not be committed.

Expected values:

```text
TRELLO_KEY=
TRELLO_TOKEN=
TRELLO_BOARD_ID=
```

List IDs can be discovered through the API or stored once the board is created:

```text
TRELLO_LIST_ROADMAP_DECISIONS=
TRELLO_LIST_BACKLOG=
TRELLO_LIST_READY=
TRELLO_LIST_BUILDING=
TRELLO_LIST_REVIEW=
TRELLO_LIST_DONE=
```

## Local Commands

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

## Trello API References

- Getting started: https://support.atlassian.com/trello/docs/getting-started-with-trello-rest-api/
- REST API reference: https://developer.atlassian.com/cloud/trello/rest/
