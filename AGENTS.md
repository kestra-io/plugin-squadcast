# Kestra Squadcast Plugin

## What

- Provides plugin components under `io.kestra.plugin.squadcast`.
- Includes classes such as `SquadcastTemplate`, `SquadcastIncomingWebhook`, `SquadcastExecution`.

## Why

- This plugin integrates Kestra with Squadcast.
- It provides tasks that create incidents in Squadcast.

## How

### Architecture

Single-module plugin. Source packages under `io.kestra.plugin`:

- `squadcast`

Infrastructure dependencies (Docker Compose services):

- `app`

### Key Plugin Classes

- `io.kestra.plugin.squadcast.SquadcastExecution`
- `io.kestra.plugin.squadcast.SquadcastIncomingWebhook`

### Project Structure

```
plugin-squadcast/
├── src/main/java/io/kestra/plugin/squadcast/
├── src/test/java/io/kestra/plugin/squadcast/
├── build.gradle
└── README.md
```

## References

- https://kestra.io/docs/plugin-developer-guide
- https://kestra.io/docs/plugin-developer-guide/contribution-guidelines
