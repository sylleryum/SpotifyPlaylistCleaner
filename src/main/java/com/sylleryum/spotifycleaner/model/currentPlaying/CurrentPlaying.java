
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
    "device",
    "shuffle_state",
    "repeat_state",
    "timestamp",
    "context",
    "progress_ms",
    "item",
    "currently_playing_type",
    "actions",
    "is_playing"
})
@Generated("jsonschema2pojo")
public class CurrentPlaying {

    @JsonProperty("device")
    private Device device;
    @JsonProperty("shuffle_state")
    private Boolean shuffleState;
    @JsonProperty("repeat_state")
    private String repeatState;
    @JsonProperty("timestamp")
    private Long timestamp;
    @JsonProperty("context")
    private Context context;
    @JsonProperty("progress_ms")
    private Integer progressMs;
    @JsonProperty("item")
    private Item item;
    @JsonProperty("currently_playing_type")
    private String currentlyPlayingType;
    @JsonProperty("actions")
    private Actions actions;
    @JsonProperty("is_playing")
    private Boolean isPlaying;
    @JsonIgnore
    private Map<String, Object> additionalProperties = new LinkedHashMap<String, Object>();

    @JsonProperty("device")
    public Device getDevice() {
        return device;
    }

    @JsonProperty("device")
    public void setDevice(Device device) {
        this.device = device;
    }

    @JsonProperty("shuffle_state")
    public Boolean getShuffleState() {
        return shuffleState;
    }

    @JsonProperty("shuffle_state")
    public void setShuffleState(Boolean shuffleState) {
        this.shuffleState = shuffleState;
    }

    @JsonProperty("repeat_state")
    public String getRepeatState() {
        return repeatState;
    }

    @JsonProperty("repeat_state")
    public void setRepeatState(String repeatState) {
        this.repeatState = repeatState;
    }

    @JsonProperty("timestamp")
    public Long getTimestamp() {
        return timestamp;
    }

    @JsonProperty("timestamp")
    public void setTimestamp(Long timestamp) {
        this.timestamp = timestamp;
    }

    @JsonProperty("context")
    public Context getContext() {
        return context;
    }

    @JsonProperty("context")
    public void setContext(Context context) {
        this.context = context;
    }

    @JsonProperty("progress_ms")
    public Integer getProgressMs() {
        return progressMs;
    }

    @JsonProperty("progress_ms")
    public void setProgressMs(Integer progressMs) {
        this.progressMs = progressMs;
    }

    @JsonProperty("item")
    public Item getItem() {
        return item;
    }

    @JsonProperty("item")
    public void setItem(Item item) {
        this.item = item;
    }

    @JsonProperty("currently_playing_type")
    public String getCurrentlyPlayingType() {
        return currentlyPlayingType;
    }

    @JsonProperty("currently_playing_type")
    public void setCurrentlyPlayingType(String currentlyPlayingType) {
        this.currentlyPlayingType = currentlyPlayingType;
    }

    @JsonProperty("actions")
    public Actions getActions() {
        return actions;
    }

    @JsonProperty("actions")
    public void setActions(Actions actions) {
        this.actions = actions;
    }

    @JsonProperty("is_playing")
    public Boolean getIsPlaying() {
        return isPlaying;
    }

    @JsonProperty("is_playing")
    public void setIsPlaying(Boolean isPlaying) {
        this.isPlaying = isPlaying;
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
        sb.append(CurrentPlaying.class.getName()).append('@').append(Integer.toHexString(System.identityHashCode(this))).append('[');
        sb.append("device");
        sb.append('=');
        sb.append(((this.device == null)?"<null>":this.device));
        sb.append(',');
        sb.append("shuffleState");
        sb.append('=');
        sb.append(((this.shuffleState == null)?"<null>":this.shuffleState));
        sb.append(',');
        sb.append("repeatState");
        sb.append('=');
        sb.append(((this.repeatState == null)?"<null>":this.repeatState));
        sb.append(',');
        sb.append("timestamp");
        sb.append('=');
        sb.append(((this.timestamp == null)?"<null>":this.timestamp));
        sb.append(',');
        sb.append("context");
        sb.append('=');
        sb.append(((this.context == null)?"<null>":this.context));
        sb.append(',');
        sb.append("progressMs");
        sb.append('=');
        sb.append(((this.progressMs == null)?"<null>":this.progressMs));
        sb.append(',');
        sb.append("item");
        sb.append('=');
        sb.append(((this.item == null)?"<null>":this.item));
        sb.append(',');
        sb.append("currentlyPlayingType");
        sb.append('=');
        sb.append(((this.currentlyPlayingType == null)?"<null>":this.currentlyPlayingType));
        sb.append(',');
        sb.append("actions");
        sb.append('=');
        sb.append(((this.actions == null)?"<null>":this.actions));
        sb.append(',');
        sb.append("isPlaying");
        sb.append('=');
        sb.append(((this.isPlaying == null)?"<null>":this.isPlaying));
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
