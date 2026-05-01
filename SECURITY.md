# Security Policy

ProjectTrellis does not require committed credentials. Trello credentials should be provided through environment variables or a local `tokens.env` file that is excluded from Git.

## Sensitive Values

Never commit:

- `TRELLO_KEY`
- `TRELLO_TOKEN`
- `TRELLO_BOARD_ID` when the board identity should remain private
- Local `tokens.env`, `.env`, or `.env.local` files
- Central Portal publishing credentials
- GPG private keys or signing passwords

Use environment variables or a local user-level Gradle properties file for publishing secrets. Do not store Maven Central credentials or signing keys in this repository.

## Reporting Security Issues

Please report security concerns privately to the repository owner instead of opening a public issue with sensitive details.
