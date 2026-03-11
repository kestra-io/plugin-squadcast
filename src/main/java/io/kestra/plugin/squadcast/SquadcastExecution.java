package io.kestra.plugin.squadcast;

import java.util.Map;

import io.kestra.core.models.annotations.Example;
import io.kestra.core.models.annotations.Plugin;
import io.kestra.core.models.property.Property;
import io.kestra.core.models.tasks.VoidOutput;
import io.kestra.core.plugins.notifications.ExecutionInterface;
import io.kestra.core.plugins.notifications.ExecutionService;
import io.kestra.core.runners.RunContext;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;
import lombok.experimental.SuperBuilder;

@SuperBuilder
@ToString
@EqualsAndHashCode
@Getter
@NoArgsConstructor
@Schema(
    title = "Send Squadcast alert with execution context",
    description = "Builds a templated Squadcast payload containing execution ID, namespace, flow name, timings, and status. Use only from [Flow trigger](https://kestra.io/docs/administrator-guide/monitoring#alerting) alerts; for `errors` tasks prefer [SquadcastIncomingWebhook](https://kestra.io/plugins/plugin-squadcast/io.kestra.plugin.squadcast.squadcastincomingwebhook)."
)
@Plugin(
    examples = {
        @Example(
            title = "Send a [Squadcast](https://www.squadcast.com/) alert via [incoming webhook](https://support.squadcast.com/integrations/incident-webhook-incident-webhook-api)",
            full = true,
            code = """
                id: failure_alert
                namespace: company.team

                tasks:
                  - id: send_alert
                    type: io.kestra.plugin.squadcast.SquadcastExecution
                    url: "{{ secret('SQUADCAST_WEBHOOK') }}" # format: https://api.squadcast.com/v2/incidents/api/xyzs
                    message: "Kestra Squadcast alert"
                    priority: P1
                    eventId: "6"
                    status: trigger
                    tags:
                      severity: high
                      tagName1: tagValue1
                    executionId: "{{ trigger.executionId }}"

                triggers:
                  - id: failed_prod_workflows
                    type: io.kestra.plugin.core.trigger.Flow
                    conditions:
                      - type: io.kestra.plugin.core.condition.ExecutionStatus
                        in:
                          - FAILED
                          - WARNING
                      - type: io.kestra.plugin.core.condition.ExecutionNamespace
                        namespace: prod
                        prefix: true
                """
        )
    },
    aliases = "io.kestra.plugin.notifications.squadcast.SquadcastExecution"
)
public class SquadcastExecution extends SquadcastTemplate implements ExecutionInterface {
    @Builder.Default
    private final Property<String> executionId = Property.ofExpression("{{ execution.id }}");

    private Property<Map<String, Object>> customFields;
    private Property<String> customMessage;

    @Override
    public VoidOutput run(RunContext runContext) throws Exception {
        this.templateUri = Property.ofValue("squadcast-template.peb");
        this.templateRenderMap = Property.ofValue(ExecutionService.executionMap(runContext, this));

        return super.run(runContext);
    }
}
