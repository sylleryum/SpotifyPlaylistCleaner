
package com.sylleryum.spotifycleaner.model.currentPlaying;

import java.util.LinkedHashMap;
import java.util.Map;
import javax.annotation.Generated;
import com.fasterxml.jackson.annotation.JsonAnyGetter;
import com.fasterxml.jackson.annotation.JsonAnySetter;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({
    "id",
    "is_active",
    "is_private_session",
    "is_restricted",
    "name",
    "type",
    "volume_percent"
})
@Generated("jsonschema2pojo")
public class Device {

    @JsonProperty("id")
    private String id;
    @JsonProperty("is_active")
    private Boolean isActive;
    @JsonProperty("is_private_session")
    private Boolean isPrivateSession;
    @JsonProperty("is_restricted")
    private Boolean isRestricted;
    @JsonProperty("name")
    private String name;
    @JsonProperty("type")
    private String type;
    @JsonProperty("volume_percent")
    private Integer volumePercent;
    @JsonIgnore
    private Map<String, Object> additionalProperties = new LinkedHashMap<String, Object>();

    @JsonProperty("id")
    public String getId() {
        return id;
    }

    @JsonProperty("id")
    public void setId(String id) {
        this.id = id;
    }

    @JsonProperty("is_active")
    public Boolean getIsActive() {
        return isActive;
    }

    @JsonProperty("is_active")
    public void setIsActive(Boolean isActive) {
        this.isActive = isActive;
    }

    @JsonProperty("is_private_session")
    public Boolean getIsPrivateSession() {
        return isPrivateSession;
    }

    @JsonProperty("is_private_session")
    public void setIsPrivateSession(Boolean isPrivateSession) {
        this.isPrivateSession = isPrivateSession;
    }

    @JsonProperty("is_restricted")
    public Boolean getIsRestricted() {
        return isRestricted;
    }

    @JsonProperty("is_restricted")
    public void setIsRestricted(Boolean isRestricted) {
        this.isRestricted = isRestricted;
    }

    @JsonProperty("name")
    public String getName() {
        return name;
    }

    @JsonProperty("name")
    public void setName(String name) {
        this.name = name;
    }

    @JsonProperty("type")
    public String getType() {
        return type;
    }

    @JsonProperty("type")
    public void setType(String type) {
        this.type = type;
    }

    @JsonProperty("volume_percent")
    public Integer getVolumePercent() {
        return volumePercent;
    }

    @JsonProperty("volume_percent")
    public void setVolumePercent(Integer volumePercent) {
        this.volumePercent = volumePercent;
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
        StringBuilder sb = new StringBuilder();
        sb.append(Device.class.getName()).append('@').append(Integer.toHexString(System.identityHashCode(this))).append('[');
        sb.append("id");
        sb.append('=');
        sb.append(((this.id == null)?"<null>":this.id));
        sb.append(',');
        sb.append("isActive");
        sb.append('=');
        sb.append(((this.isActive == null)?"<null>":this.isActive));
        sb.append(',');
        sb.append("isPrivateSession");
        sb.append('=');
        sb.append(((this.isPrivateSession == null)?"<null>":this.isPrivateSession));
        sb.append(',');
        sb.append("isRestricted");
        sb.append('=');
        sb.append(((this.isRestricted == null)?"<null>":this.isRestricted));
        sb.append(',');
        sb.append("name");
        sb.append('=');
        sb.append(((this.name == null)?"<null>":this.name));
        sb.append(',');
        sb.append("type");
        sb.append('=');
        sb.append(((this.type == null)?"<null>":this.type));
        sb.append(',');
        sb.append("volumePercent");
        sb.append('=');
        sb.append(((this.volumePercent == null)?"<null>":this.volumePercent));
        sb.append(',');
        sb.append("additionalProperties");
        sb.append('=');
        sb.append(((this.additionalProperties == null)?"<null>":this.additionalProperties));
        sb.append(',');
        if (sb.charAt((sb.length()- 1)) == ',') {
            sb.setCharAt((sb.length()- 1), ']');
        } else {
            sb.append(']');
        }
        return sb.toString();
    }

}
