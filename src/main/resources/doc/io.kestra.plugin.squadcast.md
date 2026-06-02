# How to use the Squadcast plugin

Trigger Squadcast incidents and send execution summaries from Kestra flows.

## Authentication

Set `url` to your Squadcast service's incoming webhook URL. Store it in a [secret](https://kestra.io/docs/concepts/secret).

## Tasks

`SquadcastIncomingWebhook` triggers an incident as a step within a flow — set `payload` to a JSON body in the Squadcast webhook format.

`SquadcastExecution` sends a structured execution summary including status, duration, and an execution link, and is designed for use with a [Flow trigger](https://kestra.io/docs/workflow-components/triggers) in a dedicated monitoring namespace that watches other namespaces for failures.
