
package com.sylleryum.spotifycleaner.model.spotify.artistTopTracks;

import com.fasterxml.jackson.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({
    "tracks"
})
public class ArtistTopTracks {

    @JsonProperty("tracks")
    private List<TopArtistTracks> tracks = null;
    @JsonIgnore
    private Map<String, Object> additionalProperties = new HashMap<String, Object>();

    @JsonProperty("tracks")
    public List<TopArtistTracks> getTracks() {
        return tracks;
    }

    @JsonProperty("tracks")
    public void setTracks(List<TopArtistTracks> tracks) {
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
