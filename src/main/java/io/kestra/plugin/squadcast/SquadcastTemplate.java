package io.kestra.plugin.squadcast;

import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

import org.apache.commons.io.IOUtils;

import io.kestra.core.models.property.Property;
import io.kestra.core.models.tasks.VoidOutput;
import io.kestra.core.runners.RunContext;
import io.kestra.core.serializers.JacksonMapper;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;
import lombok.experimental.SuperBuilder;

@SuperBuilder
@ToString
@EqualsAndHashCode
@Getter
@NoArgsConstructor
public abstract class SquadcastTemplate extends SquadcastIncomingWebhook {
    @Schema(
        title = "Set incident message",
        description = "Required alert message shown in Squadcast after template rendering"
    )
    @NotNull
    protected Property<String> message;

    @Schema(
        title = "Incident priority code",
        description = "One of P1–P5; invalid values fall back to Squadcast \"Unset\""
    )
    protected Property<String> priority;

    @Schema(
        title = "Incident event identifier",
        description = "Unique event ID to correlate trigger/resolve calls; required by Squadcast when updating incidents"
    )
    protected Property<String> eventId;

    @Schema(
        title = "Incident status action",
        description = "Squadcast action such as `trigger` or `resolve`; controls incident lifecycle"
    )
    protected Property<String> status;

    @Schema(
        title = "Tags applied to incident",
        description = "Key-value tags added to the incident payload after template rendering"
    )
    protected Property<Map<String, String>> tags;

    @Schema(
        title = "Template to use",
        hidden = true
    )
    protected Property<String> templateUri;

    @Schema(
        title = "Render variables for template",
        description = "Data map provided to the message template before JSON parsing"
    )
    protected Property<Map<String, Object>> templateRenderMap;

    @Override
    public VoidOutput run(RunContext runContext) throws Exception {
        Map<String, Object> map = new HashMap<>();

        final var renderedTemplateUri = runContext.render(this.templateUri).as(String.class);
        if (renderedTemplateUri.isPresent()) {
            String template = IOUtils.toString(
                Objects.requireNonNull(this.getClass().getClassLoader().getResourceAsStream(renderedTemplateUri.get())),
                StandardCharsets.UTF_8
            );

            String render = runContext.render(
                template, templateRenderMap != null ? runContext.render(templateRenderMap).asMap(String.class, Object.class) : Map.of()
            );
            map = (Map<String, Object>) JacksonMapper.ofJson().readValue(render, Object.class);
        }

        if (runContext.render(this.message).as(String.class).isPresent()) {
            map.put("message", runContext.render(this.message).as(String.class).get());
        }

        if (runContext.render(this.priority).as(String.class).isPresent()) {
            map.put("priority", runContext.render(this.priority).as(String.class).get());
        }

        if (runContext.render(this.eventId).as(String.class).isPresent()) {
            map.put("event_id", runContext.render(this.eventId).as(String.class).get());
        }

        if (runContext.render(this.status).as(String.class).isPresent()) {
            map.put("status", runContext.render(this.status).as(String.class).get());
        }

        final Map<String, String> tags = runContext.render(this.tags).asMap(String.class, String.class);
        if (!tags.isEmpty()) {
            map.put("tags", tags);
        }

        this.payload = Property.ofValue(JacksonMapper.ofJson().writeValueAsString(map));

        return super.run(runContext);
    }
}
