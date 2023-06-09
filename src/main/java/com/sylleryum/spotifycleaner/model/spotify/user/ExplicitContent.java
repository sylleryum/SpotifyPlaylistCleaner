
package com.sylleryum.spotifycleaner.model.spotify.user;

import com.fasterxml.jackson.annotation.*;

import java.util.HashMap;
import java.util.Map;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({
    "filter_enabled",
    "filter_locked"
})
public class ExplicitContent {

    @JsonProperty("filter_enabled")
    private Boolean filterEnabled;
    @JsonProperty("filter_locked")
    private Boolean filterLocked;
    @JsonIgnore
    private Map<String, Object> additionalProperties = new HashMap<String, Object>();

    /**
     * No args constructor for use in serialization
     * 
     */
    public ExplicitContent() {
    }

    /**
     * 
     * @param filterEnabled
     * @param filterLocked
     */
    public ExplicitContent(Boolean filterEnabled, Boolean filterLocked) {
        super();
        this.filterEnabled = filterEnabled;
        this.filterLocked = filterLocked;
    }

    @JsonProperty("filter_enabled")
    public Boolean getFilterEnabled() {
        return filterEnabled;
    }

    @JsonProperty("filter_enabled")
    public void setFilterEnabled(Boolean filterEnabled) {
        this.filterEnabled = filterEnabled;
    }

    @JsonProperty("filter_locked")
    public Boolean getFilterLocked() {
        return filterLocked;
    }

    @JsonProperty("filter_locked")
    public void setFilterLocked(Boolean filterLocked) {
        this.filterLocked = filterLocked;
    }

    @JsonAnyGetter
    public Map<String, Object> getAdditionalProperties() {
        return this.additionalProperties;
    }

    @JsonAnySetter
    public void setAdditionalProperty(String name, Object value) {
        this.additionalProperties.put(name, value);
    }

    @Override
    public String toString() {
        return "ExplicitContent{" +
                "filterEnabled=" + filterEnabled +
                ", filterLocked=" + filterLocked +
                ", additionalProperties=" + additionalProperties +
                '}';
    }
}
