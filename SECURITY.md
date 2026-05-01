# Security Policy

ProjectTrellis does not require committed credentials. Trello credentials should be provided through environment variables or a local `tokens.env` file that is excluded from Git.

## Sensitive Values

Never commit:

- `TRELLO_KEY`
- `TRELLO_TOKEN`
- `TRELLO_BOARD_ID` when the board identity should remain private
- Local `tokens.env`, `.env`, or `.env.local` files

## Reporting Security Issues

Please report security concerns privately to the repository owner instead of opening a public issue with sensitive details.
