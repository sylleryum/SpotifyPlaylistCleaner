
package com.sylleryum.spotifycleaner.model.spotify.albumTracksPopularity;

import com.fasterxml.jackson.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({
    "tracks"
})
public class AlbumTracksPopularity {

    @JsonProperty("tracks")
    private List<TrackPopularity> tracks = null;
    @JsonIgnore
    private Map<String, Object> additionalProperties = new HashMap<String, Object>();

    @Override
    public String toString() {
        return "AlbumTracksPopularity{" +
                "tracks=" + tracks +
                ", additionalProperties=" + additionalProperties +
                '}';
    }

    @JsonProperty("tracks")
    public List<TrackPopularity> getTracks() {
        return tracks;
    }

    @JsonProperty("tracks")
    public void setTracks(List<TrackPopularity> tracks) {
        this.tracks = tracks;
    }

    @JsonAnyGetter
    public Map<String, Object> getAdditionalProperties() {
        return this.additionalProperties;
    }

    @JsonAnySetter
    public void setAdditionalProperty(String name, Object value) {
        this.additionalProperties.put(name, value);
    }

}
