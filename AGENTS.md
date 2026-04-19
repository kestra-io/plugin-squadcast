# Kestra Squadcast Plugin

## What

- Provides plugin components under `io.kestra.plugin.squadcast`.
- Includes classes such as `SquadcastTemplate`, `SquadcastIncomingWebhook`, `SquadcastExecution`.

## Why

- What user problem does this solve? Teams need to create incidents in Squadcast from orchestrated workflows instead of relying on manual console work, ad hoc scripts, or disconnected schedulers.
- Why would a team adopt this plugin in a workflow? It keeps Squadcast steps in the same Kestra flow as upstream preparation, approvals, retries, notifications, and downstream systems.
- What operational/business outcome does it enable? It reduces manual handoffs and fragmented tooling while improving reliability, traceability, and delivery speed for processes that depend on Squadcast.

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
